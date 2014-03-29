/*
 * File: flax.activity.Item
 *
 * License: Greenstone 3 (GSDL3)
 * Copyright (C) 2003 New Zealand Digital Libraries, University Of Waikato
 * Greenstone3 comes with ABSOLUTELY NO WARRANTY; for details see LICENSE.txt
 * This is free software, and you are welcome to redistribute it
 * 
 * Reference: http://developer.android.com
 * 
 * Notes: In order to create a custom adapter, an Item object is needed to store 
 * data being passed into the list view.
 */
package flax.activity;

/**
 * Item class
 * 
 * Used to store activity information for the list view
 * 
 * @author Jemma Konig
 */
public class Item {
	 
	// Declare variables
    private String title;
    private String status;
 
    /*
     * Item class constructor
     * 
     * @param t - the title of the activity
     * @param s - the status for the activity (ie new)
     */
    public Item(String t, String s) {
        super();
        this.title = t;
        this.status = s;
    }

    /* getTitle method
     * @return title
     */
    public String getTitle(){
    	return title;
    }
    
    /* getStatus method
     * @return status
     */
    public String getStatus(){
    	return status;
    }
} // end of class
