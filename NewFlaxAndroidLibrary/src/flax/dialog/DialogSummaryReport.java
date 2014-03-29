/*
 * File: flax.dialog.DialogSummaryReport
 *
 * License: Greenstone 3 (GSDL3)
 * Copyright (C) 2003 New Zealand Digital Libraries, University Of Waikato
 * Greenstone3 comes with ABSOLUTELY NO WARRANTY; for details see LICENSE.txt
 * This is free software, and you are welcome to redistribute it
 * 
 * Reference: http://developer.android.com
 * 
 * Notes: This file holds the dialog that shows the summary
 * report for each game.
 */
package flax.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * DialogSummaryReport Class
 * 
 * This file holds the dialog that shows the summary
 * report for each game.
 * 
 * @author Jemma Konig
 */
public class DialogSummaryReport {
	
	// Declare context for dialogs
	protected Context context;
	
	/*
	 * DialogSummaryReport class constructor
	 * @param context. The context for the activity where the
	 * dialog will be displayed.
	 */
	public DialogSummaryReport(Context c){
		context = c;
	}
	
	/*
	 * displaySummaryReportDialog method
	 * 
	 * Displays the dialog for the summary report
	 */
	public void displaySummaryReportDialog(int score, int possibleScore, String startTime, 
			String endTime, int attempts){
		
		// Determine score percentage
		double percent = (((double) score / (double) possibleScore) * 100);
		double percentage = Math.ceil(percent);	
		
		// Instantiate alert dialog builder
		AlertDialog.Builder alertDialogBuilder 		= new AlertDialog.Builder(context);		
		alertDialogBuilder
		
		// set dialog title
		.setTitle("Summary report")
		
		// set message
		.setMessage(
				"Start time:   " + startTime + "\n" +
				"End time:     " + endTime + "\n \n" +
				"Attempts:     " + attempts + "\n" +
				"Total Score:  " + score + " out of " + possibleScore + " (" + percentage + "%)" 
				)
		
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
