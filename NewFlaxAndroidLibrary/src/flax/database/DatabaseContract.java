/*
 * File: flax.database.DatabaseContract
 *
 * License: Greenstone 3 (GSDL3)
 * Copyright (C) 2003 New Zealand Digital Libraries, University Of Waikato
 * Greenstone3 comes with ABSOLUTELY NO WARRANTY; for details see LICENSE.txt
 * This is free software, and you are welcome to redistribute it
 * 
 * Reference: http://developer.android.com
 * 
 * Notes: This file holds any constants relating to the internal database. 
 * ie. table and column names. The DatabaseContract class only holds the 
 * constants for the activity. For each custom application, extend
 * DatabaseContract to add constants for specific elements (ie collocations)
 */
package flax.database;

import android.provider.BaseColumns;

/**
 * DatabaseContract Class
 *
 * This class is used to hold constants for the internal database 
 * 
 * @author Jemma Konig 
 */
public class DatabaseContract {
	
	/* Database class constructor 
	 */
	public DatabaseContract(){
	}
	
    /** FeedActivity Class
     * 
     * Inner class that defines the Activity Table constants 
     */
    public static abstract class FeedActivity implements BaseColumns {
        public static final String TABLE_NAME 								= "activity_table";
        public static final String COLUMN_NAME_ACTIVITY_ID 					= "activity_id";
        public static final String COLUMN_NAME_ACT_CATEGORY_ID 				= "category_id";
        public static final String COLUMN_NAME_ACTIVITY_TYPE 				= "type";
        public static final String COLUMN_NAME_ACTIVITY_NAME	 			= "name";
        public static final String COLUMN_NAME_ACTIVITY_URL 				= "url";
        public static final String COLUMN_NAME_ACTIVITY_STATUS 				= "status";
        public static final String COLUMN_NAME_ACTIVITY_ORDER				= "ordering";
        public static final String COLUMN_NAME_ACTIVITY_WORD_COUNT			= "word_count";

    }
    
    /** FeedSummary Class
     * 
     * Inner class that defines the Summary Report Table constants 
     */
    public static abstract class FeedSummary implements BaseColumns {
        public static final String TABLE_NAME 								= "summary_table";
        public static final String COLUMN_NAME_START_TIME					= "start_time";
        public static final String COLUMN_NAME_END_TIME						= "end_time";
        public static final String COLUMN_NAME_ATTEMPTS						= "attempts";
        public static final String COLUMN_NAME_SCORE						= "score";
        public static final String COLUMN_NAME_ACTIVITY_ID 					= "activity_id";
    }
} // end of class
