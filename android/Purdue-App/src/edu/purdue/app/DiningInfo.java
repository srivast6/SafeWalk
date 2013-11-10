package edu.purdue.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.RatingBar;

public class DiningInfo extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_dining_info);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.dining_info, menu);
		final NumberPicker time = (NumberPicker) findViewById(R.id.numberPicker1);
		final Button sendData = (Button) findViewById(R.id.sendData);
		final RatingBar rb1 = (RatingBar) findViewById(R.id.ratingBar1);
		final RatingBar rb2 = (RatingBar) findViewById(R.id.ratingBar2);
		
		// NumberPicker
		time.setMinValue(0);
		time.setMaxValue(30);
		// end NumberPicker
		
		//Send Data Button
		sendData.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				rb1.setRating(3);
				rb2.setRating(4);
				rb1.setIsIndicator(true);
				rb2.setIsIndicator(true);
			}
		});
		// end SendDataButton
		return true;
	}

}
