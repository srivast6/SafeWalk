package edu.purdue.safewalk.settings;

import java.util.Calendar;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceScreen;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import edu.purdue.safewalk.R;

public class SettingsFragment extends PreferenceFragment {
	Calendar lastVolModeClick;
	int numVolModeClicks = 0;
	static final int neededVolModeClicks = 7;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setupSimplePreferencesScreen();


		PreferenceScreen ps = getPreferenceScreen();
		PreferenceCategory pc = (PreferenceCategory) ps.getPreference(ps
				.getPreferenceCount() - 2);
        // Set up click listener for voltuneer mode, 7 clicks needed
		pc.getPreference(0).setOnPreferenceClickListener(
				new OnPreferenceClickListener() {
					public boolean onPreferenceClick(Preference preference) {
						if (!PreferenceManager.getDefaultSharedPreferences(
								getActivity()).getBoolean("volunteer_mode",
								false)) {
							Calendar now = Calendar.getInstance();
							if (lastVolModeClick == null) {
								Log.d("Volunteer Mode", "First Tap");
								numVolModeClicks++;
								lastVolModeClick = now;
								return false;
							}

							long timeElapsed = now.getTime().getTime()
									- lastVolModeClick.getTime().getTime();
							Log.d("Volunteer Mode", "Time Elapsed = "
									+ timeElapsed);
							if (timeElapsed < 700) {
								numVolModeClicks++;
								Log.d("Volunteer Mode", "Tap #"
										+ numVolModeClicks);
								if (numVolModeClicks == neededVolModeClicks) {
									enableVolunteerMode();
									numVolModeClicks = 0;
								}
							} else {
								numVolModeClicks = 0;
							}
							lastVolModeClick = now;
						}
						return false;

					}
				});

        // Set up listener for whether to use Dev server or not.
        pc = (PreferenceCategory)ps.getPreference(0);
        pc.getPreference(0).setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                if (preference instanceof CheckBoxPreference){
                    CheckBoxPreference checkBoxPreference = (CheckBoxPreference) preference;
                    SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getActivity()).edit();
                    if(checkBoxPreference.isChecked()){
                        checkBoxPreference.setSummary("safewalk.parseapp.com");
                        editor.putString("pref_server","http://safewalk.parseapp.com").commit();
                    } else {
                        checkBoxPreference.setSummary("safewalkdev.parseapp.com");
                        editor.putString("pref_server","http://safewalkdev.parseapp.com").commit();
                    }
                }
                return true;
            }
        });

		Preference pref = ps.getPreference(ps.getPreferenceCount() - 1);
		pref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			public boolean onPreferenceClick(Preference preference) {
				if (PreferenceManager
						.getDefaultSharedPreferences(getActivity()).getBoolean(
								"volunteer_mode", false)) {
					((SettingsActivity) getActivity())
							.swapFragments(new VolunteerSettingsFragment());
					return true;
				}
				return false;
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.d("SettingsFragment", "onResume()");
		PreferenceScreen ps = getPreferenceScreen();
		Preference pref = ps.getPreference(ps.getPreferenceCount() - 1);
		pref.setEnabled(PreferenceManager.getDefaultSharedPreferences(
				getActivity()).getBoolean("volunteer_mode", false));
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = super.onCreateView(inflater, container, savedInstanceState);

		return view;
	}

	public void enableVolunteerMode() {
		Log.d("Volunteer Mode", "Volunteer mode enabled!!!");
		Toast.makeText(getActivity().getApplicationContext(),
				"Volunteer Mode Enabled!", Toast.LENGTH_LONG).show();

		SharedPreferences myPreference = PreferenceManager
				.getDefaultSharedPreferences(getActivity());
		SharedPreferences.Editor editor = myPreference.edit();
		editor.putBoolean("volunteer_mode", true);
		editor.apply();

		PreferenceScreen ps = getPreferenceScreen();
		Preference pref = ps.getPreference(ps.getPreferenceCount() - 1);
		pref.setEnabled(true);
	}

	/**
	 * Shows the simplified settings UI if the device configuration if the
	 * device configuration dictates that a simplified, single-pane UI should be
	 * shown.
	 */
	private void setupSimplePreferencesScreen() {

		addPreferencesFromResource(R.xml.preferences);

		bindPreferenceSummaryToValue(findPreference("pref_loc_update_frequency"));
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
