package edu.purdue.app;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

// Currently, World Weather Online API is being used for weather data.
// We should change to another API if we find one, or pay for a nice one if/when we get sponsoring
// Example Request: http://api.worldweatheronline.com/free/v1/weather.ashx?q=47906&format=json&num_of_days=1&key=64etqgfe58nejx4579ds94a3
// API USERNAME: purduesigapp
// API PASSWORD: sigapp1purdue

// TODO Add caching of weather data. Example: If the weather API we use only updates once an hour, we should cache the request per hour.

public class WeatherActivity extends Activity
{
	static final String API_KEY = "64etqgfe58nejx4579ds94a3";  //API key for worldweatheronline.com
	WifiManager mainWifi;  // Will use this Wi-Fi manager to determine if Wi-Fi is enabled

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.weather_page_layout);

		mainWifi = (WifiManager) getSystemService(Context.WIFI_SERVICE); 
		final TextView testTextView = (TextView) findViewById(R.id.weatherTestTextView);
		final TextView tempTextView = (TextView) findViewById(R.id.temperatureTextView);

		final Button testAPIButton = (Button) findViewById(R.id.testWeatherButton);

		testAPIButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				// Check if Wi-Fi is enabled or not
				if(mainWifi.isWifiEnabled()==false)
				{
					testTextView.setText("Wi-Fi is not enabled.\nPlease enable to test API call.");
				} else {
					// Make an HTTP GET request to retrieve the JSON weather data
					new Thread(new Runnable() {
						public void run() {
							String weatherResponse = sendGetRequest("http://api.worldweatheronline.com/free/v1/weather.ashx?q=west+lafayette,in&format=json&num_of_days=1&key="+API_KEY, null);
							System.out.println(weatherResponse);
							try {
								JSONObject object = (JSONObject) new JSONTokener(weatherResponse).nextValue();
								// JSON parsing to get the current weather in West Lafayette
								final String weatherDescription = (String) object.getJSONObject("data").getJSONArray("current_condition").getJSONObject(0).getJSONArray("weatherDesc").getJSONObject(0).get("value");
								final String currentTemp = (String) object.getJSONObject("data").getJSONArray("current_condition").getJSONObject(0).get("temp_F");
								// Update the test text view's text to reflect the weather description
								Handler handler = new Handler(Looper.getMainLooper());
								handler.post(new Runnable(){
									@Override
									public void run() {
										tempTextView.setText(currentTemp+"¡");
										testTextView.setText("API TEST:\nThe weather in \nWest Lafayette is currently: "+weatherDescription);
									} 
								});
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								testTextView.setText("API test call failed.\nCheck LogCat for details.");
								e.printStackTrace();
							}
						}
					}).start();
				}
			}
		});

	}



	/**
	 * Sends an HTTP GET request to a url
	 *
	 * @param endpoint - The URL of the server. 
	 * @param requestParameters - all the request parameters (Example: "param1=val1&param2=val2"). Note: This method will add the question mark (?) to the request - DO NOT add it yourself
	 * @return - The response from the end point
	 */
	public static String sendGetRequest(String endpoint, String requestParameters)
	{
		String result = null;
		if (endpoint.startsWith("http://"))
		{
			// Send a GET request
			try
			{
				// Send data
				String urlStr = endpoint;
				if (requestParameters != null && requestParameters.length () > 0)
				{
					urlStr += "?" + requestParameters;
				}
				URL url = new URL(urlStr);
				URLConnection conn = url.openConnection ();

				// Get the response
				BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				StringBuffer sb = new StringBuffer();
				String line;
				while ((line = rd.readLine()) != null)
				{
					sb.append(line);
				}
				rd.close();
				result = sb.toString();
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return result;
	}
}



