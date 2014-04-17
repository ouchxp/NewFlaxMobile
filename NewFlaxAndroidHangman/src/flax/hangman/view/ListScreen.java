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

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import flax.activity.ActivityCustomAdapter;
import flax.activity.ActivityItem;
import flax.activity.Item;
import flax.collocation.CollocationDatabaseHelper;
import flax.collocation.CollocationDatabaseManager;
import flax.database.DatabaseDaoHelper;
import flax.dialog.DialogHelp;
import flax.entity.exercise.Exercise;
import flax.entity.hangman.HangmanResponse;
import flax.hangman.R;
import flax.hangman.game.GameItem;

/**
 * ListScreen Class
 * 
 * This class is the List Screen Activity. This screen displays a list of all
 * existing activities. It is from this screen that the user can select which game 
 * they would like to play.
 * 
 * Note: Areas of code that need modifying are highlighted with a TODO tag.
 * 
 * @author Jemma Konig
 */
public class ListScreen extends ListActivity {
	
	/** Ormlite database helper, use getDBHelper method to get a instance */
	private DatabaseDaoHelper databaseHelper = null;
	
	// Declare class instances for list screen
	protected ListView listview;
	protected ActivityItem currentExercise;
	protected CollocationDatabaseManager dbManager;
	protected CollocationDatabaseHelper hangmanDb;
	
	// Declare variables for list screen
	protected String uid;
	protected Context context 							= this;
	protected ArrayList<Item> items;
	protected ArrayList<ActivityItem> exerciseList 		= new ArrayList<ActivityItem>();
	private List<Exercise> exercises;

	// Declare constants for list screen
	protected static final String TAG 					= "logging process";
	protected static final String ACT_STATUS_REMOVED	= "removed";
	
	// TODO Change to the name of the current activity. Should match the activity specified in the xml retrieved from the server.
	protected static final String ACTIVITY_TYPE			= "Hangman";													
	
	// TODO Change wording to more specifically target each activity. 
	protected static final String HELP_MESSAGE			= "Click on one of the games in the list to begin playing the activity"		
															+ "\n \n" 
															+ "If no games appear in the list, return to the home screen " 
															+ "and in the options menu, select 'Check for New Exercises'"
															+ "\n \n" 
															+ "Good Luck! \n";

	/*
	 * onCreate method
	 * 
	 * Display the list of activities when the user moves
	 * to this screen.
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Get a list of all activities in the internal database
		dbManager = new CollocationDatabaseManager(context);
		exerciseList = dbManager.selectAllActivities(ACTIVITY_TYPE);
		try {
			Dao<Exercise,String> dao3 = getDBHelper().getDao(Exercise.class);
			exercises = dao3.queryForAll(); // TODO: Should Change to category list 
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// Set the list adapter 
        setListAdapter(new ActivityCustomAdapter(this, exercises));
        
        // TODO Set the list screen title to be the activity type i.e. "Collocation Hangman"
    	setTitle("Activity Type");																										
    }
	
	/*
	 * onResume method
	 * 
	 * The list needs to be updated each time the 
	 * screen is resumed. ie. when moving back to
	 * the list screen from within a game.
	 * 
	 * @see android.app.Activity#onResume()
	 */
	@Override
	public void onResume(){
		
		// Call super onResume method
		super.onResume();
//		
//        // Get a list of all activities in the internal database
//		dbManager = new CollocationDatabaseManager(context);
//		exerciseList = dbManager.selectAllActivities(ACTIVITY_TYPE);
//        
//		// Set the list adapter 
//        setListAdapter(new ActivityCustomAdapter(this, generateData()));
//        
//        // TODO Set the list screen title to be the activity type i.e. "Collocation Dominoes"
//    	setTitle("Activity Type");																										
	}
	
	/*
	 * onListItemClick method
	 * 
	 * When a list item is selected, create an intent (including added data 'uniqueId')
	 * and start the GameScreen activity using the intent. 
	 * 
	 * @see android.app.ListActivity#onListItemClick(android.widget.ListView, android.view.View, int, long)
	 */
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		GameItem.setPageNumber(0);
		// Get selected item
		uid = String.valueOf(exerciseList.get(position).uniqueId);
		// TODO: Change to exercise.get(position).getUrl();
		String exerciseId = exercises.get(position).getUrl();
		
        // Create intent and pass 'uniqueId' through to the GameScreen
		Intent i = new Intent(ListScreen.this, GameScreen.class);
		i.putExtra("uniqueId", uid);
		i.putExtra("exerciseId", exerciseId);
		startActivity(i);
    }
 
	
	/*
	 * onCreateOptionsMenu method 
	 * 
	 * Inflate the menu; this adds items to the action bar if it is present.
	 * 
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.list_screen, menu);
		return true;
	}
	
	/*
	 * onOptionsSelected
	 * 
	 * Display help icon and show help dialog when the icon is selected
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {   	
    	switch (item.getItemId()) {
            case R.id.help:
            	// Display Help Dialog
            	DialogHelp help = new DialogHelp(context);
            	help.displayHelpDialog(HELP_MESSAGE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
     * @return
     */
    private DatabaseDaoHelper getDBHelper() {
        if (databaseHelper == null) {
            databaseHelper =
                OpenHelperManager.getHelper(this, DatabaseDaoHelper.class);
        }
        return databaseHelper;
    }
}