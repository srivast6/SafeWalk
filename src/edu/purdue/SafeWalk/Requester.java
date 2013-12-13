package edu.purdue.SafeWalk;

import org.json.*;

public class Requester {
	private String name;
	private String timeOfRequest;
	private String phoneNumber;
	private String urgency;
	
	public Requester(String name, String time, String number, String urgency){
		this.setName(name);
		this.setTimeOfRequest(time);
		this.setPhoneNumber(number);
		this.setUrgency(urgency);
		
		
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
	
	
	public JSONObject toJSON(){
		JSONObject jObject = new JSONObject();
		try {
			jObject.put("name", this.getName());
			jObject.put("requestTime", this.getTimeOfRequest());
			jObject.put("phoneNumber", this.getPhoneNumber());
			jObject.put("urgency", this.getUrgency());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		
		return jObject;
		
	}
	

	

}
