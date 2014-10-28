package edu.purdue.safewalk.Tasks;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import edu.purdue.safewalk.R;
import edu.purdue.safewalk.Interfaces.OnAllRequestsReceivedListener;
import android.content.Context;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

public class GetAllRequestsTask extends AsyncTask<Void,Void,Void> {
	private Context context;
	private OnAllRequestsReceivedListener l;
	
	public GetAllRequestsTask(Context context, OnAllRequestsReceivedListener l){
		this.context = context;
		this.l = l;
		
		
		
	}

	@Override
	protected Void doInBackground(Void... params) {
		// TODO Auto-generated method stub
		Log.d("GETALLREQ", "starting");
		
		Log.d("debug-list", this.l.toString());
		
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
		if(l != null){
			Log.d("debug", "calling listener");
			Log.d("resp",sResp);
			l.onAllRequestsReceived(sResp);
		}
		
		
		return null;
	}

}
