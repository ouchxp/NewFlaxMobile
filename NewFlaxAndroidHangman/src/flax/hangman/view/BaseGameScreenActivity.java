/*
 * File: flax.collocationdominoes.view.GameScreen
 *
 * License: Greenstone 3 (GSDL3)
 * Copyright (C) 2003 New Zealand Digital Libraries, University Of Waikato
 * Greenstone3 comes with ABSOLUTELY NO WARRANTY; for details see LICENSE.txt
 * This is free software, and you are welcome to redistribute it
 * 
 * Reference: http://developer.android.com
 * 
 * Notes: This file is the Game Screen Activity, it handles the game display
 * for the activity.
 */
package flax.hangman.view;

import java.sql.SQLException;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import flax.activity.ExerciseTypeEnum;
import flax.database.DatabaseDaoHelper;
import flax.dialog.DialogEnableSamples;
import flax.dialog.DialogHelp;
import flax.dialog.DialogHowToPlay;
import flax.dialog.DialogSummaryReport;
import flax.entity.base.BaseEntity;
import flax.entity.exercise.Exercise;
import flax.entity.hangman.HangmanResponse;
import flax.hangman.R;
import flax.hangman.game.GameEngine;
import flax.hangman.game.GameItem;

/**
 * GameScreen Class
 * 
 * This class is the Game Screen Activity, it handles the game display for the
 * activity.
 * 
 * Note: Areas of code that need modifying are highlighted with a todo tag.
 * 
 * @author Jemma Konig
 */
public abstract class BaseGameScreenActivity extends Activity {
	/** Ormlite database helper, use getDBHelper method to get a instance */
	private DatabaseDaoHelper databaseHelper = null;
	protected Exercise exercise;
	protected HangmanResponse exercise_content;
	protected final Context context = this;
	
	protected static final String TAG = "GAME_SCREEN";
	protected static final int SCORE_ID = 1000;
	protected ExerciseTypeEnum EXERCISE_TYPE;
	protected abstract String getHowToPlayMessage();

	protected abstract String getHelpMessage();
	
	protected abstract ExerciseTypeEnum getExerciseType();
	protected DialogEnableSamples sampleDialog;
	
	
	// Declare variables for game screen
	protected GameScreen game;
	protected GameEngine gameEngine;
	
	

	// Declare constants for game screen


	/*
	 * onCreate method
	 * 
	 * Load all data for the game. Such as collocations, summary report, score
	 * etc. Display collocations game in which ever state the user last left it.
	 * i.e. new, incomplete, complete.
	 * 
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	protected void onCreate(Bundle savedInstanceState) {

		// Initialize game screen
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_screen);
		EXERCISE_TYPE = getExerciseType();
		
		game = (GameScreen) this;

		// TODO: Load game data using ormlite
		String uid = this.getIntent().getStringExtra("uniqueId");
		String exerciseId = this.getIntent().getStringExtra("exerciseId");
		try {
			Dao<Exercise, String> execDao = getDBHelper().getDao(Exercise.class);
			exercise = execDao.queryForId(exerciseId);
			exercise.getName();
			exercise.getStatus();
			
			Dao<BaseEntity, String> dao = getDBHelper().getDao(EXERCISE_TYPE.getRootEntityClass());
			BaseEntity result = dao.queryForId(exerciseId);
			exercise_content = (HangmanResponse)result;
			exercise_content.getHints();
			exercise_content.getWords();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		// Load game data based on the unique activity id
		gameEngine = new GameEngine(context, game);
		gameEngine.loadGameData(uid);

		// Display the game
		gameEngine.displayGameData();

		// Set the game screen title to the title of the game
		setTitle(GameItem.getGameTitle());

	}

	public void onLetterClick(View view) {
		gameEngine.onLetterClick(view);
	}

	public void onClickLeftArrow(View view) {
		gameEngine.onClickLeftArrow(view);
	}

	public void onClickRightArrow(View view) {
		gameEngine.onClickRightArrow(view);
	}

	/*
	 * onPause method
	 * 
	 * Update all aspects of the game when ever the game is paused. i.e. the
	 * game will pasue when the orientation is changed.
	 * 
	 * @see android.app.Activity#onPause()
	 */
	@Override
	public void onPause() {
		super.onPause();
		gameEngine.updateDatabase();
	}

	/*
	 * onCreateOptionsMenu method
	 * 
	 * Inflate the menu; this adds items to the action bar if it is present.
	 * 
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate menu
		getMenuInflater().inflate(R.menu.game_screen, menu);

		// // If the game is complete, hide the 'Check Answer' menu item
		// if(GameItem.getActivityStatus().equals("complete")){
		// MenuItem checkAnswer = menu.findItem(R.id.check_answer);
		// checkAnswer.setVisible(false);
		// }

		// Set the menu items
		gameEngine.setMenu(menu);
		return true;
	}

	/*
	 * onOptionsSelected
	 * 
	 * Handle menu option items being selected
	 * 
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		// Help icon pressed ...
		case R.id.help:

			// Display Help Dialog
			DialogHelp help = new DialogHelp(context);
			help.displayHelpDialog(getHelpMessage());
			return true;

			// Menu Item -- how to play pressed ...
		case R.id.how_to_play:

			// Display How to Play Dialog
			DialogHowToPlay d = new DialogHowToPlay(context);
			d.displayHowToPlayDialog(getHowToPlayMessage());
			return true;

			// Menu Item -- check answer pressed ...
			// case R.id.check_answer:
			//
			// // Display Check Answer Dialog
			// GameItem.setEndTime();
			// gameEngine.checkAnswer();
			//
			// // Re-display score
			// TextView tvScore = (TextView)findViewById(SCORE_ID);
			// tvScore.setText("Score: " + GameItem.getScore() + " / " +
			// GameItem.getPossibleScore());
			// return true;

			// Menu Item -- restart game pressed ...
		case R.id.restart_game:

			// Restart Collocation Dominoes Game
			displayRestartGameDialog();
			return true;

			// Menu Item -- summary report pressed ...
		case R.id.summary_report:

			// Display Summary Report Dialog
			DialogSummaryReport s = new DialogSummaryReport(context);
			s.displaySummaryReportDialog(GameItem.getScore(), GameItem.getPossibleScore(), GameItem.getStartTime(),
					GameItem.getEndTime(), GameItem.getAttempts());
			return true;

			// Menu Item -- summary report pressed ...
			// case R.id.enable_sample:
			//
			// // Display Sample Dialog
			// sampleDialog = new DialogEnableSamples(context);
			// sampleDialog.loadSampleSettings();
			// sampleDialog.displaySampleDialog();
			// return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/*
	 * displayRestartGameDialog method
	 * 
	 * Displays the restart game dialog
	 */
	public void displayRestartGameDialog() {

		// Instantiate alert dialog builder
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

		// set dialog title
		alertDialogBuilder.setTitle("Restart Game");

		// set dialog content
		alertDialogBuilder.setMessage("Would you like to restart this game?")

		// set dialog save button
				.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// Restart game
						gameEngine.restartGame();
						dialog.cancel();
					}
				})

				// set dialog Done button
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});

		// create alert dialog and display it
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		// Release DatabaseDaoHelper
		if (databaseHelper != null) {
			OpenHelperManager.releaseHelper();
			databaseHelper = null;
		}
	}

	/**
	 * Generate DatabaseDaoHelper for database operation.
	 * 
	 * @return
	 */
	protected DatabaseDaoHelper getDBHelper() {
		if (databaseHelper == null) {
			databaseHelper = OpenHelperManager.getHelper(this, DatabaseDaoHelper.class);
		}
		return databaseHelper;
	}
} // end of class
