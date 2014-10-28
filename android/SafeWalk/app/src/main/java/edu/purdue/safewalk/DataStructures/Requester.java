package edu.purdue.safewalk.DataStructures;

import org.json.*;

import android.util.Log;

public class Requester {
	private String name;
	private String timeOfRequest;
	private String phoneNumber;
	private String urgency;
	// location of user.
	private String requestId;

	private double startLocation_lat, startLocation_lon, endLocation_lat,
			endLocation_lon;

	public Requester(String name, String time, String number, String urgency,
			Double startLat, Double startLong, Double endLat, Double endLong) {
		this.setName(name);
		this.setTimeOfRequest(time);
		this.setPhoneNumber(number);
		this.setUrgency(urgency);
		requestId = "";
		startLocation_lat = startLat;
		startLocation_lon = startLong;
		endLocation_lat = endLat;
		endLocation_lon = endLong;
	}

	public Requester(String id, String name, String time, String number,
			String urgency, Double startLat, Double startLong, Double endLat,
			Double endLong) {
		this(name, time, number, urgency, startLat, startLong, endLat, endLong);
		Log.d("startLocation_lon", "" + startLong);
		// overwrites UUID created on other constructor.
		requestId = id;
	}
	
	public Requester(JSONObject data)
	{
		try {
			setName(data.getString("name"));
			setTimeOfRequest(data.getString("requestTime"));
			setPhoneNumber(data.getString("phoneNumber"));
			setUrgency(data.getString("urgency"));
			requestId =data.getString("requestId");
			startLocation_lat = Double.parseDouble(data.getString("start_lat"));
			startLocation_lon = Double.parseDouble(data.getString("start_long"));
			endLocation_lat = Double.parseDouble(data.getString("end_lat"));
			endLocation_lon = Double.parseDouble(data.getString("end_long"));
		} catch (JSONException e) {
			Log.e("Creating Requester", "Error occurred", e);
			throw new RuntimeException("Not valid Requester JSON");
		}
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public void setTimeOfRequest(String req) {
		this.timeOfRequest = req;
	}

	public void setPhoneNumber(String num) {
		this.phoneNumber = num;
	}

	public void setUrgency(String urgency) {
		this.urgency = urgency;
	}
	
	public String getName() {
		return this.name;
	}

	public String getTimeOfRequest() {
		return this.timeOfRequest;
	}

	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	public String getUrgency() {
		return this.urgency;
	}

	public String getUUID() {
		return requestId;
	}
	
	public JSONObject toJSON() {
		JSONObject jObject = new JSONObject();
		try {
			jObject.put("name", this.getName());
			jObject.put("requestTime", this.getTimeOfRequest());
			jObject.put("phoneNumber", this.getPhoneNumber());
			jObject.put("urgency", this.getUrgency());
			jObject.put("requestId", this.requestId);
			jObject.put("start_lat", startLocation_lat);
			jObject.put("start_long", startLocation_lon);
			jObject.put("end_lat", endLocation_lat);
			jObject.put("end_long", endLocation_lon);
		} catch (JSONException e) {
			Log.e("Request", "Error occurred", e);
		}

		return jObject;

	}
}
