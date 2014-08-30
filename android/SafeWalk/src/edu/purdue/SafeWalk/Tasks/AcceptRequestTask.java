package edu.purdue.SafeWalk.Tasks;

import java.io.IOException;
import java.util.Calendar;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import edu.purdue.SafeWalk.R;
import edu.purdue.SafeWalk.DataStructures.Requester;
import edu.purdue.SafeWalk.Interfaces.OnNewRequestFinished;
import android.content.Context;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

public class AcceptRequestTask extends AsyncTask<Void,Void,Void> {
	
	private String requestId;
	private Context context;
	public static String TAG = "AcceptRequestTask";
	
	public AcceptRequestTask(String id, Context context){
		this.context = context;
		requestId = id;
	}

	@Override
	protected Void doInBackground(Void... arg0) {
		// TODO Auto-generated method stub
		Log.d(TAG,"Starting");
		

		try{
			
		String hostname = PreferenceManager.getDefaultSharedPreferences(context)
				.getString("pref_server", context.getString(R.string.pref_server_default));
		HttpClient client = new DefaultHttpClient();
		HttpPost postReq = new HttpPost(hostname+"/request/"+requestId);
		
		
		HttpResponse resp = null;
		resp = client.execute(postReq);
		} catch(Exception e){
			e.printStackTrace();
		}
		/*
		if(listener != null){
			listener.onNewRequestFinished();
		}
		*/
		return null;
		
		
	
	}
		
		
}
