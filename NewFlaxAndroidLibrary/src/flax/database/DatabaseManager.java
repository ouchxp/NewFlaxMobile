/*
 * File: flax.database.DatabaseManager
 *
 * License: Greenstone 3 (GSDL3)
 * Copyright (C) 2003 New Zealand Digital Libraries, University Of Waikato
 * Greenstone3 comes with ABSOLUTELY NO WARRANTY; for details see LICENSE.txt
 * This is free software, and you are welcome to redistribute it
 * 
 * Reference: http://developer.android.com
 * 
 * Notes: This file is used to manage any SQL queries relating to the activity
 * table stored in the internal database. For each custom application, extend 
 * DatabaseManager and add any SQL queries needed that relate to the specific 
 * elements (ie. collocations)
 */
package flax.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import flax.activity.ActivityItem;
import flax.collocation.CollocationDatabaseHelper;
import flax.database.DatabaseContract.FeedActivity;
import flax.database.DatabaseContract.FeedSummary;

import java.util.ArrayList;
import java.util.List;

/**
 * DatabaseManager class
 * 
 * This class is used to manage any SQL queries relating to the activity
 * table stored in the internal database.
 * 
 * @author Jemma Konig
 */
public class DatabaseManager {
	
	// Declare context for db manager
	protected final Context context;
	
	/* DatabaseManager class constructor
	 */
	public DatabaseManager(Context c){
		context = c;
	}
	
	/* 
	 * addActivity method
	 * 
	 * adds an activity to the db 
	 * @param activityID, categoryId, type, name, url, status, order, word count.
	 */
	public long addActivity(String activityId, String categoryId, 
			String type, String name, String url, String status, int order, int count){
		
		// Reference database
		DatabaseHelper flaxDb = new DatabaseHelper(context);
		SQLiteDatabase db = flaxDb.getWritableDatabase();
		
		// Map values for new row in db
		ContentValues values = new ContentValues();
		values.put(FeedActivity.COLUMN_NAME_ACTIVITY_ID, activityId);
		values.put(FeedActivity.COLUMN_NAME_ACT_CATEGORY_ID, categoryId);
		values.put(FeedActivity.COLUMN_NAME_ACTIVITY_TYPE, type);
		values.put(FeedActivity.COLUMN_NAME_ACTIVITY_NAME, name);
		values.put(FeedActivity.COLUMN_NAME_ACTIVITY_URL, url);
		values.put(FeedActivity.COLUMN_NAME_ACTIVITY_STATUS, status);
		values.put(FeedActivity.COLUMN_NAME_ACTIVITY_ORDER, order);
		values.put(FeedActivity.COLUMN_NAME_ACTIVITY_WORD_COUNT, count);

		// insert new row in db
		long rowId = db.insert(FeedActivity.TABLE_NAME, null, values);	
		flaxDb.closeDatabase();
		return rowId;
	}
	
	/*
	 * updateActivity Method
	 * 
	 * updates a row in the activity table
	 * @param status and activity id
	 */
	public void updateActivity(String status, String aid){
		
		// Reference database
		CollocationDatabaseHelper flaxDb = new CollocationDatabaseHelper(context);
		SQLiteDatabase db = flaxDb.getWritableDatabase();
		
		// Set 'order' to be one greater than the number of rows in the table
		long tmp = DatabaseUtils.queryNumEntries(db, FeedActivity.TABLE_NAME);
		int order = (int) tmp++;
		
		// Selection statement (where unique id = given id)
		String selection = FeedActivity._ID + "='" + aid + "'";
		
		// Map values for updated row in db
		ContentValues values = new ContentValues();
		values.put(FeedActivity.COLUMN_NAME_ACTIVITY_STATUS, status);
		values.put(FeedActivity.COLUMN_NAME_ACTIVITY_ORDER, order);
		
		// update row in db
		db.update(FeedActivity.TABLE_NAME, values, selection, null);
		flaxDb.closeDatabase();		
	}
	
	/*
	 * updateActivityWordCount Method
	 * 
	 * updates a number od words in an activity
	 * @param count and activity id
	 */
	public void updateActivityWordCount(int count, int aid){
		
		// Reference database
		CollocationDatabaseHelper flaxDb = new CollocationDatabaseHelper(context);
		SQLiteDatabase db = flaxDb.getWritableDatabase();
		
		// Selection statement (where unique id = given id)
		String selection = FeedActivity._ID + "='" + aid + "'";
		
		// Map values for updated row in db
		ContentValues values = new ContentValues();
		values.put(FeedActivity.COLUMN_NAME_ACTIVITY_WORD_COUNT, count);
		
		// update row in db
		db.update(FeedActivity.TABLE_NAME, values, selection, null);
		flaxDb.closeDatabase();		
	}
	
	/*
	 * selectAllActivities method
	 * 
	 * Selects all activities from the db with the
	 * specifies type value ie type = CollocationDominoes
	 * 
	 * @param type, Activity type. ie CollocationDominoes
	 * @return ArrayList of exercises 
	 */
	public ArrayList<ActivityItem> selectAllActivities(String type){

		// Declare arrayList to hold activities
		ArrayList<ActivityItem> arrayList = new ArrayList<ActivityItem>();
		
		// Reference database
		DatabaseHelper flaxDb = new DatabaseHelper(context);
		SQLiteDatabase db = flaxDb.getReadableDatabase();

		// selection query - attributes to be selected
		String[] projection = {
		    FeedActivity._ID,
		    FeedActivity.COLUMN_NAME_ACTIVITY_ID,
		    FeedActivity.COLUMN_NAME_ACT_CATEGORY_ID,
		    FeedActivity.COLUMN_NAME_ACTIVITY_TYPE,
		    FeedActivity.COLUMN_NAME_ACTIVITY_NAME,
		    FeedActivity.COLUMN_NAME_ACTIVITY_URL,
		    FeedActivity.COLUMN_NAME_ACTIVITY_STATUS,
		    FeedActivity.COLUMN_NAME_ACTIVITY_ORDER,
		    FeedActivity.COLUMN_NAME_ACTIVITY_WORD_COUNT,
		    };
		
		// Selection query - 'where' clause
		String selection = FeedActivity.COLUMN_NAME_ACTIVITY_TYPE + "='" + type + "'";
		
		// Selection query - sorting order
		String orderBy = FeedActivity.COLUMN_NAME_ACTIVITY_STATUS + " DESC, " + FeedActivity.COLUMN_NAME_ACTIVITY_ORDER + " DESC";
				
		// Create cursor for db query
		Cursor cursor = db.query(
		    FeedActivity.TABLE_NAME,  				// select from activity_table
		    projection,                          	// return all columns
		    selection,                              // where type	= CollocationDominoes
		    null,                          			// where		= null
		    null,                              		// group		= null
		    null,                                   // filter 		= null
		    orderBy              	                // order 		= status then order
		    );
		cursor.moveToFirst();
		
		// Move through the cursor results, adding each entry to the arrayList
		for(int i = 0; i < cursor.getCount(); i++){
			ActivityItem exercise = new ActivityItem(
					cursor.getInt(cursor.getColumnIndexOrThrow(FeedActivity._ID)), 
					cursor.getString(cursor.getColumnIndexOrThrow(FeedActivity.COLUMN_NAME_ACTIVITY_ID)), 
					cursor.getString(cursor.getColumnIndexOrThrow(FeedActivity.COLUMN_NAME_ACT_CATEGORY_ID)), 
					cursor.getString(cursor.getColumnIndexOrThrow(FeedActivity.COLUMN_NAME_ACTIVITY_NAME)), 
					cursor.getString(cursor.getColumnIndexOrThrow(FeedActivity.COLUMN_NAME_ACTIVITY_TYPE)), 
					cursor.getString(cursor.getColumnIndexOrThrow(FeedActivity.COLUMN_NAME_ACTIVITY_URL)), 
					cursor.getString(cursor.getColumnIndexOrThrow(FeedActivity.COLUMN_NAME_ACTIVITY_STATUS)),
					cursor.getInt(cursor.getColumnIndexOrThrow(FeedActivity.COLUMN_NAME_ACTIVITY_ORDER)),
					cursor.getInt(cursor.getColumnIndexOrThrow(FeedActivity.COLUMN_NAME_ACTIVITY_WORD_COUNT))
					);
			arrayList.add(exercise);
			cursor.moveToNext();
		}
		flaxDb.closeDatabase();
		return arrayList;
	}
	
	/*
	 * selectCompleteActivities method
	 * 
	 * Selects all complete activities from the db with the
	 * specifies type value ie type = CollocationDominoes
	 * 
	 * @param type, Activity type. ie CollocationDominoes
	 * @return ArrayList of exercises 
	 */
	public ArrayList<ActivityItem> selectCompleteActivities(String type){

		// Declare arrayList to hold activities
		ArrayList<ActivityItem> arrayList = new ArrayList<ActivityItem>();
		
		// Reference database
		DatabaseHelper flaxDb = new DatabaseHelper(context);
		SQLiteDatabase db = flaxDb.getReadableDatabase();

		// selection query - attributes to be selected
		String[] projection = {
		    FeedActivity._ID,
		    FeedActivity.COLUMN_NAME_ACTIVITY_ID,
		    FeedActivity.COLUMN_NAME_ACT_CATEGORY_ID,
		    FeedActivity.COLUMN_NAME_ACTIVITY_TYPE,
		    FeedActivity.COLUMN_NAME_ACTIVITY_NAME,
		    FeedActivity.COLUMN_NAME_ACTIVITY_URL,
		    FeedActivity.COLUMN_NAME_ACTIVITY_STATUS,
		    FeedActivity.COLUMN_NAME_ACTIVITY_ORDER,
		    FeedActivity.COLUMN_NAME_ACTIVITY_WORD_COUNT,
		    };
		
		// Selection query - 'where' clause
		String selection = FeedActivity.COLUMN_NAME_ACTIVITY_TYPE + "='" + type + "' AND " +  
							FeedActivity.COLUMN_NAME_ACTIVITY_STATUS + "='complete'";
		
		// Selection query - sorting order
		String orderBy = FeedActivity.COLUMN_NAME_ACTIVITY_ORDER + " ASC";
				
		// Create cursor for db query
		Cursor cursor = db.query(
		    FeedActivity.TABLE_NAME,  				// select from activity_table
		    projection,                          	// return all columns
		    selection,                              // where type	= CollocationDominoes
		    null,                          			// where		= null
		    null,                              		// group		= null
		    null,                                   // filter 		= null
		    orderBy              	                // order 		= id ascending
		    );
		cursor.moveToFirst();
		
		// Move through the cursor results, adding each entry to the arrayList
		for(int i = 0; i < cursor.getCount(); i++){
			ActivityItem exercise = new ActivityItem(
					cursor.getInt(cursor.getColumnIndexOrThrow(FeedActivity._ID)), 
					cursor.getString(cursor.getColumnIndexOrThrow(FeedActivity.COLUMN_NAME_ACTIVITY_ID)), 
					cursor.getString(cursor.getColumnIndexOrThrow(FeedActivity.COLUMN_NAME_ACT_CATEGORY_ID)), 
					cursor.getString(cursor.getColumnIndexOrThrow(FeedActivity.COLUMN_NAME_ACTIVITY_NAME)), 
					cursor.getString(cursor.getColumnIndexOrThrow(FeedActivity.COLUMN_NAME_ACTIVITY_TYPE)), 
					cursor.getString(cursor.getColumnIndexOrThrow(FeedActivity.COLUMN_NAME_ACTIVITY_URL)), 
					cursor.getString(cursor.getColumnIndexOrThrow(FeedActivity.COLUMN_NAME_ACTIVITY_STATUS)),
					cursor.getInt(cursor.getColumnIndexOrThrow(FeedActivity.COLUMN_NAME_ACTIVITY_ORDER)),
					cursor.getInt(cursor.getColumnIndexOrThrow(FeedActivity.COLUMN_NAME_ACTIVITY_WORD_COUNT))
					);
			arrayList.add(exercise);
			cursor.moveToNext();
		}
		flaxDb.closeDatabase();
		return arrayList;
	}
	
	/*
	 * selectActivity method
	 * 
	 * Selects activity from the db with the
	 * specifies unique id.
	 * 
	 * @param uid, Activity unique id. 
	 * @return ActivityExercise
	 */
	public ActivityItem selectActivity(String uid){
		
		// Reference database
		DatabaseHelper flaxDb = new DatabaseHelper(context);
		SQLiteDatabase db = flaxDb.getReadableDatabase();

		// selection query - attributes to be selected
		String[] projection = {
		    FeedActivity._ID,
		    FeedActivity.COLUMN_NAME_ACTIVITY_ID,
		    FeedActivity.COLUMN_NAME_ACT_CATEGORY_ID,
		    FeedActivity.COLUMN_NAME_ACTIVITY_TYPE,
		    FeedActivity.COLUMN_NAME_ACTIVITY_NAME,
		    FeedActivity.COLUMN_NAME_ACTIVITY_URL,
		    FeedActivity.COLUMN_NAME_ACTIVITY_STATUS,
		    FeedActivity.COLUMN_NAME_ACTIVITY_ORDER,
		    FeedActivity.COLUMN_NAME_ACTIVITY_WORD_COUNT,
		    };
		
		// Selection query - 'where' clause
		String selection = FeedActivity._ID + "='" + uid + "'";
				
		Cursor cursor = db.query(
		    FeedActivity.TABLE_NAME,  				// select from activity_table
		    projection,                          	// return all columns
		    selection,                              // where type	= CollocationDominoes
		    null,                          			// where 		= null
		    null,                                   // group 		= null
		    null,                                   // filter 		= null
		    null	                                // order 		= null 
		    );
		cursor.moveToFirst();

		// Retrieve cursor results and store in ActivityExercise object
		ActivityItem exercise = new ActivityItem(
				cursor.getInt(cursor.getColumnIndexOrThrow(FeedActivity._ID)), 
				cursor.getString(cursor.getColumnIndexOrThrow(FeedActivity.COLUMN_NAME_ACTIVITY_ID)), 
				cursor.getString(cursor.getColumnIndexOrThrow(FeedActivity.COLUMN_NAME_ACT_CATEGORY_ID)), 
				cursor.getString(cursor.getColumnIndexOrThrow(FeedActivity.COLUMN_NAME_ACTIVITY_NAME)), 
				cursor.getString(cursor.getColumnIndexOrThrow(FeedActivity.COLUMN_NAME_ACTIVITY_TYPE)), 
				cursor.getString(cursor.getColumnIndexOrThrow(FeedActivity.COLUMN_NAME_ACTIVITY_URL)), 
				cursor.getString(cursor.getColumnIndexOrThrow(FeedActivity.COLUMN_NAME_ACTIVITY_STATUS)),
				cursor.getInt(cursor.getColumnIndexOrThrow(FeedActivity.COLUMN_NAME_ACTIVITY_ORDER)),
				cursor.getInt(cursor.getColumnIndexOrThrow(FeedActivity.COLUMN_NAME_ACTIVITY_WORD_COUNT))
				);
		flaxDb.closeDatabase();
		return exercise;
	}
	
	/* 
	 * addSummary method
	 * 
	 * adds a summary report row to the db 
	 * @param start time, end time, attempts, score, activity id.
	 */
	public long addSummary(String startTime, String endTime, 
			int attempts, int score, int activityId ){
		
		// Reference database
		DatabaseHelper flaxDb = new DatabaseHelper(context);
		SQLiteDatabase db = flaxDb.getWritableDatabase();
		
		// Map values for new row in db
		ContentValues values = new ContentValues();
		values.put(FeedSummary.COLUMN_NAME_START_TIME, startTime);
		values.put(FeedSummary.COLUMN_NAME_END_TIME, endTime);
		values.put(FeedSummary.COLUMN_NAME_ATTEMPTS, attempts);
		values.put(FeedSummary.COLUMN_NAME_SCORE, score);
		values.put(FeedSummary.COLUMN_NAME_ACTIVITY_ID, activityId);

		// insert new row in db
		long rowId = db.insert(FeedSummary.TABLE_NAME, null, values);	
		flaxDb.closeDatabase();
		return rowId;
	}
	
	/*
	 * selectSummary method
	 * 
	 * Selects a summary report row from the db with the
	 * specified unique id.
	 * 
	 * @param uid, Activity unique id. 
	 * @return ArrayList of summary report columns
	 */
	public ArrayList<String> selectSummary(String uid){
		
		// Reference database
		DatabaseHelper flaxDb = new DatabaseHelper(context);
		SQLiteDatabase db = flaxDb.getReadableDatabase();

		// selection query - attributes to be selected
		String[] projection = {
		    FeedSummary._ID,
		    FeedSummary.COLUMN_NAME_START_TIME,
		    FeedSummary.COLUMN_NAME_END_TIME,
		    FeedSummary.COLUMN_NAME_ATTEMPTS,
		    FeedSummary.COLUMN_NAME_SCORE,
		    FeedSummary.COLUMN_NAME_ACTIVITY_ID
		    };
		
		// Selection query - 'where' clause
		String selection = FeedSummary.COLUMN_NAME_ACTIVITY_ID + "='" + uid + "'";
				
		Cursor cursor = db.query(
		    FeedSummary.TABLE_NAME,  				// select from summary_table
		    projection,                          	// return all columns
		    selection,                              // where activityId	= uid
		    null,                          			// where 			= null
		    null,                                   // group 			= null
		    null,                                   // filter 			= null
		    null	                                // order 			= null 
		    );
		cursor.moveToFirst();

		// Retrieve cursor results and store in array
		ArrayList<String> summary = new ArrayList<String>();
		int id 			= cursor.getInt(cursor.getColumnIndexOrThrow(FeedSummary._ID));
		String start 	= cursor.getString(cursor.getColumnIndexOrThrow(FeedSummary.COLUMN_NAME_START_TIME)); 
		String end 		= cursor.getString(cursor.getColumnIndexOrThrow(FeedSummary.COLUMN_NAME_END_TIME));
		int attempts 	= cursor.getInt(cursor.getColumnIndexOrThrow(FeedSummary.COLUMN_NAME_ATTEMPTS));
		int score 		= cursor.getInt(cursor.getColumnIndexOrThrow(FeedSummary.COLUMN_NAME_SCORE));
		String aId 		= cursor.getString(cursor.getColumnIndexOrThrow(FeedSummary.COLUMN_NAME_ACTIVITY_ID));
		
		summary.add(Integer.toString(id));
		summary.add(start);
		summary.add(end);
		summary.add(Integer.toString(attempts));
		summary.add(Integer.toString(score));
		summary.add(aId);

		flaxDb.closeDatabase();
		return summary;
	}
	
	/*
	 * updateSummary Method
	 * 
	 * updates a row in the summary table
	 * @param start time, end time, attempts, score and activity id
	 */
	public void updateSummary(String st, String et, int a, int s, String aid){
		
		// Reference database
		CollocationDatabaseHelper flaxDb = new CollocationDatabaseHelper(context);
		SQLiteDatabase db = flaxDb.getWritableDatabase();
		
		// Selection statement (where activity id = given id)
		String selection = FeedSummary.COLUMN_NAME_ACTIVITY_ID + "='" + aid + "'";
		
		// Map values for updated row in db
		ContentValues values = new ContentValues();
		values.put(FeedSummary.COLUMN_NAME_START_TIME, st);
		values.put(FeedSummary.COLUMN_NAME_END_TIME, et);
		values.put(FeedSummary.COLUMN_NAME_ATTEMPTS, a);
		values.put(FeedSummary.COLUMN_NAME_SCORE, s);

		// update row in db
		db.update(FeedSummary.TABLE_NAME, values, selection, null);
		flaxDb.closeDatabase();		
	}
	
	
	public List<String> selectExistUrls(String[] urls){
		
		// Reference database
		DatabaseHelper flaxDb = new DatabaseHelper(context);
		SQLiteDatabase db = flaxDb.getReadableDatabase();

		// selection query - attributes to be selected
		String[] projection = {
		    FeedActivity.COLUMN_NAME_ACTIVITY_URL
		    };
		
		// Selection query - 'where' clause
		StringBuilder selection = new StringBuilder(FeedActivity.COLUMN_NAME_ACTIVITY_URL + " in (");
		for (String url : urls) {
			selection.append("'" + url + "',");
		}
		selection.deleteCharAt(selection.length()-1);
		selection.append(")");
				
		Cursor cursor = db.query(
		    FeedActivity.TABLE_NAME,  				// select from activity_table
		    projection,                          	// return all columns
		    selection.toString(),                              // where type	= CollocationDominoes
		    null,                          			// where 		= null
		    null,                                   // group 		= null
		    null,                                   // filter 		= null
		    null	                                // order 		= null 
		    );
		cursor.moveToFirst();

		// Move through the cursor results, adding each entry to the arrayList
		List<String> existUrls = new ArrayList<String>();
		for(int i = 0; i < cursor.getCount(); i++){
			existUrls.add(cursor.getString(cursor.getColumnIndexOrThrow(FeedActivity.COLUMN_NAME_ACTIVITY_URL)));
			cursor.moveToNext();
		}
		cursor.close();
		flaxDb.closeDatabase();
		return existUrls;
	}
} // end of class
