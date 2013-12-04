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

	JSONObject json = null;
	List<String> categoryList;
	List<Location> academicBuildings, adminBuildings, resHalls, diningCourts, miscBuildings;
	Map<MapActivity.DrawerState, List<Location>> stateListMap = new HashMap<DrawerState, List<Location>>();
	
	class Location {
		String full_name, short_name;
		double lat, lng;
	}
	
	public MapData(Resources r) {
		// Open JSON file for map data
		InputStream jsonFile = r.openRawResource(R.raw.buildings);
		BufferedReader bfr = new BufferedReader(new InputStreamReader(jsonFile));
		
		// Read in the map data and create the JSON object
		String line = null;
		StringBuilder sb = new StringBuilder();
		try {
			while ((line = bfr.readLine()) != null) {
				sb.append(line);
			}
			json = new JSONObject(sb.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Create the list of academic locations
		academicBuildings = new ArrayList<Location>();
		adminBuildings = new ArrayList<Location>();
		resHalls = new ArrayList<Location>();
		diningCourts = new ArrayList<Location>();
		miscBuildings = new ArrayList<Location>();
		
		try {
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
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		// Prepare the category list
		categoryList = new ArrayList<String>();
		categoryList.add("Academic Buildings");
		categoryList.add("Administrative Buildings");
		categoryList.add("Residence Halls");
		categoryList.add("Dining Courts");
		categoryList.add("Misc. Buildings");
		
		stateListMap.put(MapActivity.DrawerState.ACAD_BUILDINGS, academicBuildings);
		stateListMap.put(MapActivity.DrawerState.ADMIN_BUILDINGS, adminBuildings);
		stateListMap.put(MapActivity.DrawerState.RES_HALLS, resHalls);
		stateListMap.put(MapActivity.DrawerState.DINING_COURTS, diningCourts);
		stateListMap.put(MapActivity.DrawerState.MISC_BUILDINGS, miscBuildings);
		
	}
	
	/** Searches for a building in the map data given a string 
	 *  Very poorly implemented right now, will be improved later. */
	public Location search(String s) {
		s = s.toLowerCase();
		
		for (Location b : academicBuildings) {
			if (b.short_name.toLowerCase().equals(s)) {
				return b;
			}
		}
		for (Location b : adminBuildings) {
			if (b.short_name.toLowerCase().equals(s)) {
				return b;
			}
		}
		for (Location b : resHalls) {
			if (b.short_name.toLowerCase().equals(s)) {
				return b;
			}
		}
		for (Location b : diningCourts) {
			if (b.short_name.toLowerCase().equals(s)) {
				return b;
			}
		}
		for (Location b : miscBuildings) {
			if (b.short_name.toLowerCase().equals(s)) {
				return b;
			}
		}
		
		return null;
	}
	
	
	
}
