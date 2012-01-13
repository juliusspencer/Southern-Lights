package org.gtugs.auckland.music;

public class SLMusicItem {

	private String trackTitle;
	private String trackArtist;
	private String trackAlbum;
	private String trackLocation;
	private String trackDuration;
	
	private boolean selected = false;
	
	public String getTrackTitle() {
		return trackTitle;
	}
	public void setTrackTitle(String trackTitle) {
		this.trackTitle = trackTitle;
	}
	public String getTrackArtist() {
		return trackArtist;
	}
	public void setTrackArtist(String trackArtist) {
		this.trackArtist = trackArtist;
	}
	public String getTrackAlbum() {
		return trackAlbum;
	}
	public void setTrackAlbum(String trackAlbum) {
		this.trackAlbum = trackAlbum;
	}
	public String getTrackLocation() {
		return trackLocation;
	}
	public void setTrackLocation(String trackLocation) {
		this.trackLocation = trackLocation;
	}
	public String getTrackDuration() {
		return trackDuration;
	}
	public void setTrackDuration(String trackDuration) {
		this.trackDuration = trackDuration;
	}
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	
}
