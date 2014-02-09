/**
 * This file has been copied from the Purdue App source code, licensed as open source. 
 * Used with permission. 
 */




/**
 * MapData
 * Class which provides interfaces to access the underlying JSON data. The actual
 * JSON parsing is done by MapJSONParser, and this class interfaces with that class
 * through the cursors it provides. 
 * 
 * To add a new building: Include it in the appropriate JSON file. That's it.
 * To add a new building category:
 * 	-> create a new json file for that category
 * 	-> define that json file in schemas.json
 * 	-> create a static string for that category here under static variables
 * 	-> modify getBuildingCategories(), getBuildings(), and getBuildings(String) appropriately
 * To add a new layer:
 *  -> create the json file and define it in schemas.json
 *  -> create a static string for that layer here under static variables
 *  -> modify getLayersList() and getLayerPoints(String) appropriately
 * To add a new bus route:
 *  -> create the json file and define it in schemas.json
 *  -> create a static string for that route here under static variables
 *  -> modify getBusRoutes() and getBusRoute(String) appropriately
 * 
 * Noticing a pattern? 
 * You dont ever have to touch MapJSONParser unless your JSON is really weird, and if your JSON
 * is formatted exactly like the others we already have (your bus route is like all the other bus
 * routes, as an example), it's like 4 lines of code to add in here. 
 */

package edu.purdue.SafeWalk;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.google.android.gms.maps.model.LatLng;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.widget.Toast;

public class MapData {

	/** ===== STATIC VARIABLES ===== */
	
	public final static double PURDUE_CENTER_LAT = 40.427231;
	public final static double PURDUE_CENTER_LNG = -86.916683;
	public final static double PURDUE_NORTHSIDE_BOUND_LAT = 40.5;
	public final static double PURDUE_SOUTHSIDE_BOUND_LAT = 40.3;
	public final static double PURDUE_WESTSIDE_BOUND_LNG = -87.0;
	public final static double PURDUE_EASTSIDE_BOUND_LNG = -86.8;
	public final static float PURDUE_CAMPUS_ZOOM = 14.7f;
	
	// Constants to access building categories
	public final static String BLDG_CAT_ACADEMIC = "Academic Buildings";
	public final static String BLDG_CAT_ADMIN = "Administrative Buildings";
	public final static String BLDG_CAT_RESHALL = "Residence Halls";
	public final static String BLDG_CAT_DINING = "Dining Courts";
	public final static String BLDG_CAT_PARKING = "Parking Garages";
	public final static String BLDG_CAT_MISC = "Misc. Buildings";
	
	// Constants for every layer recognized by the app
	public final static String LAYER_BIKERACKS = "Bike Racks";
	
	// Constants for every bus route recognized by the app
	public final static String BUSROUTE_GOLDLOOP = "Gold Loop";
	
	/** ===== STATIC METHODS ===== */
	
	/** Determines whether a given LatLng point is inside of the Purdue Campus. */
	public static boolean isNearPurdue(double lat, double lng) {
		if (lat <= PURDUE_NORTHSIDE_BOUND_LAT && lat >= PURDUE_SOUTHSIDE_BOUND_LAT) {
			if (lng <= PURDUE_EASTSIDE_BOUND_LNG && lng >= PURDUE_WESTSIDE_BOUND_LNG) {
				return true;
			}
		}
		return false;
	}
	
	/** Determines how far away two LatLng points are from one-another in kilometers. */
	public static double distanceBetween(double lat1, double lng1, double lat2, double lng2) {
		double dLat = Math.toRadians(lat2-lat1);
		double dLng = Math.toRadians(lng2-lng1);
		
		double step1 = Math.sin(dLat/2) * Math.sin(dLat/2) + 
				Math.sin(dLng/2) * Math.sin(dLng/2) * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));
		double step2 = 6371 * 2 * Math.atan2(Math.sqrt(step1), Math.sqrt(1-step1));
		
		return step2;
	}
	
	/** ===== INTERNAL CLASS DEFINITIONS ===== */
	
	class Building {
		String full_name, short_name;
		double lat, lng;
	}
	
	/** ===== OBJECT MEMBERS ===== */
	
	/** JSON Parser which contains all the data we need */
	private MapJSONParser json;
	
	/** Data structures which contain information about locations around campus 
	 *  Because this information never changes, these kind of act as "cache" structures; they
	 *  start as null and are generated whenever their respective getters are called. If the
	 *  getter is called again, it doesn't have to be generated again. */
	private List<String> buildingCategories, layersList, busRoutes;
	private List<Building> allBuildings;
	private List<Building> academicBuildings, adminBuildings, resHalls, diningCourts, parking, miscBuildings;
	private List<LatLng> bikeRacks;
	private List<LatLng> goldLoop;
	
	/** ===== OBJECT METHODS ===== */
	
	/** Constructor which parses the included json files into Location array lists */
	public MapData(Context c) {	
		json = new MapJSONParser(c);
	}
	
	/** Returns a printable list of all the different building categories */
	public List<String> getBuildingCategories() {
		if (buildingCategories != null) {
			return buildingCategories;
		}
		
		// Generate category list
		buildingCategories = new ArrayList<String>();
		buildingCategories.add(BLDG_CAT_ACADEMIC);
		buildingCategories.add(BLDG_CAT_ADMIN);
		buildingCategories.add(BLDG_CAT_RESHALL);
		buildingCategories.add(BLDG_CAT_DINING);
		buildingCategories.add(BLDG_CAT_PARKING);
		buildingCategories.add(BLDG_CAT_MISC);
		
		return buildingCategories;
	}
	
	/** Returns an unordered list of all the buildings on campus */
	public List<Building> getBuildings() {
		if (allBuildings != null) {
			return allBuildings;
		}
		
		allBuildings = new ArrayList<Building>();
		for (Building b : getBuildings(BLDG_CAT_ACADEMIC)) {
			allBuildings.add(b);
		}
		for (Building b : getBuildings(BLDG_CAT_ADMIN)) {
			allBuildings.add(b);
		}
		for (Building b : getBuildings(BLDG_CAT_RESHALL)) {
			allBuildings.add(b);
		}
		for (Building b : getBuildings(BLDG_CAT_DINING)) {
			allBuildings.add(b);
		}
		for (Building b : getBuildings(BLDG_CAT_PARKING)) {
			allBuildings.add(b);
		}
		for (Building b : getBuildings(BLDG_CAT_MISC)) {
			allBuildings.add(b);
		}
		
		return allBuildings;
	}
	
	/** Returns a list of buildings on campus under the specific category. 
	 *  Pass in one of the static BLDG_ strings in this class. */
	public List<Building> getBuildings(String category) {
		
		// Get the list and cursor for the category selected
		List<Building> selL = null;
		Cursor selC = null;
		
		if (category.equals(BLDG_CAT_ACADEMIC)) {
			selL = academicBuildings;
			selC = json.fileCursorMap.get("buildings_acad");
		} else if (category.equals(BLDG_CAT_ADMIN)) {
			selL = adminBuildings;
			selC = json.fileCursorMap.get("buildings_admin");
		} else if (category.equals(BLDG_CAT_RESHALL)) {
			selL = resHalls;
			selC = json.fileCursorMap.get("buildings_reshall");
		} else if (category.equals(BLDG_CAT_DINING)) {
			selL = diningCourts;
			selC = json.fileCursorMap.get("buildings_dining");
		} else if (category.equals(BLDG_CAT_PARKING)) {
			selL = parking;
			selC = json.fileCursorMap.get("buildings_parking");
		} else if (category.equals(BLDG_CAT_MISC)) {
			selL = miscBuildings;
			selC = json.fileCursorMap.get("buildings_misc");
		}
		
		// If the cursor is null, return null
		if (selC == null) {
			return null;
		}
		
		// If the list is not null, return it
		if (selL != null) {
			return selL;
		}
		
		// Otherwise, generate the building list
		selL = new ArrayList<Building>();
		selC.moveToFirst();
		do {
			Building b = new Building();
			b.short_name = selC.getString(0);
			b.full_name = selC.getString(1);
			b.lat = selC.getDouble(2);
			b.lng = selC.getDouble(3);
			selL.add(b);
		} while (selC.moveToNext());
		
		// Alphabetize the buildings 
		Collections.sort(selL, new Comparator<Building>() {
			public int compare(Building arg0, Building arg1) {
				return arg0.full_name.compareTo(arg1.full_name);
			}
		});
		
		return selL;
	}
	
	/** Returns a list of all the layers recognized by the application */
	public List<String> getLayersList() {
		if (layersList != null) {
			return layersList;
		}
		
		// Generate layers list
		layersList = new ArrayList<String>();
		layersList.add(LAYER_BIKERACKS);
		
		return layersList;
	}
	
	/** Returns a list of LatLng points for the requested layer
	 *  Use the static LAYER_ constants inside this class as your argument */
	public List<LatLng> getLayerPoints(String layer) {
		
		// Get the list and cursor for the layer selected
		List<LatLng> selL = null;
		Cursor selC = null;
		
		// Find the one we selected
		if (layer.equals(LAYER_BIKERACKS)) {
			selL = bikeRacks;
			selC = json.fileCursorMap.get("layer_bikeracks");
		}
		
		// If the cursor is null, return null
		if (selC == null) {
			return null;
		}
		
		// If the list isn't null, just return it
		if (selL != null) {
			return selL;
		}
		
		// Otherwise, generate the list
		selL = new ArrayList<LatLng>();
		selC.moveToFirst();
		do {
			double lat = selC.getDouble(0);
			double lng = selC.getDouble(1);
			LatLng point = new LatLng(lat, lng);
			selL.add(point);
		} while (selC.moveToNext());
		
		return selL;
	}
	
	/** Returns a printable list of all the bus routes recognized by the app */
	public List<String> getBusRoutes() {
		if (busRoutes != null) {
			return busRoutes;
		}
		
		// Generate the bus routes list
		busRoutes = new ArrayList<String>();
		busRoutes.add(BUSROUTE_GOLDLOOP);
		
		return busRoutes;
	}
	
	/** Returns the requested list of latlng points for a given bus route */
	public List<LatLng> getBusRoute(String route) {
		// Get a generic list and cursor 
		List<LatLng> selL = null;
		Cursor selC = null;
		
		// Find the one we selected
		if (route.equals(BUSROUTE_GOLDLOOP)) {
			selL = goldLoop;
			selC = json.fileCursorMap.get("bus_goldloop");
		}
		
		// If the cursor is null, return null
		if (selC == null) {
			return null;
		}
		
		// If the list isn't null, return it
		if (selL != null) {
			return selL;
		}
		
		// Otherwise, generate the list
		selL = new ArrayList<LatLng>();
		selC.moveToFirst();
		do {
			double lat = selC.getDouble(0);
			double lng = selC.getDouble(1);
			selL.add(new LatLng(lat, lng));
		} while (selC.moveToNext());
		
		return selL;
	}
	
	/** Searches for a building in the map data given a string 
	 *  Very poorly implemented right now, will be improved later. */
	public Building searchBuilding(String s) {
		s = s.toLowerCase();
		Building result;
		
		Log.d("MapData", "SEARCH QUERY" + s);
		for (Building b : getBuildings()) {
			Log.d("MapData", b.full_name);
		}
		
		
		if ((result = searchExactMatch(s)) != null) {
			return result;
		}
		if ((result = searchPartialMatch(s)) != null) {
			return result;
		}
		
		return null;
	}
	
	/** Finds exact string matches (not including case) across all the buildings */
	private Building searchExactMatch(String s) {
		for (Building b : getBuildings()) {
			if (b.full_name.toLowerCase().equals(s) || b.short_name.toLowerCase().equals(s)) {
				return b;
			}
		}
		return null;
	}
	
	/** Finds partial string matches across all the buildings */
	private Building searchPartialMatch(String s) {
		for (Building b : getBuildings()) {
			if (b.full_name.toLowerCase().contains(s) || b.short_name.toLowerCase().contains(s)) {
				return b;
			}
		}
		return null;
	}
	
}
