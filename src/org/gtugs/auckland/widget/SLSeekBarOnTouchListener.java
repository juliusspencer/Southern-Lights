package org.gtugs.auckland.widget;

import org.gtugs.auckland.SLConstants;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.SeekBar;

public class SLSeekBarOnTouchListener implements OnTouchListener {

	private SeekBar mSeekBar;
//	private int mChannel;
	
	public SLSeekBarOnTouchListener(SeekBar seekbar) {
		super();
		mSeekBar = seekbar;
//		mChannel = channel;
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if(event.getAction() == MotionEvent.ACTION_DOWN) {
			mSeekBar.setProgress(SLConstants.SEEKBAR_MAX_PROGRESS);
			//					setLed(true, 2, SEEKBAR_MAX_PROGRESS);
		} else if(event.getAction() == MotionEvent.ACTION_UP) {
			mSeekBar.setProgress(0);
			//					setLed(false, 2, 0);
		}
		return false;
	}

}
