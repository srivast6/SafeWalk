package edu.purdue.app.map;

import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.*;
import com.google.android.gms.maps.model.LatLngBounds.Builder;

import edu.purdue.app.R;
import edu.purdue.app.map.MapData.Building;
import android.app.Activity;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
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
import android.widget.SearchView;
import android.widget.Toast;

public class MapActivity extends Activity implements OnItemClickListener  {
	
	DrawerLayout drawer;
	GoogleMap gmap;
	ListView listviewDrawerMaster;
	MapData mpd;
	String[] categoryList;
	
	Activity_State currentState = Activity_State.NORMAL;
	enum Activity_State {
		NORMAL,
		ACAD_BUILDINGS,
		ADMIN_BUILDINGS,
		RES_HALLS,
		DINING_COURTS,
		MISC_BUILDINGS
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_mainview);
		this.setTitle("Purdue Map");
		
		// Initialize the gms maps services
		try {
			MapsInitializer.initialize(this);
		} catch (GooglePlayServicesNotAvailableException e) {
			e.printStackTrace();
		}
		
		// Draw a mapfragment onto the screen
		MapFragment mf = (MapFragment) getFragmentManager().findFragmentById(R.id.map_slot_googlemap_expanded);
		
		// Set the map's default location (west lafayette)
		gmap = mf.getMap();
		gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(40.427231,-86.916683), 14.7f));
		gmap.setMyLocationEnabled(true);
		
		// Prepare the drawer layout
		prepareDrawer();
	}
	
	@Override
	protected void onNewIntent(Intent i) {
		if (Intent.ACTION_SEARCH.equals(i.getAction())) {
			String query = i.getStringExtra(SearchManager.QUERY);
			Toast.makeText(this, query, Toast.LENGTH_LONG).show();
		}
	}
	
	private void prepareDrawer() {
		// Prepare drawer layout
		drawer = (DrawerLayout) findViewById(R.id.map_drawer_layout);
		
		// Get listview in drawer, category list, and set onclicklister and array adapters
		listviewDrawerMaster = (ListView) findViewById(R.id.map_right_drawer);
		listviewDrawerMaster.setOnItemClickListener(this);
		categoryList = getResources().getStringArray(R.array.map_drawer_categories);
		listviewDrawerMaster.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, categoryList));
		
		// Parse the JSON to prepare for user click
		mpd = new MapData(getResources());
	}
	
	private void openDrawer() {
		listviewDrawerMaster.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, this.categoryList));
		this.currentState = Activity_State.NORMAL;
		drawer.openDrawer(Gravity.RIGHT);
	}

	private void closeDrawer() {
		listviewDrawerMaster.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, this.categoryList));
		this.currentState = Activity_State.NORMAL;
		drawer.closeDrawers();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = this.getMenuInflater();
		inflater.inflate(R.menu.map_actionbarmenu, menu);
		
		SearchManager searchman = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchView sv = (SearchView) menu.findItem(R.id.map_actionbar_searchbuildings).getActionView();
		sv.setSearchableInfo(searchman.getSearchableInfo(getComponentName()));
	    sv.setIconifiedByDefault(false);
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.map_actionbar_listbuildings:
			if (drawer.isDrawerOpen(Gravity.RIGHT)) {
				closeDrawer();
			} else {
				openDrawer();
			}
			break;
		case R.id.map_actionbar_searchbuildings:
			//onSearchRequested();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View lv, int i, long arg3) {
		if (currentState == Activity_State.NORMAL) {
			List<String> display = new ArrayList<String>();
			switch (i) {
			case 0:
				// Academic Buildings
				for (Building b : mpd.academicBuildings) {
					display.add(b.full_name);
				}
				listviewDrawerMaster.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, display));
				this.currentState = Activity_State.ACAD_BUILDINGS;
				return;
			case 1:
				// Administrative Buildings
				for (Building b : mpd.adminBuildings) {
					display.add(b.full_name);
				}
				listviewDrawerMaster.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, display));
				this.currentState = Activity_State.ADMIN_BUILDINGS;
				return;
			case 2:
				// Residence Halls
				for (Building b : mpd.resHalls) {
					display.add(b.full_name);
				}
				listviewDrawerMaster.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, display));
				this.currentState = Activity_State.RES_HALLS;
				return;
			case 3:
				// Dining Courts
				for (Building b : mpd.diningCourts) {
					display.add(b.full_name);
				}
				listviewDrawerMaster.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, display));
				this.currentState = Activity_State.DINING_COURTS;
				return;
			case 4:
				// Misc.
				for (Building b : mpd.miscBuildings) {
					display.add(b.full_name);
				}
				listviewDrawerMaster.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, display));
				this.currentState = Activity_State.MISC_BUILDINGS;
				return;
			}
		} else {
			
			// Clear the map
			gmap.clear();
			
			// Get the building the user selected and add a marker
			Building selected = mpd.activityMap.get(currentState).get(i);
			LatLng latlng = new LatLng(selected.lat, selected.lng);
			gmap.addMarker(new MarkerOptions()
				.title(selected.short_name)
				.position(latlng));
			
			// The layout bounds are set to include two LatLng points: 
			// The location of the user and the latlng of the point they selected
			Builder llb = LatLngBounds.builder().include(latlng).include(new LatLng(gmap.getMyLocation().getLatitude(), gmap.getMyLocation().getLongitude()));
			// The camera is then animated to zoom to those points with a padding of 300
			gmap.animateCamera(CameraUpdateFactory.newLatLngBounds(llb.build(), 300));
			
			// Reset the state of the activity to normal+drawer closed
			currentState = Activity_State.NORMAL;
			closeDrawer();
			
			return;
		}
	}
	
	
}
