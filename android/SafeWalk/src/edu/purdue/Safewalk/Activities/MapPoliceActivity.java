package edu.purdue.Safewalk.Activities;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;

import edu.purdue.SafeWalk.R;
import edu.purdue.SafeWalk.SafeWalk;
import edu.purdue.SafeWalk.R.drawable;
import edu.purdue.SafeWalk.R.id;
import edu.purdue.SafeWalk.R.layout;
import edu.purdue.SafeWalk.R.string;
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
		
		mapPolice.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(40.4173698, -86.8765488), 16));

		// You can customize the marker image using images bundled with
		// your app, or dynamically generated bitmaps.
		mapPolice.addMarker(new MarkerOptions()
		.icon(BitmapDescriptorFactory
				.fromResource(R.drawable.map_pin))
		.anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
		.position(new LatLng(40.4183698, -86.8765388)));
		
		mapPolice.addMarker(new MarkerOptions()
		.icon(BitmapDescriptorFactory
				.fromResource(R.drawable.map_pin))
		.anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
		.position(new LatLng(40.5183678, -86.8265378)));
		
		mapPolice.addMarker(new MarkerOptions()
		.icon(BitmapDescriptorFactory
				.fromResource(R.drawable.map_pin))
		.anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
		.position(new LatLng(41.4183698, -86.8765300)));
		
		mapPolice.addMarker(new MarkerOptions()
		.icon(BitmapDescriptorFactory
				.fromResource(R.drawable.map_pin))
		.anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
		.position(new LatLng(40.4083698, -86.8769088)));
		
		mapPolice.addMarker(new MarkerOptions()
		.icon(BitmapDescriptorFactory
				.fromResource(R.drawable.map_pin))
		.anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
		.position(new LatLng(40.4183608, -86.8165385)));
		
		mapPolice.addMarker(new MarkerOptions()
		.icon(BitmapDescriptorFactory
				.fromResource(R.drawable.map_pin))
		.anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
		.position(new LatLng(40.4180697, -86.1765378)));
		
		
		mapPolice.addMarker(new MarkerOptions()
		.icon(BitmapDescriptorFactory
				.fromResource(R.drawable.map_pin))
		.anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
		.position(new LatLng(40.52345,-86.9013452)));
		
		mapPolice.addMarker(new MarkerOptions()
		.icon(BitmapDescriptorFactory
				.fromResource(R.drawable.map_pin))
		.anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
		.position(new LatLng(40.48746,-86.809823)));
		
		mapPolice.addMarker(new MarkerOptions()
		.icon(BitmapDescriptorFactory
				.fromResource(R.drawable.map_pin))
		.anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
		.position(new LatLng(40.61885,-86.72345)));
		
		mapPolice.addMarker(new MarkerOptions()
		.icon(BitmapDescriptorFactory
				.fromResource(R.drawable.map_pin))
		.anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
		.position(new LatLng(40.723467,-86.602362)));
	}
}