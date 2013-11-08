package edu.purdue.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class PurdueAppActivity extends Activity implements OnClickListener {
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        ((Button) findViewById(R.id.tempMapButton)).setOnClickListener(this);
        ((Button) findViewById(R.id.tempDiningButton)).setOnClickListener(this);
        ((Button) findViewById(R.id.tempEventsButton)).setOnClickListener(this);
        ((Button) findViewById(R.id.tempWeatherButton)).setOnClickListener(this);
    }

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.tempMapButton:
			Intent i = new Intent(this, edu.purdue.app.map.MapActivity.class);
			startActivity(i);
			break;
		case R.id.tempDiningButton:
			Intent j = new Intent(this, edu.purdue.app.DiningCourts.class);
			startActivity(j);
			break;
		case R.id.tempEventsButton:
			Intent k = new Intent(this, edu.purdue.app.EventsPageActivity.class);
			startActivity(k);
			break;
		case R.id.tempWeatherButton:
			Intent l = new Intent(this, edu.purdue.app.WeatherActivity.class);
			startActivity(l);
			break;
		}
	}
    
}
