/*
 * File: flax.activity.ActivityItem
 *
 * License: Greenstone 3 (GSDL3)
 * Copyright (C) 2003 New Zealand Digital Libraries, University Of Waikato
 * Greenstone3 comes with ABSOLUTELY NO WARRANTY; for details see LICENSE.txt
 * This is free software, and you are welcome to redistribute it
 * 
 * Reference: http://developer.android.com
 * 
 * Notes: This file holds all of the information for an individual activity.
 * The data is initially downloaded from the flax server and parsed into an ActivityItem
 * object. Each ActivityItem object is then saved into the internal database. 
 * ActivityItem objects are used to hold the data when loaded into and out of the db.
 */
package flax.activity;

/**
 * ActivityItem Class
 * 
 * This file holds all of the information for an individual activity.
 * unique id, index, activity id, category id, activity name, activity type,
 * activity url, activity status and activity order.
 * 
 * @author Jemma Konig
 */
public class ActivityItem {
	
	// Declare variables for activity
	public int	  uniqueId;
	public int 	  index;
	public int	  activityOrder;	
	public int	  activityWordCount;		
	public String activityId 			= null;
	public String categoryId 			= null;
	public String activityName 			= null;
	public String activityType 			= null;
	public String activityUrl 			= null;
	public String activityStatus 		= null;

	/* 
	 * ActivityItem class constructor 
	 * 
	 * @param id 	- unique id (generated when added to db)
	 * @param aid 	- activity id (parsed from xml)
	 * @param cid 	- category id (parsed from xml)
	 * @param aNam	- activity name (parsed from xml)
	 * @param aTyp	- activity type (parsed from xml)
	 * @param aUrl	- activity url (parsed from xml)
	 * @param aStat	- activity state (ie new, incomplete, complete) 
	 * @param ord	- order in listView (generated when added to db)
	 * @param count - number of collo words in activity
	 */
    public ActivityItem(int id, String aId, String cId, String aNam, 
    		String aTyp, String aUrl, String aStat, int ord, int count) {
    	this.uniqueId 					= id;
		this.activityId 				= aId;
		this.categoryId 				= cId;
		this.activityName 				= aNam;
		this.activityType 				= aTyp;
		this.activityUrl 				= aUrl;
		this.activityStatus 			= aStat;
		this.activityOrder				= ord;
		this.activityWordCount			= count;
    }
    
    /* getActivityName method 
     * @return activity name 
     */
    public String getActivityName(){
    	return this.activityName;
    }
    
    /* getActivityStatus method 
     * @return activity status 
     */
    public String getActivityStatus(){
    	return this.activityStatus;
    }
    
    /* getActivityIndex method 
     * @return activity index 
     */    
    public int getActivityIndex(){
    	return this.index;
    }
    
    /* setActivityIndex method 
     * @param i - activity index 
     */    
    public void setActivityIndex(int i){
    	this.index = i;
    }
    
    /* getActivityId method 
     * @return activity id 
     */    
    public int getUniqueId(){
    	return this.uniqueId;
    }
    
    /* getActivityOrder Method
     * @return activity order
     */
    public int getActivityOrder(){
    	return this.activityOrder;
    }
    
    /* getActivityWordCount Method
     * @return activity word count
     */
    public int getActivityWordCount(){
    	return this.activityWordCount;
    }
    
    /* setActivityWordCount Method
     * @param activity word count
     */
    public void setActivityWordCount(int count){
    	this.activityWordCount = count;
    }
    
    /* getActivityUrl Method
     * @return activity url
     */
    public String getActivityUrl(){
    	return this.activityUrl;
    }
    
    /* setActivityUrl Method
     * @param activity url
     */
    public void setActivityUrl(String url){
    	this.activityUrl = url;
    }
} // end of class
