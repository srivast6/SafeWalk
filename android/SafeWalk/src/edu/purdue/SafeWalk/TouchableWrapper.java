package edu.purdue.SafeWalk;

import android.content.Context;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/*
 * This class is needed to make the popupBubble on the map disappear when a user drags and reappear when a user lets go.
 * 
 */

public  class TouchableWrapper extends FrameLayout {

	private long lastTouched = 0;
	private static final long SCROLL_TIME = 5L; // 200 Milliseconds, but you can adjust that to your liking
	private UpdateMapAfterUserInterection updateMapAfterUserInterection;

	public TouchableWrapper(Context context) {
		super(context);
		// Force the host activity to implement the UpdateMapAfterUserInterection Interface
		try {
			updateMapAfterUserInterection = (SafeWalk) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement UpdateMapAfterUserInterection");
        }
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			updateMapAfterUserInterection.onMapDrag();
			break;
		case MotionEvent.ACTION_UP:
			updateMapAfterUserInterection.onMapLift();
			break;
		}
		return super.dispatchTouchEvent(ev);
	}

	// Map Activity must implement this interface
    public interface UpdateMapAfterUserInterection {
    	public void onMapDrag();
    	public void onMapLift();
    
    }
}