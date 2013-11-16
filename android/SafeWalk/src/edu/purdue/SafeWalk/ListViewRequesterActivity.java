package edu.purdue.SafeWalk;

import java.util.ArrayList;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.view.*;

public class ListViewRequesterActivity extends ListActivity {
	
	static final String[] NAMES = {"Kyle", "John", "Luke", "Adam", "Sam", "Suzie", "Jessie", "James", "Meowth"};
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		// Create ArrayList of names to be put into ListItems
		ArrayList<String>stringList = new ArrayList<String>();
		for(int i=0; i<NAMES.length; i++)
			stringList.add(NAMES[i]);
		
		RequesterListAdapter listAdapter = new RequesterListAdapter(this, stringList);
		
		this.setListAdapter(listAdapter);
		ListView lv = this.getListView();
		lv.setTextFilterEnabled(true);
		
		// Anon class for ListView OnClick
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
			    // When clicked, show a toast with the TextView text
			    Toast.makeText(getApplicationContext(),
				"This should do stuff (Not Implemented Yet)", Toast.LENGTH_SHORT).show();
			}
		});
		
	}
	
	

}
