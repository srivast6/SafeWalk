package edu.purdue.SafeWalk;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.loopj.android.http.AsyncHttpResponseHandler;

public class customHTTPHandler extends AsyncHttpResponseHandler {
	private String httpResponse = null;
	char[] response;
	static boolean receivedResponse = false;

    @Override
    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
        // Successfully got a response
    	String response = new String(responseBody);
    	ListViewRequesterActivity.httpResponse = response;
    	receivedResponse = true;
    	
    }

	
    @Override
     public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error)
 {
          Log.d("failure", Integer.toString(statusCode));
     }
    

    

	

}
