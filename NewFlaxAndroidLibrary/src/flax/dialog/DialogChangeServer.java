/*
 * File: flax.dialog.DialogChangeServer
 *
 * License: Greenstone 3 (GSDL3)
 * Copyright (C) 2003 New Zealand Digital Libraries, University Of Waikato
 * Greenstone3 comes with ABSOLUTELY NO WARRANTY; for details see LICENSE.txt
 * This is free software, and you are welcome to redistribute it
 * 
 * Reference: http://developer.android.com
 * 
 * Notes: This file is used to create and display the dialog box that allows the 
 * user to change the server path.
 */
package flax.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.Toast;

/**
 * DialogChangeServer Class
 *
 * This class is used to create and display the dialog box that allows the 
 * user to change the server path.
 * 
 * @author Jemma Konig
 */
public class DialogChangeServer {
	
	// Declare variables for the class
	protected static Context context;
	protected static String serverPath;
	protected static int serverIndex 					= 0;
	
	// Declare constants for the class
	protected static final String DEFAULT_SERVER_PATH 	= "http://flax.nzdl.org/greenstone3/flax";
	
	/* Constructor for DialogChangeServer Class */
	public DialogChangeServer(Context c){
		context = c;
		loadServerPath();
	}
	
	/*
	 * saveServerPath method
	 * 
	 * Uses shared preferences to save the 
	 * Users chosen server path
	 */
	public void saveServerPath(){
		
		// Declare shared preference manager
        SharedPreferences sharedPref 					= PreferenceManager
        												.getDefaultSharedPreferences(context);
        // Declare shared preference editor
        Editor editor 									= sharedPref.edit();
        
        // Save shared preference value for "server"
        editor.putString("server", serverPath);
        editor.commit();
	}
	
	/*
	 * loadServerPath method
	 * 
	 * Uses shared preferences to load the 
	 * Users chosen server path
	 */
	public void loadServerPath(){
		
		// Declare shared preference manager
		SharedPreferences sharedPref 					= PreferenceManager
														.getDefaultSharedPreferences(context);
        // Retrieve value from shared pref
		serverPath	 									= sharedPref.getString("server", DEFAULT_SERVER_PATH);
		
		// Set server index (used for dialog)
		if(serverPath.equals(DEFAULT_SERVER_PATH)){
			serverIndex 								= 0;
		}
		else{
			serverIndex 								= 1;
		}
	}
	
	/*
	 * displayChangeServerDialog method
	 * 
	 * displays the dialog box that allows the user to
	 * change the server path.
	 */
	public void displayChangeServerDialog(){
				
		// Declare char arr for options
		final CharSequence[] items 						= { "Default", "Custom" };
		
		// Instantiate alert dialog builder
		AlertDialog.Builder alertDialogBuilder 			= new AlertDialog.Builder(context);
		
		// Create dialog
		alertDialogBuilder
		
		// set dialog title
		.setTitle("Change server path")
		
		// set dialog content -- radio buttons to choose 'custom' or 'default' server
		.setSingleChoiceItems(items, serverIndex, new DialogInterface.OnClickListener()  {
			public void onClick(DialogInterface dialog, int item) {
				switch(item)
				{
				case 0:
					serverIndex 			= item;
				case 1:
					serverIndex 			= item;
					// if the custom server option is chosen ... 
					if(serverIndex == 1){
						// ... open new dialog so that the user may enter the new server path
						displayEnterServerDialog();
						dialog.cancel();
					}
				}
			}
		})
		
		// set dialog save button
		.setPositiveButton("Save",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				// if "Save" is clicked, save the server path into shared pref
				if(serverIndex == 0){
					serverPath 							= DEFAULT_SERVER_PATH;
					saveServerPath();
				}
				dialog.cancel();
				Toast.makeText(context, 
						"Server path saved.", Toast.LENGTH_SHORT).show();
		}})	
			
		// set dialog cancel button
		.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				dialog.cancel();
		}});
		
		// create alert dialog and display it
		AlertDialog alertDialog 						= alertDialogBuilder.create();
		alertDialog.show();
	}	
	
	/*
	 * diaplayEnterServerDialog method
	 * 
	 * displays the dialog box with input for the user to enter a custom server
	 * path. This dialog is opened when the user clicks on the 'custom' radio button
	 * in the initial server dialog box.
	 */
	public void displayEnterServerDialog(){
		
		// Set up the input
		final EditText input = new EditText(context);
		
		// Specify the type of input expected
		input.setInputType(InputType.TYPE_CLASS_TEXT);
		input.setGravity(Gravity.CENTER);
		input.setText(serverPath);
		
		// Instantiate alert dialog builder
		AlertDialog.Builder alertDialogBuilder 			= new AlertDialog.Builder(context);
		
		// Create dialog
		alertDialogBuilder
		
		// set dialog title
		.setTitle("Enter server path")
		
		// set dialog text input
		.setView(input)
		
		// set dialog save button
		.setPositiveButton("Save",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				// Get the server path from the user input and check that it is formatted correctly
				serverPath							= input.getText().toString();
				if(!serverPath.startsWith("http://") || !serverPath.endsWith("/greenstone3/flax")){
					dialog.cancel();
					displayServerErrorDialog();
				}
				else{
					saveServerPath();
					dialog.cancel();
					Toast.makeText(context, 
							"Server path saved.", Toast.LENGTH_SHORT).show();
				}
		}})	
			
		// set dialog cancel button
		.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				dialog.cancel();
		}});
		
		// create alert dialog and display it
		AlertDialog alertDialog 						= alertDialogBuilder.create();
		alertDialog.show();
	}
	
	/*
	 * displayServerErrorDialog method
	 * 
	 * displays the error dialog for when the user has entered an incorrect path
	 */
	public void displayServerErrorDialog(){
		
		// Instantiate alert dialog builder
		AlertDialog.Builder alertDialogBuilder 			= new AlertDialog.Builder(context);
		
		// Create dialog
		alertDialogBuilder
		
		// set dialog title
		.setTitle("Server path error")
		
		// set dialog text input
		.setMessage("The path you have entered is not a flax server. "
				+ "Would you like to use the default server or re-enter your custom server?")
		
		// set dialog save button
		.setPositiveButton("Re-Enter Server",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				displayEnterServerDialog();
				dialog.cancel();
		}})	
			
		// set dialog cancel button
		.setNegativeButton("Use Default Server",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				serverPath 							= DEFAULT_SERVER_PATH;
				saveServerPath();
				dialog.cancel();
				Toast.makeText(context, 
						"Server path saved.", Toast.LENGTH_SHORT).show();
		}});
		
		// create alert dialog and display it
		AlertDialog alertDialog 						= alertDialogBuilder.create();
		alertDialog.show();
		
	}
} // end of class
