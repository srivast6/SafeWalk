package edu.purdue.SafeWalk.settings;

import edu.purdue.SafeWalk.R;
import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.util.Log;
import android.view.Menu;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsActivity extends PreferenceActivity {
	public static final String TAG = "SettingsActivity";

	private Class currentFragment; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		// Display the fragment as the main content.
		getFragmentManager().beginTransaction()
				.replace(android.R.id.content, new SettingsFragment()).commit();
	}

	public void swapFragments(
			VolunteerSettingsFragment volunteerSettingsFragment) {

		getFragmentManager().beginTransaction()
				.add(android.R.id.content, volunteerSettingsFragment)
				.commit();
	}
	
	@Override
	public void onAttachFragment(Fragment fragment) {
	    super.onAttachFragment(fragment);

	    currentFragment = fragment.getClass();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		if(currentFragment.equals(VolunteerSettingsFragment.class))
			getMenuInflater().inflate(R.menu.volunteer_mode_menu, menu);
		else
			menu.clear();
		return super.onCreateOptionsMenu(menu);
	}
}
