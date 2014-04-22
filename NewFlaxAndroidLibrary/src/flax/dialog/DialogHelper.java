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

import java.text.MessageFormat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import flax.library.R;

/**
 * DialogHelp Class
 * 
 * This class holds the help dialog for the app. Specific messages are set when
 * the method is called.
 * 
 * @author Jemma Konig
 */
public class DialogHelper {

	// Declare context for help dialogs
	protected Context context;

	/*
	 * DialogHelp class constructor
	 * 
	 * @param context. The context for the activity where the dialog will be
	 * displayed.
	 */
	public DialogHelper(Context c) {
		context = c;
	}

	/*
	 * displayGameHelpDialog method
	 * 
	 * Displays the dialog for the game help icon
	 */
	public void displayHelpDialog(String helpString) {

		// Instantiate alert dialog builder
		new AlertDialog.Builder(context)

		// set dialog title
				.setTitle("Help")

				// set dialog content
				.setMessage(helpString)

				// set dialog Done button
				.setNegativeButton("Done", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				})

				// create alert dialog and display it
				.create().show();
	}

	/*
	 * displayHowToPlayDialog method
	 * 
	 * Displays the dialog that describes how to play the game
	 */
	public void displayHowToPlayDialog(String howToPlay) {

		// Instantiate alert dialog builder
		new AlertDialog.Builder(context)

		// set dialog title
				.setTitle("How to play")

				// set dialog content
				.setMessage(howToPlay)

				// set dialog Done button
				.setNegativeButton("Done", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				})

				// create alert dialog and display it
				.create().show();
	}

	/*
	 * displayRestartGameDialog method
	 * 
	 * Displays the restart game dialog
	 */
	public void displayRestartGameDialog(final DialogInterface.OnClickListener restartCallback) {

		// Instantiate alert dialog builder
		new AlertDialog.Builder(context)

		// set dialog title
				.setTitle("Restart Game")

				// set dialog content
				.setMessage("Would you like to restart this game?")

				// set dialog save button
				.setPositiveButton("Yes", restartCallback)

				// set dialog Done button
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				})

				// create alert dialog and display it
				.create().show();
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
		String message = MessageFormat.format(context.getString(R.string.summary_message),  startTime,endTime,attempts,score,possibleScore,percentage);
		
		// Instantiate alert dialog builder
		new AlertDialog.Builder(context)	
		
		// set dialog title
		.setTitle("Summary report")
		
		// set message
//		.setMessage(
//				"Start time:   " + startTime + "\n" +
//				"End time:     " + endTime + "\n \n" +
//				"Attempts:     " + attempts + "\n" +
//				"Total Score:  " + score + " out of " + possibleScore + " (" + percentage + "%)" 
//				)
		.setMessage(message)
		
		// set dialog Done button
		.setNegativeButton("Done",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				dialog.cancel();
		}})
		
		// create alert dialog and display it
		.create().show();
	}
} // end of class
