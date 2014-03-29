/*
 * File: flax.dialog.DialogSampleSentences
 *
 * License: Greenstone 3 (GSDL3)
 * Copyright (C) 2003 New Zealand Digital Libraries, University Of Waikato
 * Greenstone3 comes with ABSOLUTELY NO WARRANTY; for details see LICENSE.txt
 * This is free software, and you are welcome to redistribute it
 * 
 * Reference: http://developer.android.com
 * 
 * Notes: This file is used to display the list of sample sentences
 */
package flax.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import java.util.ArrayList;

/**
 * DialogSampleSentences Class
 *
 * This file is used to display the list of sample sentences
 * 
 * @author Jemma Konig
 */
public class DialogSampleSentences {
	
	// Declare context for dialog
	protected Context context;

	/* DialogSampleSentences class constructor */
	public DialogSampleSentences(Context c){
		context = c;
	}

	/*
	 * displaySampleDialog method
	 * 
	 * Displays a list of sample sentences in an alert dialog
	 */
	public void displaySampleDialog(ArrayList<String> sampleList){
		
		// Create body of dialog
		String body ="";
		for(String s : sampleList){
			body += (s + "\n\n");
		}
		
		// Instantiate alert dialog builder
		AlertDialog.Builder alertDialogBuilder 		= new AlertDialog.Builder(context);
		
		// Create dialog
		alertDialogBuilder
			
		// set dialog title
		.setTitle("Sample sentences")
		
		// set dialog content
		.setMessage(body)
			
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

