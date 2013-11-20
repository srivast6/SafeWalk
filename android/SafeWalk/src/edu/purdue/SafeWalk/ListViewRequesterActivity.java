package edu.purdue.SafeWalk;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;
import android.util.Log;
import android.view.*;

public class ListViewRequesterActivity extends ListActivity implements PopupDialog.NoticeDialogListener{
	
	static final String[] NAMES = {"Kyle", "John", "Luke", "Adam", "Sam", "Suzie", "Jessie", "James", "Meowth"};
	public static boolean isPopupOpen = false;
	PopupWindow popUp;
	
	


	
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
		        PopupDialog dialog =  new PopupDialog();
		        dialog.show(getFragmentManager(), "NoticeDialogFragment");
					//initPopupWindow();
			    // When clicked, show a toast with the TextView text
			  //  Toast.makeText(getApplicationContext(),
				//"This should do stuff (Not Implemented Yet)", Toast.LENGTH_SHORT).show();
			}
		});
		
	}



	@Override
	public void onDialogPositiveClick(DialogFragment dialog) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void onDialogNegativeClick(DialogFragment dialog) {
		// TODO Auto-generated method stub
		
	}
	
	

}
