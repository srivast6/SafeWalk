package edu.purdue.SafeWalk;

import org.json.*;

import android.util.Log;

import java.util.*;

public class Requester {
	private String name;
	private String timeOfRequest;
	private String phoneNumber;
	private String urgency;
	//location of user.
	private double latitude; 
	private double longitude;
	private UUID requestId;
	
	private double start_lat, start_long, end_lat, end_long;
	
	public Requester(String name, String time, String number, String urgency, Double lat, Double longitude, Double startLat, Double startLong, Double endLat, Double endLong){
		this.setName(name);
		this.setTimeOfRequest(time);
		this.setPhoneNumber(number);
		this.setUrgency(urgency);
		this.setLat(lat);
		this.setLong(longitude);
		requestId = UUID.randomUUID();
		
		start_lat = startLat;
		start_long = startLong; 
		end_lat = endLat; 
		end_long = endLong; 
	}
	
	public Requester(String id, String name, String time, String number, String urgency, Double lat, Double longitude, Double startLat, Double startLong, Double endLat, Double endLong){
		this( name,  time,  number,  urgency,  lat,  longitude,  startLat,  startLong,  endLat,  endLong);
		//overwrites UUID created on other constructor. 
		requestId = UUID.fromString(id);
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public void setTimeOfRequest(String req){
		this.timeOfRequest = req;
	}
	
	public void setPhoneNumber(String num){
		this.phoneNumber=num;
	}
	
	public void setUrgency(String urgency){
		this.urgency=urgency;
	}
	
	public void setLat(Double lat){
		this.latitude = lat;
	}
	
	public void setLong(Double longitude){
		this.longitude = longitude;
	}
	
	public String getName(){
		return this.name;
	}
	
	public String getTimeOfRequest(){
		return this.timeOfRequest;
	}
	
	public String getPhoneNumber(){
		return this.phoneNumber;
	}
	
	public String getUrgency(){
		return this.urgency;
	}
	
	public Double getLat(){
		return this.latitude;
	}
	
	public Double getLong(){
		return this.longitude;
	}
	
	public String getUUID(){
		return requestId.toString();
	}
	
	public JSONObject toJSON(){
		JSONObject jObject = new JSONObject();
		try {
			jObject.put("name", this.getName());
			jObject.put("requestTime", this.getTimeOfRequest());
			jObject.put("phoneNumber", this.getPhoneNumber());
			jObject.put("urgency", this.getUrgency());
			jObject.put("lat", this.getLat());
			jObject.put("long", this.getLong());
            jObject.put("requestId", this.requestId.toString());
            
            jObject.put("start_lat", start_lat);
            jObject.put("start_long", start_long);
            jObject.put("end_lat", end_lat);
            jObject.put("end_long",end_long);
		} catch (JSONException e) {
			Log.e("Request", "Error occurred", e);
		}
		
		return jObject;
		
	}
	

	

}
