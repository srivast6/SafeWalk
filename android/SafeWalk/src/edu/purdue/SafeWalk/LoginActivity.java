package edu.purdue.SafeWalk;

import java.io.UnsupportedEncodingException;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.provider.Settings.Secure;

/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
public class LoginActivity extends Activity {
	/**
	 * A dummy authentication store containing known user names and passwords.
	 * TODO: remove after connecting to a real authentication system.
	 */
	private static final String[] DUMMY_CREDENTIALS = new String[] {
			"foo@example.com:hello", "bar@example.com:world", "david@debug.edu:test",  };
	

	

	/**
	 * The default email to populate the email field with.
	 */
	public static final String EXTRA_EMAIL = "com.example.android.authenticatordemo.extra.EMAIL";

	/**
	 * Keep track of the login task to ensure we can cancel it if requested.
	 */
	private UserLoginTask mAuthTask = null;
	
	/**
	 * Store Default Device ID, Note this will be changed with the phone is wiped.
	 */
	private String casTicket = "XXXXX";
	private String deviceId = null;
	public static AsyncHttpResponseHandler handler = null;
	
	public static boolean isValidID = false;
	public static boolean validatedID = false;

	// Values for email and password at the time of the login attempt.
	private String mEmail;
	private String mPassword;
	private String mFirstName;
	private String mLastName;
	private String mPhoneNumber;


	// UI references.
	private EditText mEmailView;
	private EditText mPasswordView;
	private EditText mUsernameView;
	private EditText mPhoneNumberView;
	private View mLoginFormView;
	private View mLoginStatusView;
	private TextView mLoginStatusMessageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_login);
		deviceId = Secure.getString(getBaseContext().getContentResolver(),Secure.ANDROID_ID);
		String uuid = getSharedPreferences("pref_server",0).getString("userID", "01");
		 handler = new AsyncHttpResponseHandler(){
			public void onSuccess(String response){
				Log.d("response", response);
				JSONObject IdJSON = null;
				String id = null;
				try {
					 IdJSON = new JSONObject(response);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					 id = IdJSON.getString("id");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				//SharedPreferences.Editor spEditor
				SharedPreferences settings= getSharedPreferences("pref_server", 0);
				SharedPreferences.Editor editor = settings.edit();
				editor.putString("userID", id).commit();
			}
			
		    @Override
		    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error)
		    {
		    	Toast.makeText(getApplicationContext(), "No connection to server", Toast.LENGTH_LONG).show();
		    	Log.d("failure", Integer.toString(statusCode));
		    }
		};

		// Set up the login form.
		mEmail = getIntent().getStringExtra(EXTRA_EMAIL);
		mEmailView = (EditText) findViewById(R.id.email);
		mEmailView.setText(mEmail);

		mPasswordView = (EditText) findViewById(R.id.password);
		mPasswordView
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView textView, int id,
							KeyEvent keyEvent) {
						if (id == R.id.login || id == EditorInfo.IME_NULL) {
							attemptLogin();
							return true;
						}
						return false;
					}
				});

		mUsernameView = (EditText)findViewById(R.id.username);
		mPhoneNumberView = (EditText)findViewById(R.id.phoneNumber);
		
		mLoginFormView = findViewById(R.id.login_form);
		mLoginStatusView = findViewById(R.id.login_status);
		mLoginStatusMessageView = (TextView) findViewById(R.id.login_status_message);
		// If we have a user ID in shared preferences, validate it with the server, and lock the login button
		// If we have an ID but it is not valid, then something bad happened and the user doesn't have an account.
		if(!(uuid.equals("01"))){
			UserLoginTask task = new UserLoginTask();
			task.verifyAccount();
			if(LoginActivity.validatedID){
			//findViewById(R.id.sign_in_button).setEnabled(false);
			}
		}

		findViewById(R.id.sign_in_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						attemptLogin();
					}
				});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptLogin() {
		if (mAuthTask != null) {
			return;
		}

		// Reset errors.
		mEmailView.setError(null);
		mPasswordView.setError(null);

		// Store values at the time of the login attempt.
		mEmail = mEmailView.getText().toString();
		mPassword = mPasswordView.getText().toString();
		String[] names = mUsernameView.getText().toString().split("\\s+");
		if(names.length >= 2){
		mFirstName = names[0].replaceAll("\\s+", "");
		mLastName = names[1].replaceAll("\\s+", "");
		} else {
			mUsernameView.setError(getString(R.string.error_first_and_last_required));
		}
		
		mPhoneNumber = mPhoneNumberView.getText().toString();
		mPhoneNumber.replaceAll("\\s+", "");

		boolean cancel = false;
		View focusView = null;

		// Check for a valid password.
		if (TextUtils.isEmpty(mPassword)) {
			mPasswordView.setError(getString(R.string.error_field_required));
			focusView = mPasswordView;
			cancel = true;
		} else if (mPassword.length() < 4) {
			mPasswordView.setError(getString(R.string.error_invalid_password));
			focusView = mPasswordView;
			cancel = true;
		}

		// Check for a valid email address.
		if (TextUtils.isEmpty(mEmail)) {
			mEmailView.setError(getString(R.string.error_field_required));
			focusView = mEmailView;
			cancel = true;
		} else if (!mEmail.contains("@")) {
			mEmailView.setError(getString(R.string.error_invalid_email));
			focusView = mEmailView;
			cancel = true;
		}
		
		// Check for valid name
	
		// Check for valid phone number
		if(!TextUtils.isDigitsOnly(mPhoneNumber)){
			mPhoneNumberView.setError(getString(R.string.error_invalid_phone_number));
		} else if(TextUtils.isEmpty(mPhoneNumber)){
			mPhoneNumberView.setError(getString(R.string.error_field_required));
			
		}

		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			mLoginStatusMessageView.setText(R.string.login_progress_signing_in);
			showProgress(true);
			mAuthTask = new UserLoginTask();
			mAuthTask.execute((Void) null);
		}
	}

	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mLoginStatusView.setVisibility(View.VISIBLE);
			mLoginStatusView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginStatusView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});

			mLoginFormView.setVisibility(View.VISIBLE);
			mLoginFormView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginFormView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

	/**
	 * Represents an asynchronous login/registration task used to authenticate
	 * the user.
	 */
	public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
			if(!verifyAccount())
			{
				
				Log.d("account not verified", "account not verified");
				createAccount();
			}

			// TODO: register the new account here.
			return true;
		}
		
		public boolean verifyAccount()
		{
			AsyncHttpClient client = new AsyncHttpClient();
			 AsyncHttpResponseHandler idHandler = new AsyncHttpResponseHandler(){
					public void onSuccess(String response){
						Log.d("IDresponse", response);
						JSONObject validIDJSON = null;
						String validID = null;
						try {
							validIDJSON = new JSONObject(response);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						try {
							validID = validIDJSON.getString("validID");
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						// ValidID will be either true or false
						if(validID.equals("true")){
							LoginActivity.isValidID = true;
						}
						
						if(validID.equals("false")){
							LoginActivity.isValidID = false;
						}
						LoginActivity.validatedID = true;
						
						//TODO: This is where we store all of the data of the user. 
					}
					
				    @Override
				    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error)
				    {
				    	Toast.makeText(getApplicationContext(), "No connection to server", Toast.LENGTH_LONG).show();
				    	LoginActivity.validatedID = true;
			
				    }
				};
				
			idHandler.setUseSynchronousMode(true);
			String id = getSharedPreferences("pref_server",0).getString("userID", "01");
			client.get("http://optical-sight-386.appspot.com/users/?"+"UUID="+id, idHandler);
			// We need to wait for the idHandler to return the response from the server.
			while(!LoginActivity.validatedID){
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			// Now the idHandler has returned and decided if the id is valid or not
			 if(LoginActivity.isValidID == false){
				 return false;
			 }
			 else{
				 return true;
				 // TODO: Send ID to server and make sure its a valid user id.
			 }
			
			//store user ID in sharedPreferences
		}
		
		public boolean createAccount()
		{
			
			AsyncHttpClient client = new AsyncHttpClient();
			String gcmID = getSharedPreferences("pref_server",0).getString("registration_id", "");
			Log.d("Sending regID AS", gcmID);
			client.post("http://optical-sight-386.appspot.com/users?"+"firstName="+mFirstName+"&lastName="+mLastName+"&phoneNumber="+mPhoneNumber+"&currentLocation_lat=0.00"+"&currentLocation_lng=0.00"+"&deviceToken="+deviceId+"&purdueCASServiceTicket="+casTicket+"&gcmID="+gcmID, LoginActivity.handler);
			// storing the user id is done in the handler
			return true; 
		}
		
		protected void onProgressUpdate(Integer integers) {
				if(integers == 1){
					Toast.makeText(LoginActivity.this, "You are already logged in", Toast.LENGTH_LONG);
				}
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			mAuthTask = null;
			showProgress(false);

			if (success) {
				Intent returnIntent = new Intent();
				setResult(RESULT_OK, returnIntent);        
				finish();
			} else {
				mPasswordView
						.setError(getString(R.string.error_incorrect_password));
				mPasswordView.requestFocus();
			}
		}

		@Override
		protected void onCancelled() {
			mAuthTask = null;
			showProgress(false);
		}
	}
}
