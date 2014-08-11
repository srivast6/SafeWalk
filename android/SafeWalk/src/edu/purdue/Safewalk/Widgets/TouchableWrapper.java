package edu.purdue.SafeWalk.Widgets;

import edu.purdue.SafeWalk.MapItems.MapOverlayHandler;
import android.content.Context;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/*
 * This class is needed to make the popupBubble on the map disappear when a user drags and reappear when a user lets go.
 * 
 */

public class TouchableWrapper extends FrameLayout {

	

	public TouchableWrapper(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	public TouchableWrapper(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	public TouchableWrapper(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	private long lastTouched = 0;
	private static final long SCROLL_TIME = 5L; // 200 Milliseconds, but you can
												// adjust that to your liking
	private UpdateMapAfterUserInterection updateMapAfterUserInterection;

//	public TouchableWrapper(MapOverlayHandler mapOverlayHandler) {
//		super(mapOverlayHandler.getContext());
//	}

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

	public void setOverlayHandler(MapOverlayHandler mapOverlayHandler) {
		// Force the host activity to implement the
		// UpdateMapAfterUserInterection Interface
		try {
			updateMapAfterUserInterection = mapOverlayHandler;
		} catch (ClassCastException e) {
			throw new ClassCastException(mapOverlayHandler.getContext()
					.toString()
					+ " must implement UpdateMapAfterUserInterection");
		}
	}
}