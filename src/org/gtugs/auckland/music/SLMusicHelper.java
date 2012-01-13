package org.gtugs.auckland.music;

import java.util.List;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio.Media;

public class SLMusicHelper {

	//  -------------------------------------
	public static Cursor createQueryforMusic(ContentResolver cr) {

		// Selection WHERE ... 
		String selection = new String(
				Media.IS_MUSIC+"!=0"
				);

		// Return
		String[] projection = new String[] {
				Media._ID,
				Media.TITLE,
				Media.ARTIST,
				Media.ARTIST_ID,
				Media.ALBUM,
				Media.ALBUM_ID,
				Media.MIME_TYPE,
				Media.DATE_MODIFIED,
				Media.DATA,
				Media.TRACK,
				Media.DURATION
		};

		// Sort order
		String sortOrder = Media.TITLE+" ASC"; 

		return cr.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
				projection,
				selection,
				null,
				sortOrder);
	}
	//  -------------------------------------
	
	public static List<SLMusicItem> getListOfMusic(ContentResolver cr, List<SLMusicItem> musicItems) {
		
		Cursor c = null;
		try {

			c = createQueryforMusic(cr);
			
			if((null!=c)&&(c.moveToFirst())) {
				
				if(0!=musicItems.size())
					musicItems.clear();
				
				while(!c.isAfterLast()) {
					
					SLMusicItem item = new SLMusicItem();
					item.setTrackAlbum(c.getString(c.getColumnIndex(Media.ALBUM)));
					item.setTrackArtist(c.getString(c.getColumnIndex(Media.ARTIST)));
					item.setTrackDuration(getDurationAsStringForLong(c.getLong(c.getColumnIndex(Media.DURATION))));
					item.setTrackLocation(c.getString(c.getColumnIndex(Media.DATA)));
					item.setTrackTitle(c.getString(c.getColumnIndex(Media.TITLE)));
					
					musicItems.add(item);
					
					c.moveToNext();
				}
			}
		} finally {
			if((null!=c)&&(!c.isClosed()))
				c.close();
		}
		
		return musicItems;
	}
	
	//  -------------------------------------
	
	public static String getDurationAsStringForLong(long durationInMs) {

		int durationInSeconds = (int) durationInMs / 1000;
		
		int hours = durationInSeconds / 3600,
		remainder = durationInSeconds % 3600,
		minutes = remainder / 60,
		seconds = remainder % 60;

		StringBuffer sb = new StringBuffer();
		if(hours>0) {
			if(hours<10)
				sb.append(0);
			sb.append(hours);
			sb.append(":");
		}

		if(minutes<10)
			sb.append("0");
		sb.append(minutes);

		sb.append(":");
		
		if(seconds<10)
			sb.append("0");
		sb.append(seconds);

		return sb.toString(); 
		
	}
	
	//  -------------------------------------
}
