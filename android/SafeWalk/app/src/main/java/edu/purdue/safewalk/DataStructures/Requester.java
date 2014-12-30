package edu.purdue.safewalk.DataStructures;

import org.json.*;

import android.util.Log;

public class Requester {
	private String name;
	private String phoneNumber;
	private String urgency;
    private String userId;
    private String objectId = "";
    private String updatedAt = "";
    private String createdAt = "";
    private boolean isAccepted = false;

	// location of user.

	private double startLocation_lat, startLocation_lon, endLocation_lat,
			endLocation_lon;

	public Requester(String name,String userId,  String number, String urgency,
			Double startLat, Double startLong, Double endLat, Double endLong) {
		this.setName(name);
		this.setPhoneNumber(number);
		this.setUrgency(urgency);
		startLocation_lat = startLat;
		startLocation_lon = startLong;
		endLocation_lat = endLat;
		endLocation_lon = endLong;
        this.userId = userId;
	}


    public Requester(JSONObject data)
    {
        try {
            setName(data.getString("name"));
            setPhoneNumber(data.getString("phoneNumber"));
            setUrgency(data.getString("urgency"));
            startLocation_lat = Double.parseDouble(data.getString("start_lat"));
            startLocation_lon = Double.parseDouble(data.getString("start_long"));
            endLocation_lat = Double.parseDouble(data.getString("end_lat"));
            endLocation_lon = Double.parseDouble(data.getString("end_long"));
        } catch (JSONException e) {
            Log.e("Creating Requester", "Error occurred", e);
            throw new RuntimeException("Not valid Requester JSON");
        }
    }

    public JSONObject toJSON() {
        JSONObject jObject = new JSONObject();
        try {
            jObject.put("name", this.getName());
            jObject.put("phoneNumber", this.getPhoneNumber());
            jObject.put("urgency", this.getUrgency());
            jObject.put("start_lat", startLocation_lat);
            jObject.put("start_long", startLocation_lon);
            jObject.put("end_lat", endLocation_lat);
            jObject.put("end_long", endLocation_lon);
        } catch (JSONException e) {
            Log.e("Request", "Error occurred", e);
        }

        return jObject;

    }


	

	
	public void setName(String name) {
		this.name = name;
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


	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	public String getUrgency() {
		return this.urgency;
	}

    public String getCreatedAt() {return this.createdAt;}

    public String getUpdatedAt() {return this.updatedAt;}

    public String getObjectId() {return this.objectId;}


}
