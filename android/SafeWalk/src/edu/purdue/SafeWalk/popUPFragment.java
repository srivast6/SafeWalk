package edu.purdue.SafeWalk;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class popUPFragment extends Fragment {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflateIt, ViewGroup container,
			Bundle savedInstanceState) {
		View pop_up = inflateIt.inflate(R.layout.pop_up, null);
		Button acptRequest = (Button) pop_up.findViewById(R.id.btn_acceptRqst);
		Button callVolnter = (Button) pop_up
				.findViewById(R.id.btn_callVolunteer);
		Button callPolice = (Button) pop_up.findViewById(R.id.btn_callPolice);
		Button callSafeWalk = (Button) pop_up
				.findViewById(R.id.btn_callSafewalk);
		Button moreInfo = (Button) pop_up.findViewById(R.id.morInfo);

		acptRequest.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				onClickAcptRqst(view);
			}
		});

		callVolnter.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				onClickcallVolunteer(view);
			}
		});

		callPolice.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				onClickcallPolice(view);

			}
		});

		callSafeWalk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				onClickcallSafeWalk(view);
			}

		});

		moreInfo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				onClickmoreInfo(view);
			}
		});

		return pop_up;
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	public void onClickAcptRqst(View view) {
		// Accept the request. Send mssg to the volunteer
	}

	public void onClickcallVolunteer(View view) {
		// Call the volunteer
		Intent callVolunteer = new Intent(Intent.ACTION_CALL);
		callVolunteer.setData(Uri.parse("tel:00000001110000000"));
		// We still need to enter the correct phone number.
		startActivity(callVolunteer);
	}

	public void onClickmoreInfo(View view) {
		// gives more info about something
	}

	public void onClickcallPolice(View view) {
		// Call the police
		Intent callPolice = new Intent(Intent.ACTION_CALL);
		callPolice.setData(Uri.parse("tel:00000001110000000"));
		// We still need to enter the correct phone number.
		startActivity(callPolice);
	}

	public void onClickcallSafeWalk(View view) {
		// Call SafeWalk people
		Intent callSafeWalk = new Intent(Intent.ACTION_CALL);
		callSafeWalk.setData(Uri.parse("tel:00000001110000000"));
		// We still need to enter the correct phone number.
		startActivity(callSafeWalk);
	}
}
