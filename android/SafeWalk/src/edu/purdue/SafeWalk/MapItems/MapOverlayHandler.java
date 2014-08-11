package edu.purdue.SafeWalk.MapItems;

import com.google.android.gms.maps.internal.m;

import edu.purdue.SafeWalk.R;
import edu.purdue.SafeWalk.R.id;
import edu.purdue.SafeWalk.Widgets.TouchableWrapper;
import edu.purdue.SafeWalk.Widgets.TouchableWrapper.UpdateMapAfterUserInterection;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;

public class MapOverlayHandler implements UpdateMapAfterUserInterection {
	
	private int mShortAnimationDuration; 
	
	public static final String TAG = "MapOverlayHandler";
	Activity applicatonContext; 
	
	public MapOverlayHandler(Activity appContext, int animationDuration) 
	{
		applicatonContext = appContext;
		mShortAnimationDuration = animationDuration;
	}
	
	@Override
	public void onMapDrag() {
		Log.d(TAG, "onMapDrag()");
		fadeBubbleOut();
	}

	@Override
	public void onMapLift() {
		Log.d(TAG, "onMapLift()");
		fadeBubbleIn();
	}
	
	public Context getContext()
	{
		return applicatonContext;
	}
	
	private void fadeBubbleOut() {
		
		applicatonContext.findViewById(R.id.mapPopUpLinLayout).animate().cancel();
		applicatonContext.findViewById(R.id.mapPopUpView1).animate().cancel();
		
	    // Animate the loading view to 0% opacity. After the animation ends,
	    // set its visibility to GONE as an optimization step (it won't
	    // participate in layout passes, etc.)
		applicatonContext.findViewById(R.id.mapPopUpLinLayout).animate()
	            .alpha(0f)
	            .setDuration(mShortAnimationDuration)
	            .setListener(new AnimatorListenerAdapter() {
	                @Override
	                public void onAnimationEnd(Animator animation) {
	                	applicatonContext.findViewById(R.id.mapPopUpLinLayout).setVisibility(View.INVISIBLE);
	                }
	            });
		applicatonContext.findViewById(R.id.mapPopUpView1).animate()
        	.alpha(0f)
        	.setDuration(mShortAnimationDuration)
        	.setListener(new AnimatorListenerAdapter() {
        		@Override
        		public void onAnimationEnd(Animator animation) {
        			applicatonContext.findViewById(R.id.mapPopUpView1).setVisibility(View.INVISIBLE);
        		}
        	});
	}
	
	private void fadeBubbleIn() {
		
		applicatonContext.findViewById(R.id.mapPopUpLinLayout).animate().setListener(null).cancel();
		applicatonContext.findViewById(R.id.mapPopUpView1).animate().setListener(null).cancel();
		
		applicatonContext.findViewById(R.id.mapPopUpLinLayout).setVisibility(View.VISIBLE);
		applicatonContext.findViewById(R.id.mapPopUpLinLayout).animate()
        	.alpha(1f)
        	.setDuration(mShortAnimationDuration)
        	.setListener(null);
		
		applicatonContext.findViewById(R.id.mapPopUpView1).setVisibility(View.VISIBLE);
		applicatonContext.findViewById(R.id.mapPopUpView1).animate()
        	.alpha(1f)
        	.setDuration(mShortAnimationDuration)
        	.setListener(null);
	}
}
