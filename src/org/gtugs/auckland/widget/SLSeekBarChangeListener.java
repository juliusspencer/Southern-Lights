package org.gtugs.auckland.widget;

import org.gtugs.auckland.usb.led.LedControllerGroup;
import org.gtugs.auckland.usb.led.LedControllerImpl;

import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class SLSeekBarChangeListener extends Object implements
		OnSeekBarChangeListener {

	private int mChannelOutput;
	private LedControllerGroup mLeds;
	
	public SLSeekBarChangeListener(int channelOutput, LedControllerGroup leds) {
		super();
		mChannelOutput = channelOutput;
		mLeds = leds;
	}
	
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		LedControllerImpl.setLed(mLeds, true, mChannelOutput, progress);
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
	}

	

}
