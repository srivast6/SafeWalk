package edu.purdue.app;

import edu.purdue.app.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

public class DiningCourts extends Activity {
	
	public final static String EXTRA_MESSAGE = "edu.purdue.app";

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
	
	Intent intent = new Intent(this, DiningInfo.class);
	
	public void earhartInfo(View view){
		// how to get the info about Earhart
		String name = "Earhart Dining Court";
		intent.putExtra(EXTRA_MESSAGE, name);
	}
	
	public void fordInfo(View view){
		// how to get the info about Ford
		String name = "Ford Dining Court";
		intent.putExtra(EXTRA_MESSAGE, name);
		
	}
	
	public void hillenbrandInfo(View view){
		// how to get the info about Hillenbrand
		String name = "Hillenbrand Dining Court";
		intent.putExtra(EXTRA_MESSAGE, name);
	}
	
	public void windsorInfo(View view){
		// how to get the info about Windsor
		String name = "Windsor Dining Court";
		intent.putExtra(EXTRA_MESSAGE, name);
	}
	
	public void wileyInfo(View view){
		// how to get the info about Wiley
		String name = "Wiley Dining Court";
		intent.putExtra(EXTRA_MESSAGE, name);
	}
	
}
