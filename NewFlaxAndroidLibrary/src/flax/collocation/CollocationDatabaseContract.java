/*
 * File: flax.collocation.CollocationDatabaseContract
 *
 * License: Greenstone 3 (GSDL3)
 * Copyright (C) 2003 New Zealand Digital Libraries, University Of Waikato
 * Greenstone3 comes with ABSOLUTELY NO WARRANTY; for details see LICENSE.txt
 * This is free software, and you are welcome to redistribute it
 * 
 * Reference: http://developer.android.com
 * 
 * Notes: This file holds constants relating to the internal database. 
 * ie. table and column names. The CollocationDatabaseContract class 
 * extends the DatabaseContract class. This is where you add any collocation
 * constants for the internal database.
 */
package flax.collocation;

import android.provider.BaseColumns;

import flax.database.DatabaseContract;

/**
 * CollocationDatabaseContract Class
 *
 * This class extends DatabaseContract and is used to hold collocation related 
 * constants for the internal database 
 * 
 * Note: Any classes relating to collocations have been stored in the FlaxAndroidLibrary
 * as they may be used by multiple games. ie. Collocation Dominoes, Collocation Matching etc.
 * 
 * @author Jemma Konig 
 */
public class CollocationDatabaseContract extends DatabaseContract{
	
	/* CollocationDatabase class constructor 
	 */
	public CollocationDatabaseContract(){
		super();
	}
	   
    /* FeedCollocation Class
     * 
     * Inner class that defines the Collocation Table constants 
     */    
	public static abstract class FeedCollocation implements BaseColumns {
        public static final String TABLE_NAME 								= "collocation_table";
        public static final String COLUMN_NAME_COLLOCATION_ID 				= "collocation_id";
        public static final String COLUMN_NAME_COLLOCATION_ORDER 			= "ordering";
        public static final String COLUMN_NAME_COLLOCATION_TYPE 			= "type";
        public static final String COLUMN_NAME_COLLOCATION_FRE 				= "fre";
        public static final String COLUMN_NAME_COLLOCATION_SENTID 			= "send_id";
        public static final String COLUMN_NAME_COLLOCATION_WORD 			= "word";
        public static final String COLUMN_NAME_COLLOCATION_USER_WORD		= "user_word";
        public static final String COLUMN_NAME_COLLOCATION_STATUS 			= "status";
        public static final String COLUMN_NAME_COLLOCATION_STATUS_FINAL		= "status_final";
        public static final String COLUMN_NAME_COLLOCATION_BASE_WORD		= "base_word";
        public static final String COLUMN_NAME_COLLOCATION_ACT_UNIQUE_ID 	= "activity_unique_id";
    }
} // end of class
