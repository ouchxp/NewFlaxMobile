/*
 * File: flax.dialog.DialogHowToPlay
 *
 * License: Greenstone 3 (GSDL3)
 * Copyright (C) 2003 New Zealand Digital Libraries, University Of Waikato
 * Greenstone3 comes with ABSOLUTELY NO WARRANTY; for details see LICENSE.txt
 * This is free software, and you are welcome to redistribute it
 * 
 * Reference: http://developer.android.com
 * 
 * Notes: This file holds the dialog that describes how to play the game
 */
package flax.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * DialogHowToPlay Class
 * 
 * This file holds the dialog that describes how to play the game.
 * The text description is set when the method is called. This allows 
 * for the dialog to be used on multiple games.
 * 
 * @author Jemma Konig
 */
public class DialogHowToPlay {
	
	// Declare context for dialogs
	protected Context context;
	
	/*
	 * DialogHowToPlay class constructor
	 * @param context. The context for the activity where the
	 * dialog will be displayed.
	 */
	public DialogHowToPlay(Context c){
		context = c;
	}
	
	/*
	 * displayHowToPlayDialog method
	 * 
	 * Displays the dialog that describes how to play the game
	 */
	public void displayHowToPlayDialog(String howToPlay){
				
		// Instantiate alert dialog builder
		AlertDialog.Builder alertDialogBuilder 		= new AlertDialog.Builder(context);
		
		// Create dialog
		alertDialogBuilder
			
		// set dialog title
		.setTitle("How to play")
		
		// set dialog content
		.setMessage(howToPlay)
			
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
