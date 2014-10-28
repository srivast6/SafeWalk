package edu.purdue.safewalk.Fragments;

import java.util.ArrayList;

import org.w3c.dom.Document;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import edu.purdue.safewalk.R;
import edu.purdue.safewalk.SafeWalk;
import edu.purdue.safewalk.MapItems.CustomMapFragment;
import edu.purdue.safewalk.MapItems.GMapV2Direction;
import edu.purdue.safewalk.MapItems.MapOverlayHandler;
import edu.purdue.safewalk.Widgets.TouchableWrapper;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WalkRequestFragment extends Fragment {

	public static enum BubbleState {
		START, END, CONFIRM;
	};

	public BubbleState mBubbleState = BubbleState.START;
	GoogleMap mMap;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.map, null);

		LinearLayout lin = (LinearLayout) v
				.findViewById(R.id.mapPopUpLinLayout);

		lin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onPopUpBubbleClick(v);
			}

		});

		int shortAnimDuration = getResources().getInteger(
				android.R.integer.config_shortAnimTime);

		((TouchableWrapper) v.findViewById(R.id.touchWrapper))
				.setOverlayHandler(new MapOverlayHandler(getActivity(),
						shortAnimDuration));

		initMap();

		return v;

		// mOriginalContentView = super.onCreateView(inflater, parent,
		// savedInstanceState);
		// int shortAnimDuration = getResources().getInteger(
		// android.R.integer.config_shortAnimTime);
		// mTouchView = new TouchableWrapper(new
		// MapOverlayHandler(getActivity(),
		// shortAnimDuration));
		// mTouchView.addView(mOriginalContentView);
		//
		// TODO: Make onPopupBubbleClick the onClick for mapPopUpLinLayout

		// return mTouchView;
	}

	private void initMap() {

		mMap = ((CustomMapFragment) getFragmentManager().findFragmentById(
				R.id.map)).getMap();
		((SafeWalk) getActivity()).mMap = mMap;
		if (mMap != null) {
			mMap.setMyLocationEnabled(true);
			UiSettings mapSettings = mMap.getUiSettings();
			mapSettings.setTiltGesturesEnabled(false);
			mapSettings.setRotateGesturesEnabled(false);
		} else {
			AlertDialog.Builder alertBuilder = new AlertDialog.Builder(
					getActivity());
			alertBuilder.setTitle("Error!");
			alertBuilder.setMessage(this.getResources().getText(
					R.string.error_no_maps));
			alertBuilder.setPositiveButton("Close "
					+ this.getResources().getText(R.string.app_name),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							getActivity().finish();
						}
					});
			alertBuilder.show();
		}
	}

	/*
	 * Function used when a request to be picked up is map, send information to
	 * server
	 */
	public void onPopUpBubbleClick(View v) {

		LatLng latlng;
		if (mBubbleState == BubbleState.START) {
			latlng = ((CustomMapFragment) getFragmentManager()
					.findFragmentById(R.id.map)).dropPinAtCenter(getActivity(),
					"Start", BitmapDescriptorFactory.HUE_GREEN);

			SharedPreferences bubbleState = getActivity().getSharedPreferences(
					"bubbleState", Context.MODE_PRIVATE);
			SharedPreferences.Editor edit = bubbleState.edit();
			edit.putString("start_lat", "" + latlng.latitude);
			edit.putString("start_long", "" + latlng.longitude);
			edit.commit();

			mBubbleState = BubbleState.END;
			updateBubbleText();

		} else if (mBubbleState == BubbleState.END) {
			latlng = ((CustomMapFragment) getFragmentManager()
					.findFragmentById(R.id.map)).dropPinAtCenter(getActivity(),
					"End", BitmapDescriptorFactory.HUE_RED);

			SharedPreferences bubbleState = getActivity().getSharedPreferences(
					"bubbleState", Context.MODE_PRIVATE);
			SharedPreferences.Editor edit = bubbleState.edit();
			edit.putString("end_lat", "" + latlng.latitude);
			edit.putString("end_long", "" + latlng.longitude);
			edit.commit();

			LatLng start = new LatLng(Double.parseDouble(bubbleState.getString(
					"start_lat", "0")), Double.parseDouble(bubbleState
					.getString("start_long", "0")));

			AsyncTask<LatLng, Void, ArrayList<LatLng>> directionsTask = new AsyncTask<LatLng, Void, ArrayList<LatLng>>() {

				@Override
				protected ArrayList<LatLng> doInBackground(LatLng... locations) {
					GMapV2Direction md = new GMapV2Direction();
					mMap = ((MapFragment) getFragmentManager()
							.findFragmentById(R.id.map)).getMap();
					Document doc = md.getDocument(locations[0], locations[1],
							GMapV2Direction.MODE_WALKING);

					return md.getDirection(doc);
				}

				@Override
				protected void onPostExecute(ArrayList<LatLng> directionPoint) {
					PolylineOptions rectLine = new PolylineOptions().width(3)
							.color(Color.RED);

					for (int i = 0; i < directionPoint.size(); i++) {
						rectLine.add(directionPoint.get(i));
					}
					Polyline polylin = mMap.addPolyline(rectLine);
				}
			};
			// TODO: Add loading bar under actionbar
			directionsTask.execute(start, latlng);

			mBubbleState = BubbleState.CONFIRM;
			updateBubbleText();

		} else if (mBubbleState == BubbleState.CONFIRM) {

			// open request activity...
			((SafeWalk) getActivity()).openRequestActivity();

			mBubbleState = BubbleState.START;
			updateBubbleText();

		}
	}

	public void updateBubbleText() {
		TextView bubble = (TextView) getView().findViewById(R.id.bubbleText);
		switch (mBubbleState) {
		case START:
			bubble.setText("Request Pickup Location");
			break;
		case END:
			bubble.setText("Set Dropoff Location");
			break;
		case CONFIRM:
			bubble.setText("Confirm Route");
			break;

		}
	}
}
