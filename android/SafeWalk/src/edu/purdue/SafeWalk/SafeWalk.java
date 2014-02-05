package edu.purdue.SafeWalk;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.w3c.dom.Document;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.IntentSender.SendIntentException;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.SnapshotReadyCallback;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import edu.purdue.SafeWalk.settings.SettingsActivity;

public class SafeWalk extends Activity implements
		GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener{
	private GoogleMap mMap;
	private LocationClient mLocationClient;
	ListView drawerList;
	DrawerLayout drawerLayout;
	ActionBarDrawerToggle mDrawerToggle;
	private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
	//static double x;
	//static double y;
	int numRequests; 
	static final String name = "John Doe";
	private static final String TAG = "SafeWalk";
	private static final String PACKAGE = "edu.purdue.SafeWalk";
	private static final int LOGIN_REQUEST = 1223523513;
	public static String hostname;

	private static enum BubbleState {
		START, END, CONFIRM;
	};
	
	private BubbleState mBubbleState = BubbleState.START;
	
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.main_activity);

		hostname = PreferenceManager.getDefaultSharedPreferences(this).getString("pref_server", "http://optical-sight-386.appspot.com");
		
		initNavDrawer();
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setSubtitle("Map");

		// Check for Google Play Services
		// TODO: This may be throwing the error in the log. 
		int googlePlayServicesAvailable = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(this);
		if (googlePlayServicesAvailable != ConnectionResult.SUCCESS) {
			GooglePlayServicesUtil.getErrorDialog(googlePlayServicesAvailable,
					this, CONNECTION_FAILURE_RESOLUTION_REQUEST).show();
		}

		mLocationClient = new LocationClient(this, this, this);

		initMap();
	}

	private void initMap() {
		mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();
		if (mMap != null) {
			mMap.setMyLocationEnabled(true);
			UiSettings mapSettings = mMap.getUiSettings();
			mapSettings.setTiltGesturesEnabled(false);
			mapSettings.setRotateGesturesEnabled(false);
		} else {
			AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
			alertBuilder.setTitle("Error!");
			alertBuilder.setMessage(this.getResources().getText(
					R.string.error_no_maps));
			alertBuilder.setPositiveButton("Close "
					+ this.getResources().getText(R.string.app_name),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							SafeWalk.this.finish();
						}
					});
			alertBuilder.show();
		}
	}

	private void initNavDrawer() {
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawerList = (ListView) findViewById(R.id.left_drawer);
		String[] menuItems = new String[] { "Settings",
				"What is SafeWalk?", "People", "Safewalk Personnel" };
		drawerList.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, menuItems));
		drawerList.setOnItemClickListener(new ListView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int order,
					long arg3) {

				if (order == 2) {
					Intent listIntent = new Intent(SafeWalk.this,
							ListViewRequesterActivity.class);
					SafeWalk.this.startActivity(listIntent);

				}else if(order == 0){
					openSettings();

				}else if(order == 3){
					Intent Safewalk_Personnel = new Intent(SafeWalk.this,
							MapPoliceActivity.class);
					SafeWalk.this.startActivity(Safewalk_Personnel);
				}else if(order == 1){
					Intent Safewalk_Personnel = new Intent(SafeWalk.this,
							AboutActivity.class);
					SafeWalk.this.startActivity(Safewalk_Personnel);
				} else {
					Toast.makeText(SafeWalk.this,
							"This feature is under construction",
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		final String title, drawerTitle;
		title = drawerTitle = (String) getTitle();

		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
				R.drawable.ic_drawer, R.string.drawer_open,
				R.string.drawer_close) {

			/** Called when a drawer has settled in a completely closed state. */
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(title);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}

			/** Called when a drawer has settled in a completely open state. */
			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(drawerTitle);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}
		};

		// Set the drawer toggle as the DrawerListener
		drawerLayout.setDrawerListener(mDrawerToggle);
	}

	private void openSettings() {
		Intent setingsIntent = new Intent(SafeWalk.this, SettingsActivity.class);
		SafeWalk.this.startActivity(setingsIntent);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		if (mLocationClient != null && mLocationClient.isConnected()) {
			mLocationClient.disconnect();
		}
	}
	
	@Override 
	protected void onResume()
	{
		super.onResume();
		if (mLocationClient != null && (!mLocationClient.isConnected() && !mLocationClient.isConnecting())) {
			mLocationClient.connect();
		}
	}

	/**
	 * Called when we fail to connect to the mLocationClient.
	 */
	@Override
	public void onConnectionFailed(ConnectionResult res) {
		if (res.isSuccess() == true)
			return;

		if (res.hasResolution() == true) {
			try {
				res.startResolutionForResult(this,
						CONNECTION_FAILURE_RESOLUTION_REQUEST);
			} catch (SendIntentException e) {
				e.printStackTrace();
				this.onConnectionFailed(res); // HACK: Possible stack overflow.
			}
		} else {
			AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
			alertBuilder.setTitle("Error connection to GPS");
			alertBuilder.setMessage("Connection result: " + res.toString());
			alertBuilder.show();
		}
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		menu.findItem(R.id.action_display_blue).setChecked(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("show_blue_lights", false));
		menu.findItem(R.id.action_display_vols).setChecked(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("show_volunteers", false));
		
		// If the nav drawer is open, hide action items related to the content
		// view
		boolean drawerOpen = drawerLayout.isDrawerOpen(drawerList);
		// menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.map_menu, menu);
	    return true;
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Pass the event to ActionBarDrawerToggle, if it returns
		// true, then it has handled the app icon touch event
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		
		SharedPreferences myPreference= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		SharedPreferences.Editor editor = myPreference.edit();
		switch (item.getItemId()) {
        case R.id.action_display_blue:
        	item.setChecked(!item.isChecked());
    		editor.putBoolean("show_blue_lights", item.isChecked());
    		editor.apply();
            return true;
        case R.id.action_display_vols:
        	item.setChecked(!item.isChecked());
    		editor.putBoolean("show_volunteers", item.isChecked());
    		editor.apply();
            return true;
        case R.id.action_settings:
        	openSettings();
            return true;
        case R.id.action_button:
        	openRequestActivity();
        	return true; 
        case R.id.action_login:
        	openLoginActivity();
        	return true;
		}
		
		
		// Handle your other action bar items...
		return super.onOptionsItemSelected(item);
	}
	
	private void openLoginActivity()
	{
		Intent loginIntent = new Intent(this, LoginActivity.class);
		startActivityForResult(loginIntent, LOGIN_REQUEST);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		  if (requestCode == LOGIN_REQUEST) {

		     if(resultCode == RESULT_OK){      
		         String result=data.getStringExtra("result");     
		         Log.d(TAG, "Login successful: " + result);
		     }
		     if (resultCode == RESULT_CANCELED) {    
		         Log.d(TAG, "Login failed!");
		     }
		  }
		}
	
	private void openRequestActivity()
	{
		
		View mapView = findViewById(R.id.mapFrame);
		

        // Create a new Fragment to be placed in the activity layout
        MakeRequestFragment requestFragment = new MakeRequestFragment();
        mMap.snapshot((SnapshotReadyCallback) requestFragment);
        
        Bundle b = new Bundle();
        b.putParcelable("location", mLocationClient.getLastLocation());
        
        
        // In case this activity was started with special instructions from an
        // Intent, pass the Intent's extras to the fragment as arguments
        requestFragment.setArguments(getIntent().getExtras());
        
        
        
        
        // Add the fragment to the 'fragment_container' FrameLayout
        getFragmentManager().beginTransaction()
                .add(R.id.fragmentContainer, requestFragment).setTransition(FragmentTransaction.TRANSIT_NONE).commit();
		
		
		/*
		
        int[] screenLocation = new int[2];
        mapView.getLocationOnScreen(screenLocation);
        Intent subActivity = new Intent(SafeWalk.this,
                MakeRequestActivity.class);
        int orientation = getResources().getConfiguration().orientation;
        subActivity.
                putExtra(PACKAGE + ".orientation", orientation).
                //putExtra(PACKAGE + ".orientation", orientation).
                putExtra(PACKAGE + ".left", screenLocation[0]).
                putExtra(PACKAGE + ".top", screenLocation[1]).
                putExtra(PACKAGE + ".width", mapView.getWidth()). //map
                putExtra(PACKAGE + ".height", mapView.getHeight()); //map
        startActivity(subActivity);
        */
        
        //TODO: Map.capture()
        
        
        // Override transitions: we don't want the normal window animation in addition
        // to our custom one
        //overridePendingTransition(0, 0);
	}

	@Override
	public void onConnected(Bundle bun) {
		Log.d("SafeWalk", "The mLocationHandler has been connected!");
        if(mLocationClient == null || mMap == null) {
            Log.e("LocationClient", (mLocationClient == null ? "mLocationClient" : "mMap") + " is null!");
            return;
        }
		CameraPosition.Builder cameraPositionBuilder = new CameraPosition.Builder();
        /*if(mLocationClient.getLastLocation() == null) {
            Log.e("LocationClient", "Last location is null!");
            mLocationClient.disconnect();
            return;
        }*/
		cameraPositionBuilder.target(new LatLng(mLocationClient
				.getLastLocation().getLatitude(), mLocationClient
				.getLastLocation().getLongitude()));
		cameraPositionBuilder.zoom((float) 16);
		mMap.animateCamera(CameraUpdateFactory
				.newCameraPosition(cameraPositionBuilder.build()));
		//mLocationClient.disconnect();
	}

	@Override
	public void onDisconnected() {
		
		Log.d("SafeWalk", "The mLocationHandler has been disconnected...");
		
		mLocationClient.connect();
	}

	
	
	

	/*
	 * Function used when a request to be picked up is map, send information to server
	 */
	public void onPopUpBubbleClick(View v){
		
		LatLng latlng; 
		if(mBubbleState == BubbleState.START)
		{
			/*
			TextView tv = new TextView(getApplicationContext());
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
			params.addRule(RelativeLayout.BELOW, R.id.mapFrame);
			//params.addRule(RelativeLayout.ABOVE, R.id.callButtons);
			
			tv.setText("Hello, this is dog.");
			tv.setTextSize(50);
			tv.setId(12493840);
			
			View callButtons = findViewById(R.id.callButtons);
			
			RelativeLayout.LayoutParams btn_layout = (RelativeLayout.LayoutParams) callButtons.getLayoutParams();
			btn_layout.addRule(RelativeLayout.BELOW, 12493840);
			btn_layout.height = callButtons.getHeight();
			
			RelativeLayout mainView = (RelativeLayout) findViewById(R.id.mainView);
			
			FrameLayout mapFrame = (FrameLayout) findViewById(R.id.mapFrame);
			RelativeLayout.LayoutParams mapparams = (RelativeLayout.LayoutParams) mapFrame.getLayoutParams();
			
			mapparams.height = 1000; 
			
			mapFrame.setLayoutParams(mapparams);
			mainView.addView(tv, params);
			callButtons.setLayoutParams(btn_layout);
			
			*/
			//---------
			
			latlng = ((CustomMapFragment) getFragmentManager().findFragmentById(R.id.map)).dropPinAtCenter(this, "Start", BitmapDescriptorFactory.HUE_GREEN);
			
			SharedPreferences bubbleState = getSharedPreferences("bubbleState", MODE_PRIVATE);
			SharedPreferences.Editor edit = bubbleState.edit();
			edit.putString("start_lat", "" + latlng.latitude);
			edit.putString("start_long", "" + latlng.longitude);
			edit.commit();
			
			TextView bubble = (TextView)findViewById(R.id.bubbleText);
			bubble.setText("Set Dropoff Location");
			mBubbleState = BubbleState.END;
			
		} else if(mBubbleState == BubbleState.END)
		{
			latlng = ((CustomMapFragment) getFragmentManager().findFragmentById(R.id.map)).dropPinAtCenter(this, "End", BitmapDescriptorFactory.HUE_RED);
			
			SharedPreferences bubbleState = getSharedPreferences("bubbleState", MODE_PRIVATE);
			SharedPreferences.Editor edit = bubbleState.edit();
			edit.putString("end_lat", "" + latlng.latitude);
			edit.putString("end_long", "" + latlng.longitude);
			edit.commit();
			
			TextView bubble = (TextView)findViewById(R.id.bubbleText);
			bubble.setText("Confirm Route");
			
			LatLng start = new LatLng(Double.parseDouble(bubbleState.getString("start_lat", "0")), Double.parseDouble(bubbleState.getString("start_long", "0")));
			
			AsyncTask<LatLng, Void, ArrayList<LatLng>> directionsTask = new AsyncTask<LatLng, Void, ArrayList<LatLng>>() {

				@Override
				protected ArrayList<LatLng> doInBackground(LatLng... locations) {
					GMapV2Direction md = new GMapV2Direction();
					mMap = ((MapFragment) getFragmentManager()
					                    .findFragmentById(R.id.map)).getMap();
					Document doc = md.getDocument(locations[0], locations[1],
					                    GMapV2Direction.MODE_WALKING);

					return md.getDirection(doc);
				}
				
				@Override 
				protected void onPostExecute(ArrayList<LatLng> directionPoint)
				{
					PolylineOptions rectLine = new PolylineOptions().width(3).color(Color.RED);

					for (int i = 0; i < directionPoint.size(); i++) {
						rectLine.add(directionPoint.get(i));
					}
					Polyline polylin = mMap.addPolyline(rectLine);
				}
			};
			//TODO: Add loading bar under actionbar
			directionsTask.execute(start, latlng);
			
			mBubbleState = BubbleState.CONFIRM;
		} else if(mBubbleState == BubbleState.CONFIRM)
		{
			AsyncHttpClient client = new AsyncHttpClient();
		
			String time = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
			String userName = name+numRequests;
			numRequests++;
			
			Location location = mLocationClient.getLastLocation(); 
			
			SharedPreferences bubbleState = getSharedPreferences("bubbleState", MODE_PRIVATE);
			double start_lat = Double.parseDouble(bubbleState.getString("start_lat", "0"));
			double start_long = Double.parseDouble(bubbleState.getString("start_long", "0"));
			double end_lat = Double.parseDouble(bubbleState.getString("end_lat", "0"));
			double end_long = Double.parseDouble(bubbleState.getString("end_long", "0"));
			
			Requester r = new Requester(userName, time,"219-933-2201", "Not Urgent", start_lat, start_long, end_lat, end_long);
			Log.d("json", r.toJSON().toString());
			StringEntity se = null;
			
			AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler(){
				public void onSuccess(String suc){
					Log.d("response", suc);
				}
				
			    @Override
			    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error)
			    {
			    	Toast.makeText(getApplicationContext(), "No connection to server", Toast.LENGTH_LONG).show();
			    	Log.d("failure", Integer.toString(statusCode));
			    }
			};
			
	        try {
				se = new StringEntity(r.toJSON().toString());
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			hostname = PreferenceManager.getDefaultSharedPreferences(this).getString("pref_server", "http://optical-sight-386.appspot.com");
	        client.post(getBaseContext(), hostname+"/request", se, "application/json", handler);
	        Log.d("debug", client.toString());
	        
	        TextView bubble = (TextView)findViewById(R.id.bubbleText);
			bubble.setText("Request Pickup Location");
			
			mMap.clear();
	        mBubbleState = BubbleState.START;
		}
	}
}
