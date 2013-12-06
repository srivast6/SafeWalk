package edu.purdue.app;

import java.util.Locale;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

public class DiningCourtsTwo extends FragmentActivity {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dining_courts_two);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.dining_courts_two, menu);
		return true;
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position){
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			Fragment fragment1 = new DummySectionFragment();
			Bundle args1 = new Bundle();
			args1.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
			fragment1.setArguments(args1);
			return fragment1;
			/*
			switch(position){
			case 0:
				Fragment fragment1 = new DummySectionFragment();
				Bundle args1 = new Bundle();
				args1.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
				fragment1.setArguments(args1);
				return fragment1;
				
			case 1:
				Fragment fragment2 = new DummySectionFragment();
				Bundle args2 = new Bundle();
				args2.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 2);
				fragment2.setArguments(args2);
				return fragment2;
				
			case 3:
				Fragment fragment3 = new DummySectionFragment();
				Bundle args3 = new Bundle();
				args3.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 3);
				fragment3.setArguments(args3);
				return fragment3;
				
			case 4:
				Fragment fragment4 = new DummySectionFragment();
				Bundle args4 = new Bundle();
				args4.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 4);
				fragment4.setArguments(args4);
				return fragment4;
				
			case 5:
				Fragment fragment5 = new DummySectionFragment();
				Bundle args5 = new Bundle();
				args5.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 5);
				fragment5.setArguments(args5);
				return fragment5;
				
			default:
				return null;
			}
			*/
			
		}

		@Override
		public int getCount() {
			// Show 5 total pages.
			return 5;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.Earhart).toUpperCase(l);
			case 1:
				return getString(R.string.Ford).toUpperCase(l);
			case 2:
				return getString(R.string.Hillenbrand).toUpperCase(l);
			case 3:
				return getString(R.string.Wiley).toUpperCase(l);
			case 4:
				return getString(R.string.Windsor).toUpperCase(l);
			}
			return null;
		}
	}

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class DummySectionFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		
		public static final String ARG_SECTION_NUMBER = "section_number";
		public DummySectionFragment() {
		}

		private View rootView;
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
			rootView = inflater.inflate(R.layout.fragment_dining_courts_two_dummy,container, false);
			//setRatingBars();
			return rootView;
		}
		/*
		public void setRatingBars(){
			final RatingBar rb1 = (RatingBar) rootView.findViewById(R.id.ratingBar1);
			final RatingBar rb2 = (RatingBar) rootView.findViewById(R.id.ratingBar2);
			final RatingBar rb3 = (RatingBar) rootView.findViewById(R.id.ratingBar3);
			rb3.setRating(1.7f);
			rb2.setRating(3.5f);
			rb1.setRating(4.2f);
		}
		*/
	}

}
