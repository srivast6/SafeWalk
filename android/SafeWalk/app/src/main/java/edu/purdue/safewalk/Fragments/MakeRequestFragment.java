package edu.purdue.safewalk.Fragments;



import java.util.List;

import com.google.android.gms.maps.GoogleMap.SnapshotReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import edu.purdue.safewalk.R;
import edu.purdue.safewalk.Interfaces.OnNewRequestFinished;
import edu.purdue.safewalk.MapItems.MapData;
import edu.purdue.safewalk.MapItems.MapData.Building;
import edu.purdue.safewalk.Tasks.NewRequestTask;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.animation.TimeInterpolator;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

public class MakeRequestFragment extends Fragment implements
		SnapshotReadyCallback, OnNewRequestFinished {

	private static final TimeInterpolator sDecelerator = new DecelerateInterpolator();
	private static final TimeInterpolator sAccelerator = new AccelerateInterpolator();
	private static final String PACKAGE_NAME = "edu.purdue.SafeWalk";
	private static final String TAG = "MakeRequestFragment";
	private int mOriginalOrientation;
	AsyncTask<Void, Void, String> buildingsTask;

	ImageView mImageView;

	TextView mBuildingText1, mBuildingText2;
	ProgressBar mMapLoading;
	
	private String mPersonDescription;
	
	double start_lat, start_long, end_lat, end_long;
	private Bitmap mapImage = null;
	private Bitmap mUserPhoto;

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
				try {
					text = getBuildings();
				} catch (RuntimeException re) {
					// If the task was cancelled, this is run.
					return null;
				}
				return text;
			}

			@Override
			public void onCancelled(String text) {
				Log.d("GetBuildingsTask",
						"Task cancelled before completion...Exiting");
			}

			@Override
			protected void onPostExecute(String text) {
				String[] bldngs = text.split("\\|");
				mBuildingText1.setText(bldngs[0]);
				mBuildingText2.setText(bldngs[1]);
			}

			private String getBuildings() {
				if (isCancelled())
					throw new RuntimeException();

				MapData mapData = new MapData(MakeRequestFragment.this
						.getActivity().getApplicationContext());
				List<Building> buildings = mapData.getBuildings();

				double start_dist = -1;
				double end_dist = -1;

				Building best_start = null, best_end = null;

				// Log.v(TAG,
				// "This is about to get crazy. I hope you don't want the logcat :)");
				Log.d(TAG, "My Start: " + start_lat + start_long);

				for (Building b : buildings) {
					if (isCancelled())
						throw new RuntimeException();

					double s = SphericalUtil.computeDistanceBetween(new LatLng(
							start_lat, start_long), new LatLng(b.lat, b.lng));
					double e = SphericalUtil.computeDistanceBetween(new LatLng(
							end_lat, end_long), new LatLng(b.lat, b.lng));

					// Log.d(TAG, "Building: " + b.short_name + ":" + b.lat +
					// " " +
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
				String first = (best_start.short_name.contains("?")) ? best_start.full_name
						.split(" ")[0] : best_start.short_name;
				String last = (best_end.short_name.contains("?")) ? best_end.full_name
						.split(" ")[0] : best_end.short_name;
				return first + "|" + last;
			}
		};
		buildingsTask.execute();

		Button buttonText = (Button) v.findViewById(R.id.btn_desc);

		buttonText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				AlertDialog.Builder alert = new AlertDialog.Builder(
						MakeRequestFragment.this.getActivity());

				alert.setTitle("Describe Yourself");
				alert.setMessage("What do you look like, so we can find you?");

				// Set an EditText view to get user input
				final EditText input = new EditText(MakeRequestFragment.this
						.getActivity());
				alert.setView(input);

				alert.setPositiveButton("Ok",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								mPersonDescription = input.getText().toString();
								dialog.dismiss();
							}
						});

				alert.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								dialog.dismiss();
							}
						});

				alert.show();
			}

		});
		
		Button buttonPic = (Button) v.findViewById(R.id.btn_pic);

		buttonPic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		        startActivityForResult(cameraIntent, 1888);
			}
			
		});
		
		mMapLoading = (ProgressBar) v.findViewById(R.id.progressBar1);
		
		mMapLoading.setIndeterminate(true);
		
		return v;
	}


	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if( requestCode == 1888 && data != null) {
	        mUserPhoto = (Bitmap) data.getExtras().get("data");
	    }
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
						mMapLoading.setVisibility(View.GONE);

					}
				});
			}
		}.start();
	}

	@Override
	public void onDetach() {
		if (buildingsTask != null
				&& buildingsTask.getStatus() != AsyncTask.Status.FINISHED) {
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
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_confirm:
			sendRequest();
			return true;

		}

		return super.onOptionsItemSelected(item);
	}

	private void sendRequest() {
		
		NewRequestTask task = new NewRequestTask(start_lat, start_long, end_lat, end_long,"John Smith", "219-555-1343",getActivity(),this);
		task.execute();
		getFragmentManager().popBackStack();
	}

	@Override
	public void onNewRequestFinished() {
		// TODO Auto-generated method stub
		
	}

}
