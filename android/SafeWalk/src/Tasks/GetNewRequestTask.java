package Tasks;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import edu.purdue.SafeWalk.R;
import edu.purdue.SafeWalk.DataStructures.Requester;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

public class GetNewRequestTask extends AsyncTask<Void,Void,Void> {
	private double start_lat;
	private double start_lon;
	private double end_lat;
	private double end_lon;
	private String userName;
	private String phoneNum;
	private Activity activity;
	private Context c;
	
	public GetNewRequestTask(double start_lat, double start_lon, double end_lat, double end_lon, String userName, String phoneNum,Activity a, Context c){
		this.start_lat = start_lat;
		this.start_lon = start_lon;
		this.end_lat = end_lat;
		this.end_lon = end_lon;
		this.userName = userName;
		this.phoneNum = phoneNum;
		this.activity = a;
		this.c = c;
	}

	@Override
	protected Void doInBackground(Void... arg0) {
		String hostname = PreferenceManager.getDefaultSharedPreferences(c).getString("pref_server", "");
		String time = java.text.DateFormat.getDateTimeInstance().format(
				Calendar.getInstance().getTime());
	
		Requester r = new Requester(userName + (int) (Math.random() * 1000),
				time, "219-555-1242", "Not Urgent", start_lat, start_lon,
				end_lat, end_lon);
		HttpClient client = new DefaultHttpClient();
		HttpGet getReq = new HttpGet(hostname+"/request");
		StringEntity se = null;
		try {
			se = new StringEntity(r.toJSON().toString());
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// TODO Auto-generated method stub
		return null;
	}

}
