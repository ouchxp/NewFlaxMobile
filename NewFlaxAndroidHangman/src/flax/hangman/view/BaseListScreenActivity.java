/*
 * File: flax.collocationdominoes.view.ListScreen
 *
 * License: Greenstone 3 (GSDL3)
 * Copyright (C) 2003 New Zealand Digital Libraries, University Of Waikato
 * Greenstone3 comes with ABSOLUTELY NO WARRANTY; for details see LICENSE.txt
 * This is free software, and you are welcome to redistribute it
 * 
 * Reference: http://developer.android.com
 * 
 * Notes: This file is the List Screen Activity. This screen displays a list of all
 * existing activities. It is from this screen that the user can select which game they 
 * would like to play. 
 */
package flax.hangman.view;

import static flax.utils.GlobalConstants.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import flax.activity.ActivityCustomAdapter;
import flax.activity.ActivityItem;
import flax.activity.ExerciseTypeEnum;
import flax.collocation.CollocationDatabaseHelper;
import flax.collocation.CollocationDatabaseManager;
import flax.database.DatabaseDaoHelper;
import flax.dialog.DialogHelper;
import flax.entity.exerciselist.ExerciseListItem;
import flax.hangman.R;
import flax.hangman.game.GameItem;

/**
 * ListScreen Class
 * 
 * This class is the List Screen Activity. This screen displays a list of all
 * existing activities. It is from this screen that the user can select which
 * game they would like to play.
 * 
 * Note: Areas of code that need modifying are highlighted with a TODO tag.
 * 
 * @author Jemma Konig
 */
public abstract class BaseListScreenActivity extends ListActivity {

	/** Ormlite database helper, use getDBHelper method to get a instance */
	private DatabaseDaoHelper databaseHelper = null;
	protected Context context = this;
	// Declare class instances for list screen
	protected CollocationDatabaseManager dbManager;
	protected CollocationDatabaseHelper hangmanDb;

	// Declare variables for list screen
	protected ArrayList<ActivityItem> exerciseList = new ArrayList<ActivityItem>();
	protected List<ExerciseListItem> exercises;

	// Declare constants for list screen
	protected static final String TAG = "ListScreen";

	// Change to the type of the current activity.
	protected ExerciseTypeEnum EXERCISE_TYPE;

	protected abstract ExerciseTypeEnum getExerciseType();




	/**
	 * Display the list of activities when the user moves to this screen.
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		EXERCISE_TYPE = getExerciseType();

		// Get a list of all activities in the internal database
		dbManager = new CollocationDatabaseManager(context);
		exerciseList = dbManager.selectAllActivities(EXERCISE_TYPE.getName());
		try {
			Dao<ExerciseListItem, String> dao3 = getDBHelper().getDao(ExerciseListItem.class);
			exercises = dao3.queryForAll(); // TODO: Should Change to category
											// list
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// Set the list adapter
		setListAdapter(new ActivityCustomAdapter(this, exercises));

		// Set the list screen title to be the activity type
		setTitle(EXERCISE_TYPE.getTitle());
	}

	/*
	 * onResume method
	 * 
	 * The list needs to be updated each time the screen is resumed. ie. when
	 * moving back to the list screen from within a game.
	 * 
	 * @see android.app.Activity#onResume()
	 */
	// @Override
	// public void onResume() {
	//
	// // Call super onResume method
	// super.onResume();
	// //
	// // // Get a list of all activities in the internal database
	// // dbManager = new CollocationDatabaseManager(context);
	// // exerciseList = dbManager.selectAllActivities(ACTIVITY_TYPE);
	// //
	// // // Set the list adapter
	// // setListAdapter(new ActivityCustomAdapter(this, generateData()));
	// //
	// // // TODO Set the list screen title to be the activity type i.e.
	// // "Collocation Dominoes"
	// // setTitle("Activity Type");
	// }

	/**
	 * onListItemClick method When a list item is selected, create an intent
	 * (including added data 'uniqueId') and start the GameScreen activity using
	 * the intent.
	 * 
	 * @see android.app.ListActivity#onListItemClick(android.widget.ListView,
	 *      android.view.View, int, long)
	 */
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		GameItem.setPageNumber(0);

		// Create intent and pass 'uniqueId' through to the GameScreen
		
		//TODO: Change to pager when done refactoring
		//Intent i = new Intent(BaseListScreenActivity.this, GameScreen.class);
		Intent i = new Intent(BaseListScreenActivity.this, PagerGameScreenActivity.class);

		// Get selected item (old)
		String uid = String.valueOf(exerciseList.get(position).uniqueId);
		i.putExtra("uniqueId", uid);

		// TODO: New
		String exerciseId = exercises.get(position).getUrl();
		i.putExtra(EXERCISE_ID, exerciseId);

		startActivity(i);
	}

	/**
	 * onCreateOptionsMenu method Inflate the menu; this adds items to the
	 * action bar if it is present.
	 * 
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.list_screen, menu);
		return true;
	}

	/**
	 * onOptionsSelected Display help icon and show help dialog when the icon is
	 * selected
	 * 
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.help:
			// Display Help Dialog
			DialogHelper help = new DialogHelper(context);
			help.displayHelpDialog(getHelpMessage());
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	/**
	 * Get help message
	 * @return
	 */
	protected String getHelpMessage() {
		return getString(R.string.home_screen_help_message);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();

		// Release DatabaseDaoHelper
		if (databaseHelper != null) {
			OpenHelperManager.releaseHelper();
			databaseHelper = null;
		}
	}

	/**
	 * Generate DatabaseDaoHelper for database operation.
	 * 
	 * @return
	 */
	protected DatabaseDaoHelper getDBHelper() {
		if (databaseHelper == null) {
			databaseHelper = OpenHelperManager.getHelper(this, DatabaseDaoHelper.class);
		}
		return databaseHelper;
	}
}