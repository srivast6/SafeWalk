package edu.purdue.SafeWalk;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class CustomMapFragment extends MapFragment{
        public View mOriginalContentView;
        public TouchableWrapper mTouchView;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
                mOriginalContentView = super.onCreateView(inflater, parent, savedInstanceState);
                mTouchView = new TouchableWrapper(getActivity());
                mTouchView.addView(mOriginalContentView);
                return mTouchView;
        }

        @Override
        public View getView() {
                return mOriginalContentView;
        }
        
        public LatLng dropPinAtCenter(Activity activity, String name, float hue)
        {
        	GoogleMap mMap;
    		mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
    		
    		Projection p = mMap.getProjection();
    		
    		int[] location = new int[2]; 
    		View mapPin = activity.findViewById(R.id.mapPin);
    		mapPin.getLocationOnScreen(location);
    		Point point = new Point (location[0], location[1]);
    		
    		activity.findViewById(R.id.mapFrame).getLocationOnScreen(location);
    		point.offset(-location[0], -location[1]);
    		
    		point.offset(mapPin.getWidth()/2, mapPin.getHeight());
    		
    		LatLng latlng = p.fromScreenLocation(point);
    		
    		mMap.addMarker(new MarkerOptions()
    		    .position(latlng)
    		    .title(name)
    		    .icon(BitmapDescriptorFactory.defaultMarker(hue)));
    		
    		return latlng;
        }
}