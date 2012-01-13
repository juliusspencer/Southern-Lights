package org.gtugs.auckland.widget;


import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

public class MultiTouchLinearLayout extends LinearLayout {

	private final DJDispatcher mDJDispatcher = new DJDispatcher();

	public MultiTouchLinearLayout(Context context) {
		super(context);
	}

	public MultiTouchLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		return mDJDispatcher.dispatchTouchEvent(this, ev);
	}
}
