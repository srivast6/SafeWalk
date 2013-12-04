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
		Uri number = Uri.parse("tel:7654947233");
		Intent dial = new Intent(Intent.ACTION_DIAL, number);
		startActivity(dial);
	}
	
	public void onClickPolice(View view)
	{
		Uri number = Uri.parse("tel:7658675309");
		Intent dial = new Intent(Intent.ACTION_DIAL, number);
		startActivity(dial);
	}
}
