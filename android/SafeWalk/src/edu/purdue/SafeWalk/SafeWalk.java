package edu.purdue.SafeWalk;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.IntentSender.SendIntentException;
import android.content.res.Configuration;
import android.graphics.Point;
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
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import edu.purdue.SafeWalk.TouchableWrapper.UpdateMapAfterUserInterection;
import edu.purdue.SafeWalk.settings.SettingsActivity;

public class SafeWalk extends Activity implements
		GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener,
		UpdateMapAfterUserInterection{
	private GoogleMap mMap;
	private LocationClient mLocationClient;
	ListView drawerList;
	DrawerLayout drawerLayout;
	ActionBarDrawerToggle mDrawerToggle;
	private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
	boolean hasMoved;
	static double x;
	static double y;
	int numRequests; 
	static final String name = "John Doe";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.main_activity);

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
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		// Check for Google Play Services
		int googlePlayServicesAvailable = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(this);
		if (googlePlayServicesAvailable != ConnectionResult.SUCCESS) {
			GooglePlayServicesUtil.getErrorDialog(googlePlayServicesAvailable,
					this, CONNECTION_FAILURE_RESOLUTION_REQUEST).show();
		}

		mLocationClient = new LocationClient(this, this, this);
		if(mLocationClient.isConnected() == false || mLocationClient.isConnecting() == false) {
			mLocationClient.connect();
		}

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
		}
		
		
		// Handle your other action bar items...
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onConnected(Bundle bun) {
        if(mLocationClient == null || mMap == null) {
            Log.e("LocationClient", (mLocationClient == null ? "mLocationClient" : "mMap") + " is null!");
            return;
        }
		CameraPosition.Builder cameraPositionBuilder = new CameraPosition.Builder();
        if(mLocationClient.getLastLocation() == null) {
            Log.e("LocationClient", "Last location is null!");
            mLocationClient.disconnect();
            return;
        }
		cameraPositionBuilder.target(new LatLng(mLocationClient
				.getLastLocation().getLatitude(), mLocationClient
				.getLastLocation().getLongitude()));
		cameraPositionBuilder.zoom((float) 16);
		mMap.animateCamera(CameraUpdateFactory
				.newCameraPosition(cameraPositionBuilder.build()));
		x = mLocationClient.getLastLocation().getLatitude();
		y = mLocationClient.getLastLocation().getLongitude();
		//Log.d("set", "Setting x and y to " + x + " " + y);
		mLocationClient.disconnect();
	}

	@Override
	public void onDisconnected() {
		mLocationClient.connect();
	}



	@Override
	public void onMapDrag() {
		
		hasMoved = true;
		View mapPopUpLinLayout = findViewById(R.id.mapPopUpLinLayout);
		View mapPopUpView = findViewById(R.id.mapPopUpView1);
		mapPopUpLinLayout.setVisibility(View.INVISIBLE);
		mapPopUpView.setVisibility(View.INVISIBLE);
		
	}

	@Override
	public void onMapLift() {
		View mapPopUpLinLayout = findViewById(R.id.mapPopUpLinLayout);
		View mapPopUpView = findViewById(R.id.mapPopUpView1);
		mapPopUpLinLayout.setVisibility(View.VISIBLE);
		mapPopUpView.setVisibility(View.VISIBLE);
		// TODO Auto-generated method stub
		
	}
	
	/*
	 * Function used when a request to be picked up is map, send information to server
	 */
	public void onPopUpBubbleClick(View v){
		AsyncHttpClient client = new AsyncHttpClient();
		Projection p = mMap.getProjection();
		Point point = new Point ((int)x, (int)y);
		LatLng latlng = p.fromScreenLocation(point);
		String time = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
		String userName = name+numRequests;
		numRequests++;
		Requester r = new Requester(userName, time,"219-933-2201", "Not Urgent", latlng.latitude, latlng.longitude);
		StringEntity se = null;
		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler(){
			public void onSuccess(String suc){
				Log.d("response", suc);
			}
			
		    @Override
		     public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error)
		 {
		          Log.d("failure", Integer.toString(statusCode));
		     }
			
		};
		
        try {
			se = new StringEntity(r.toJSON().toString());
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        client.post(getBaseContext(), "http://192.168.1.68:8080", se, "application/json", handler);
        Log.d("debug", client.toString());
        
	}


		
	
		
	
}
