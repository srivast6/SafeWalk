package edu.purdue.app.map;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.*;

import android.content.res.Resources;

import edu.purdue.app.R;
import edu.purdue.app.map.MapActivity.DrawerState;

public class MapData {

	public List<String> categoryList;
	public List<Location> academicBuildings, adminBuildings, resHalls, diningCourts, miscBuildings, bikeRacks;
	public Map<MapActivity.DrawerState, List<Location>> stateListMap = new HashMap<DrawerState, List<Location>>();
	
	/** A location which includes a full and short name and a lat/long pair */
	class Location {
		String full_name, short_name;
		double lat, lng;
	}
	
	/** Constructor which parses the included json files into Location array lists */
	public MapData(Resources r) {
		// Create buffered readers for each JSON file
		BufferedReader buildingsBFR = new BufferedReader(new InputStreamReader(r.openRawResource(R.raw.buildings)));
		BufferedReader bikeracksBFR = new BufferedReader(new InputStreamReader(r.openRawResource(R.raw.bikeracks)));
		
		// Create the JSON objects for each file
		JSONObject buildingsJSON = null;
		JSONObject bikeracksJSON = null;
		
		// Read in the data from each file and store it in the JSON object
		String line = null;
		StringBuilder sb = new StringBuilder();
		try {
			
			// Read in building data
			while ((line = buildingsBFR.readLine()) != null) {
				sb.append(line);
			}
			buildingsJSON = new JSONObject(sb.toString());
			sb = new StringBuilder();
			
			// Read in bike rack data
			while ((line = bikeracksBFR.readLine()) != null) {
				sb.append(line);
			}
			bikeracksJSON = new JSONObject(sb.toString());
			sb = new StringBuilder();
			
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Create the list structures for each type of data
		academicBuildings = new ArrayList<Location>();
		adminBuildings = new ArrayList<Location>();
		resHalls = new ArrayList<Location>();
		diningCourts = new ArrayList<Location>();
		miscBuildings = new ArrayList<Location>();
		bikeRacks = new ArrayList<Location>();
		
		// Parse the JSON objects and populate each list
		try {
			parseBuildingJSON(buildingsJSON);
			parseBikeracksJSON(bikeracksJSON);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		// categoryList stores all off the top-tier category strings for the side drawer
		categoryList = new ArrayList<String>();
		categoryList.add("Academic Buildings");
		categoryList.add("Administrative Buildings");
		categoryList.add("Residence Halls");
		categoryList.add("Dining Courts");
		categoryList.add("Misc. Buildings");
		
		// stateListMap is a Map which ties a MapActivity.DrawerState to an aforementioned Location list
		stateListMap.put(MapActivity.DrawerState.ACAD_BUILDINGS, academicBuildings);
		stateListMap.put(MapActivity.DrawerState.ADMIN_BUILDINGS, adminBuildings);
		stateListMap.put(MapActivity.DrawerState.RES_HALLS, resHalls);
		stateListMap.put(MapActivity.DrawerState.DINING_COURTS, diningCourts);
		stateListMap.put(MapActivity.DrawerState.MISC_BUILDINGS, miscBuildings);
		
	}
	
	/** Parses building data from a json object */
	private void parseBuildingJSON(JSONObject json) throws JSONException {
		JSONArray ja = json.getJSONArray("academic_buildings");
		for (int i = 0; i < ja.length(); i++) {
			JSONObject j = ja.getJSONObject(i);
			Location b = new Location();
			b.full_name = j.getString("full_nm");
			b.short_name = j.getString("short_nm");
			b.lat = j.getDouble("lat");
			b.lng = j.getDouble("lng");
			academicBuildings.add(b);
		}
		ja = json.getJSONArray("admin_buildings");
		for (int i = 0; i < ja.length(); i++) {
			JSONObject j = ja.getJSONObject(i);
			Location b = new Location();
			b.full_name = j.getString("full_nm");
			b.short_name = j.getString("short_nm");
			b.lat = j.getDouble("lat");
			b.lng = j.getDouble("lng");
			adminBuildings.add(b);
		}
		ja = json.getJSONArray("res_halls");
		for (int i = 0; i < ja.length(); i++) {
			JSONObject j = ja.getJSONObject(i);
			Location b = new Location();
			b.full_name = j.getString("full_nm");
			b.short_name = j.getString("short_nm");
			b.lat = j.getDouble("lat");
			b.lng = j.getDouble("lng");
			resHalls.add(b);
		}
		ja = json.getJSONArray("dining_courts");
		for (int i = 0; i < ja.length(); i++) {
			JSONObject j = ja.getJSONObject(i);
			Location b = new Location();
			b.full_name = j.getString("full_nm");
			b.short_name = j.getString("short_nm");
			b.lat = j.getDouble("lat");
			b.lng = j.getDouble("lng");
			diningCourts.add(b);
		}
		ja = json.getJSONArray("misc");
		for (int i = 0; i < ja.length(); i++) {
			JSONObject j = ja.getJSONObject(i);
			Location b = new Location();
			b.full_name = j.getString("full_nm");
			b.short_name = j.getString("short_nm");
			b.lat = j.getDouble("lat");
			b.lng = j.getDouble("lng");
			miscBuildings.add(b);
		}
	}
	
	/** Parses bike rack data from a json object */
	private void parseBikeracksJSON(JSONObject json) throws JSONException {
		JSONArray ja = json.getJSONArray("bike_racks");
		for (int i = 0; i < ja.length(); i++) {
			JSONObject j = ja.getJSONObject(i);
			Location rack = new Location();
			rack.full_name = "?";
			rack.short_name = "?";
			rack.lat = j.getDouble("lat");
			rack.lng = j.getDouble("long");
			bikeRacks.add(rack);
		}
	}
	
	/** Searches for a building in the map data given a string 
	 *  Very poorly implemented right now, will be improved later. */
	public Location search(String s) {
		s = s.toLowerCase();
		Location result;
		
		if ((result = searchExactMatch(s)) != null) {
			return result;
		}
		if ((result = searchPartialMatch(s)) != null) {
			return result;
		}
		
		return null;
	}
	
	/** Finds exact string matches (not including case) across all the buildings */
	private Location searchExactMatch(String s) {
		for (Location b : academicBuildings) {
			if (b.short_name.toLowerCase().equals(s)) {
				return b;
			}
			if (b.full_name.toLowerCase().equals(s)) {
				return b;
			}
		}
		for (Location b : adminBuildings) {
			if (b.short_name.toLowerCase().equals(s)) {
				return b;
			}
			if (b.full_name.toLowerCase().equals(s)) {
				return b;
			}
		}
		for (Location b : resHalls) {
			if (b.short_name.toLowerCase().equals(s)) {
				return b;
			}
			if (b.full_name.toLowerCase().equals(s)) {
				return b;
			}
		}
		for (Location b : diningCourts) {
			if (b.short_name.toLowerCase().equals(s)) {
				return b;
			}
			if (b.full_name.toLowerCase().equals(s)) {
				return b;
			}
		}
		for (Location b : miscBuildings) {
			if (b.short_name.toLowerCase().equals(s)) {
				return b;
			}
			if (b.full_name.toLowerCase().equals(s)) {
				return b;
			}
		}
		return null;
	}
	
	/** Finds partial string matches across all the buildings */
	private Location searchPartialMatch(String s) {
		for (Location b : academicBuildings) {
			if (b.full_name.toLowerCase().contains(s)) {
				return b;
			}
		}
		for (Location b : adminBuildings) {
			if (b.full_name.toLowerCase().contains(s)) {
				return b;
			}
		}
		for (Location b : resHalls) {
			if (b.full_name.toLowerCase().contains(s)) {
				return b;
			}
		}
		for (Location b : diningCourts) {
			if (b.full_name.toLowerCase().contains(s)) {
				return b;
			}
		}
		for (Location b : miscBuildings) {
			if (b.full_name.toLowerCase().contains(s)) {
				return b;
			}
		}
		return null;
	}
	
}
