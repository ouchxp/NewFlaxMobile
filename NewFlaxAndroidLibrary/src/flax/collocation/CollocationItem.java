/*
 * File: flax.collocation.CollocationActivityExercise
 *
 * License: Greenstone 3 (GSDL3)
 * Copyright (C) 2003 New Zealand Digital Libraries, University Of Waikato
 * Greenstone3 comes with ABSOLUTELY NO WARRANTY; for details see LICENSE.txt
 * This is free software, and you are welcome to redistribute it
 * 
 * Reference: http://developer.android.com
 * 
 * Notes: This file holds all of the information for an individual collocation.
 * The data is initially downloaded from the flax server and parsed into a 
 * CollocationActivityExercise object. Each CollocationActivityExercise object is 
 * then saved into the internal database. CollocationActivityExercise objects are used to hold the data when loaded into 
 * and out of the db. It is used for any activity applications involving collocations.
 */
package flax.collocation;

/**
 * ActivityExercise Class
 * 
 * This file holds all of the information for an individual collocation.
 * unique id, index, activity id, collocation id, send id, fre, word, type,
 * status.
 * 
 * Note: All classes relating to collocations have been stored in the FlaxAndroidLibrary
 * as they may be used by multiple games. ie. Collocation Dominoes, Collocation Matching etc.
 * 
 * @author Jemma Konig
 */
public class CollocationItem {

	// Declare variables for exercise
	public int	  uniqueId;
	public int	  index;
	public int 	  activityId;
	public String collocationId 	= null;
	public String sendId 			= null;
	public String fre 				= null;
	public String word 				= null;
	public String userWord			= null;
	public String type				= null;
	public String status			= null;
	public String statusFinal		= null;
	public String baseWord			= null;
	
	/* CollocationExercise class constructor 
	 * 
	 * @param id 	- unique id (generated when added to db)
	 * @param cid 	- collocation id (parsed from xml)
	 * @param sid	- send id (parsed from xml)
	 * @param i		- index of the collocation in listView
	 * @param f		- fre (parsed from xml)
	 * @param w		- word is the collocation (parsed from xml)
	 * @param t		- type (ie new, incomplete, complete)
	 * @param s		- status (used to determine which dominoes have text)
	 * @param sf	- status final (used to determine which dominoes are completed) 
	 * @param aId	- activity id that the collocation belongs to 
	 */
	public CollocationItem(int id, String cId, String sId, int i, 
			String f, String w, String uw, String t, String s, String sf, String bw, int aId) {
		this.uniqueId				= id;
		this.collocationId 			= cId;
		this.sendId 				= sId;
		this.index 					= i;
		this.fre 					= f;
		this.word 					= w;
		this.userWord				= uw;
		this.type					= t;
		this.status					= s;
		this.statusFinal			= sf;
		this.baseWord				= bw;
		this.activityId 			= aId;
	}
	    
    /* getCollocationWord method 
     * @return collocation word 
     */	
	public String getCollocationWord(){
		return this.word;
	}
	
    /* getBaseWord method 
     * @return base word 
     */	
	public String getBaseWord(){
		return this.baseWord;
	}
		
    /* getSendId method 
     * @return sendId 
     */	
	public String getSendId(){
		return this.sendId;
	}
	    
     /* getCollocationStatus method 
      * @return collocation status 
      */	
	public String getCollocationStatus(){
		return this.status;
	}
	
    /* getCollocationStatusFinal method 
     * @return collocation status final
     */	
	public String getCollocationStatusFinal(){
		return this.statusFinal;
	}
} // end of class