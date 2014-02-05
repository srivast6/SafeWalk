package edu.purdue.SafeWalk;

import com.google.android.gms.maps.GoogleMap.SnapshotReadyCallback;

import android.os.Bundle;
import android.animation.TimeInterpolator;
import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.support.v4.app.NavUtils;

public class MakeRequestFragment extends Fragment implements SnapshotReadyCallback{

	private static final TimeInterpolator sDecelerator = new DecelerateInterpolator();
    private static final TimeInterpolator sAccelerator = new AccelerateInterpolator();
	private static final String PACKAGE_NAME = "edu.purdue.SafeWalk";
	private int mOriginalOrientation;
	
	
	ImageView mImageView; 
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Show the Up button in the action bar.
		setupActionBar();
		getActivity().getActionBar().setTitle("Make Request");
		getActivity().getActionBar().setSubtitle("Confirm Information");
		
		
		
		
		/*Start DevBytes Code
		
		// Retrieve the data we need for the picture/description to display and
        // the thumbnail to animate it from
        Bundle bundle = getIntent().getExtras();
        Bitmap bitmap = BitmapUtils.getBitmap(getResources(),
                bundle.getInt(PACKAGE_NAME + ".resourceId"));
        String description = bundle.getString(PACKAGE_NAME + ".description");
        final int thumbnailTop = bundle.getInt(PACKAGE_NAME + ".top");
        final int thumbnailLeft = bundle.getInt(PACKAGE_NAME + ".left");
        final int thumbnailWidth = bundle.getInt(PACKAGE_NAME + ".width");
        final int thumbnailHeight = bundle.getInt(PACKAGE_NAME + ".height");
        mOriginalOrientation = bundle.getInt(PACKAGE_NAME + ".orientation");
        
        mBitmapDrawable = new BitmapDrawable(getResources(), bitmap);
        mImageView.setImageDrawable(mBitmapDrawable);
        mTextView.setText(description);
        
        mBackground = new ColorDrawable(Color.BLACK);
        mTopLevelLayout.setBackground(mBackground);

        // Only run the animation if we're coming from the parent activity, not if
        // we're recreated automatically by the window manager (e.g., device rotation)
        if (savedInstanceState == null) {
            ViewTreeObserver observer = mImageView.getViewTreeObserver();
            observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                
                @Override
                public boolean onPreDraw() {
                    mImageView.getViewTreeObserver().removeOnPreDrawListener(this);

                    // Figure out where the thumbnail and full size versions are, relative
                    // to the screen and each other
                    int[] screenLocation = new int[2];
                    mImageView.getLocationOnScreen(screenLocation);
                    mLeftDelta = thumbnailLeft - screenLocation[0];
                    mTopDelta = thumbnailTop - screenLocation[1];
                    
                    // Scale factors to make the large version the same size as the thumbnail
                    mWidthScale = (float) thumbnailWidth / mImageView.getWidth();
                    mHeightScale = (float) thumbnailHeight / mImageView.getHeight();
    
                    runEnterAnimation();
                    
                    return true;
                }
            });
        }
		
		//End DevBytes Code*/
		
	}

	@Override 
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.activity_make_request, container, false);
		mImageView = (ImageView) v.findViewById(R.id.mapImage);
		return v; 
	}
	
	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// Inflate the menu; this adds items to the action bar if it is present.
		inflater.inflate(R.menu.make_request, menu);
	}

	@Override
	public void onSnapshotReady(Bitmap snapshot) {
		// TODO Auto-generated method stub
		
	}

}
