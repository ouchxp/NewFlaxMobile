/*
 * File: flax.database.DatabaseHelper
 *
 * License: Greenstone 3 (GSDL3)
 * Copyright (C) 2003 New Zealand Digital Libraries, University Of Waikato
 * Greenstone3 comes with ABSOLUTELY NO WARRANTY; for details see LICENSE.txt
 * This is free software, and you are welcome to redistribute it
 * 
 * Reference: http://developer.android.com
 * 
 * Notes: This file is used to create, open and update the internal database.
 * It holds the create and delete SQL statements relating to the activity table.
 * For each custom application, extend DatabaseHelper and add create and delete
 * SQL statements relating to the specific elements (ie collocations)
 */
package flax.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import flax.database.DatabaseContract.*;

/**
 * DatabaseHelper Class
 *
 * This class is used to create, open and update the internal database.
 * It holds the create and delete SQL statements relating to the activity table. 
 * 
 * @author Jemma Konig
 */
public class DatabaseHelper extends SQLiteOpenHelper{
	
	// Declare constants for the database
    public static final int DATABASE_VERSION 				= 1;
    public static final String DATABASE_NAME 				= "FlaxAndroidApplication.db";
	
    // String SQL statement to create the activity table
    private static final String SQL_CREATE_ACTIVITY_TABLE =
            "CREATE TABLE " + FeedActivity.TABLE_NAME 		+ " (" +
            FeedActivity._ID 								+ " INTEGER PRIMARY KEY," +
            FeedActivity.COLUMN_NAME_ACTIVITY_ID 			+ " TEXT NOT NULL," +
            FeedActivity.COLUMN_NAME_ACT_CATEGORY_ID 		+ " TEXT NOT NULL," +
            FeedActivity.COLUMN_NAME_ACTIVITY_TYPE 			+ " TEXT NOT NULL," +
            FeedActivity.COLUMN_NAME_ACTIVITY_NAME 			+ " TEXT NOT NULL," +
            FeedActivity.COLUMN_NAME_ACTIVITY_URL 			+ " TEXT NOT NULL," +
            FeedActivity.COLUMN_NAME_ACTIVITY_STATUS 		+ " TEXT NOT NULL," +
    		FeedActivity.COLUMN_NAME_ACTIVITY_ORDER 		+ " INTEGER," +
    		FeedActivity.COLUMN_NAME_ACTIVITY_WORD_COUNT 	+ " INTEGER)";

    // String SQL statement to create the summary table
    private static final String SQL_CREATE_SUMMARY_TABLE =
            "CREATE TABLE " + FeedSummary.TABLE_NAME 		+ " (" +
            FeedSummary._ID 								+ " INTEGER PRIMARY KEY," +    
            FeedSummary.COLUMN_NAME_START_TIME				+ " TEXT NOT NULL," +
            FeedSummary.COLUMN_NAME_END_TIME 				+ " TEXT NOT NULL," +
            FeedSummary.COLUMN_NAME_ATTEMPTS 				+ " INTEGER," +
            FeedSummary.COLUMN_NAME_SCORE 					+ " INTEGER," +
            FeedSummary.COLUMN_NAME_ACTIVITY_ID 			+ " INTEGER NOT NULL)";
    
    /* DatabaseHelper class constructor */
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    
    /* OnCreate method - creates the initial activity table in the database */
    public void onCreate(SQLiteDatabase db) {       
        db.execSQL(SQL_CREATE_ACTIVITY_TABLE);
        db.execSQL(SQL_CREATE_SUMMARY_TABLE);
    }
    
    /* onUpgrade method - to upgrade the db, delete the existing table and create new */
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    
    /* closeDatabase method - closes the database after use */
    public void closeDatabase() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }
} // end of class
