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
import flax.activity.ExerciseTypeEnum;
import flax.asynctask.BackgroundDowloadExercises;
import flax.dialog.DialogChangeServer;
import flax.dialog.DialogHelper;
import flax.dialog.DialogNetworkSettings;
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

	protected static final String TAG = "HomeScreen";

	/**
	 * Exercise type enum, contains exercise name, data module, converters
	 * information
	 */
	protected ExerciseTypeEnum EXERCISE_TYPE;

	/**
	 * Displays the Home Screen when the app is loaded Overlays the Home Screen
	 * with the new activities dialog box
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_screen);

		EXERCISE_TYPE = getExerciseType();

		// TODO: On first time - create activities
		// insertStartingActivities();

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
	protected abstract ExerciseTypeEnum getExerciseType();

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
			DialogChangeServer serverDialog = new DialogChangeServer(this);
			serverDialog.loadServerPath();
			serverDialog.displayChangeServerDialog();
			return true;
		} else if (itemId == R.id.network_settings) {
			// Set network settings
			DialogNetworkSettings networkSettings = new DialogNetworkSettings(this);
			networkSettings.loadNetworkSettings();
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

	//TODO: Sould add insertStartingActivities function
	/**
	 * insertStartingActivities method
	 * 
	 * This method inserts ten activities into the database when the app is
	 * opened for the first time. For the collocation xml see res / raw /
	 * game_name.xml Ten activities will need to be added.
	 */
	/*
	 *  insertStartingActivities function should add back after refactoring
	 * public void insertStartingActivities() {
	 * 
	 * CollocationNetworkXmlParser colloParser = new
	 * CollocationNetworkXmlParser(); // Check is first
	 * time execution, and set first time flag automatically. if
	 * (FlaxUtil.isFirstTime()) { try { // Add activities to the database
	 * dbManager.addActivity("1a", "1", LocalConstants.ACTIVITY_TYPE, "hangman",
	 * "none", "new", 0, 0);
	 * 
	 * // Retrieve the input stream based on the xml files saved in res // / raw
	 * / game_name.xml List<ActivityItem> firstActivities =
	 * dbManager.selectAllActivities(LocalConstants.ACTIVITY_TYPE);
	 * ArrayList<InputStream> isArr = new ArrayList<InputStream>(); InputStream
	 * is0 = this.getResources().openRawResource(R.raw.activity_template_xml);
	 * 
	 * // Put input streams in an array for more efficient access // (enables
	 * use of a for loop) isArr.add(is0);
	 * 
	 * // Add collocations to the database int i = firstActivities.size() - 1;
	 * for (ActivityItem a : firstActivities) {
	 * 
	 * int j = 0; List<CollocationItem> colloList = new
	 * ArrayList<CollocationItem>(); colloList =
	 * colloParser.parse(isArr.get(i)); for (CollocationItem c : colloList) {
	 * 
	 * dbManager.addCollocation(c.collocationId, j, c.type, c.fre, c.sendId,
	 * c.word, "none", "none", "none", c.baseWord, a.uniqueId); j++; }
	 * 
	 * // Add initial entry to summary report table in db
	 * dbManager.addSummary("", "", 0, 0, a.uniqueId); i--; colloList.clear(); }
	 * 
	 * } catch (XmlPullParserException e) { e.printStackTrace(); } catch
	 * (IOException e) { e.printStackTrace(); } } }
	 */

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
			backgoundDownload.execute(getUrls());
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