package edu.purdue.Safewalk.Widgets;

import java.io.UnsupportedEncodingException;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import edu.purdue.SafeWalk.R;
import edu.purdue.SafeWalk.DataStructures.Requester;
import edu.purdue.SafeWalk.R.id;
import edu.purdue.SafeWalk.R.layout;
import edu.purdue.SafeWalk.R.string;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class PopupDialog extends DialogFragment {

    Requester requester; 
    
    public PopupDialog()
    {
    	
    }
  
    public Dialog onCreateDialog(Bundle savedInstanceState) {
    	JSONObject requestData; 
    	try{
    		requestData = new JSONObject(getArguments().getString("Request"));
    	}catch (JSONException je)
    	{
    		Log.e("PopupDialog", "Unable to create popup: Not a valid request JSON string.", je);
    		throw new RuntimeException();
    	}
    	requester = new Requester(requestData);
    	
    	
    	
    	LayoutInflater inflater = this.getActivity().getLayoutInflater();
        // Build the dialog and set up the button click handlers
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View v = inflater.inflate(R.layout.pop_up, null);

        // 3. Get the two text view from the rowView
        TextView nameView = (TextView) v.findViewById(R.id.name);
        TextView timeView = (TextView) v.findViewById(R.id.time_requested);
        TextView phoneView = (TextView) v.findViewById(R.id.phone);
        TextView locationView = (TextView) v.findViewById(R.id.location);
        
        // 4. Set the text for textView 
        nameView.setText("Name: "+requester.getName());
        timeView.setText("Time Requested: "+ requester.getTimeOfRequest());
        phoneView.setText("Phone: "+ requester.getPhoneNumber());
        locationView.setText("Location: "+ "0000, 0000" );

        
        Button callButton = (Button) v.findViewById(R.id.btn_call);
        Button messageButton = (Button) v.findViewById(R.id.btn_message);
        Button acceptButton = (Button) v.findViewById(R.id.btn_accept);
        
        callButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Toast t = Toast.makeText(getActivity().getApplicationContext(),
						"Calling...", Toast.LENGTH_SHORT);
				t.show();
				PopupDialog.this.dismiss();
			} 
		});
        
        messageButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Toast t = Toast.makeText(getActivity().getApplicationContext(),
						"Messaging...", Toast.LENGTH_SHORT);
				t.show();
				PopupDialog.this.dismiss();
			} 
		});
        
        acceptButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				AsyncHttpClient client = new AsyncHttpClient();
				AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
					public void onSuccess(String response) {
						// Log.d("response", response);
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							byte[] responseBody, Throwable error) {
						//Toast.makeText(getActivity().getApplicationContext(),
						//		"No connection to server\nPlease call directly.", Toast.LENGTH_LONG).show();
						Log.d("failure", Integer.toString(statusCode));
					}
				};
				String message = "accept";
				StringEntity se = null;
				try {
					se = new StringEntity(message);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String hostname = PreferenceManager.getDefaultSharedPreferences(
						getActivity()).getString("pref_server",
						getString(R.string.pref_server_default));
				String url = hostname + "/request" + "/"
						+ requester.getUUID() + "/accept";
				Log.d("url", url);
				client.get(getActivity().getBaseContext(), hostname + "/request" + "/"
						+ requester.getUUID() + "/accept", handler);
				Toast t = Toast.makeText(getActivity().getApplicationContext(),
						"Sending Accept", Toast.LENGTH_SHORT);
				t.show();
				PopupDialog.this.dismiss();
			} 
		});
        
        
        
        builder.setView(v);
        
        return builder.create();
    }
    
    public static PopupDialog getInstance(Requester request)
    {
    	Bundle data = new Bundle();
    	data.putString("Request", request.toJSON().toString());
    	PopupDialog dialog = new PopupDialog();
    	dialog.setArguments(data);
    	return dialog;
    }
}


