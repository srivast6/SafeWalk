package edu.purdue.SafeWalk;

import java.util.ArrayList;

import org.apache.http.entity.StringEntity;

import android.app.ActionBar;
import android.app.ListFragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;

public class ListViewRequesterFragment extends ListFragment {

	String[] NAMES;
	private final static String TAG = "ListViewRequesterFragment";
	public static final String SUCCESS = "edu.purdue.SafeWalk.SUCCESS";
	public static final String FAILURE = "edu.purdue.SafeWalk.FAILURE";
	public static final String RESPONSE = "edu.purdue.SafeWalk.RESPONCE_REQUESTS";
	public static boolean isPopupOpen = false;
	PopupDialog dialog;
	public static AsyncHttpClient client;
	public static StringEntity se = null;
	public static String httpResponse = null;
	static ArrayList<Requester> requests = new ArrayList<Requester>();
	Requester r;
	customHTTPHandler chandler;
	public static int index;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent,
			Bundle bundle) {
		return inflater.inflate(R.layout.list_view_requester_activity, null);
	}

	@Override
	public void onStart() {
		super.onStart();
		chandler = new customHTTPHandler(this);
		Log.d(TAG, "onStart()");
		getRequests();// starts to communicate to server.

		ActionBar actionBar = getActivity().getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		getActivity().getActionBar().setSubtitle("All pending requests");
	}

	@Override
	public void onResume() {
		super.onResume();

		// Register mMessageReceiver to receive messages.
		LocalBroadcastManager.getInstance(getActivity()).registerReceiver(
				mMessageReceiver,
				new IntentFilter(ListViewRequesterFragment.RESPONSE));
	}

	// handler for received Intents for the ListViewRequesterFragment.RESPONSE event
	private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			// Extract data included in the Intent
			String message = intent
					.getStringExtra(ListViewRequesterFragment.RESPONSE);
			Log.d("receiver", "Got message: " + message);
			
			if(message == SUCCESS)
				onSuccess();
			else 
				onFailure();
		}
	};

	@Override
	public void onPause() {
		// Unregister since the activity is not visible
		LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(
				mMessageReceiver);
		super.onPause();
	}

	public void getRequests() {
		AsyncHttpClient client = new AsyncHttpClient();
		// using our own custom httpHandler to save the response
		// customHTTPHandler chandler = new customHTTPHandler();
		// this will be on the main thread, kind of hacky
		// TODO: add a progress bar for loading
		chandler.setUseSynchronousMode(true);
		SafeWalk.hostname = PreferenceManager.getDefaultSharedPreferences(
				getActivity()).getString("pref_server",
				"http://optical-sight-386.appspot.com");
		client.get(SafeWalk.hostname + "/request", chandler);
	}

	public void onFailure() {
		Toast.makeText(getActivity(), "No connection to server",
				Toast.LENGTH_LONG).show();
	}

	public void onSuccess() {
		Log.d("response", httpResponse);
		requests.clear(); // Remove old received requests from list.

		JSONArray jArray;
		try {
			jArray = new JSONArray(httpResponse);
			for (int i = 0; i < jArray.length(); i++) {
				JSONObject j = jArray.getJSONObject(i);
				r = new Requester(j); //moved to inside class. Makes more sense. 
				requests.add(r);
			}
		} catch (JSONException e) {
			Log.e("JSON Parsing Exception", "JSON failed to parse");
			e.printStackTrace();
		}
		updateList();
	}

	private void updateList() {
		// Create ArrayList of names to be put into ListItems
		ArrayList<String> stringList = new ArrayList<String>();
		// Log.d("Size", ""+arrayOfRequests.length);
		for (int i = 0; i < requests.size(); i++) {
			stringList.add(requests.get(i).getName());
		}

		RequesterListAdapter listAdapter = new RequesterListAdapter(
				getActivity(), requests);

		this.setListAdapter(listAdapter);
		ListView lv = this.getListView();
		lv.setTextFilterEnabled(true);

		// Anon class for ListView OnClick
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				index = position;
				dialog = PopupDialog.getInstance(((Requester) getListAdapter().getItem(position)));
				dialog.show(getFragmentManager(), "PopUpDialogFragment");
			}
		});
	}
}
