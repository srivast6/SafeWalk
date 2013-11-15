package edu.purdue.app.map;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.json.*;

import android.content.res.Resources;

import edu.purdue.app.R;

public class MapData {

	JSONObject json = null;
	List<String> acad_bldngs;
	
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
		acad_bldngs = new ArrayList<String>();
		try {
			JSONArray ja = json.getJSONArray("academic_buildings");
			for (int i = 0; i < ja.length(); i++) {
				acad_bldngs.add(ja.getJSONObject(i).getString("short_nm"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	
	
}
