package edu.purdue.SafeWalk.Tasks;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import edu.purdue.SafeWalk.R;
import edu.purdue.SafeWalk.Interfaces.OnAllRequestsReceivedListener;
import android.content.Context;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

public class GetAllRequestsTask extends AsyncTask<Void,Void,Void> {
	private Context context;
	private OnAllRequestsReceivedListener listener;
	
	public GetAllRequestsTask(Context context, OnAllRequestsReceivedListener listner){
		this.context = context;
		this.listener = listener;
		
	}

	@Override
	protected Void doInBackground(Void... params) {
		// TODO Auto-generated method stub
		Log.d("GETALLREQ", "starting");
		String hostname = PreferenceManager.getDefaultSharedPreferences(context)
				.getString("pref_server", context.getString(R.string.pref_server_default));
		HttpClient client = new DefaultHttpClient();
		HttpGet getReq = new HttpGet(hostname+"/request");
		HttpResponse resp = null;
		try{
			resp = client.execute(getReq);
		} catch(Exception e){
			e.printStackTrace();
		}
		
		String sResp = null;
		try {
			sResp = EntityUtils.toString(resp.getEntity());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Log.d("listener",);
		if(listener != null){
			Log.d("debug", "calling listener");
			listener.onAllRequestsReceived(sResp);
		}
		
		return null;
	}

}
