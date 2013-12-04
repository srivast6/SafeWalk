package edu.purdue.SafeWalk;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.AlertDialog;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class CallButtonFragment extends Fragment
{
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View vg = inflater.inflate(R.layout.call_button_fragment, null);
		Button daPopo = (Button) vg.findViewById(R.id.btn_callPolice);
		Button safewalk = (Button) vg.findViewById(R.id.btn_callSafewalk);
		
		daPopo.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				onClickPolice(v);
			}
		});
		safewalk.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				onClickSafeWalk(v);
			}
		});
		
		return vg;
	}
	
	@Override
	public void onPause()
	{
		super.onPause();
	}
	
	public void onClickSafeWalk(View view)
	{
		executeCall("7654947233");
	}
	
	public void onClickPolice(View view)
	{
		SharedPreferences myPreference= PreferenceManager.getDefaultSharedPreferences(getActivity());
		if(myPreference.getBoolean("policeButtonSafety", false)) 
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	        builder.setMessage("Are you sure you want to call the police?")
	               .setPositiveButton("Call 911", new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {
	                	   dialog.dismiss();
	                	   executeCall("411");
	                   }
	               })
	               .setNeutralButton("Call non-emergency number", new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {
	                	   dialog.dismiss();
	                	   executeCall("7654948221");
	                   }
	               })
	               .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {
	                       dialog.dismiss();
	                   }
	               });
	        builder.create().show();
		}
		else 
		{
			executeCall("411");
		}
	}

	private void executeCall(String phoneNumber)
	{
		Uri number = Uri.parse("tel:"+phoneNumber);
		Intent dial = new Intent(Intent.ACTION_DIAL, number);
		startActivity(dial);
	}
}
