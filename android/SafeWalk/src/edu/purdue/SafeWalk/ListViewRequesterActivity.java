package edu.purdue.SafeWalk;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.entity.StringEntity;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;
import android.util.Log;
import android.view.*;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;





public class ListViewRequesterActivity extends ListActivity implements PopupDialog.NoticeDialogListener{
	
	//static final String[] NAMES = {"Kyle", "John", "Luke", "Adam", "Sam", "Suzie", "Jessie", "James", "Meowth"};
	String[] NAMES;
	public static boolean isPopupOpen = false;
	PopupDialog dialog;
	public static AsyncHttpClient client;
	public static StringEntity se = null;
	public static String httpResponse = null;
	static ArrayList<Requester> requests = new ArrayList<Requester>();
	Requester arrayOfRequests[];
	Requester r;
	customHTTPHandler chandler;
	
	
	


	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		chandler = new customHTTPHandler();
		try {
			getRequests();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(chandler.hasFailed){
			Toast.makeText(getApplicationContext(), "No connection to server", Toast.LENGTH_LONG).show();
		}
		
		// Create ArrayList of names to be put into ListItems
		ArrayList<String>stringList = new ArrayList<String>();
		//Log.d("Size", ""+arrayOfRequests.length);
		for(int i=0; i<requests.size(); i++){
			stringList.add(requests.get(i).getName());
		}

		
		RequesterListAdapter listAdapter = new RequesterListAdapter(this, stringList, requests);
		
		this.setListAdapter(listAdapter);
		ListView lv = this.getListView();
		lv.setTextFilterEnabled(true);
		
		// Anon class for ListView OnClick
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
		        dialog =  new PopupDialog(getApplicationContext(),position);
		        dialog.show(getFragmentManager(), "PopUpDialogFragment");
			}
		});
		
		ActionBar actionBar = getActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);
		
	}





	@Override
	public void onPopUpAcceptClick(View v) {
		// TODO Auto-generated method stub
		Toast t = Toast.makeText(getApplicationContext(), "Accept Me", Toast.LENGTH_SHORT);
		t.show();
		dialog.dismiss();
		
	}





	@Override
	public void onPopUpMessageClick(View v) {
		// TODO Auto-generated method stub
		Toast t = Toast.makeText(getApplicationContext(), "Message me!", Toast.LENGTH_SHORT);
		t.show();
		dialog.dismiss();
		
	}





	@Override
	public void onPopUpCallClick(View v) {
		// TODO Auto-generated method stub
		Toast t = Toast.makeText(getApplicationContext(), "Call me Maybe!", Toast.LENGTH_SHORT);
		t.show();
		dialog.dismiss();
		
	}
	
	public void getRequests() throws JSONException{
		AsyncHttpClient client = new AsyncHttpClient();
		// using our own custom httpHandler to save the response
		//customHTTPHandler chandler = new customHTTPHandler();
		//this will be on the main thread, kind of hacky
		//TODO: add a progress bar for loading
		chandler.setUseSynchronousMode(true);
		client.get(SafeWalk.hostname+"/request",chandler); // remeber to change host and ip
		while(!chandler.receivedResponse){
			if(chandler.hasFailed)
				return;

		}
		Log.d("response", httpResponse);
		JSONArray jArray = new JSONArray(httpResponse);
		for(int i=0; i<jArray.length(); i++){
			JSONObject j = jArray.getJSONObject(i);
			 r = new Requester(j.getString("name"), j.getString("requestTime"), j.getString("phoneNumber"), j.getString("urgency"), j.getDouble("lat"),j.getDouble("long"));
			 requests.add(r);
		}
		
		
		
	}






	
	

}





