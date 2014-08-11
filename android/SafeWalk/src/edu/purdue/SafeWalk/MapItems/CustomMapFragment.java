package edu.purdue.SafeWalk.MapItems;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import edu.purdue.SafeWalk.R;
import edu.purdue.SafeWalk.R.id;
import edu.purdue.SafeWalk.Widgets.TouchableWrapper;

public class CustomMapFragment extends MapFragment {
	public View mOriginalContentView;
	public TouchableWrapper mTouchView;
	
	ArrayList<Marker> currentMarkers = new ArrayList<Marker>();
	
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	public LatLng dropPinAtCenter(Activity activity, String name, float hue) {
		GoogleMap mMap;
		mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();

		Projection p = mMap.getProjection();

		int[] location = new int[2];
		View mapPin = activity.findViewById(R.id.mapPin);
		mapPin.getLocationOnScreen(location);
		Point point = new Point(location[0], location[1]);

		activity.findViewById(R.id.map).getLocationOnScreen(location);
		point.offset(-location[0], -location[1]);

		point.offset(mapPin.getWidth() / 2, mapPin.getHeight());

		LatLng latlng = p.fromScreenLocation(point);

		mMap.addMarker(new MarkerOptions().position(latlng).title(name)
				.icon(BitmapDescriptorFactory.defaultMarker(hue)));

		return latlng;
	}
	
	
}