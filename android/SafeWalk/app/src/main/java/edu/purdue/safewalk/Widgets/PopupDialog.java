package edu.purdue.safewalk.Widgets;

import org.json.JSONException;
import org.json.JSONObject;

import edu.purdue.safewalk.R;
import edu.purdue.safewalk.DataStructures.Requester;
import edu.purdue.safewalk.Interfaces.OnRequestAcceptedHandler;
import edu.purdue.safewalk.Tasks.AcceptRequestTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class PopupDialog extends DialogFragment {
	public static String TAG = "PopUpDialog";
	private Activity a;
	private OnRequestAcceptedHandler handler;

    Requester requester; 
    
    public PopupDialog(OnRequestAcceptedHandler handler)
    {
    	this.handler = handler;
    	a = this.getActivity();
    	
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
        final Activity act = this.getActivity();
        acceptButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				AcceptRequestTask task = new AcceptRequestTask(requester.getUUID(),act);
				task.execute();
				PopupDialog.this.dismiss();
				handler.onRequestAccepted();
			} 
		});
        builder.setView(v);
        
        return builder.create();
    }
    
    public static PopupDialog getInstance(Requester request,OnRequestAcceptedHandler handler)
    {
    	Bundle data = new Bundle();
    	Log.d(TAG, request.toJSON().toString());
    	
    	data.putString("Request", request.toJSON().toString());
    	PopupDialog dialog = new PopupDialog(handler);
    	dialog.setArguments(data);
    	return dialog;
    }
}


