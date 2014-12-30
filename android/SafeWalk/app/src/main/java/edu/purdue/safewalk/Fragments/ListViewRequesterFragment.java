package edu.purdue.safewalk.Fragments;

import android.app.ActionBar;
import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.List;

import edu.purdue.safewalk.Adapters.RequesterListAdapter;
import edu.purdue.safewalk.DataStructures.Requester;
import edu.purdue.safewalk.Interfaces.SafeWalkAPIServiceInterface;
import edu.purdue.safewalk.R;
import edu.purdue.safewalk.SafeWalkAPI;
import edu.purdue.safewalk.Widgets.PopupDialog;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ListViewRequesterFragment extends ListFragment implements  AdapterView.OnItemClickListener {

	private final static String TAG = "ListViewRequesterFragment";
	public static final String SUCCESS = "edu.purdue.SafeWalk.SUCCESS";
	public static final String FAILURE = "edu.purdue.SafeWalk.FAILURE";
	public static final String RESPONSE = "edu.purdue.SafeWalk.RESPONCE_REQUESTS";
	PopupDialog dialog;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent,
			Bundle bundle) {
		return inflater.inflate(R.layout.list_view_requester_activity, null);
	}

	@Override
	public void onStart() {
		super.onStart();
		Log.d(TAG, "onStart()");
		getRequests();// starts to communicate to server.

		ActionBar actionBar = getActivity().getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		getActivity().getActionBar().setSubtitle("All pending requests");
	}


	public void getRequests() {
        SafeWalkAPIServiceInterface service = SafeWalkAPI.getAPI(getActivity());
        getListView().setOnItemClickListener(this);
        service.getRequests(new Callback<List<Requester>>() {
            @Override
            public void success(List<Requester> requesters, Response response) {
                setListAdapter(new RequesterListAdapter(getActivity(), requesters));

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });

	}




    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        dialog = new PopupDialog(((Requester) getListAdapter().getItem(position)));
        dialog.show(getFragmentManager(), "PopUpDialogFragment");

    }
}
