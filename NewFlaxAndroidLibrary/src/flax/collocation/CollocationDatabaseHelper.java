/*
 * File: flax.collocation.CollocationDatabaseHelper
 *
 * License: Greenstone 3 (GSDL3)
 * Copyright (C) 2003 New Zealand Digital Libraries, University Of Waikato
 * Greenstone3 comes with ABSOLUTELY NO WARRANTY; for details see LICENSE.txt
 * This is free software, and you are welcome to redistribute it
 * 
 * Reference: http://developer.android.com
 * 
 * Notes: This file is used to create, open and update the internal database.
 * It holds the create and delete SQL statements relating to the collocations table.
 */
package flax.collocation;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import flax.database.DatabaseHelper;
import flax.collocation.CollocationDatabaseContract.FeedCollocation;

/**
 * CollocationDatabaseHelper
 * 
 * The CollocationDatabaseHelper class extends the DatabaseHelper class. 
 * This is where you add the create and delete SQL for the collocations table. 
 * Must override onCreate and onUpgrade methods to include the collocations.
 * 
 * Note: All classes relating to collocations have been stored in the FlaxAndroidLibrary
 * as they may be used by multiple games. ie. Collocation Dominoes, Collocation Matching etc.
 * 
 * @author Jemma Konig
 *
 */
public class CollocationDatabaseHelper extends DatabaseHelper{
    
    // String SQL statement to create the collocation table
    private static final String SQL_CREATE_COLLOCATION_TABLE = 
            "CREATE TABLE " + FeedCollocation.TABLE_NAME 			+ "(" +
            FeedCollocation._ID 							    	+ " INTEGER PRIMARY KEY, " +
            FeedCollocation.COLUMN_NAME_COLLOCATION_ID 				+ " TEXT NOT NULL, " +	
            FeedCollocation.COLUMN_NAME_COLLOCATION_ORDER 			+ " INTEGER NOT NULL, " +	
            FeedCollocation.COLUMN_NAME_COLLOCATION_TYPE 			+ " TEXT NOT NULL, " +	
            FeedCollocation.COLUMN_NAME_COLLOCATION_FRE 			+ " TEXT NOT NULL, " +		
            FeedCollocation.COLUMN_NAME_COLLOCATION_SENTID 			+ " TEXT NOT NULL, " +	
            FeedCollocation.COLUMN_NAME_COLLOCATION_WORD 			+ " TEXT NOT NULL, " +	
            FeedCollocation.COLUMN_NAME_COLLOCATION_USER_WORD 		+ " TEXT NOT NULL, " +	
            FeedCollocation.COLUMN_NAME_COLLOCATION_STATUS 			+ " TEXT NOT NULL, " +
            FeedCollocation.COLUMN_NAME_COLLOCATION_STATUS_FINAL	+ " TEXT NOT NULL, " +
            FeedCollocation.COLUMN_NAME_COLLOCATION_BASE_WORD		+ " TEXT NOT NULL, " +
            FeedCollocation.COLUMN_NAME_COLLOCATION_ACT_UNIQUE_ID 	+ " TEXT NOT NULL)";
    
    /* DatabaseHelper class constructor */
    public CollocationDatabaseHelper(Context context) {
        super(context);
    }
    
    /* onCreate method 
     * 
     * @Override - creates both the activity and collocation tables
     */
    @Override
    public void onCreate(SQLiteDatabase db) {       
    	super.onCreate(db);
    	db.execSQL(SQL_CREATE_COLLOCATION_TABLE);
    }
    
    /* onUpgrade method 
     * 
     * @Override - upgrades both the activity and collocation tables
     */   
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    	super.onUpgrade(db, oldVersion, newVersion);
    }
    
    /* closeDatabase method 
     * 
     * @Override - calls DatabaseHelper closeDatabase method
     */
    @Override
    public void closeDatabase() {
    	super.closeDatabase();
    }
} // end of class


