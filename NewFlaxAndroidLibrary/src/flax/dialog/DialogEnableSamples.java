/*
 * File: flax.dialog.DialogEnableSamples
 *
 * License: Greenstone 3 (GSDL3)
 * Copyright (C) 2003 New Zealand Digital Libraries, University Of Waikato
 * Greenstone3 comes with ABSOLUTELY NO WARRANTY; for details see LICENSE.txt
 * This is free software, and you are welcome to redistribute it
 * 
 * Reference: http://developer.android.com
 * 
 * Notes: This file is used to create and display the dialog box that allows the 
 * user to choose whether to allow the app to download sample sentences in collocation games.
 */
package flax.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.widget.Toast;

/**
 * DialogEnableSamples Class
 *
 * This file is used to create and display the dialog box that allows the 
 * user to choose whether to allow the app to download sample sentences in 
 * collocation games.
 * 
 * @author Jemma Konig
 */
public class DialogEnableSamples {

	// Declare variables for samples
	protected static int sampleIndex;
	protected static Context context;
	protected static String sampleChoice;

	
	// Declare constants for samples
	protected static final String ENABLE					= "enable";
	protected static final String DISABLE					= "disable";
	protected static final String SAMPLE_KEY				= "sampleChoice";
	protected static final String SAMPLE_DISPLAY_KEY		= "sampleDisplay";
	protected static final String SAMPLE_DEFAULT 			= "disable";
	protected static final String SAMPLE_DISPLAY_DEFAULT 	= "true";
	protected static final String SAMPLE_MSG				= "Would you like to enable the "
																+ "option to connect to the server and download sample "
																+ "sentences? \n\n"
																+ "(If you wish to change this setting at a later date, " 
																+ "it can be found in the options menu) \n";
	
	/* DialogEnableSamples class constructor */
	public DialogEnableSamples(Context c){
		context = c;
		loadSampleSettings();
	}
	
	/*
	 * saveSampleSettings method
	 * 
	 * Uses shared preferences to save the 
	 * Users chosen sample settings
	 */
	public void saveDisplaySampleSettings(String sampleDisplay){
		
		// Declare shared preference manager
        SharedPreferences sharedPref 				= PreferenceManager
        												.getDefaultSharedPreferences(context);
        // Declare shared preference editor
        Editor editor 								= sharedPref.edit();
        
        // Save shared preference value for "sampleChoice"
        editor.putString(SAMPLE_DISPLAY_KEY, sampleDisplay);
        editor.commit();
	}
	
	/*
	 * loadSampleSettings method
	 * 
	 * Uses shared preferences to load the 
	 * Users chosen sample settings
	 */
	public String loadDisplaySampleSettings(){
		
		// Declare shared preference manager
		SharedPreferences sharedPref 				= PreferenceManager
														.getDefaultSharedPreferences(context);
        // Retrieve value from shared pref
		String sampleDisplay						= sharedPref.getString(SAMPLE_DISPLAY_KEY, SAMPLE_DISPLAY_DEFAULT);
		
		// Return the sample choice
		return sampleDisplay;
	}
	
	/*
	 * saveSampleSettings method
	 * 
	 * Uses shared preferences to save the 
	 * Users chosen sample settings
	 */
	public void saveSampleSettings(){
		
		// Declare shared preference manager
        SharedPreferences sharedPref 				= PreferenceManager
        												.getDefaultSharedPreferences(context);
        // Declare shared preference editor
        Editor editor 								= sharedPref.edit();
        
        // Save shared preference value for "sampleChoice"
        editor.putString(SAMPLE_KEY, sampleChoice);
        editor.commit();
	}
	
	/*
	 * loadSampleSettings method
	 * 
	 * Uses shared preferences to load the 
	 * Users chosen sample settings
	 */
	public String loadSampleSettings(){
		
		// Declare shared preference manager
		SharedPreferences sharedPref 				= PreferenceManager
														.getDefaultSharedPreferences(context);
        // Retrieve value from shared pref
		sampleChoice 								= sharedPref.getString(SAMPLE_KEY, SAMPLE_DEFAULT);
		
		// Return the sample choice
		return sampleChoice;
	}
	
	/*
	 * displaySampleDialog method
	 * 
	 * Displays the dialog for the sample settings
	 */
	public void displaySampleDialog(){
		
		// Instantiate alert dialog builder
		AlertDialog.Builder alertDialogBuilder 		= new AlertDialog.Builder(context);
		
		// Create dialog
		alertDialogBuilder
			
		// set dialog title
		.setTitle("Enable sample sentences")
				
		// set message
		.setMessage(SAMPLE_MSG)
		
		// set dialog save button
		.setPositiveButton("Enable",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				
				// if "Enable" is clicked, save sample choice
				sampleChoice 					= ENABLE;
				saveSampleSettings();
				
				dialog.cancel();
				Toast.makeText(context, 
						"sample sentences enabled", Toast.LENGTH_SHORT).show();
		}})	
			
		// set dialog cancel button
		.setNegativeButton("Disable",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				
				// if "Disable" is clicked, save sample choice
				sampleChoice 					= DISABLE;
				saveSampleSettings();
				
				dialog.cancel();
				Toast.makeText(context, 
						"sample sentences disabled", Toast.LENGTH_SHORT).show();
		}});
		
		// create alert dialog and display it
		AlertDialog alertDialog 					= alertDialogBuilder.create();
		alertDialog.show();
	}
} // end of class

