package edu.purdue.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class EventsPageActivity extends Activity {
	Button mPrevButton;
	Button mNextButton;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.events_page_layout);
		
		mPrevButton = (Button)findViewById(R.id.prev_event_button);
		mPrevButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//Get previous event
				
			}
		});
		
		mNextButton = (Button)findViewById(R.id.next_event_button);
		mNextButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//Get next event
				
			}
		});
	}
}
