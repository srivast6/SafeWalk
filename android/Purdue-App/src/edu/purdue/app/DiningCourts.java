package edu.purdue.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

public class DiningCourts extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dining_courts);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    //inflater.inflate(R.menu.dining_court_action, menu);
	    inflater.inflate(R.menu.dining_courts, menu);
	    return super.onCreateOptionsMenu(menu);
	}

}
