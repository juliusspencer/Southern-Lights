package org.gtugs.auckland.states;

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;

public class SLChannelControllerViews {

	private Button mChannelButton;
	private SeekBar mChannelSeekBar;
	private CheckBox mChannelCheckBox;
	
	public SLChannelControllerViews(Button channelButton, SeekBar channelSeekBar, CheckBox checkBox) {
		mChannelButton = channelButton;
		mChannelSeekBar = channelSeekBar;
		mChannelCheckBox = checkBox;
	}
	
	public SeekBar getChannelSeekBar() {
		return mChannelSeekBar;
	}
	
	public Button getChannelButton() {
		return mChannelButton;
	}
	
	public CheckBox getChannelCheckBox() {
		return mChannelCheckBox;
	}
}
