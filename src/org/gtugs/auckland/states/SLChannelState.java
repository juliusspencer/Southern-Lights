package org.gtugs.auckland.states;

public class SLChannelState {

	private int mChannelNumber;
	private int mVelocity = 0;
	private boolean mOnOffState = false;

	
	
	
	public int getChannelNumber() {
		return mChannelNumber;
	}
	public void setChannelNumber(int mChannelNumber) {
		this.mChannelNumber = mChannelNumber;
	}
	public int getVelocity() {
		return mVelocity;
	}
	public void setVelocity(int mVelocity) {
		this.mVelocity = mVelocity;
	}
	public boolean getOnOffState() {
		return mOnOffState;
	}
	public void setOnOffState(boolean mOnOffState) {
		this.mOnOffState = mOnOffState;
	}

	
	
}
