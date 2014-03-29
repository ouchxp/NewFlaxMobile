/*
 * File: flax.dialog.DialogHelp
 *
 * License: Greenstone 3 (GSDL3)
 * Copyright (C) 2003 New Zealand Digital Libraries, University Of Waikato
 * Greenstone3 comes with ABSOLUTELY NO WARRANTY; for details see LICENSE.txt
 * This is free software, and you are welcome to redistribute it
 * 
 * Reference: http://developer.android.com
 * 
 * Notes: This file holds the help dialog for the app.
 * Specific messages are set when the method is called.
 */
package flax.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * DialogHelp Class
 * 
 * This class holds the help dialog for the app.
 * Specific messages are set when the method is called.
 * 
 * @author Jemma Konig
 */
public class DialogHelp {
	
	// Declare context for help dialogs
	protected Context context;
	
	/*
	 * DialogHelp class constructor
	 * @param context. The context for the activity where the
	 * dialog will be displayed.
	 */
	public DialogHelp(Context c){
		context = c;
	}
	
	/*
	 * displayGameHelpDialog method
	 * 
	 * Displays the dialog for the game help icon
	 */
	public void displayHelpDialog(String helpString){
				
		// Instantiate alert dialog builder
		AlertDialog.Builder alertDialogBuilder 		= new AlertDialog.Builder(context);
		
		// Create dialog
		alertDialogBuilder
			
		// set dialog title
		.setTitle("Help")
		
		// set dialog content
		.setMessage(helpString)
			
		// set dialog Done button
		.setNegativeButton("Done",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				dialog.cancel();
		}});
		
		// create alert dialog and display it
		AlertDialog alertDialog 			= alertDialogBuilder.create();
		alertDialog.show();
	}
} // end of class
