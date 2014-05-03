/*
 * File: flax.collocationdominoes.view.HomeScreen
 *
 * License: Greenstone 3 (GSDL3)
 * Copyright (C) 2003 New Zealand Digital Libraries, University Of Waikato
 * Greenstone3 comes with ABSOLUTELY NO WARRANTY; for details see LICENSE.txt
 * This is free software, and you are welcome to redistribute it
 * 
 * Reference: http://developer.android.com
 * 
 * Note: This file is the Home Screen Activity. It is from this screen that the 
 * user has the option to change the server path, change network settings, download 
 * new activities or move to the list view of activities to play.
 */
package flax.baseview;

import static flax.utils.GlobalConstants.*;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import flax.asynctask.BackgroundDowloadExercises;
import flax.asynctask.DefaultExercisesLoader;
import flax.core.ExerciseType;
import flax.dialog.DialogHelper;
import flax.library.R;
import flax.utils.FlaxUtil;
import flax.utils.Mock;

/**
 * HomeScreen Class
 * 
 * This class is the Home Screen Activity. It is from this screen that the user
 * has the option to change the server path, change network settings, download
 * new activities or move to the list view of activities to play.
 * 
 * Note: The package name needs to be changed to suit the current activity. To
 * change the package name right click on the package -> android tools -> rename
 * application package.
 * 
 * Note: 'app_name', 'app_name_launcher' and 'main_activity_text' need to be
 * changed to match the current activity. They are found in res -> values ->
 * strings.xml
 * 
 * Note: Areas of code that need modifying are highlighted with a TODO tag.
 * 
 * Note: If not working with collocations, a new ActivityProcess class will need
 * to be created to handle the downloading of additional data needing to be
 * retrieved from the server. This will also result in the need for additional
 * database tables and select statements. See library project for existing
 * classes to use as a base.
 * 
 * @author Nan Wu
 */
public abstract class BaseHomeScreenActivity extends Activity {

	public static final String TAG = "HomeScreen";
	private final Executor SINGLE_THREAD_EXECUTOR = Executors.newSingleThreadExecutor();

	/**
	 * Exercise type enum, contains exercise name, data module, converters
	 * information
	 */
	protected final ExerciseType EXERCISE_TYPE = getExerciseType();

	/**
	 * Displays the Home Screen when the app is loaded Overlays the Home Screen
	 * with the new activities dialog box
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_screen);

		// On first time, load and save default exercises from
		// assets folder
		loadDefaultExercises();
		
		

		// Show download Dialog
		showDownloadDialog();

		// Make sure there is no text in the action bar on the home screen
		setTitle("");
		
		// Represent the exercise name on home screen, which in large font.
		TextView execiseName = (TextView)findViewById(R.id.text_view_home_game_title);
		execiseName.setText(EXERCISE_TYPE.getName());

		// TODO: Mocking to be removed
		Mock.r = this.getResources();
	}

	/**
	 * Get exercise type information for different exercise activity
	 */
	protected abstract ExerciseType getExerciseType();

	/**
	 * Inflate the menu; this adds items to the action bar if it is present.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.home_screen, menu);
		return true;
	}

	/**
	 * Display server, network or download dialog depending on which menu item
	 * has been selected
	 * @see http://tools.android.com/tips/non-constant-fields
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		if (itemId == R.id.help_home) {
			DialogHelper help = new DialogHelper(this);
			// Change words to more specifically for each activity in
			// strings.xml.
			help.displayHelpDialog(getHelpMessage());
			return true;
		} else if (itemId == R.id.change_server) {
			// Set server settings
			DialogHelper serverDialog = new DialogHelper(this);
			serverDialog.displayChangeServerDialog();
			return true;
		} else if (itemId == R.id.network_settings) {
			// Set network settings
			DialogHelper networkSettings = new DialogHelper(this);
			networkSettings.displayNetworkDialog();
			return true;
		} else if (itemId == R.id.download_exercises) {
			Log.d(TAG, "call showDownloadDialog()");
			showDownloadDialog();
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}
	
	/**
	 * showDownloadDialog method
	 * 
	 * Creates the download dialog box. If "Yes" is pressed, calls
	 * startingDownload()
	 */
	protected void showDownloadDialog() {
		DialogHelper de = new DialogHelper(this);
		de.displayDownloadDialog(new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				startingDownload();
			}
		});
	}

	/**
	 * Get help message, for building help dialog.
	 */
	public String getHelpMessage() {
		return getString(R.string.default_home_screen_help_message);
	}

	/**
	 * play method Opens the ListScreen Activity when the play button on the
	 * Home Screen is pressed.
	 */
	public void play(View view) {
		Intent i = new Intent(BaseHomeScreenActivity.this, getNextActivityClass());
		startActivity(i);
	}

	/**
	 * Return the class of next activity for building Intent.
	 */
	public abstract Class<?> getNextActivityClass();

	/**
	 * This method load default exercises into the database when the app is
	 * opened for the first time. 
	 */
	public void loadDefaultExercises(){
		DefaultExercisesLoader loader = new DefaultExercisesLoader(this, EXERCISE_TYPE);
		// Run on SINGLE_THREAD_EXECUTOR then won't conflict with downloading process
		loader.executeOnExecutor(SINGLE_THREAD_EXECUTOR, DEFAULT_EXERCISE_LIST_FILE);
	}

	/**
	 * Return URLs should be download by this APP;
	 */
	public abstract String[] getUrls();

	
	/**
	 * Download all exercises in given URLs.
	 */
	private void startingDownload() {
		// if connected
		if (FlaxUtil.isConnected()) {
			Log.d(TAG, "connected. Start downloading");
			BackgroundDowloadExercises backgoundDownload = new BackgroundDowloadExercises(this, EXERCISE_TYPE);
			// Run on SINGLE_THREAD_EXECUTOR then won't conflict with loading default exercise process
			backgoundDownload.executeOnExecutor(SINGLE_THREAD_EXECUTOR, getUrls());
		} else {
			Log.d(TAG, "Internet not Connected");
			Toast.makeText(
					this,
					"There was a problem connecting to the server. "
							+ "Please ensure that you are connected to the internet "
							+ "and that your Network Settings are set correctly", Toast.LENGTH_LONG).show();
		}
	}
}