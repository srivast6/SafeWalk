package edu.purdue.safewalk;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.IntentSender.SendIntentException;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.CancelableCallback;
import com.google.android.gms.maps.GoogleMap.SnapshotReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import edu.purdue.safewalk.Fragments.ListViewRequesterFragment;
import edu.purdue.safewalk.Fragments.MakeRequestFragment;
import edu.purdue.safewalk.Fragments.WalkRequestFragment;
import edu.purdue.safewalk.GCM.CloudMessaging;
import edu.purdue.safewalk.settings.SettingsActivity;
import edu.purdue.safewalk.Activities.*;


public class SafeWalk extends Activity implements
		GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener{
	
	public GoogleMap mMap;
	private LocationClient mLocationClient;
	ListView drawerList;
	DrawerLayout drawerLayout;
	ActionBarDrawerToggle mDrawerToggle;
	private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
	int numRequests; 
	static final String name = "John Doe";
	private static final String TAG = "SafeWalk";
	private static final String PACKAGE = "edu.purdue.SafeWalk";
	private static final int LOGIN_REQUEST = 1223523513;
	public static String hostname;
	private CloudMessaging cm;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.main_activity);

		hostname = PreferenceManager.getDefaultSharedPreferences(this).getString("pref_server", getString(R.string.pref_server_default));
		
		initNavDrawer();
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		// Check for Google Play Services
		// TODO: This may be throwing the error in the log. EDIT[DT]: It doesn't seem to be...
		int googlePlayServicesAvailable = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(this);
		if (googlePlayServicesAvailable != ConnectionResult.SUCCESS) {
			Log.v(TAG, "Google Play availability" + googlePlayServicesAvailable);
			GooglePlayServicesUtil.getErrorDialog(googlePlayServicesAvailable,
					this, CONNECTION_FAILURE_RESOLUTION_REQUEST).show();
		}

		mLocationClient = new LocationClient(this, this, this);

		cm = new CloudMessaging(this.getApplicationContext(), this.getSharedPreferences("pref_profile", MODE_PRIVATE));
		
		pushMapFragment();
	}

	private void pushMapFragment() 
	{
		WalkRequestFragment walkRequestFragment = new WalkRequestFragment();
        
        // Add the fragment to the 'fragment_container' FrameLayout
        getFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, walkRequestFragment).addToBackStack("MAP_FRAGMENT").commit(); //setTransition(FragmentTransaction.TRANSIT_NONE)
        getActionBar().setSubtitle("Map");
	}

	private void initNavDrawer() {
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawerList = (ListView) findViewById(R.id.drawer_list);
		final String[] menuItems = new String[] { "What is SafeWalk?", "People", "Safewalk Personnel", "Settings" };
		drawerList.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, menuItems));
		drawerList.setOnItemClickListener(new ListView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int order,
					long arg3) {
				DrawerLayout mDrawerLayout;
				mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
				mDrawerLayout.closeDrawers();
				
				String item = menuItems[order];
				
				if (item.equals("People")) {
					openRequestList();
				}else if(item.equals("Settings")){
					openSettings();

				}else if(item.equals("Safewalk Personnel")){
					Intent Safewalk_Personnel = new Intent(SafeWalk.this,
							MapPoliceActivity.class);
					SafeWalk.this.startActivity(Safewalk_Personnel);
				}else if(item.equals("What is SafeWalk?")){
					Intent Safewalk_About = new Intent(SafeWalk.this,
							AboutActivity.class);
					SafeWalk.this.startActivity(Safewalk_About);
				} else {
					Toast.makeText(SafeWalk.this,
							"This feature is under construction\nOr unavailable.",
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		final String title, drawerTitle;
		title = drawerTitle = (String) getTitle();

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

	public void openRequestList()
	{
		String fragmentTag = getFragmentManager().getBackStackEntryAt(getFragmentManager().getBackStackEntryCount() - 1).getName();
		if(!fragmentTag.equals("REQUESTS_FRAGMENT"))
		{
	        // Create a new Fragment to be placed in the activity layout
	        ListViewRequesterFragment requesterFragment = new ListViewRequesterFragment();
	        
	        // Add the fragment to the 'fragment_container' FrameLayout
	        getFragmentManager().beginTransaction()
	                .add(R.id.fragmentContainer, requesterFragment).addToBackStack("REQUESTS_FRAGMENT").commit(); //setTransition(FragmentTransaction.TRANSIT_NONE)
		}
		else 
		{
			ListViewRequesterFragment requesterFragment = (ListViewRequesterFragment) getFragmentManager().findFragmentByTag("REQUESTS_FRAGMENT");
			requesterFragment.getListView().invalidate();
		}
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
		boolean drawerOpen = drawerLayout.isDrawerOpen(drawerLayout.getChildAt(1));
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
		
		
		switch (item.getItemId()) {
        case R.id.action_display_blue:
        	SharedPreferences myPreference= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    		SharedPreferences.Editor editor = myPreference.edit();
        	item.setChecked(!item.isChecked());
    		editor.putBoolean("show_blue_lights", item.isChecked());
    		editor.apply();
            return true;
        case R.id.action_display_vols:
        	SharedPreferences _myPreference= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    		SharedPreferences.Editor _editor = _myPreference.edit();
        	item.setChecked(!item.isChecked());
    		_editor.putBoolean("show_volunteers", item.isChecked());
    		_editor.apply();
            return true;
        case R.id.action_settings:
        	openSettings();
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
	
	@Override
	public void onBackPressed()
	{
		FragmentManager fm = getFragmentManager();
		if(fm.getBackStackEntryCount() > 1) 
		{
			fm.popBackStack();
		}
		else 
			finish();
	}
	
	@Override
	public void onConnected(Bundle bun) {
		Log.d("SafeWalk", "The mLocationHandler has been connected!");
        
		String fragmentTag = getFragmentManager().getBackStackEntryAt(getFragmentManager().getBackStackEntryCount() - 1).getName();
		if(fragmentTag.equals("MAP_FRAGMENT"))
		{
			Log.v(TAG,"Zooming camera!!!");
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
	}

	@Override
	public void onDisconnected() {
		
		Log.d("SafeWalk", "The mLocationHandler has been disconnected...");
		
		mLocationClient.connect();
	}
	
	public void openRequestActivity()
	{
		//View mapView = findViewById(R.id.mapFrame);
		
        // Create a new Fragment to be placed in the activity layout
        final MakeRequestFragment requestFragment = new MakeRequestFragment();
        

        SharedPreferences bubbleState = getSharedPreferences("bubbleState", Activity.MODE_PRIVATE);
        
        LatLng start = new LatLng(Double.parseDouble(bubbleState.getString("start_lat", "0")), Double.parseDouble(bubbleState.getString("start_long", "0")));
        LatLng end = new LatLng(Double.parseDouble(bubbleState.getString("end_lat", "0")), Double.parseDouble(bubbleState.getString("end_long", "0")));
        
        
        //Calculate the markers to get their position
        LatLngBounds.Builder lb = new LatLngBounds.Builder();
        
        lb.include(start);
        lb.include(end);
        
        LatLngBounds bounds = lb.build();
        //Change the padding as per needed
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 20);
        mMap.animateCamera(cu, new CancelableCallback() {

			@Override
			public void onCancel() {
				mMap.snapshot((SnapshotReadyCallback) requestFragment);
				mMap.clear();
			}

			@Override
			public void onFinish() {
				mMap.snapshot((SnapshotReadyCallback) requestFragment);
				mMap.clear();
			}
        	
        });
        
        Bundle b = new Bundle();
        
		b.putDouble("start_lat", start.latitude);
		b.putDouble("start_long", start.longitude);
		b.putDouble("end_lat", end.latitude);
		b.putDouble("end_long", end.longitude);
		
        // In case this activity was started with special instructions from an
        // Intent, pass the Intent's extras to the fragment as arguments
        requestFragment.setArguments(b);
        
        // Add the fragment to the 'fragment_container' FrameLayout
        getFragmentManager().beginTransaction()
                .add(R.id.fragmentContainer, requestFragment).addToBackStack("REQUEST_FRAGMENT").commit(); //setTransition(FragmentTransaction.TRANSIT_NONE)
		
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
}
