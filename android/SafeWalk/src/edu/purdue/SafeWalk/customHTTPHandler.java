package edu.purdue.SafeWalk;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.loopj.android.http.AsyncHttpResponseHandler;

import edu.purdue.SafeWalk.Fragments.ListViewRequesterFragment;

public class customHTTPHandler extends AsyncHttpResponseHandler {
	private String httpResponse = null;
	char[] response;
	static boolean receivedResponse = false;
	static boolean hasFailed = false;
	ListViewRequesterFragment listener;

	public customHTTPHandler(ListViewRequesterFragment listener)
	{
		this.listener = listener; 
	}
	
    @Override
    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
        // Successfully got a response
    	String response = new String(responseBody);
    	ListViewRequesterFragment.httpResponse = response;
    	receivedResponse = true;
    	Intent intent = new Intent(ListViewRequesterFragment.RESPONSE);
    	intent.putExtra(ListViewRequesterFragment.RESPONSE, ListViewRequesterFragment.SUCCESS);
        LocalBroadcastManager.getInstance(listener.getActivity()).sendBroadcast(intent);
    }
	
    @Override
    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error)
    {
    	hasFailed = true;
        Log.d("failure", Integer.toString(statusCode));
        Intent intent = new Intent(ListViewRequesterFragment.RESPONSE);
    	intent.putExtra(ListViewRequesterFragment.RESPONSE, ListViewRequesterFragment.FAILURE);
        LocalBroadcastManager.getInstance(listener.getActivity()).sendBroadcast(intent);
    }
}
