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

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;





public class ListViewRequesterActivity extends ListActivity implements PopupDialog.NoticeDialogListener{
	
	static final String[] NAMES = {"Kyle", "John", "Luke", "Adam", "Sam", "Suzie", "Jessie", "James", "Meowth"};
	public static boolean isPopupOpen = false;
	PopupDialog dialog;
	public static AsyncHttpClient client;
	public static StringEntity se = null;
	
	
	


	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		client = new AsyncHttpClient();
		
		
		// Create ArrayList of names to be put into ListItems
		ArrayList<String>stringList = new ArrayList<String>();
		for(int i=0; i<NAMES.length; i++)
			stringList.add(NAMES[i]);
		
		RequesterListAdapter listAdapter = new RequesterListAdapter(this, stringList);
		
		this.setListAdapter(listAdapter);
		ListView lv = this.getListView();
		lv.setTextFilterEnabled(true);
		
		// Anon class for ListView OnClick
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler(){
					public void onSuccess(String suc){
						Log.d("response", suc);
					}
					
				    @Override
				     public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error)
				 {
				          Log.d("failure", Integer.toString(statusCode));
				     }
					
				};
		        dialog =  new PopupDialog();
		        String name = NAMES[position];
		        // Date and phone number will have to be generated later
		        Requester r = new Requester(name, "11:04", "219-------", "Not Urgent");
		        Log.d("JSON", r.toJSON().toString());
		        try {
					se = new StringEntity(r.toJSON().toString());
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        client.post(getBaseContext(), "http://192.168.1.68:8080", se, "application/json", handler);
		        Log.d("debug", client.toString());
		        
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






	
	

}
