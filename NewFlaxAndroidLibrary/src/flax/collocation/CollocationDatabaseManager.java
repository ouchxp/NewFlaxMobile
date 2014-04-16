/*
 * File: flax.collocation.CollocationDatabaseManager
 *
 * License: Greenstone 3 (GSDL3)
 * Copyright (C) 2003 New Zealand Digital Libraries, University Of Waikato
 * Greenstone3 comes with ABSOLUTELY NO WARRANTY; for details see LICENSE.txt
 * This is free software, and you are welcome to redistribute it
 * 
 * Reference: http://developer.android.com
 * 
 * Notes: This file is used to manage any SQL queries relating to the collocations
 * table stored in the internal database. 
 */
package flax.collocation;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import flax.database.DatabaseManager;
import flax.collocation.CollocationDatabaseContract.FeedCollocation;

import java.util.ArrayList;

/**
 * CollocationDatabaseManager
 * 
 * The CollocationDatabaseManager class extends the DatabaseManager class. 
 * This is where you add any SQL queries for the collocations table. 
 * 
 * Note: All classes relating to collocations have been stored in the FlaxAndroidLibrary
 * as they may be used by multiple games. ie. Collocation Dominoes, Collocation Matching etc.
 * 
 * @author Jemma Konig
 */
public class CollocationDatabaseManager extends DatabaseManager{
	
	// Declare context for db manager
	protected final Context context;
	
	/*
	 * CollocationDatabaseManager class constructor
	 */
	public CollocationDatabaseManager(Context c){
		super(c);
		context = c;
	}
		
	/* 
	 * addCollocation method
	 * 
	 * adds a collocation to the db 
	 * @param collocation id, index, type, fre, send id, word and activity id
	 */
	public long addCollocation(String cid, int i, String tp, 
			String fre, String sid, String w, String uw, String st, String stf, String bw, int auid ){
		
		// Reference database
		CollocationDatabaseHelper flaxDb = new CollocationDatabaseHelper(context);
		SQLiteDatabase db = flaxDb.getWritableDatabase();
		
		// Map values for new row in db
		ContentValues values = new ContentValues();
		values.put(FeedCollocation.COLUMN_NAME_COLLOCATION_ID, cid);
		values.put(FeedCollocation.COLUMN_NAME_COLLOCATION_ORDER, i);
		values.put(FeedCollocation.COLUMN_NAME_COLLOCATION_TYPE, tp);
		values.put(FeedCollocation.COLUMN_NAME_COLLOCATION_FRE, fre);
		values.put(FeedCollocation.COLUMN_NAME_COLLOCATION_SENTID, sid);
		values.put(FeedCollocation.COLUMN_NAME_COLLOCATION_WORD, w);
		values.put(FeedCollocation.COLUMN_NAME_COLLOCATION_USER_WORD, uw);
		values.put(FeedCollocation.COLUMN_NAME_COLLOCATION_STATUS, st);
		values.put(FeedCollocation.COLUMN_NAME_COLLOCATION_STATUS_FINAL, stf);
		values.put(FeedCollocation.COLUMN_NAME_COLLOCATION_BASE_WORD, bw);
		values.put(FeedCollocation.COLUMN_NAME_COLLOCATION_ACT_UNIQUE_ID, auid);

		// insert new row in db
		long rowId = db.insert(FeedCollocation.TABLE_NAME, null, values);
		flaxDb.closeDatabase();
		return rowId;
	}
	
	/*
	 * updateCollocation Method
	 * 
	 * updates a collocation in the db 
	 * @param unique id, status, final status, user word.
	 * 
	 */
	public void updateCollocation(int id, String s, String sf, String uw){
		
		// Reference database
		CollocationDatabaseHelper flaxDb = new CollocationDatabaseHelper(context);
		SQLiteDatabase db = flaxDb.getWritableDatabase();
		
		// Selection statement (where unique id = given id)
		String selection = FeedCollocation._ID + "='" + id + "'";
		
		// Map values to be altered in db
		ContentValues values = new ContentValues();
		values.put(FeedCollocation.COLUMN_NAME_COLLOCATION_STATUS, s);
		values.put(FeedCollocation.COLUMN_NAME_COLLOCATION_STATUS_FINAL, sf);
		values.put(FeedCollocation.COLUMN_NAME_COLLOCATION_USER_WORD, uw);
		
		// update row in db
		db.update(FeedCollocation.TABLE_NAME, values, selection, null);
		flaxDb.closeDatabase();		
	}
	
	/*
	 * updateCollocationOrder Method
	 * 
	 * updates the order of the collocations in the db 
	 * @param unique id, order.
	 * 
	 */
	public void updateCollocationOrder(int id, int order){
		
		// Reference database
		CollocationDatabaseHelper flaxDb = new CollocationDatabaseHelper(context);
		SQLiteDatabase db = flaxDb.getWritableDatabase();
		
		// Selection statement (where unique id = given id)
		String selection = FeedCollocation._ID + "='" + id + "'";
		
		// Map values to be altered in db
		ContentValues values = new ContentValues();
		values.put(FeedCollocation.COLUMN_NAME_COLLOCATION_ORDER, order);
		
		// update row in db
		db.update(FeedCollocation.TABLE_NAME, values, selection, null);
		flaxDb.closeDatabase();		
	}
	
	/*
	 * deleteCollocation Method
	 * 
	 * deletes a collocation from the db 
	 * @param activity id.
	 * 
	 */
	public void deleteCollocation(int aid){
		
		// Reference database
		CollocationDatabaseHelper flaxDb = new CollocationDatabaseHelper(context);
		SQLiteDatabase db = flaxDb.getWritableDatabase();
		
		// Selection statement (where activity id = given id)
		String selection = FeedCollocation.COLUMN_NAME_COLLOCATION_ACT_UNIQUE_ID + "= '" + aid + "'";		
		
		// Delete all rows in db that match select statement
		db.delete(FeedCollocation.TABLE_NAME, selection , null);
		
		// Close the db
		flaxDb.closeDatabase();		
	}
	
	/*
	 * selectGivenCollocations method
	 * 
	 * Selects all collocations for specified activity
	 * 
	 * @param uid, the unique id of the activity the collocations match
	 * @return ArrayList of collocations 
	 */
	public ArrayList<CollocationItem> selectGivenCollocations(String uid){

		// Declare arrayList to hold collocations
		ArrayList<CollocationItem> arrayList = new ArrayList<CollocationItem>();
		
		// Reference database
		CollocationDatabaseHelper flaxDb = new CollocationDatabaseHelper(context);
		SQLiteDatabase db = flaxDb.getReadableDatabase();

		// selection query - attributes to be selected
		String[] projection = {
		    FeedCollocation._ID,
		    FeedCollocation.COLUMN_NAME_COLLOCATION_ID,
		    FeedCollocation.COLUMN_NAME_COLLOCATION_SENTID,
		    FeedCollocation.COLUMN_NAME_COLLOCATION_ORDER,
		    FeedCollocation.COLUMN_NAME_COLLOCATION_FRE,
		    FeedCollocation.COLUMN_NAME_COLLOCATION_WORD,
		    FeedCollocation.COLUMN_NAME_COLLOCATION_USER_WORD,
		    FeedCollocation.COLUMN_NAME_COLLOCATION_TYPE,
		    FeedCollocation.COLUMN_NAME_COLLOCATION_STATUS,
		    FeedCollocation.COLUMN_NAME_COLLOCATION_STATUS_FINAL,
		    FeedCollocation.COLUMN_NAME_COLLOCATION_BASE_WORD,
		    FeedCollocation.COLUMN_NAME_COLLOCATION_ACT_UNIQUE_ID
		    };
		
		// Selection query - 'where' clause
		String selection 	= FeedCollocation.COLUMN_NAME_COLLOCATION_ACT_UNIQUE_ID + "='" + uid + "'";
		
		// Selection query - 'order' clause
		String orderBy		= FeedCollocation.COLUMN_NAME_COLLOCATION_ORDER + " ASC"; 
		
		// Create cursor for db query
		Cursor cursor = db.query(
		    FeedCollocation.TABLE_NAME,  			// select from collocation_table
		    projection,                          	// return all columns
		    selection,                         		// where uid 	= activityid
		    null,                          			// where 		= null
		    null,                                   // group 		= null
		    null,                                   // having 		= null
		    orderBy                        			// order 		= index
		    );
		cursor.moveToFirst();
		
		// Move through the cursor results, adding each collocation to the arrayList
		for(int i = 0; i < cursor.getCount(); i++){
			CollocationItem collocation = new CollocationItem( 
					cursor.getInt(cursor.getColumnIndexOrThrow(FeedCollocation._ID)), 
					cursor.getString(cursor.getColumnIndexOrThrow(FeedCollocation.COLUMN_NAME_COLLOCATION_ID)), 
					cursor.getString(cursor.getColumnIndexOrThrow(FeedCollocation.COLUMN_NAME_COLLOCATION_SENTID)), 
					cursor.getInt(cursor.getColumnIndexOrThrow(FeedCollocation.COLUMN_NAME_COLLOCATION_ORDER)), 
					cursor.getString(cursor.getColumnIndexOrThrow(FeedCollocation.COLUMN_NAME_COLLOCATION_FRE)), 
					cursor.getString(cursor.getColumnIndexOrThrow(FeedCollocation.COLUMN_NAME_COLLOCATION_WORD)), 
					cursor.getString(cursor.getColumnIndexOrThrow(FeedCollocation.COLUMN_NAME_COLLOCATION_USER_WORD)), 
					cursor.getString(cursor.getColumnIndexOrThrow(FeedCollocation.COLUMN_NAME_COLLOCATION_TYPE)),
					cursor.getString(cursor.getColumnIndexOrThrow(FeedCollocation.COLUMN_NAME_COLLOCATION_STATUS)),
					cursor.getString(cursor.getColumnIndexOrThrow(FeedCollocation.COLUMN_NAME_COLLOCATION_STATUS_FINAL)),
					cursor.getString(cursor.getColumnIndexOrThrow(FeedCollocation.COLUMN_NAME_COLLOCATION_BASE_WORD)),
					cursor.getInt(cursor.getColumnIndexOrThrow(FeedCollocation.COLUMN_NAME_COLLOCATION_ACT_UNIQUE_ID)) 
					);
			arrayList.add(collocation);
			cursor.moveToNext();
		}
		
		// Close database and return arrayList of collocations
		flaxDb.closeDatabase();
		return arrayList;
	}
} // end of class
