/*
 * File: flax.dialog.DialogCheckAnswer
 *
 * License: Greenstone 3 (GSDL3)
 * Copyright (C) 2003 New Zealand Digital Libraries, University Of Waikato
 * Greenstone3 comes with ABSOLUTELY NO WARRANTY; for details see LICENSE.txt
 * This is free software, and you are welcome to redistribute it
 * 
 * Reference: http://developer.android.com
 * 
 * Notes: This file holds the dialog that shows whether or not 
 * the user has gotten a question correct.
 */
package flax.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * DialogCheckAnswer Class
 * 
 * This file holds the dialog that shows whether or not 
 * the user has gotten a question correct.
 * 
 * @author Jemma Konig
 */
public class DialogCheckAnswer {
	
	// Declare context for dialogs
	protected Context context;
	
	/*
	 * DialogCheckAnswer class constructor
	 * @param context. The context for the activity where the
	 * dialog will be displayed.
	 */
	public DialogCheckAnswer(Context c){
		context = c;
	}
	
	/*
	 * displayCheckAnswerDialog method
	 * 
	 * Displays the check answer dialog
	 */
	public void displayCheckAnswerDialog(String msgTitle, String msgBody){
				
		// Instantiate alert dialog builder
		AlertDialog.Builder alertDialogBuilder 		= new AlertDialog.Builder(context);		
		
		// set dialog title
		alertDialogBuilder.setTitle(msgTitle);
		
		// set dialog content
		alertDialogBuilder.setMessage(msgBody);
	
		// set dialog Done button
		alertDialogBuilder.setNegativeButton("Done",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				dialog.cancel();
		}});
		
		// create alert dialog and display it
		AlertDialog alertDialog 			= alertDialogBuilder.create();
		alertDialog.show();
	}
} // end of class

