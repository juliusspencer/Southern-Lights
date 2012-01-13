package org.gtugs.auckland.states;

import java.util.ArrayList;
import java.util.List;

public class SLChannelStates {

	private long mTimestamp = 0;

	private List<SLChannelState> mChannelStates = new ArrayList<SLChannelState>();

	public long getTimestamp() {
		return mTimestamp;
	}

	public void setTimestamp(long mTimestamp) {
		this.mTimestamp = mTimestamp;
	}

	public List<SLChannelState> getChannelStates() {
		return mChannelStates;
	}

	public void setChannelStates(List<SLChannelState> mChannelStates) {
		this.mChannelStates = mChannelStates;
	}

	
	
	
}
