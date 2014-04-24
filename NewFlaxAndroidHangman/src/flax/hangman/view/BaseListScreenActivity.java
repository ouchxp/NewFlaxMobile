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
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import flax.activity.ExerciseListAdapter;
import flax.activity.ExerciseTypeEnum;
import flax.database.DatabaseDaoHelper;
import flax.dialog.DialogHelper;
import flax.entity.exerciselist.Exercise;
import flax.hangman.R;

/**
 * This class is the List Screen Activity. This screen displays a list of all
 * existing activities. It is from this screen that the user can select which
 * game they would like to play.
 * 
 * @author Nan Wu
 */
public abstract class BaseListScreenActivity extends ListActivity {

	protected static final String TAG = "ListScreen";
	
	/** Ormlite database helper, use getDBHelper method to get a instance */
	private DatabaseDaoHelper mDaoHelper = null;
	
	protected List<Exercise> mExercises;
	protected ExerciseTypeEnum EXERCISE_TYPE;

	/**
	 * Display the list of activities when the user moves to this screen.
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		EXERCISE_TYPE = getExerciseType();
		
		// TODO: Should Change to category list, and change ListView to ExpandableListView
		// Get a list of all activities in the internal database
		mExercises = getExercises();
		
		// Set the list adapter
		setListAdapter(new ExerciseListAdapter(this, mExercises));

		// Set the list screen title to be the activity type
		setTitle(EXERCISE_TYPE.getTitle());
	}

	/**
	 * Get list items for ListView to display.
	 */
	private List<Exercise> getExercises() {
		List<Exercise> items = null;
		try {
			Dao<Exercise, String> exerciseDao = getDaoHelper().getDao(Exercise.class);
			items = exerciseDao.queryForAll();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return items;
	}

	/**
	 * onListItemClick method When a list item is selected, create an intent
	 * (including added data 'uniqueId') and start the GameScreen activity using
	 * the intent.
	 */
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Intent i = new Intent(BaseListScreenActivity.this, getNextActivityClass());
		// Pass exercise id
		String exerciseId = mExercises.get(position).getUrl();
		i.putExtra(EXERCISE_ID, exerciseId);
		startActivity(i);
	}
	
	/**
	 * Return the class of next activity for building Intent.
	 */
	public abstract Class<?> getNextActivityClass();

	/**
	 * Get exercise type for initialize activity
	 */
	public abstract ExerciseTypeEnum getExerciseType();
	
	/**
	 * Get help message
	 */
	protected String getHelpMessage() {
		return getString(R.string.home_screen_help_message);
	}
	
	/**
	 * onCreateOptionsMenu method Inflate the menu; this adds items to the
	 * action bar if it is present.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.list_screen, menu);
		return true;
	}

	/**
	 * onOptionsSelected Display help icon and show help dialog when the icon is
	 * selected
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.help:
			// Display Help Dialog
			DialogHelper help = new DialogHelper(this);
			help.displayHelpDialog(getHelpMessage());
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();

		// Release DatabaseDaoHelper
		if (mDaoHelper != null) {
			OpenHelperManager.releaseHelper();
			mDaoHelper = null;
		}
	}

	/**
	 * Generate DatabaseDaoHelper for database operation.
	 */
	protected DatabaseDaoHelper getDaoHelper() {
		if (mDaoHelper == null) {
			mDaoHelper = OpenHelperManager.getHelper(this, DatabaseDaoHelper.class);
		}
		return mDaoHelper;
	}
}