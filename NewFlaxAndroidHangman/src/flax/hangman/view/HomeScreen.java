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
package flax.hangman.view;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import flax.activity.ActivityItem;
import flax.asynctask.BackgroundDowloadExercises;
import flax.collocation.CollocationDatabaseHelper;
import flax.collocation.CollocationDatabaseManager;
import flax.collocation.CollocationItem;
import flax.collocation.CollocationNetworkXmlParser;
import flax.dialog.DialogChangeServer;
import flax.dialog.DialogHelp;
import flax.dialog.DialogNetworkSettings;
import flax.hangman.R;
import flax.hangman.utils.Constants;
import flax.hangman.utils.HangmanURLConverter;
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
 * @author Jemma Konig
 */
public class HomeScreen extends Activity {

	// Declare constants and static variables for home screen
	protected final Context context = this;
	protected CollocationDatabaseManager dbManager;
	protected static final String TAG = "logging process";

	

	/*
	 * onCreate method
	 * 
	 * Displays the Home Screen when the app is loaded Overlays the Home Screen
	 * with the new activities dialog box
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_screen);
		
		// On first time - create db
		initialiseDatabase();

		// On first time - create activities
		insertStartingActivities();

		// Show download Dialog
		showDownloadDialog();

		// Make sure there is no text in the action bar on the home screen
		setTitle("");
		
		//TODO: Mocking to be removed
		Mock.r=this.getResources();
	}

	/*
	 * onCreateOptionsMenu method
	 * 
	 * Inflate the menu; this adds items to the action bar if it is present.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.home_screen, menu);
		return true;
	}

	/*
	 * onOptionsSelected
	 * 
	 * Display server, network or download dialog depending on which menu item
	 * has been selected
	 * 
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		// Help button is presses
		case R.id.help_home:
			DialogHelp help = new DialogHelp(context);
			//Change words to more specifically for each activity in strings.xml.
			help.displayHelpDialog(context.getString(R.string.help_message));
			return true;

			// Menu item -- change server is pressed
		case R.id.change_server:

			// Set server settings
			DialogChangeServer serverDialog = new DialogChangeServer(context);
			serverDialog.loadServerPath();
			serverDialog.displayChangeServerDialog();
			return true;

			// Menu item -- change network settings is pressed
		case R.id.network_settings:

			// Set network settings
			DialogNetworkSettings networkSettings = new DialogNetworkSettings(context);
			networkSettings.loadNetworkSettings();
			networkSettings.displayNetworkDialog();
			return true;

			// Menu item -- download exercises is pressed
		case R.id.download_exercises:
			Log.d(TAG, "call showDownloadDialog()");
			showDownloadDialog();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/*
	 * onPause method
	 */
	@Override
	public void onPause() {
		super.onPause();
		//TODO: save bundle data here
	}

	/*
	 * play method
	 * Opens the ListScreen Activity when the play button on the Home Screen is
	 * pressed.
	 */
	public void play(View view) {
		Intent i = new Intent(HomeScreen.this, ListScreen.class);
		startActivity(i);
	}

	/*
	 * initialiseDatabase method
	 * The first time the app is opened, create the internal database
	 */
	public void initialiseDatabase() {
		CollocationDatabaseHelper dbHelper = new CollocationDatabaseHelper(context);
		dbManager = new CollocationDatabaseManager(context);
		dbHelper.closeDatabase();
	}

	/*
	 * insertStartingActivities method
	 * 
	 * This method inserts ten activities into the database when the app is
	 * opened for the first time. For the collocation xml see res / raw /
	 * game_name.xml Ten activities will need to be added.
	 */
	public void insertStartingActivities() {

		CollocationNetworkXmlParser colloParser = new CollocationNetworkXmlParser();

		// Check is first time execution, and set first time flag automatically.
		if (FlaxUtil.isFirstTime()) {
			try {
				// Add activities to the database
				dbManager.addActivity("1a", "1", Constants.ACTIVITY_TYPE, "hangman", "none", "new", 0, 0);

				// Retrieve the input stream based on the xml files saved in res
				// / raw / game_name.xml
				List<ActivityItem> firstActivities = dbManager.selectAllActivities(Constants.ACTIVITY_TYPE);
				ArrayList<InputStream> isArr = new ArrayList<InputStream>();
				InputStream is0 = this.getResources().openRawResource(R.raw.activity_template_xml);

				// Put input streams in an array for more efficient access
				// (enables use of a for loop)
				isArr.add(is0);

				// Add collocations to the database
				int i = firstActivities.size() - 1;
				for (ActivityItem a : firstActivities) {

					int j = 0;
					List<CollocationItem> colloList = new ArrayList<CollocationItem>();
					colloList = colloParser.parse(isArr.get(i));
					for (CollocationItem c : colloList) {

						dbManager.addCollocation(c.collocationId, j, c.type, c.fre, c.sendId, c.word, "none", "none",
								"none", c.baseWord, a.uniqueId);
						j++;
					}

					// Add initial entry to summary report table in db
					dbManager.addSummary("", "", 0, 0, a.uniqueId);
					i--;
					colloList.clear();
				}

			} catch (XmlPullParserException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}


	/**
	 * showDownloadDialog method
	 * 
	 * Creates the download dialog box. If "Yes" is pressed, calls
	 * startingDownload()
	 */
	public void showDownloadDialog() {
		Log.d(TAG, "download dialog being created ...");

		// Create dialog
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
		alertDialogBuilder

		// set title
				.setTitle("Check for new exercises?")

				// set dialog content
				.setMessage("Would you like to connect to the server and download new exercises?").setCancelable(false)

				// set "Yes" button
				.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// if "yes" is clicked, search new exercises
						dialog.cancel();
						startingDownload();
					}
				})

				// set "No" button
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// if "no" is clicked, close dialog
						dialog.cancel();
					}
				});

		Log.d(TAG, "display download dialog");
		alertDialogBuilder.create().show();
	}

	/**
	 * startingDownload method
	 * Starting to download all exercises in the specific activity.
	 */
	private void startingDownload() {
		// if connected
		if (FlaxUtil.isConnected()) {
			Log.d(TAG, "connected. Start downloading");
			String url = FlaxUtil.getServerPath() + Constants.HANGMAN_URL;
			BackgroundDowloadExercises backgoundDownload = new BackgroundDowloadExercises(context,new HangmanURLConverter());
			//TODO: add bean converter
			backgoundDownload.execute(url);
		} else {
			Log.d(TAG, "Internet not Connected");
			Toast.makeText(
					context,
					"There was a problem connecting to the server. "
							+ "Please ensure that you are connected to the internet "
							+ "and that your Network Settings are set correctly", Toast.LENGTH_LONG).show();
		}
	}

} // end of HomeScreen class