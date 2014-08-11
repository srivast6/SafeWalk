package edu.purdue.SafeWalk.Fragments;

import java.util.ArrayList;

import org.apache.http.entity.StringEntity;

import android.app.ActionBar;
import android.app.ListFragment;
import android.app.ProgressDialog;
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

import edu.purdue.SafeWalk.R;
import edu.purdue.SafeWalk.SafeWalk;
import edu.purdue.SafeWalk.customHTTPHandler;
import edu.purdue.SafeWalk.DataStructures.Requester;
import edu.purdue.SafeWalk.Interfaces.OnAllRequestsReceivedListener;
import edu.purdue.SafeWalk.Tasks.GetAllRequestsTask;
import edu.purdue.SafeWalk.R.layout;
import edu.purdue.SafeWalk.R.string;
import edu.purdue.Safewalk.Adapters.RequesterListAdapter;
import edu.purdue.Safewalk.Widgets.PopupDialog;

public class ListViewRequesterFragment extends ListFragment implements OnAllRequestsReceivedListener {

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
	static ArrayList<Requester> requests_ = new ArrayList<Requester>();
	Requester r;
	customHTTPHandler chandler;
	public static int index;
	private ProgressDialog progDialog;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent,
			Bundle bundle) {
		
		return inflater.inflate(R.layout.list_view_requester_activity, null);
	}

	@Override
	public void onStart() {
		super.onStart();
		
		Log.d(TAG, "onStart()");
		progDialog = new ProgressDialog(getActivity());
		progDialog.show();
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
		GetAllRequestsTask task = new GetAllRequestsTask(getActivity(),this);
		task.execute();
	}

	public void onFailure() {
		Toast.makeText(getActivity(), "No connection to server",
				Toast.LENGTH_LONG).show();
	}

	public void onSuccess() {
		Log.d("response", httpResponse);
		requests_.clear(); // Remove old received requests from list.

		JSONArray jArray;
		try {
			jArray = new JSONArray(httpResponse);
			for (int i = 0; i < jArray.length(); i++) {
				JSONObject j = jArray.getJSONObject(i);
				r = new Requester(j); //moved to inside class. Makes more sense. 
				requests_.add(r);
			}
		} catch (JSONException e) {
			Log.e("JSON Parsing Exception", "JSON failed to parse");
			e.printStackTrace();
		}
		updateList(requests_);
	}

	private void updateList(ArrayList<Requester> requests) {
		// Create ArrayList of names to be put into ListItems
		ArrayList<String> stringList = new ArrayList<String>();
		
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

	@Override
	public void onAllRequestsReceived(String resp) {
		// TODO Auto-generated method stub
		Log.d("debug", "onIT!");
		JSONObject jObject;
		ArrayList<Requester> requests = null;
		try {
			jObject = new JSONObject(resp);
			
		
		JSONArray jArray;
		jArray = jObject.getJSONArray("results");
		requests = new ArrayList<Requester>();
		Log.d("array-len", ""+jArray.length());
			for (int i = 0; i < jArray.length(); i++) {
				JSONObject j = jArray.getJSONObject(i);
				r = new Requester(j); //moved to inside class. Makes more sense. 
				Log.d("name", r.getName());
				requests.add(r);
			}
		
		}catch(Exception e){
			e.printStackTrace();
		}
		if(progDialog.isShowing()){
			progDialog.dismiss();
		}
		
		updateList(requests);
		
		
	}
}
