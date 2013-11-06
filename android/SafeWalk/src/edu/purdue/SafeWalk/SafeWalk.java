package edu.purdue.SafeWalk;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
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
        mLocationClient.disconnect();
        mLocationClient.connect();
        
        mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();        
        if(mMap != null) {
        	mMap.setMyLocationEnabled(true);
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
	public void onConnectionFailed(ConnectionResult res) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onConnected(Bundle bun) {
		CameraPosition.Builder cameraPositionBuilder = new CameraPosition.Builder();
        cameraPositionBuilder.target(new LatLng(mLocationClient.getLastLocation().getLatitude(), mLocationClient.getLastLocation().getLongitude()));
        cameraPositionBuilder.zoom(12);
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPositionBuilder.build()));
        mLocationClient.disconnect();
	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub
	}
	public void onClick(){
		System.out.print("Bitch");
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setData(Uri.parse("tel:3174573102"));
			startActivity(intent);
}
}
