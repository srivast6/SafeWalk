package edu.purdue.SafeWalk;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;

public class SafeWalk extends Activity
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
}
