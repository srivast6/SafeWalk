package edu.purdue.app.map;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.*;

import edu.purdue.app.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

public class MapActivity extends Activity {

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_mainview);
		
		GoogleMap map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(40.427231,-86.916683), 14.7f));
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = this.getMenuInflater();
		inflater.inflate(R.menu.map_actionbarmenu, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
}
