package edu.purdue.SafeWalk;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

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

	private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

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

	public void onClickSafeWalk(View view){
		Uri number = Uri.parse("tel:7654947233");
		Intent dial = new Intent(Intent.ACTION_DIAL, number);
		startActivity(dial);
	}

	public void onClickPolice(View view){
		Uri number = Uri.parse("tel:911");
		Intent dial = new Intent(Intent.ACTION_DIAL, number);
		startActivity(dial);
	}
}
