package edu.purdue.SafeWalk;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;

import com.google.android.gms.maps.GoogleMap.SnapshotReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import edu.purdue.SafeWalk.MapData.Building;

import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.animation.TimeInterpolator;
import android.app.Activity;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;

public class MakeRequestFragment extends Fragment implements
		SnapshotReadyCallback {

	private static final TimeInterpolator sDecelerator = new DecelerateInterpolator();
	private static final TimeInterpolator sAccelerator = new AccelerateInterpolator();
	private static final String PACKAGE_NAME = "edu.purdue.SafeWalk";
	private static final String TAG = "MakeRequestFragment";
	private int mOriginalOrientation;
	AsyncTask<Void, Void, String> buildingsTask;
	
	ImageView mImageView;

	TextView mBuildingText1, mBuildingText2;

	double start_lat, start_long, end_lat, end_long;
	private Bitmap mapImage = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Show the Up button in the action bar.

		setHasOptionsMenu(true);

		setupActionBar();
		getActivity().getActionBar().setTitle("Make Request");
		getActivity().getActionBar().setSubtitle("Confirm Information");

		/*
		 * Start DevBytes Code
		 * 
		 * // Retrieve the data we need for the picture/description to display
		 * and // the thumbnail to animate it from Bundle bundle =
		 * getIntent().getExtras(); Bitmap bitmap =
		 * BitmapUtils.getBitmap(getResources(), bundle.getInt(PACKAGE_NAME +
		 * ".resourceId")); String description = bundle.getString(PACKAGE_NAME +
		 * ".description"); final int thumbnailTop = bundle.getInt(PACKAGE_NAME
		 * + ".top"); final int thumbnailLeft = bundle.getInt(PACKAGE_NAME +
		 * ".left"); final int thumbnailWidth = bundle.getInt(PACKAGE_NAME +
		 * ".width"); final int thumbnailHeight = bundle.getInt(PACKAGE_NAME +
		 * ".height"); mOriginalOrientation = bundle.getInt(PACKAGE_NAME +
		 * ".orientation");
		 * 
		 * mBitmapDrawable = new BitmapDrawable(getResources(), bitmap);
		 * mImageView.setImageDrawable(mBitmapDrawable);
		 * mTextView.setText(description);
		 * 
		 * mBackground = new ColorDrawable(Color.BLACK);
		 * mTopLevelLayout.setBackground(mBackground);
		 * 
		 * // Only run the animation if we're coming from the parent activity,
		 * not if // we're recreated automatically by the window manager (e.g.,
		 * device rotation) if (savedInstanceState == null) { ViewTreeObserver
		 * observer = mImageView.getViewTreeObserver();
		 * observer.addOnPreDrawListener(new
		 * ViewTreeObserver.OnPreDrawListener() {
		 * 
		 * @Override public boolean onPreDraw() {
		 * mImageView.getViewTreeObserver().removeOnPreDrawListener(this);
		 * 
		 * // Figure out where the thumbnail and full size versions are,
		 * relative // to the screen and each other int[] screenLocation = new
		 * int[2]; mImageView.getLocationOnScreen(screenLocation); mLeftDelta =
		 * thumbnailLeft - screenLocation[0]; mTopDelta = thumbnailTop -
		 * screenLocation[1];
		 * 
		 * // Scale factors to make the large version the same size as the
		 * thumbnail mWidthScale = (float) thumbnailWidth /
		 * mImageView.getWidth(); mHeightScale = (float) thumbnailHeight /
		 * mImageView.getHeight();
		 * 
		 * runEnterAnimation();
		 * 
		 * return true; } }); }
		 * 
		 * //End DevBytes Code
		 */

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Bundle b = getArguments();

		start_lat = b.getDouble("start_lat");
		start_long = b.getDouble("start_long");
		end_lat = b.getDouble("end_lat");
		end_long = b.getDouble("end_long");

		Log.d(TAG, "Start: " + start_lat + " " + start_long + " End: "
				+ end_lat + " " + end_long);

		View v = inflater.inflate(R.layout.activity_make_request, container,
				false);

		mImageView = (ImageView) v.findViewById(R.id.mapImage);
		mImageView.setScaleType(ScaleType.CENTER_CROP);

		mBuildingText1 = (TextView) v.findViewById(R.id.txt_building1);
		mBuildingText2 = (TextView) v.findViewById(R.id.txt_building2);
		

		buildingsTask = new AsyncTask<Void, Void, String>() {

			@Override
			protected String doInBackground(Void... params) {
				String text; 
				try{
					text = getBuildings(); 
				} catch(RuntimeException re)
				{
					//If the task was cancelled, this is run. 
					return null; 
				}
				return text; 
			}

			@Override
			public void onCancelled(String text)
			{
				Log.d("GetBuildingsTask", "Task cancelled before completion...Exiting");
			}
			
			@Override
			protected void onPostExecute(String text) {
				String[] bldngs = text.split("\\|");
				mBuildingText1.setText(bldngs[0]);
				mBuildingText2.setText(bldngs[1]);
			}
			
			private String getBuildings() {
				if(isCancelled()) throw new RuntimeException(); 
				
				MapData mapData = new MapData(MakeRequestFragment.this.getActivity()
						.getApplicationContext());
				List<Building> buildings = mapData.getBuildings();

				double start_dist = -1;
				double end_dist = -1;

				Building best_start = null, best_end = null;

				//Log.v(TAG,
				//		"This is about to get crazy. I hope you don't want the logcat :)");
				Log.d(TAG, "My Start: " + start_lat + start_long);

				for (Building b : buildings) {
					if(isCancelled()) throw new RuntimeException(); 
					
					double s = SphericalUtil.computeDistanceBetween(new LatLng(
							start_lat, start_long), new LatLng(b.lat, b.lng));
					double e = SphericalUtil.computeDistanceBetween(new LatLng(end_lat,
							end_long), new LatLng(b.lat, b.lng));

					// Log.d(TAG, "Building: " + b.short_name + ":" + b.lat + " " +
					// b.lng);
					// Log.d(TAG, "   Start:   " + s);
					// Log.d(TAG, "     End:   " + e);

					if (start_dist == -1) {
						start_dist = s;
						best_start = b;
					}
					if (end_dist == -1) {
						end_dist = e;
						best_end = b;
					}

					if (start_dist > s) {
						best_start = b;
						start_dist = s;
					}
					if (end_dist > e) {
						best_end = b;
						end_dist = e;
					}
				}
				String first = (best_start.short_name.contains("?")) ? best_start.full_name.split(" ")[0] : best_start.short_name;
				String last = (best_end.short_name.contains("?")) ? best_end.full_name.split(" ")[0] : best_end.short_name;
				return first + "|" + last;
			}
		};
		buildingsTask.execute();

		return v;
	}

	@Override
	public void onViewCreated(final View v, Bundle state) {
		super.onViewCreated(v, state);
		new Thread() {
			public void run() {
				while (true) {
					if (mapImage != null)
						break; // TODO: THIS NEEDS TO NOT BE INFINITE!!!!!!!
				}
				Handler h = new Handler(getActivity().getMainLooper());
				h.post(new Runnable() {
					public void run() {
						((ImageView) v.findViewById(R.id.mapImage))
								.setImageBitmap(mapImage);

					}
				});
			}
		}.start();
	}
	
	@Override
	public void onDetach()
	{
		if(buildingsTask != null && buildingsTask.getStatus() != AsyncTask.Status.FINISHED)
		{
			buildingsTask.cancel(false);
		}
		super.onDetach();
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// Inflate the menu; this adds items to the action bar if it is present.
		inflater.inflate(R.menu.make_request, menu);
	}

	@Override
	public void onSnapshotReady(Bitmap snapshot) {
		Log.v("MAP IMAGE", "Image is ready!!!!!!!!");
		mapImage = snapshot;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		switch (item.getItemId()) {
		case R.id.action_confirm:
			sendRequest();
			return true; 
			
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	private void sendRequest() {
		AsyncHttpClient client = new AsyncHttpClient();

		String time = java.text.DateFormat.getDateTimeInstance().format(
				Calendar.getInstance().getTime());
		String userName = "David Tschida";


		Requester r = new Requester(userName+ (int) (Math.random()*1000), time, "219-555-1242",
				"Not Urgent", start_lat, start_long, end_lat, end_long);
		Log.d("json", r.toJSON().toString());
		StringEntity se = null;

		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
			public void onSuccess(String suc) {
				Log.d("response", suc);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				Toast.makeText(getActivity().getApplicationContext(),
						"No connection to server", Toast.LENGTH_LONG).show();
				Log.d("failure", Integer.toString(statusCode));
			}
		};

		try {
			se = new StringEntity(r.toJSON().toString());
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String hostname = PreferenceManager.getDefaultSharedPreferences(
				getActivity()).getString("pref_server",
						getString(R.string.pref_server_default));
		client.post(getActivity().getBaseContext(), hostname + "/request", se,
				"application/json", handler);
		Log.d("debug", client.toString());
		getFragmentManager().popBackStack();
	}

}
