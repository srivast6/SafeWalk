package edu.purdue.SafeWalk.settings;

import java.util.Calendar;

import edu.purdue.SafeWalk.R;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.preference.Preference.OnPreferenceClickListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class VolunteerSettingsFragment extends PreferenceFragment {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setupSimplePreferencesScreen();
		
		setHasOptionsMenu(true);
		
		displayWarning();
	}
	
	private void displayWarning() {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Alert Dialog");
        builder.setMessage("This screen should only be accessed by volunteers approved by the Purdue Police Department. Unauthorized access of this screen is not permitted.\nAre you a certified SafeWalk volunteer?");
        builder.setPositiveButton("Yes", null);
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                setVolunteerMode(false);
                getActivity().getFragmentManager().popBackStackImmediate();
                arg0.cancel();
            }
        });
        builder.show(); //To show the AlertDialog
	}

	@Override
	public void onCreateOptionsMenu(
			      Menu menu, MenuInflater inflater)
	{
		inflater.inflate(R.menu.volunteer_mode_menu, menu);
		
		MenuItem item = menu.getItem(0);
		Switch _switch = (Switch) item.getActionView().findViewById(R.id.switchForActionBar);
		_switch.setChecked(PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean("volunteer_mode", false));
		_switch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked)
				{
					PreferenceScreen ps = getPreferenceScreen();
					for(int i = 0; i < ps.getPreferenceCount(); i++)
					{
						ps.getPreference(i).setEnabled(false);
					}
					setVolunteerMode(false);
					displayWarning();
				}
				else
				{
					PreferenceScreen ps = getPreferenceScreen();
					for(int i = 0; i < ps.getPreferenceCount(); i++)
					{
						ps.getPreference(i).setEnabled(true);
					}
					setVolunteerMode(true);
					
				}
			}
		});
	}
	
	public void setVolunteerMode(boolean bool) {
		Log.d("Volunteer Mode", "Volunteer mode (" + bool + ")!!!");

		SharedPreferences myPreference = PreferenceManager
				.getDefaultSharedPreferences(getActivity());
		SharedPreferences.Editor editor = myPreference.edit();
		editor.putBoolean("volunteer_mode", bool);
		editor.apply();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	    View view = super.onCreateView(inflater, container, savedInstanceState);
	    view.setBackgroundColor(getResources().getColor(android.R.color.black));

	    return view;
	}
	
	/**
	 * Shows the simplified settings UI if the device configuration if the
	 * device configuration dictates that a simplified, single-pane UI should be
	 * shown.
	 */
	private void setupSimplePreferencesScreen() {

		addPreferencesFromResource(R.xml.volunteer_preferences);

		bindPreferenceSummaryToValue(findPreference("pref_volunteer_id"));
	}

	/**
	 * Binds a preference's summary to its value. More specifically, when the
	 * preference's value is changed, its summary (line of text below the
	 * preference title) is updated to reflect the value. The summary is also
	 * immediately updated upon calling this method. The exact display format is
	 * dependent on the type of preference.
	 * 
	 * @see #sBindPreferenceSummaryToValueListener
	 */

	private void bindPreferenceSummaryToValue(Preference preference) {
		// Set the listener to watch for value changes.
		preference
				.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

		// Trigger the listener immediately with the preference's
		// current value.
		sBindPreferenceSummaryToValueListener.onPreferenceChange(
				preference,
				PreferenceManager.getDefaultSharedPreferences(
						preference.getContext()).getString(preference.getKey(),
						""));
	}

	/**
	 * A preference value change listener that updates the preference's summary
	 * to reflect its new value.
	 */
	private Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
		@Override
		public boolean onPreferenceChange(Preference preference, Object value) {
			String stringValue = value.toString();

			if (preference instanceof ListPreference) {
				// For list preferences, look up the correct display value
				// in
				// the preference's 'entries' list.
				ListPreference listPreference = (ListPreference) preference;
				int index = listPreference.findIndexOfValue(stringValue);

				// Set the summary to reflect the new value.
				preference
						.setSummary(index >= 0 ? listPreference.getEntries()[index]
								: null);
			} else {
				// For all other preferences, set the summary to the value's
				// simple string representation.
				preference.setSummary(stringValue);
			}
			return true;
		}
	};
}
