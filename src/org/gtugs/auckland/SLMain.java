package org.gtugs.auckland;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.gtugs.auckland.music.SLFetchMusicAsyncTask;
import org.gtugs.auckland.music.SLMusicItem;
import org.gtugs.auckland.music.SLMusicItemAdapter;
import org.gtugs.auckland.states.SLChannelControllerViews;
import org.gtugs.auckland.states.SLChannelState;
import org.gtugs.auckland.states.SLChannelStates;
import org.gtugs.auckland.states.SLRecording;
import org.gtugs.auckland.usb.UsbAccessoryController;
import org.gtugs.auckland.usb.led.LedControllerGroup;
import org.gtugs.auckland.usb.led.LedControllerImpl;
import org.gtugs.auckland.widget.SLSeekBarChangeListener;
import org.gtugs.auckland.widget.SLSeekBarOnTouchListener;
import org.gtugs.auckland.widget.VerticalSeekBar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Toast;

public class SLMain extends Activity implements OnItemClickListener, OnCompletionListener, OnErrorListener {

//	private String TAG = "SLS";

	private static final String RECORDED_ENTRIES = "RECORDED_ENTRIES";
	
	// Handler
	private Handler mHandler = new Handler();

	// Json Mapper
	private ObjectMapper mapper = new ObjectMapper();
	
	// Views
	private VerticalSeekBar mChannelOneSeekBar;
	private VerticalSeekBar mChannelTwoSeekBar;
	private VerticalSeekBar mChannelThreeSeekBar;
	private VerticalSeekBar mChannelFourSeekBar;
	private VerticalSeekBar mChannelFiveSeekBar;
	private VerticalSeekBar mChannelSixSeekBar;
	private VerticalSeekBar mChannelSevenSeekBar;
	private VerticalSeekBar mChannelEightSeekBar;
	private VerticalSeekBar mChannelNineSeekBar;

	private Button mChannelOneButton;
	private Button mChannelTwoButton;
	private Button mChannelThreeButton;
	private Button mChannelFourButton;
	private Button mChannelFiveButton;
	private Button mChannelSixButton;
	private Button mChannelSevenButton;
	private Button mChannelEightButton;
	private Button mChannelNineButton;

	private CheckBox mChannelOneCheckBox;
	private CheckBox mChannelTwoCheckBox;
	private CheckBox mChannelThreeCheckBox;
	private CheckBox mChannelFourCheckBox;
	private CheckBox mChannelFiveCheckBox;
	private CheckBox mChannelSixCheckBox;
	private CheckBox mChannelSevenCheckBox;
	private CheckBox mChannelEightCheckBox;
	private CheckBox mChannelNineCheckBox;

	//	private ImageButton mRecordImageButton;
	//	private ImageButton mPlayImageButton;
	//	private ImageButton mRepeatImageButton;

	private ListView mMusicListView;
	private ProgressBar mMusicLoadingProgressBar;

	// Music info
	private SLMusicItemAdapter mMusicAdapter;
	private boolean mIsLoadingMusic = false;
	private List<SLMusicItem> mMusicItems = new ArrayList<SLMusicItem>();
	private int mLastSelectedItemPosition = -1;
	private String mLastSelectedItemPath = null;

	// Music Player
	private MediaPlayer mMediaPlayer;

	// Groups
	private List<SLChannelControllerViews> mChannelViews = new ArrayList<SLChannelControllerViews>();

	// Channels State
	private List<SLChannelStates> mChannelsStates = new ArrayList<SLChannelStates>();

	// Core State
	private boolean mIsPlaying = false;
	private boolean mIsRecording = false;
	private boolean mIsRepeating = false;

	// Data storage
	private ProgressDialog mProgressDialog;
	
	// Playback tick
	private int mCurrentTick = 0;
	private long mStartTimestamp = 0;

	private UsbAccessoryController controller;

	private LedControllerGroup leds;

	//   -----------------------------------------------

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Bind layout
		setContentView(R.layout.main);


		// Bind views
		mChannelOneSeekBar = (VerticalSeekBar) findViewById(R.id.channel_one_seekbar);
		mChannelTwoSeekBar = (VerticalSeekBar) findViewById(R.id.channel_two_seekbar);
		mChannelThreeSeekBar = (VerticalSeekBar) findViewById(R.id.channel_three_seekbar);
		mChannelFourSeekBar = (VerticalSeekBar) findViewById(R.id.channel_four_seekbar);
		mChannelFiveSeekBar = (VerticalSeekBar) findViewById(R.id.channel_five_seekbar);
		mChannelSixSeekBar = (VerticalSeekBar) findViewById(R.id.channel_six_seekbar);
		mChannelSevenSeekBar = (VerticalSeekBar) findViewById(R.id.channel_seven_seekbar);
		mChannelEightSeekBar = (VerticalSeekBar) findViewById(R.id.channel_eight_seekbar);
		mChannelNineSeekBar = (VerticalSeekBar) findViewById(R.id.channel_nine_seekbar);

		mChannelOneButton = (Button) findViewById(R.id.channel_one_Button);
		mChannelTwoButton = (Button) findViewById(R.id.channel_two_Button);
		mChannelThreeButton = (Button) findViewById(R.id.channel_three_Button);
		mChannelFourButton = (Button) findViewById(R.id.channel_four_Button);
		mChannelFiveButton = (Button) findViewById(R.id.channel_five_Button);
		mChannelSixButton = (Button) findViewById(R.id.channel_six_Button);
		mChannelSevenButton = (Button) findViewById(R.id.channel_seven_Button);
		mChannelEightButton = (Button) findViewById(R.id.channel_eight_Button);
		mChannelNineButton = (Button) findViewById(R.id.channel_nine_Button);

		mChannelOneCheckBox = (CheckBox) findViewById(R.id.channel_one_checkbox);
		mChannelTwoCheckBox = (CheckBox) findViewById(R.id.channel_two_checkbox);
		mChannelThreeCheckBox = (CheckBox) findViewById(R.id.channel_three_checkbox);
		mChannelFourCheckBox = (CheckBox) findViewById(R.id.channel_four_checkbox);
		mChannelFiveCheckBox = (CheckBox) findViewById(R.id.channel_five_checkbox);
		mChannelSixCheckBox = (CheckBox) findViewById(R.id.channel_six_checkbox);
		mChannelSevenCheckBox = (CheckBox) findViewById(R.id.channel_seven_checkbox);
		mChannelEightCheckBox = (CheckBox) findViewById(R.id.channel_eight_checkbox);
		mChannelNineCheckBox = (CheckBox) findViewById(R.id.channel_nine_checkbox);


		// Usb controller
		controller = new UsbAccessoryController();
		controller.initialise(getApplicationContext());
		leds = new LedControllerGroup(controller);


		// Configure Views
		configureSeekBars();
		configureButtons();

		// Music List setup
		mMusicListView = (ListView) findViewById(R.id.music_listview);
		mMusicAdapter = new SLMusicItemAdapter(getApplicationContext(), R.id.track_title_textview, mMusicItems);
		mMusicListView.setAdapter(mMusicAdapter);
		mMusicListView.setFastScrollEnabled(true);
		mMusicListView.setOnItemClickListener(this);

		// Fetch Music
		mMusicLoadingProgressBar = (ProgressBar) findViewById(R.id.loading_music_progressbar);
		if(!mIsLoadingMusic) {
			mIsLoadingMusic = true;
			new SLFetchMusicAsyncTask(getApplicationContext(), mMusicLoadingProgressBar, mMusicListView, mMusicAdapter).execute();
		}

		// Set up media player
		mMediaPlayer = new MediaPlayer();

		// Configure mapper
		mapper.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}


	@Override
	public void onPause() {
		super.onPause();
		if(null!=controller) {
			controller.closeAccessory();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

		if(null!=controller) {
			controller.openAccessory();
		}
	}


	@Override
	public void onDestroy() {
		if(null!=controller) {
			controller.destroy(getApplicationContext());
		}

		if(null!=mMediaPlayer)
			mMediaPlayer.release();

		if(null!=mProgressDialog)
			mProgressDialog.dismiss();
		
		super.onDestroy();
	}


	//   -----------------------------------------------
	/**
	 * Set up the Buttons
	 */
	private void configureButtons() {

		// Set button listeners
		for(SLChannelControllerViews views : mChannelViews) {
			views.getChannelButton().setOnTouchListener(new SLSeekBarOnTouchListener(views.getChannelSeekBar()));
		}
	}

	//   -----------------------------------------------
	/**
	 * Set up seekbars
	 */
	private void configureSeekBars() {

		// Add seekbars and buttons for control
		mChannelViews.add(new SLChannelControllerViews(mChannelOneButton, mChannelOneSeekBar, mChannelOneCheckBox));
		mChannelViews.add(new SLChannelControllerViews(mChannelTwoButton, mChannelTwoSeekBar, mChannelTwoCheckBox));
		mChannelViews.add(new SLChannelControllerViews(mChannelThreeButton, mChannelThreeSeekBar, mChannelThreeCheckBox));
		mChannelViews.add(new SLChannelControllerViews(mChannelFourButton, mChannelFourSeekBar, mChannelFourCheckBox));
		mChannelViews.add(new SLChannelControllerViews(mChannelFiveButton, mChannelFiveSeekBar, mChannelFiveCheckBox));
		mChannelViews.add(new SLChannelControllerViews(mChannelSixButton, mChannelSixSeekBar, mChannelSixCheckBox));
		mChannelViews.add(new SLChannelControllerViews(mChannelSevenButton, mChannelSevenSeekBar, mChannelSevenCheckBox));
		mChannelViews.add(new SLChannelControllerViews(mChannelEightButton, mChannelEightSeekBar, mChannelEightCheckBox));
		mChannelViews.add(new SLChannelControllerViews(mChannelNineButton, mChannelNineSeekBar, mChannelNineCheckBox));

		// Configure seek listeners and seek bars' max
		for(SLChannelControllerViews sb : mChannelViews) {
			sb.getChannelSeekBar().setOnSeekBarChangeListener(new SLSeekBarChangeListener(mChannelViews.indexOf(sb), leds));
			sb.getChannelSeekBar().setMax(SLConstants.SEEKBAR_MAX_PROGRESS);
		}

	}

	//   -----------------------------------------------

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

		// Unselect last item
		if(mLastSelectedItemPosition!=-1) {
			mMusicAdapter.getItem(mLastSelectedItemPosition).setSelected(false);
		}

		// Update selected position
		mLastSelectedItemPosition = position;

		// Select item
		SLMusicItem item = mMusicAdapter.getItem(mLastSelectedItemPosition);
		mLastSelectedItemPath = item.getTrackLocation(); 
		item.setSelected(true);

		// Update
		mMusicAdapter.notifyDataSetChanged();
	}

	//   -----------------------------------------------
	//   Player	
	//   -----------------------------------------------


	protected void startPlayer() {
		mIsPlaying = true;
		mCurrentTick = 0;
		mHandler.post(mPlayChannelStatesRunnable);

		mStartTimestamp = System.currentTimeMillis();

		startMusic();
	}


	protected void stopPlayer() {

		// Stop "playing" events
		mHandler.removeCallbacks(mPlayChannelStatesRunnable);

		// Toggle state
		mIsPlaying = false;

		//  Update views
		invalidateOptionsMenu();

		stopMusic();

		for(SLChannelControllerViews sb : mChannelViews) {
			sb.getChannelCheckBox().setChecked(false);
		}
	}

	private Runnable mPlayChannelStatesRunnable = new Runnable() {
		@Override
		public void run() {

			// Check whether we have already shown the last available recorded state
			if(mCurrentTick < mChannelsStates.size()) {


				// Get the states of the channels at this time 
				SLChannelStates css = mChannelsStates.get(mCurrentTick);

				// Post again
				long timeSinceStartingPlaying = System.currentTimeMillis() - mStartTimestamp;

				if(mCurrentTick + 1 < mChannelsStates.size()) {
					long timeSinceFirstStateOfNextState = mChannelsStates.get(mCurrentTick).getTimestamp() - mChannelsStates.get(0).getTimestamp();
					mHandler.postDelayed(mPlayChannelStatesRunnable, timeSinceFirstStateOfNextState - timeSinceStartingPlaying);
				} else {
					mHandler.postDelayed(mPlayChannelStatesRunnable, SLConstants.RECORDING_PERIOD_IN_MILLIS);
				}



				for(SLChannelState cs : css.getChannelStates()) {

					switch (cs.getChannelNumber()) {
					case 0:
						if(mChannelOneCheckBox.isChecked()) {
							cs.setVelocity(mChannelOneSeekBar.getProgress());
						} else {
							// Change views
							mChannelOneSeekBar.setProgress(cs.getVelocity());
						}
						break;
					case 1:
						if(mChannelTwoCheckBox.isChecked()) {
							cs.setVelocity(mChannelTwoSeekBar.getProgress());
						} else {
							mChannelTwoSeekBar.setProgress(cs.getVelocity());
						}
						break;
					case 2:
						if(mChannelThreeCheckBox.isChecked()) {
							cs.setVelocity(mChannelThreeSeekBar.getProgress());						
						} else {
							mChannelThreeSeekBar.setProgress(cs.getVelocity());
						}
						break;
					case 3:
						if(mChannelFourCheckBox.isChecked()) {
							cs.setVelocity(mChannelFourSeekBar.getProgress());						
						} else {
							mChannelFourSeekBar.setProgress(cs.getVelocity());
						}
						break;
					case 4:
						if(mChannelFiveCheckBox.isChecked()) {
							cs.setVelocity(mChannelFiveSeekBar.getProgress());
						} else {
							mChannelFiveSeekBar.setProgress(cs.getVelocity());
						}
						break;
					case 5:
						if(mChannelSixCheckBox.isChecked()) {
							cs.setVelocity(mChannelSixSeekBar.getProgress());
						} else {
							mChannelSixSeekBar.setProgress(cs.getVelocity());
						}
						break;
					case 6:
						if(mChannelSevenCheckBox.isChecked()) {
							cs.setVelocity(mChannelSevenSeekBar.getProgress());
						} else {
							mChannelSevenSeekBar.setProgress(cs.getVelocity());
						}
						break;
					case 7:
						if(mChannelEightCheckBox.isChecked()) {
							cs.setVelocity(mChannelEightSeekBar.getProgress());
						} else {
							mChannelEightSeekBar.setProgress(cs.getVelocity());
						}
						break;
					case 8:
						if(mChannelNineCheckBox.isChecked()) {
							cs.setVelocity(mChannelNineSeekBar.getProgress());
						} else {
							mChannelNineSeekBar.setProgress(cs.getVelocity());
						}
						break;
					default:
						break;
					}

					// Change leds
					LedControllerImpl.setLed(leds, cs.getOnOffState(), cs.getChannelNumber(), cs.getVelocity());

				}

				mCurrentTick++;
			} else {
				if(!mIsRepeating) {
					stopPlayer();
				} else {
					stopMusic();

					startPlayer();
				}
			}
		}
	};

	//   -----------------------------------------------
	//   Recorder
	//   -----------------------------------------------

	private void startRecorder() {
		mChannelsStates = new ArrayList<SLChannelStates>();

		mHandler.post(mRecordChannelStatesRunnable);

		startMusic();
	}

	protected void stopRecorder() {
		mHandler.removeCallbacks(mRecordChannelStatesRunnable);

		stopMusic();

		invalidateOptionsMenu();
	}

	private Runnable mRecordChannelStatesRunnable = new Runnable() {
		@Override
		public void run() {

			// Post again (put to the beginning for consistency)
			mHandler.postDelayed(mRecordChannelStatesRunnable, SLConstants.RECORDING_PERIOD_IN_MILLIS);

			// Create the states holder
			SLChannelStates mChannelStates = new SLChannelStates();
			mChannelStates.setTimestamp(System.currentTimeMillis());

			// Store each channel's state
			for(int i=0; i < mChannelViews.size(); i++) {
				SeekBar seekBar = mChannelViews.get(i).getChannelSeekBar();

				SLChannelState cs = new SLChannelState();
				cs.setChannelNumber(i);
				cs.setOnOffState(true);
				cs.setVelocity(seekBar.getProgress());

				mChannelStates.getChannelStates().add(cs);
			}

			mChannelsStates.add(mChannelStates);

		}
	};

	//   -----------------------------------------------
	//	 Media Player
	//   -----------------------------------------------

	private void startMusic() {
		// Set up media player
		if(null!=mLastSelectedItemPath) {
			try {
				mMediaPlayer.reset();
				mMediaPlayer.setDataSource(mLastSelectedItemPath);
				mMediaPlayer.setLooping(mIsRepeating);
				mMediaPlayer.prepare();
				mMediaPlayer.start();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {}
		}
	}

	private void stopMusic() {
		// Stop the music
		if(mMediaPlayer.isPlaying()) {
			mMediaPlayer.stop();
		}
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
	}


	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		return false;
	}


	//   -----------------------------------------------
	//   OPTIONS / MENU
	//   -----------------------------------------------

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);

		return super.onCreateOptionsMenu(menu);
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.menu_about:

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(R.string.about_message)
			.setNegativeButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.cancel();
				}
			})
			.setTitle(R.string.about_title)
			.setIcon(R.drawable.ic_launcher);
			AlertDialog alert = builder.create();
			alert.show();

			return true;
		case R.id.menu_play:

			if(mIsPlaying) {
				// Stop playing!
				stopPlayer();
			} else {
				// Start playing!
				item.setIcon(R.drawable.btn_ic_video_record_stop);
				startPlayer();
			}

			invalidateOptionsMenu();

			return true;
		case R.id.menu_record:


			
			if(mIsRecording) {
				// Stop recording!
				item.setIcon(R.drawable.btn_ic_video_record);

				stopRecorder();
				for(SLChannelControllerViews sb : mChannelViews) {
					sb.getChannelCheckBox().setChecked(false);
				}
			} else {
				
				for(SLChannelControllerViews sb : mChannelViews) {
					sb.getChannelCheckBox().setChecked(true);
				}

				// Start recording!
				item.setIcon(R.drawable.btn_ic_video_record_stop);

				startRecorder();
			}
			// Toggle state
			mIsRecording = !mIsRecording;

			invalidateOptionsMenu();

			return true;
		case R.id.menu_repeat:

			if(!mIsRepeating)
				item.setIcon(R.drawable.ic_mp_repeat_all_btn);
			else
				item.setIcon(R.drawable.ic_mp_repeat_off_btn);

			mIsRepeating = !mIsRepeating;

			invalidateOptionsMenu();

			return true;
		case R.id.menu_load:
			// Show load dialog
			Set<String> entries = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getStringSet(RECORDED_ENTRIES, null);
			if(null==entries) {
				Toast.makeText(getApplicationContext(), R.string.no_recordings_have_been_saved, Toast.LENGTH_SHORT).show();
			} else {
				showLoadDialog();
			}
			return true;
		case R.id.menu_save:
			// Show save dialog
			if((null==mChannelsStates)||(0==mChannelsStates.size())) {
				Toast.makeText(getApplicationContext(), R.string.no_recordings_have_been_made, Toast.LENGTH_SHORT).show();
			} else {
				showSaveDialog();
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}

	}


	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {

		MenuItem playItem = menu.findItem(R.id.menu_play);
		MenuItem recItem = menu.findItem(R.id.menu_record);
		MenuItem repeatItem = menu.findItem(R.id.menu_repeat);

		// Play state
		if(mIsPlaying) {
			recItem.setEnabled(false);
			playItem.setIcon(R.drawable.btn_ic_video_record_stop);
		} else {
			recItem.setEnabled(true);
			if(mChannelViews.size() > 0) {
				playItem.setIcon(android.R.drawable.ic_media_play);
				playItem.setEnabled(true);
			} else {
				playItem.setEnabled(false);
			}
		}

		// Play state
		if(mIsRecording) {
			playItem.setEnabled(false);
			recItem.setIcon(R.drawable.btn_ic_video_record_stop);
		} else {
			playItem.setEnabled(true);
			recItem.setIcon(R.drawable.btn_ic_video_record);
		}

		// Repeat
		if(mIsRepeating) {
			repeatItem.setIcon(R.drawable.ic_mp_repeat_all_btn);
		} else {
			repeatItem.setIcon(R.drawable.ic_mp_repeat_off_btn);
		}


		return super.onPrepareOptionsMenu(menu);
	}

	//   -----------------------------------------------
	//	 SAVE / LOAD States
	//   -----------------------------------------------
	
	private void showLoadDialog() {
		Set<String> entries = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getStringSet(RECORDED_ENTRIES, null);
		final String[] entriesArray = Arrays.copyOf(entries.toArray(), entries.toArray().length, String[].class);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.pick_a_recording);
		builder.setItems(entriesArray, new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int item) {
		    	// Load the JSON
		    	new LoadRecordingAsyncTask(entriesArray[item]).execute();
		    }
		});
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	//   -----------------------------------------------
	
	private void showSaveDialog() {
		
		View saveDialogView = getLayoutInflater().inflate(R.layout.save_dialog, null);

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setView(saveDialogView);

		builder.setTitle(R.string.enter_in_a_name);
		
		SaveRecordingNameDialogListener dl = new SaveRecordingNameDialogListener(saveDialogView);
		
		builder.setPositiveButton(R.string.save,dl);
		builder.setNegativeButton(R.string.cancel,dl);
		
		AlertDialog alert = builder.create();
		alert.show();
		
	}
	
	//   -----------------------------------------------
	
	class SaveRecordingNameDialogListener implements OnClickListener {
		
		View dialogView = null;

		public SaveRecordingNameDialogListener(View dialogView) {
			this.dialogView = dialogView;
		}

		@Override
		public void onClick(DialogInterface dialog, int which) {
			if(which==DialogInterface.BUTTON_POSITIVE) {
				EditText nameEditText = (EditText) dialogView.findViewById(R.id.recording_name_edittext);
				String nameForRecording = nameEditText.getText().toString();
				
				if(nameForRecording.length() == 0) {
					Toast.makeText(getApplicationContext(), R.string.please_enter_in_a_name, Toast.LENGTH_SHORT).show();
				} else {
					Set<String> entries = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getStringSet(RECORDED_ENTRIES, null);
					if((null!=entries)&&(entries.contains(nameForRecording))) {
						// Let user know
					}

					new SaveRecordingAsyncTask(nameForRecording).execute();
				}
			} else if(which==DialogInterface.BUTTON_NEGATIVE) {
				dialog.dismiss();
			}
		}

		
		
	}
	
	//   -----------------------------------------------
	
	class SaveRecordingAsyncTask extends AsyncTask<Void, Void, Integer> {

		String recordingName;
		
		public SaveRecordingAsyncTask(String recordingName) {
			this.recordingName = recordingName;
		}
		
		@Override
		protected Integer doInBackground(Void... params) {
			
			int errorResId = 0;
			
			try {
				// Store in textual format
				SLRecording recording = new SLRecording();
				recording.setChannelsStates(mChannelsStates);
				recording.setTrackLocation(mLastSelectedItemPath);
				
				String recordingAsString = mapper.writeValueAsString(recording);
				
				SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
				Set<String> recordingNames = prefs.getStringSet(RECORDED_ENTRIES, new HashSet<String>());
				recordingNames.add(recordingName);
				
				Editor e = prefs.edit();
				e.putString(recordingName, recordingAsString);
				e.putStringSet(RECORDED_ENTRIES, recordingNames);
				e.commit();
			} catch (JsonGenerationException e) {
				errorResId = R.string.problem_saving;
				e.printStackTrace();
			} catch (JsonMappingException e) {
				errorResId = R.string.problem_saving;
				e.printStackTrace();
			} catch (IOException e) {
				errorResId = R.string.problem_saving;
				e.printStackTrace();
			}

			
			// Check for error
			if(0==errorResId)
				errorResId = R.string.recording_saved;
			
			return errorResId;
		}

		@Override
		protected void onPostExecute(Integer result) {
			if(null!=mProgressDialog)
				mProgressDialog.dismiss();
			
			Toast.makeText(getApplicationContext(),result, Toast.LENGTH_LONG).show();
			
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {
			mProgressDialog = ProgressDialog.show(SLMain.this, "", getString(R.string.saving_recording));
			super.onPreExecute();
		}
		
	}
	
	//   -----------------------------------------------
	
	class LoadRecordingAsyncTask extends AsyncTask<Void, Void, Integer> {

		String recordingName;
		
		public LoadRecordingAsyncTask(String recordingName) {
			this.recordingName = recordingName;
		}
		
		@Override
		protected Integer doInBackground(Void... params) {
			
			int errorResId = 0;
			
			// Read in textual format
			mChannelsStates = null;
			String recordingAsText = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString(recordingName, null);
			if(null==recordingAsText) {
				errorResId = R.string.recording_not_found;
			} else {
				try {
					SLRecording recording = mapper.readValue(recordingAsText, SLRecording.class);
					mChannelsStates = recording.getChannelsStates();
					mLastSelectedItemPath = recording.getTrackLocation();
				} catch (JsonParseException e) {
					errorResId = R.string.problem_loading_recording;
					e.printStackTrace();
				} catch (JsonMappingException e) {
					errorResId = R.string.problem_loading_recording;
					e.printStackTrace();
				} catch (IOException e) {
					errorResId = R.string.problem_loading_recording;
					e.printStackTrace();
				}
			}
			
			
			// Check for error
			if(0==errorResId)
				errorResId = R.string.recording_loaded;

			return errorResId;
		}

		@Override
		protected void onPostExecute(Integer result) {
			if(null!=mProgressDialog)
				mProgressDialog.dismiss();
			
			Toast.makeText(getApplicationContext(),result, Toast.LENGTH_LONG).show();

			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {
			mProgressDialog = ProgressDialog.show(SLMain.this, "", getString(R.string.loading_recording));
			super.onPreExecute();
		}
		
	}
	
	//   -----------------------------------------------

}