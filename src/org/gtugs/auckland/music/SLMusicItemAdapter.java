package org.gtugs.auckland.music;

import java.util.List;

import org.gtugs.auckland.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class SLMusicItemAdapter extends ArrayAdapter<SLMusicItem> {

	
	//  -------------------------------------
	public SLMusicItemAdapter(Context context, int textViewResourceId,
			List<SLMusicItem> objects) {
		super(context, textViewResourceId, objects);
	}

	//  -------------------------------------
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		final RowWrapper wrapper;

		View row = convertView;

		// Create the view if necessary
		if(null==row) {
			
			LayoutInflater inflater = LayoutInflater.from(getContext());
			row = inflater.inflate(R.layout.music_item_row, null);

			wrapper = new RowWrapper(row);
			row.setTag(wrapper);

		} else {
			wrapper = (RowWrapper) row.getTag();
		}
		
		// Fill the view
		wrapper.getTrackTitleTextView().setText("");
		if(null!=getItem(position).getTrackTitle())
			wrapper.getTrackTitleTextView().setText(getItem(position).getTrackTitle());
		
		wrapper.getAlbumTextView().setText("");
		if(null!=getItem(position).getTrackAlbum())
			wrapper.getAlbumTextView().setText(getItem(position).getTrackAlbum());
		
		wrapper.getArtistTextView().setText("");
		if(null!=getItem(position).getTrackArtist())
			wrapper.getArtistTextView().setText(getItem(position).getTrackArtist());
		
		wrapper.getDurationTextView().setText("");
		if(null!=getItem(position).getTrackDuration())
			wrapper.getDurationTextView().setText(getItem(position).getTrackDuration());

		if(getItem(position).isSelected())
			row.setBackgroundColor(getContext().getResources().getColor(R.color.dark_grey));
		else
			row.setBackgroundColor(getContext().getResources().getColor(R.color.black));
		
		return row;
	}
	
	
	//  -------------------------------------

	public void setItems(List<SLMusicItem> items) {
		clear();
		
		for(SLMusicItem item : items) {
			add(item);
		}
	}
	
	//  -------------------------------------

	class RowWrapper {
		
		TextView durationTextView;
		TextView trackTitleTextView;
		TextView albumTextView;
		TextView artistTextView;
		
		View row;
		
		public RowWrapper(View row) {
			this.row = row;
		}
		
		
		public TextView getDurationTextView() {
			if(null==durationTextView) {
				durationTextView = (TextView) row.findViewById(R.id.duration_textview);
			}
			return durationTextView;
		}

		public TextView getTrackTitleTextView() {
			if(null==trackTitleTextView) {
				trackTitleTextView = (TextView) row.findViewById(R.id.track_title_textview);
			}
			return trackTitleTextView;
		}
		
		public TextView getAlbumTextView() {
			if(null==albumTextView) {
				albumTextView = (TextView) row.findViewById(R.id.track_album_textview);
			}
			return albumTextView;
		}
		
		public TextView getArtistTextView() {
			if(null==artistTextView) {
				artistTextView = (TextView) row.findViewById(R.id.track_artist_textview);
			}
			return artistTextView;
		}
		
	}
	
	//  -------------------------------------
}
