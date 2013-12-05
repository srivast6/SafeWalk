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
import edu.purdue.app.map.MapData.Location;
import android.app.Activity;
import android.app.SearchManager;
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
	
	/** Enum which stores the current state of the drawer in the activity 
	 *  Call populateDrawer(state) to change what the drawer is currently displaying */
	DrawerState currentState = DrawerState.CATEGORY_LIST;
	enum DrawerState {
		CATEGORY_LIST,
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
		mpd = new MapData(getResources());
		prepareDrawer();
	}
	
	/** Called when the user initializes a search */
	protected void onNewIntent(Intent i) {
		if (Intent.ACTION_SEARCH.equals(i.getAction())) {
			String query = i.getStringExtra(SearchManager.QUERY);
			search(query);
		}
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
			closeDrawer();
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View lv, int i, long arg3) {
		
		// If the state is on the category list, then we just populate the drawer with the category the user selected.
		if (currentState == DrawerState.CATEGORY_LIST) {
			switch (i) {
			case 0:
				populateDrawer(DrawerState.ACAD_BUILDINGS);
				return;
			case 1:
				populateDrawer(DrawerState.ADMIN_BUILDINGS);
				return;
			case 2:
				populateDrawer(DrawerState.RES_HALLS);
				return;
			case 3:
				populateDrawer(DrawerState.DINING_COURTS);
				return;
			case 4:
				populateDrawer(DrawerState.MISC_BUILDINGS);
				return;
			}
			
		// Otherwise it is on one of the sub-categories and we need to display a marker.
		} else {
			// Get the building the user selected and add a marker
			// The building data is saved in MapData as a Map structure which maps an Activity_State to a List<Building>.
			Location selected = mpd.stateListMap.get(currentState).get(i);
			LatLng latlng = new LatLng(selected.lat, selected.lng);
			addPinToMap(latlng, selected.short_name);
			closeDrawer();
			return;
		}
	}
	
	/** Prepares the side drawer by populating it with the category list */
	private void prepareDrawer() {
		// Prepare drawer layout
		drawer = (DrawerLayout) findViewById(R.id.map_drawer_layout);
		
		// Get listview in drawer, category list, and set onclicklister and array adapters
		listviewDrawerMaster = (ListView) findViewById(R.id.map_right_drawer);
		listviewDrawerMaster.setOnItemClickListener(this);
		populateDrawer(DrawerState.CATEGORY_LIST);
	}
	
	/** Opens the side drawer */
	private void openDrawer() {
		populateDrawer(DrawerState.CATEGORY_LIST);
		drawer.openDrawer(Gravity.RIGHT);
	}

	/** Closes the side drawer */
	private void closeDrawer() {
		populateDrawer(DrawerState.CATEGORY_LIST);
		drawer.closeDrawers();
	}
	
	/** Populates the drawer with a list depending on the desired state. Pass in an 
	 *  Activity_State enum and this method will populate the drawer with that state and also
	 *  set the currentState class variable */
	private void populateDrawer(DrawerState state) {
		List<String> list = new ArrayList<String>();
		
		switch (state) {
		case CATEGORY_LIST:
			list = mpd.categoryList;
			break;
		case ACAD_BUILDINGS:
			for (Location b : mpd.academicBuildings) {
				list.add(b.full_name);
			}
			break;
		case ADMIN_BUILDINGS:
			for (Location b : mpd.adminBuildings) {
				list.add(b.full_name);
			}
			break;
		case RES_HALLS:
			for (Location b : mpd.resHalls) {
				list.add(b.full_name);
			}
			break;
		case DINING_COURTS:
			for (Location b : mpd.diningCourts) {
				list.add(b.full_name);
			}
			break;
		case MISC_BUILDINGS:
			for (Location b : mpd.miscBuildings) {
				list.add(b.full_name);
			}
			break;
		}
		
		//listviewDrawerMaster.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list));
		listviewDrawerMaster.setAdapter(new MapDrawerListAdapter(this, list, this.getResources()));
		this.currentState = state;
	}
	
	// Adds a pin to the map and animates the camera to gracefully show that pin.
	private void addPinToMap(LatLng loc, String label) {
		// Clear the map
		gmap.clear();
		
		// Add the marker to the map
		gmap.addMarker(new MarkerOptions()
			.title(label)
			.position(loc));
					
		// The layout bounds are set to include two LatLng points: 
		// The location of the user and the latlng of the point they selected
		LatLngBounds.Builder builder;
		if (gmap.getMyLocation() != null) {
			
			LatLng myloc = new LatLng(gmap.getMyLocation().getLatitude(), gmap.getMyLocation().getLongitude());
			builder = LatLngBounds.builder().include(loc).include(myloc);
			
			// The camera is then animated to zoom to those points with a padding of 300
			gmap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 300));
			
		} else {
			// GPS is not available, so fall back to simpler functionality
			gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 15.5f));
		}
	}
	
	/** Does a search on a given string and adds a pin to the map if it is found.
	 *  Does nothing if the search returned nothing. */
	private void search(String s) {
		// Do the search, which is in MapData
		Location result = mpd.search(s);
		
		// The result is null if there are no results, so do nothing.
		if (result == null) {
			return;
		}
		
		// Add a pin to the map and toast the full name of the building 
		addPinToMap(new LatLng(result.lat, result.lng), result.full_name);
		Toast.makeText(this, result.full_name, Toast.LENGTH_SHORT).show();
	}
	
	
}
