/**
 * This file has been copied from the Purdue App source code, licensed as open source. 
 * Used with permission. 
 */




/** MapJSONParser
 *  Automatically parses all JSON files in the app which have schema information
 *  provided in schemas.json. The parsed information is made available via cursors
 *  with fields named the same as the original json file. These cursors are located 
 *  in a map which maps the name of the json file (minus the extension) to the cursor.
 *  And if you forgot the name of your file, those are available here as well. 
 *  
 *  I've reorganized the way this part of the app handles its underlying data like 5 times
 *  now, and I think this will be the end-all fits-all way of doing it.
 *  Until next week. 
 */

package edu.purdue.safewalk.MapItems;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.purdue.safewalk.R;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.util.Log;

public class MapJSONParser {

	/** Global context */
	Context context;
	
	/** A list of all the json files registered in schemas.json */
	String[] jsonFiles;
	
	/** Map between a string in jsonFiles and its corresponding schema definition */
	Map<String, String[]> fileSchemaMap;
	
	/** Map between a string in jsonFiles and its corresponding parsed cursor */
	Map<String, Cursor> fileCursorMap;
	
	public MapJSONParser(Context c) {
		this.context = c;
		
		try {
			parseSchema();
			parseJSON();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}

	/** Parses schemas.json and fills jsonFiles and  the fileSchemaMap*/
	private void parseSchema() throws JSONException {
		// Create the buffered reader which will read in the schemas file
		BufferedReader bfr = new BufferedReader(
				new InputStreamReader(context.getResources().openRawResource(R.raw.schemas)));
		
		// Read in the schemas line-by-line and store it in a JSONArray
		JSONArray schemas = null;
		try {
			String line = null;
			StringBuilder sb = new StringBuilder();
			while ((line = bfr.readLine()) != null) {
				sb.append(line);
			}
			schemas = new JSONArray(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Iterate over the schemas array
		jsonFiles = new String[schemas.length()];
		fileSchemaMap = new HashMap<String, String[]>();
		for (int i = 0; i < schemas.length(); i++) {
			// Current JSON Object
			JSONObject jO = schemas.getJSONObject(i);
			
			// Extract the filename of the current json file
			jsonFiles[i] = jO.getString("name");
			
			// Create the string array for schema information
			String[] jsonTags = new String[jO.getJSONArray("schema").length()];
			for (int j = 0; j < jO.getJSONArray("schema").length(); j++) {
				jsonTags[j] = jO.getJSONArray("schema").getString(j);
			}
			
			// Map our filename to that array for later access
			fileSchemaMap.put(jsonFiles[i], jsonTags);
		}
		
	}
	
	/** Parses all the JSON files defined in the schemas and provides cursors to the data in each */
	private void parseJSON() throws JSONException {
		
		// Create the fileCursorMap
		fileCursorMap = new HashMap<String, Cursor>();
		
		// Iterate over each JSON file
		for (String file : jsonFiles) {
			
			// Get the resource identifier for the JSON file we're parsing
			int id = context.getResources().getIdentifier("edu.purdue.SafeWalk:raw/"+ file, null, null);
			
			Log.d("JSONParsing", "edu.purdue.SafeWalk:raw/"+ file);
			
			// Create the buffered reader which will read in the json file
			BufferedReader bfr = new BufferedReader(
					new InputStreamReader(context.getResources().openRawResource(id)));
			
			// Read in the file line-by-line and store it in a JSONArray
			JSONArray array = null;
			try {
				String line = null;
				StringBuilder sb = new StringBuilder();
				while ((line = bfr.readLine()) != null) {
					sb.append(line);
				}
				array = new JSONArray(sb.toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			// Create the cursor which will store our data. 
			// Its columns are defined by the Map provided by the schema
			MatrixCursor c = new MatrixCursor(fileSchemaMap.get(file));
			
			// Populate the rows of the cursor by iterating over the json array
			for (int i = 0; i < array.length(); i++) {
				JSONObject entry = array.getJSONObject(i);
				
				// And populate the columns by iterating over the known schemas for each row
				Object[] row = new Object[fileSchemaMap.get(file).length];
				for (int col = 0; col < fileSchemaMap.get(file).length; col++) {
					row[col] = entry.get(fileSchemaMap.get(file)[col]);
				}
				c.addRow(row);
			}
			
			// Map the filename string to the cursor we just created
			fileCursorMap.put(file, c);
			
		}
	}
	
}
