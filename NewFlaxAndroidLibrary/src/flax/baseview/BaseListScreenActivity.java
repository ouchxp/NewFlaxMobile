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
import java.util.ArrayList;
import java.util.List;

import android.app.ExpandableListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import flax.activity.ExerciseType;
import flax.database.DatabaseDaoHelper;
import flax.database.FlaxDao;
import flax.dialog.DialogHelper;
import flax.entity.exerciselist.Category;
import flax.entity.exerciselist.Exercise;
import flax.library.R;

/**
 * This class is the List Screen Activity. This screen displays a list of all
 * existing activities. It is from this screen that the user can select which
 * game they would like to play.
 * 
 * @author Nan Wu
 */
public abstract class BaseListScreenActivity extends ExpandableListActivity {
	protected static final String TAG = "ListScreen";
	private static final int INVALID_POS = -1;

	private int mCurrentGroup = INVALID_POS;
	private int mCurrentPos = INVALID_POS;

	/** Ormlite database helper, use getDaoHelper method to get a instance */
	private DatabaseDaoHelper mDaoHelper = null;
	private FlaxDao<Exercise, String> mExerciseDao = null;
	private FlaxDao<Category, String> mCategoryDao = null;

	protected ExpandableListView mListView;
	protected ExpandableExerciseListAdapter mAdapter;
	protected List<Category> mCategories;
	protected List<List<Exercise>> mExerciseGroups;
	protected final ExerciseType EXERCISE_TYPE = getExerciseType();

	/**
	 * Display the list of activities when the user moves to this screen.
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Get a list of all categories in the internal database
		mCategories = getCategoryDao().queryForAll();

		// Set up the list adapter
		setUpAdapter();

		// Set the list view
		mListView = getExpandableListView();

		// Expand all groups
		for (int i = 0; i < mCategories.size(); i++) {
			mListView.expandGroup(i);
		}

		// Set the list screen title to be the activity type
		setTitle(EXERCISE_TYPE.getTitle());
	}

	/**
	 * Set up adapter for expandable list view
	 */
	private void setUpAdapter() {
		mExerciseGroups = new ArrayList<List<Exercise>>();
		for (Category category : mCategories) {
			List<Exercise> group = new ArrayList<Exercise>(category.getExercises());
			mExerciseGroups.add(group);
		}
		mAdapter = new ExpandableExerciseListAdapter(this, mCategories, mExerciseGroups);
		setListAdapter(mAdapter);
	}

	/**
	 * onListItemClick method When a list item is selected, create an intent
	 * (including added data 'uniqueId') and start the GameScreen activity using
	 * the intent.
	 */
	public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
		// Save positions
		mCurrentGroup = groupPosition;
		mCurrentPos = childPosition;

		Intent i = new Intent(BaseListScreenActivity.this, getNextActivityClass());
		// Pass exercise id
		String exerciseId = mExerciseGroups.get(groupPosition).get(childPosition).getUrl();
		i.putExtra(EXERCISE_ID, exerciseId);
		startActivity(i);
		return true;
	}

	/**
	 * Return the class of next activity for building Intent.
	 */
	public abstract Class<?> getNextActivityClass();

	/**
	 * Get exercise type for initialize activity
	 */
	public abstract ExerciseType getExerciseType();

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
		if (mCurrentGroup != INVALID_POS && mCurrentPos != INVALID_POS) {
			final Exercise currentExercise = mExerciseGroups.get(mCurrentGroup).get(mCurrentPos);
			final int oldStatus = currentExercise.getStatus();

			// Delay for 500ms, cause GameScreen's onStop runs later than
			// ListScreen's onResume
			mListView.postDelayed(new Runnable() {
				@Override
				public void run() {
					// refresh current entity
					getExerciseDao().refresh(currentExercise);

					// Get new status
					final int newStatus = currentExercise.getStatus();

					// If status changed then update list
					if (oldStatus != newStatus) {
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
	 * Generate Dao
	 */
	protected FlaxDao<Category, String> getCategoryDao() {
		if (mCategoryDao == null) {
			Dao<Category, String> dao;
			try {
				dao = getDaoHelper().getDao(Category.class);
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
			mCategoryDao = new FlaxDao<Category, String>(dao);
		}
		return mCategoryDao;
	}

	/**
	 * Generate Dao
	 */
	protected FlaxDao<Exercise, String> getExerciseDao() {
		if (mExerciseDao == null) {
			Dao<Exercise, String> dao;
			try {
				dao = getDaoHelper().getDao(Exercise.class);
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
			mExerciseDao = new FlaxDao<Exercise, String>(dao);
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