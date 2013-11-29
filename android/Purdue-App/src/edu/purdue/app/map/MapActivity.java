/*package edu.purdue.app.map;

import java.io.BufferedReader;
import java.io.InputStream;
import java.util.List;

import org.json.JSONObject;

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
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MapActivity extends Activity implements OnItemClickListener  {
	
	DrawerLayout drawer;
	ListView drawerLV;
	String[] categoryList;
	MapData mpd;
	
	Activity_State currentState = Activity_State.NORMAL;
	private enum Activity_State {
		NORMAL,
		ACAD_BUILDINGS,
	}
	
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
		
		// Set the map's default location (west lafayette)
		mf.getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(40.427231,-86.916683), 14.7f));
		
		// Prepare the drawer layout
		prepareDrawer();
	}
	
	private void prepareDrawer() {
		// Prepare drawer layout
		drawer = (DrawerLayout) findViewById(R.id.map_drawer_layout);
		
		// Get listview in drawer, category list, and set onclicklister and array adapters
		drawerLV = (ListView) findViewById(R.id.map_right_drawer);
		drawerLV.setOnItemClickListener(this);
		categoryList = getResources().getStringArray(R.array.map_drawer_categories);
		drawerLV.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, categoryList));
		
		// Parse the JSON to prepare for user click
		mpd = new MapData(getResources());
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
				drawerLV.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, this.categoryList));
				drawer.openDrawer(Gravity.RIGHT);
			}
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int i, long arg3) {
		switch (i) {
		case 0:
			// Academic Buildings
			drawerLV.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mpd.acad_bldngs));
			this.currentState = Activity_State.ACAD_BUILDINGS;
			break;
		case 1:
			// Administrative Buildings
			
			break;
		case 2:
			// Residence Halls
			
			break;
		case 3:
			// Dining Courts
			
			break;
		case 4:
			// Misc.
			
			break;
		}
		
	}

	@Override
	public void onBackPressed() {
		switch (this.currentState) {
		case ACAD_BUILDINGS:
			drawerLV.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, this.categoryList));
			break;
		default:
			super.onBackPressed();
			break;
		}
	}
	
	
}*/
