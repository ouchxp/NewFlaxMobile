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
package flax.baseview;

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
import flax.library.R;

/**
 * This class is the List Screen Activity. This screen displays a list of all
 * existing activities. It is from this screen that the user can select which
 * game they would like to play.
 * 
 * @author Nan Wu
 */
public abstract class BaseListScreenActivity extends ListActivity {
	protected static final String TAG = "ListScreen";
	private static final int INVALID_POS = -1;
	private int mCurrentPos = INVALID_POS;
	/** Ormlite database helper, use getDaoHelper method to get a instance */
	private DatabaseDaoHelper mDaoHelper = null;
	private Dao<Exercise, String> mExerciseDao = null;
	
	protected ExerciseListAdapter mAdapter;
	protected List<Exercise> mExercises;
	protected final ExerciseTypeEnum EXERCISE_TYPE = getExerciseType();

	/**
	 * Display the list of activities when the user moves to this screen.
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// TODO: Should Change to category list, and change ListView to ExpandableListView
		// Get a list of all activities in the internal database
		mExercises = getExercises();
		
		// Set the list adapter
		mAdapter = new ExerciseListAdapter(this, mExercises);
		setListAdapter(mAdapter);
		// Set the list screen title to be the activity type
		setTitle(EXERCISE_TYPE.getTitle());
	}

	/**
	 * Get list items for ListView to display.
	 */
	private List<Exercise> getExercises() {
		List<Exercise> items = null;
		try {
			items = getExerciseDao().queryForAll();
		} catch (SQLException e) {
			throw new RuntimeException(e);
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
		//Save position
		mCurrentPos = position;
		
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
		return getString(R.string.default_home_screen_help_message);
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
		int itemId = item.getItemId();
		if (itemId == R.id.help) {
			// Display Help Dialog
			DialogHelper help = new DialogHelper(this);
			help.displayHelpDialog(getHelpMessage());
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}
	
	/**
	 * Check exercise changes and update list view.
	 */
	@Override
	protected void onResume() {
		super.onResume();
		
		// Check whether exercise status changed.
		if(mCurrentPos != INVALID_POS){
			final Exercise currentExercise = mExercises.get(mCurrentPos);
			final int oldStatus = currentExercise.getStatus();
			
			// Delay for 500ms, cause GameScreen's onStop runs later than ListScreen's onResume
			getListView().postDelayed(new Runnable() {
				@Override
				public void run() {
					
					// refresh current entity
					try {
						getExerciseDao().refresh(currentExercise);
					} catch (SQLException e) {
						throw new RuntimeException(e);
					}
					
					// Get new status
					final int newStatus = currentExercise.getStatus();
					
					// If status changed then update list
					if(oldStatus != newStatus){
						mAdapter.notifyDataSetChanged();
					}
				}
			}, 500);
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
	 * Generate DatabaseDao
	 * @throws SQLException 
	 */
	protected Dao<Exercise, String> getExerciseDao() throws SQLException {
		if (mExerciseDao == null) {
			mExerciseDao = getDaoHelper().getDao(Exercise.class);
		}
		return mExerciseDao;
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