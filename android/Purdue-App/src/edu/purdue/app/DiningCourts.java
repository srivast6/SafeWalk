package edu.purdue.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

public class DiningCourts extends Activity {
	
	public final static String EXTRA_MESSAGE = "edu.purdue.app.MESSAGE";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dining_courts);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.dining_courts, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	
	//Intent intent;
}
