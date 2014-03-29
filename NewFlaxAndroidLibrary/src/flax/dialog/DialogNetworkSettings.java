/*
 * File: flax.network.NetworkSettings
 *
 * License: Greenstone 3 (GSDL3)
 * Copyright (C) 2003 New Zealand Digital Libraries, University Of Waikato
 * Greenstone3 comes with ABSOLUTELY NO WARRANTY; for details see LICENSE.txt
 * This is free software, and you are welcome to redistribute it
 * 
 * Reference: http://developer.android.com
 * 
 * Notes: This file is used to display the dialog containing network settings.
 * Saves user preferences based on input in the dialog.
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
 * NetworkSetting Class
 *
 * This class is used to display the dialog containing network settings.
 * Saves user preferences based on input in the dialog.
 * 
 * @author Jemma Konig
 */
public class DialogNetworkSettings {

	// Declare variables for settings
	protected Context context;
	protected int networkIndex;
	protected static String networkType;
	
	/* NetworkSetting class constructor */
	public DialogNetworkSettings(Context c){
		context = c;
		loadNetworkSettings();
	}
	
	/*
	 * saveNetworkSettings method
	 * 
	 * Uses shared preferences to save the 
	 * Users chosen network settings
	 */
	public void saveNetworkSettings(){
		
		// Declare shared preference manager
        SharedPreferences sharedPref 				= PreferenceManager
        												.getDefaultSharedPreferences(context);
        // Declare shared preference editor
        Editor editor 								= sharedPref.edit();
        
        // Save shared preference value for "network"
        editor.putString("network", networkType);
        editor.commit();
	}
	
	/*
	 * loadNetworkSettings method
	 * 
	 * Uses shared preferences to load the 
	 * Users chosen network settings
	 */
	public void loadNetworkSettings(){
		
		// Declare shared preference manager
		SharedPreferences sharedPref 				= PreferenceManager
														.getDefaultSharedPreferences(context);
        // Retrieve value from shared pref
		networkType 								= sharedPref.getString("network", "Wi-Fi");
		
		// Set network index (used for dialog)
		if(networkType.equals("Wi-Fi")){
			networkIndex 							= 0;
		}
		else{
			networkIndex 							= 1;
		}
	}
	
	/*
	 * displayNetworkDialog method
	 * 
	 * Displays the dialog for the network settings
	 */
	public void displayNetworkDialog(){
		
		// Declare char arr for options
		final CharSequence[] items 					= { "wi-fi", "any" };
		
		// Instantiate alert dialog builder
		AlertDialog.Builder alertDialogBuilder 		= new AlertDialog.Builder(context);
		
		// Create dialog
		alertDialogBuilder
			
		// set dialog title
		.setTitle("Network settings")
		
		// set dialog content
		.setSingleChoiceItems(items, networkIndex, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {
				switch(item)
				{
				case 0:
					networkIndex 					= item;
				case 1:
					networkIndex 					= item;
				}
			}
		})
		
		// set dialog save button
		.setPositiveButton("Save",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				// if "Save" is clicked, search new exercises
				if(networkIndex == 0){
					networkType 					= "Wi-Fi";
					saveNetworkSettings();
				}
				else if(networkIndex == 1){
					networkType 					= "Any";
					saveNetworkSettings();
				}
				dialog.cancel();
				Toast.makeText(context, 
						"Network Settings Saved.", Toast.LENGTH_SHORT).show();
		}})	
			
		// set dialog cancel button
		.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				dialog.cancel();
		}});
		
		// create alert dialog and display it
		AlertDialog alertDialog 					= alertDialogBuilder.create();
		alertDialog.show();
	}
} // end of class
