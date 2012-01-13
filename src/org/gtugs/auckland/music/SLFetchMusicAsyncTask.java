package org.gtugs.auckland.music;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

public class SLFetchMusicAsyncTask extends AsyncTask<Void, Void, Integer> {

	// Views
	ProgressBar pb;
	ListView listView;
	SLMusicItemAdapter listAdapter;
	Context context;
	List<SLMusicItem> musicItems = new ArrayList<SLMusicItem>();
	
	
	public SLFetchMusicAsyncTask(Context context, ProgressBar pb, ListView listView, SLMusicItemAdapter adapter) {
		this.pb = pb;
		this.listAdapter = adapter;
		this.listView = listView;
		this.context = context;
	}
	
	
	@Override
	protected Integer doInBackground(Void... params) {
		int errorResId = 0;
		
		// Set items form Music Store
		SLMusicHelper.getListOfMusic(context.getContentResolver(), musicItems);
		
		return errorResId;
	}

	@Override
	protected void onPostExecute(Integer result) {
		
		pb.setVisibility(View.GONE);
		
		// Update listview
		listAdapter.setItems(musicItems);
		listAdapter.notifyDataSetChanged();
		
		super.onPostExecute(result);
	}

	@Override
	protected void onPreExecute() {
		
		pb.setVisibility(View.VISIBLE);
		
		super.onPreExecute();
	}


}
