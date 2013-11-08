package edu.purdue.app.map;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.*;

import edu.purdue.app.R;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class MapActivity extends Activity {
	
	DrawerLayout drawer;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_mainview);
		
		// Initialize the gms maps services
		try {
			MapsInitializer.initialize(this);
		} catch (GooglePlayServicesNotAvailableException e) {
			e.printStackTrace();
		}
		
		// Draw a mapfragment onto the screen
		MapFragment mf = (MapFragment) getFragmentManager().findFragmentById(R.id.map_slot_googlemap_expanded);
		
		// Set the map's default location
		mf.getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(40.427231,-86.916683), 14.7f));
		
		// Prepare the drawer layout for button clicks
		drawer = (DrawerLayout) findViewById(R.id.map_drawer_layout);
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = this.getMenuInflater();
		inflater.inflate(R.menu.map_actionbarmenu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.map_actionbar_listbuildings:
			if (drawer.isDrawerOpen(Gravity.RIGHT)) {
				drawer.closeDrawers();
			} else {
				drawer.openDrawer(Gravity.RIGHT);
			}
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	
	
}
