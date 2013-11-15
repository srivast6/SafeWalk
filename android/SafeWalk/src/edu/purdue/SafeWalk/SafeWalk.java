package edu.purdue.SafeWalk;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

public class SafeWalk extends Activity implements
GooglePlayServicesClient.ConnectionCallbacks,
GooglePlayServicesClient.OnConnectionFailedListener
{
	private GoogleMap mMap;
	private LocationClient mLocationClient;
	ListView drawerList; 
	DrawerLayout drawerLayout;
	ActionBarDrawerToggle mDrawerToggle;
	private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);

		ViewGroup v = (ViewGroup) findViewById(R.id.content_frame);
		LayoutInflater linflate = getLayoutInflater();
		View contentpane = linflate.inflate(R.layout.map, v);

		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawerList = (ListView) findViewById(R.id.left_drawer);
		String[] menuItems = new String[] { "Settings", "About", "What is SafeWalk?", "Walker Activity" };
		drawerList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, menuItems));
		drawerList.setOnItemClickListener(new ListView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int order,
					long arg3)
			{
				if(order == 3) {
					Intent i = new Intent(SafeWalk.this, WalkerActivity.class);
					SafeWalk.this.startActivity(i);
				} else {
					Toast.makeText(SafeWalk.this, "This feature is under construction", Toast.LENGTH_SHORT).show();
				}
			}

		});

		final String title, drawerTitle;
		title = drawerTitle = (String) getTitle();

		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
				R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close) {

			/** Called when a drawer has settled in a completely closed state. */
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(title);
				invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
			}

			/** Called when a drawer has settled in a completely open state. */
			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(drawerTitle);
				invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
			}
		};

		// Set the drawer toggle as the DrawerListener
		drawerLayout.setDrawerListener(mDrawerToggle);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		// Check for Google Play Services
		int googlePlayServicesAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		if(googlePlayServicesAvailable != ConnectionResult.SUCCESS) {
			GooglePlayServicesUtil.getErrorDialog(googlePlayServicesAvailable, this, CONNECTION_FAILURE_RESOLUTION_REQUEST).show();
		}

		mLocationClient = new LocationClient(this, this, this);
		mLocationClient.connect();

		mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		if(mMap != null) {
			mMap.setMyLocationEnabled(true);
			UiSettings mapSettings = mMap.getUiSettings();
			mapSettings.setTiltGesturesEnabled(false);
			mapSettings.setRotateGesturesEnabled(false);
		} else {
			AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
			alertBuilder.setTitle("Error!");
			alertBuilder.setMessage(this.getResources().getText(R.string.error_no_maps));
			alertBuilder.setPositiveButton("Close " + this.getResources().getText(R.string.app_name), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					SafeWalk.this.finish();
				}
			});
			alertBuilder.show();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if(mLocationClient != null && mLocationClient.isConnected()) {
			mLocationClient.disconnect();
		}
	}

	/**
	 * Called when we fail to connect to the mLocationClient.
	 */
	@Override
	public void onConnectionFailed(ConnectionResult res) {
		if(res.isSuccess() == true)
			return;

		if(res.hasResolution() == true) {
			try {
				res.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
			} catch (SendIntentException e) {
				e.printStackTrace();
				this.onConnectionFailed(res); //HACK: Possible stack overflow.
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
		// If the nav drawer is open, hide action items related to the content view
		boolean drawerOpen = drawerLayout.isDrawerOpen(drawerList);
		//menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
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
		// Handle your other action bar items...
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onConnected(Bundle bun) {
		CameraPosition.Builder cameraPositionBuilder = new CameraPosition.Builder();
		cameraPositionBuilder.target(new LatLng(mLocationClient.getLastLocation().getLatitude(), mLocationClient.getLastLocation().getLongitude()));
		cameraPositionBuilder.zoom((float) 16);
		mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPositionBuilder.build()));
		mLocationClient.disconnect();
	}

	@Override
	public void onDisconnected() {
		mLocationClient.connect();
	}
}
