package edu.purdue.SafeWalk;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

public class MapPoliceActivity extends SafeWalk {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_police);
		GoogleMap mapPolice = ((MapFragment) getFragmentManager().findFragmentById(
				R.id.mapPolice)).getMap();

		if (mapPolice != null) {
			mapPolice.setMyLocationEnabled(true);
			UiSettings mapSettings = mapPolice.getUiSettings();
			mapSettings.setTiltGesturesEnabled(false);
			mapSettings.setRotateGesturesEnabled(true);
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
							MapPoliceActivity.this.finish();
						}
					});
			alertBuilder.show();
		}
		
		mapPolice.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(41.889,
				-87.622), 16));

		// You can customize the marker image using images bundled with
		// your app, or dynamically generated bitmaps.
		mapPolice.addMarker(new MarkerOptions()
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.ic_launcher))
				.anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
				.position(new LatLng(41.889, -87.622)));
	}
}