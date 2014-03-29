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

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import flax.activity.ActivityCustomAdapter;
import flax.activity.ActivityItem;
import flax.activity.Item;
import flax.collocation.CollocationDatabaseHelper;
import flax.collocation.CollocationDatabaseManager;
import flax.dialog.DialogHelp;
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
	
	// Declare class instances for list screen
	protected ListView listview;
	protected ActivityItem currentExercise;
	protected CollocationDatabaseManager dbManager;
	protected CollocationDatabaseHelper hangmanDb;
	
	// Declare variables for list screen
	protected int uid;
	protected Context context 							= this;
	protected ArrayList<Item> items;
	protected ArrayList<ActivityItem> exerciseList 		= new ArrayList<ActivityItem>();

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
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        
        // Get a list of all activities in the internal database
		dbManager = new CollocationDatabaseManager(context);
		exerciseList = dbManager.selectAllActivities(ACTIVITY_TYPE);
        
		// Set the list adapter 
        setListAdapter(new ActivityCustomAdapter(this, generateData()));
        
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
		
        // Get a list of all activities in the internal database
		dbManager = new CollocationDatabaseManager(context);
		exerciseList = dbManager.selectAllActivities(ACTIVITY_TYPE);
        
		// Set the list adapter 
        setListAdapter(new ActivityCustomAdapter(this, generateData()));
        
        // TODO Set the list screen title to be the activity type i.e. "Collocation Dominoes"
    	setTitle("Activity Type");																										
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
        for(ActivityItem i : exerciseList){
        	if(i.index == position){
        		Log.d(TAG, "e index: " + i.index + " position: " + position);
        		uid = i.uniqueId;
        		break;
        	}
        }
        
        // Create intent and pass 'uniqueId' through to the GameScreen
		Intent i = new Intent(ListScreen.this, GameScreen.class);
		i.putExtra("uniqueId", uid);
		startActivity(i);
    }
 
	/*
	 * generateData method
	 * 
	 * Go through the existing activities, saving all
	 * new, incomplete and complete activities in an Item list.
	 * If the activity status is 'removed' (i.e. no longer a valid activity) 
	 * it will not be saved into the Item list.
	 */
	public ArrayList<Item> generateData(){
		
		// Need activities to be saved as Item objects to display in list view
        items = new ArrayList<Item>();
		int i = 0;
		for(ActivityItem e : exerciseList){
			if(!e.getActivityStatus().equals(ACT_STATUS_REMOVED)){
				e.setActivityIndex(i);
		        items.add(new Item(e.activityName.toString(), e.activityStatus.toString()));
				i++;
			}
		}
		return items;
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
}