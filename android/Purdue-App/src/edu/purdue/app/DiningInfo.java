package edu.purdue.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.*;
import android.content.Intent;

public class DiningInfo extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		String message = intent.getStringExtra(DiningCourts.EXTRA_MESSAGE);
	    setContentView(R.layout.activity_dining_info);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.dining_info, menu);
		return true;
	}

}
