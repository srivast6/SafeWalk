package edu.purdue.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PurdueAppActivity extends Activity{
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Button mapbutton = (Button) findViewById(R.id.MapButton);
        mapbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), edu.purdue.app.map.MapActivity.class));
            }
        });
        Button weatherbutton = (Button) findViewById(R.id.WeatherButton);
        weatherbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	final Intent newStuff = new Intent(getApplicationContext(), WeatherActivity.class);
            	startActivity(newStuff);
            }
        });
        Button emailbutton = (Button) findViewById(R.id.EmailButton);
        emailbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Do something in response to button click
            }
        });
        Button corecbutton = (Button) findViewById(R.id.CoRecButton);
        corecbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Do something in response to button click
            }
        });
        Button diningcourtsbutton = (Button) findViewById(R.id.DiningCourtsButton);
        diningcourtsbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	final Intent newStuff = new Intent(getApplicationContext(), DiningCourtsTwo.class);
            	startActivity(newStuff);
            }
        });
        Button eventsbutton = (Button) findViewById(R.id.EventsButton);
        eventsbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Do something in response to button click
            }
        });
        Button sportsbutton = (Button) findViewById(R.id.SportsButton);
        sportsbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Do something in response to button click
            }
        });
        
        
    }

	
    
}
