package org.gtugs.auckland.states;

import java.util.List;

public class SLRecording {

	private List<SLChannelStates> mChannelsStates;
	private String mTrackLocation;
	
	public List<SLChannelStates> getChannelsStates() {
		return mChannelsStates;
	}
	public void setChannelsStates(List<SLChannelStates> mChannelsStates) {
		this.mChannelsStates = mChannelsStates;
	}
	public String getTrackLocation() {
		return mTrackLocation;
	}
	public void setTrackLocation(String mTrackLocation) {
		this.mTrackLocation = mTrackLocation;
	}
	
	
	
}
