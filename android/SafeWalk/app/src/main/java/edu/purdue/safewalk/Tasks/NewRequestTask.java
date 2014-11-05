package edu.purdue.safewalk.Tasks;

import java.util.Calendar;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import edu.purdue.safewalk.R;
import edu.purdue.safewalk.DataStructures.Requester;
import edu.purdue.safewalk.Interfaces.OnNewRequestFinished;
import android.content.Context;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

public class NewRequestTask extends AsyncTask<Void,Void,Void> {
	
	private double start_lat;
	private double start_long;
	private double end_lat;
	private double end_long;
	private String userName;
	private String phoneNum;
	private Context context;
	private OnNewRequestFinished listener;
    private String TAG = "NewRequestTask";
	
	
	public NewRequestTask(double start_lat, double start_long, double end_lat, double end_long, String userName, String phonenNum, Context context, OnNewRequestFinished listener){
		this.start_lat = start_lat;
		this.start_long = start_long;
		this.end_lat = end_lat;
		this.end_long = end_long;
		this.userName = userName;
		this.phoneNum = phoneNum;
		this.context = context;
		this.listener = listener;
		
	}

	@Override
	protected Void doInBackground(Void... arg0) {
		// TODO Auto-generated method stub
		
		String time = java.text.DateFormat.getDateTimeInstance().format(
				Calendar.getInstance().getTime());
		String userName = "John Smith";

		Requester r = new Requester(userName + (int) (Math.random() * 1000),
				time, "219-555-1242", "Not Urgent", start_lat, start_long,
				end_lat, end_long);
		
		StringEntity se = null;
		try{
			
		
			se = new StringEntity(r.toJSON().toString());

		
		String hostname = PreferenceManager.getDefaultSharedPreferences(context)
				.getString("pref_server", context.getString(R.string.pref_server_default));
            Log.d(TAG,"hostname="+hostname);
            HttpClient client = new DefaultHttpClient();
		HttpPost postReq = new HttpPost(hostname+"/request");
		postReq.setEntity(se);
		postReq.setHeader("Content-type", "application/json");
		
		HttpResponse resp = null;
		resp = client.execute(postReq);
		} catch(Exception e){
			e.printStackTrace();
		}
		
		if(listener != null){
			listener.onNewRequestFinished();
		}
		return null;
		
	
	}
		
		
}
