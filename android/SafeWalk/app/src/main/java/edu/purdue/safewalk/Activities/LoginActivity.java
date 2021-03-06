package edu.purdue.safewalk.Activities;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import edu.purdue.safewalk.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
	//private UserLoginTask mAuthTask = null;
	
	/**
	 * Store Default Device ID, Note this will be changed with the phone is wiped.
	 */
	private String casTicket = "XXXXX";
	private String deviceId = null;

	
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
    private AsyncHttpResponseHandler handler;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_login);
        /*
		deviceId = Secure.getString(getBaseContext().getContentResolver(),Secure.ANDROID_ID);
		String uuid = getSharedPreferences("pref_profile",0).getString("userID", "01");
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
				SharedPreferences settings= getSharedPreferences("pref_profile", 0);
				SharedPreferences.Editor editor = settings.edit();
				editor.putString("userID", id).commit();

				*/
        final EditText username = (EditText) findViewById(R.id.username);
        final EditText phoneNumber = (EditText) findViewById(R.id.phoneNumber);
        final EditText email = (EditText) findViewById(R.id.email);
        final EditText password = (EditText) findViewById(R.id.password);
        Button signInButton = (Button) findViewById(R.id.sign_in_button);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser user = new ParseUser();
                user.setUsername(username.getText().toString());
                user.setPassword(password.getText().toString());
                user.setEmail(email.getText().toString());
                user.put("phone", phoneNumber.getText().toString());
                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            finish();
                        } else {
                            // Try to login
                            ParseUser.logInInBackground(username.getText().toString(), password.getText().toString(), new LogInCallback() {
                                @Override
                                public void done(ParseUser parseUser, ParseException e) {
                                    if(e == null) {
                                        finish();
                                    } else {
                                        AlertDialog.Builder ab = new AlertDialog.Builder(LoginActivity.this);
                                        ab.setTitle("Authentication Error");
                                        ab.setMessage(e.getLocalizedMessage());
                                        ab.show();
                                    }
                                }
                            });
                        }
                    }
                });
            }
        });

	}




}
