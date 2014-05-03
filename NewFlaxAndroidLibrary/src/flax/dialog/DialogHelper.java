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

import static flax.utils.GlobalConstants.*;

import java.text.MessageFormat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.Toast;
import flax.library.R;
import flax.utils.FlaxUtil;

/**
 * DialogHelp Class
 * 
 * This class holds the help dialog for the app. Specific messages are set when
 * the method is called.
 * 
 * @author Nan Wu
 */
public class DialogHelper {

	// Declare context for help dialogs
	protected Context context;

	/**
	 * DialogHelp class constructor
	 * @param context. The context for the activity where the dialog will be
	 * displayed.
	 */
	public DialogHelper(Context c) {
		context = c;
	}

	/**
	 * Displays the dialog for the game help icon
	 */
	public void displayHelpDialog(String helpString) {
		// Instantiate alert dialog builder
		new AlertDialog.Builder(context)

		// set dialog title
				.setTitle(R.string.help_dialog_title)

				// set dialog content
				.setMessage(helpString)

				// set dialog Done button
				.setNegativeButton(R.string.dialog_btn_done, null)

				// create alert dialog and display it
				.create().show();
	}

	/**
	 * Displays the dialog that describes how to play the game
	 */
	public void displayHowToPlayDialog(String howToPlay) {
		// Instantiate alert dialog builder
		new AlertDialog.Builder(context)

		// set dialog title
				.setTitle(R.string.how_to_play_dialog_title)

				// set dialog content
				.setMessage(howToPlay)

				// set dialog Done button
				.setNegativeButton(R.string.dialog_btn_done, null)

				// create alert dialog and display it
				.create().show();
	}

	/**
	 * Displays the restart game dialog
	 */
	public void displayRestartGameDialog(final DialogInterface.OnClickListener yesCallback) {

		// Instantiate alert dialog builder
		new AlertDialog.Builder(context)

		// set dialog title
				.setTitle(R.string.restart_dialog_title)

				// set dialog content
				.setMessage(R.string.restart_dialog_message)

				// set dialog save button
				.setPositiveButton(R.string.dialog_btn_yes, yesCallback)

				// set dialog Done button
				.setNegativeButton(R.string.dialog_btn_no, null)

				// create alert dialog and display it
				.create().show();
	}

	/**
	 * Displays the dialog for the summary report
	 */
	public void displaySummaryReportDialog(int score, int possibleScore, String startTime, String endTime, int attempts) {

		// Determine score percentage
		double percent = (((double) score / (double) possibleScore) * 100);
		double percentage = Math.ceil(percent);

		if (startTime == null)
			startTime = "";
		if (endTime == null)
			endTime = "";
		String message = MessageFormat.format(context.getString(R.string.summary_dialog_message), startTime, endTime,
				attempts, score, possibleScore, percentage);

		// Instantiate alert dialog builder
		new AlertDialog.Builder(context)

		// set dialog title
				.setTitle(R.string.summary_dialog_title)

				// set message
				.setMessage(message)

				// set dialog Done button
				.setNegativeButton(R.string.dialog_btn_done, null)

				// create alert dialog and display it
				.create().show();
	}

	/**
	 * Creates the download dialog box. If "Yes" is pressed, calls
	 * startingDownload()
	 */
	public void displayDownloadDialog(final DialogInterface.OnClickListener yesCallback) {
		// Log.d(TAG, "download dialog being created ...");

		// Create dialog
		new AlertDialog.Builder(context)

		// set title
				.setTitle(R.string.download_dialog_title)

				// set dialog content
				.setMessage(R.string.download_dialog_message).setCancelable(false)

				// set "Yes" button
				.setPositiveButton(R.string.dialog_btn_yes, yesCallback)

				// set "No" button
				.setNegativeButton(R.string.dialog_btn_no, null).create().show();
	}

	/**
	 * displayCheckAnswerDialog method Displays the check answer dialog
	 */
	public void displayCheckAnswerDialog(String msgTitle, String msgBody) {

		// Instantiate alert dialog builder
		new AlertDialog.Builder(context).setTitle(msgTitle).setMessage(msgBody)
		// set dialog Done button
				.setNegativeButton(R.string.dialog_btn_done, null).create().show();
	}
	
	/**
	 * displays the dialog box that allows the user to change the server path.
	 */
	public void displayChangeServerDialog() {
		// get server path
		final String serverPath = FlaxUtil.getServerPath();
		final boolean isDefServerWhenOpen = serverPath.equals(DEFAULT_SERVER_PATH);

		// listener for dialog
		final DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
			// Store the options status.
			private boolean isDef = isDefServerWhenOpen;

			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				// Save
				case DialogInterface.BUTTON_POSITIVE:
					if (isDef) {
						FlaxUtil.setServerPath(DEFAULT_SERVER_PATH);
					}
					Toast.makeText(context, R.string.server_saved_message, Toast.LENGTH_SHORT).show();
					break;
				// Default server option selected.
				case 0:
					isDef = true;
					break;
				// Custom server option selected.
				case 1:
					isDef = false;
					displayEnterServerDialog();
					dialog.cancel();
					break;
				}
			}
		};

		// Instantiate alert dialog builder
		new AlertDialog.Builder(context)
		// set dialog title
				.setTitle(R.string.change_server_dialog_title)
				// set dialog content -- radio buttons to choose 'custom' or
				// 'default' server
				.setSingleChoiceItems(R.array.change_server_dialog_options, (isDefServerWhenOpen ? 0 : 1), listener)
				// set dialog save button
				.setPositiveButton(R.string.dialog_btn_save, listener)
				// set dialog cancel button
				.setNegativeButton(R.string.dialog_btn_cancel, null).create().show();
	}

	/**
	 * displays the dialog box with input for the user to enter a custom server
	 * path. This dialog is opened when the user clicks on the 'custom' radio
	 * button in the initial server dialog box.
	 */
	private void displayEnterServerDialog() {

		// Set up the input
		// Specify the type of input expected
		final EditText input = new EditText(context);
		input.setInputType(InputType.TYPE_CLASS_TEXT);
		input.setGravity(Gravity.CENTER);
		input.setText(FlaxUtil.getServerPath());

		// Instantiate alert dialog builder
		new AlertDialog.Builder(context)

		// set dialog title
				.setTitle(R.string.enter_server_dialog_title)

				// set dialog text input
				.setView(input)

				// set dialog save button
				.setPositiveButton(R.string.dialog_btn_save, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// Get the server path from the user input and check
						// that it is formatted correctly
						String newServerPath = input.getText().toString();
						if (!newServerPath.startsWith(VALID_SERVER_PREFIX)
								|| !newServerPath.endsWith(VALID_SERVER_SUFFIX)) {
							displayServerErrorDialog();
						} else {
							FlaxUtil.setServerPath(newServerPath);
							Toast.makeText(context, R.string.server_saved_message, Toast.LENGTH_SHORT).show();
						}
					}
				})

				// set dialog cancel button
				.setNegativeButton(R.string.dialog_btn_cancel, null).create().show();
	}

	/**
	 * displays the error dialog for when the user has entered an incorrect path
	 */
	private void displayServerErrorDialog() {

		// Instantiate alert dialog builder
		new AlertDialog.Builder(context)

		// set dialog title
				.setTitle(R.string.server_error_dialog_title)

				// set dialog text input
				.setMessage(R.string.server_error_dialog_message)

				// set dialog save button
				.setPositiveButton(R.string.dialog_btn_reenter_server, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						displayEnterServerDialog();
					}
				})

				// set dialog cancel button
				.setNegativeButton(R.string.dialog_btn_use_default_server, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						FlaxUtil.setServerPath(DEFAULT_SERVER_PATH);
						Toast.makeText(context, R.string.server_saved_message, Toast.LENGTH_SHORT).show();
					}
				}).create().show();

	}
} // end of class
