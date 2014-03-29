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

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import flax.dialog.DialogEnableSamples;
import flax.dialog.DialogHelp;
import flax.dialog.DialogHowToPlay;
import flax.dialog.DialogSummaryReport;
import flax.hangman.R;
import flax.hangman.game.GameEngine;
import flax.hangman.game.GameItem;

/**
 * GameScreen Class
 * 
 * This class is the Game Screen Activity, it handles the game display
 * for the activity.
 * 
 * Note: Areas of code that need modifying are highlighted with a todo tag.
 * 
 * @author Jemma Konig
 */
public class GameScreen extends Activity {
	
	// Declare variables for game screen
	int uid;
	protected GameScreen game;
	protected GameEngine gameEngine;
	protected final Context context 					= this;
	protected DialogEnableSamples sampleDialog;
	
	// Declare constants for game screen
	protected static final String TAG 					= "logging process";
	protected static final int SCORE_ID 				= 1000;
	
	// Change wording to more specifically target each activity. 
	protected static final String HELP_MESSAGE			= "How to play: \n" 																				
															+ "In the options menu, select 'How to Play' to learn how to play this activity. \n \n"
															+ "Check Answer: \n"
															+ "To check whether your answers are correct, select 'Check Answer' either in the "
															+ "options menu or the button at the bottom of the screen. \n \n"
															+ "Summary Report: \n"
															+ "�Summary Report� in the Options menu shows " 
															+ "your start time, end time and score for the current game. \n \n"
															+ "Restart Game: \n" 
															+ "To restart the game, select 'Restart Game' from the options menu.";
	
	// Change wording to more specifically target each activity. 
	protected static final String HOW_TO_PLAY_MESSAGE	= "Players guess a hidden word by guessing individual letters. A correct guess reveals the letter's position in the word, while an incorrect guess brings you one step closer to being \"hanged\". The game is over when you guess all the letters in the word correctly, or you make 10 incorrect guesses.\n"+
"You can guess the letters by clicking on them at the top of the exercise, or by typing the chosen letter on your keyboard.\n"+
"Sometimes the word may already have correct letters guessed for you. You may also be given a hint that describes the hidden word. There will be a lightbulb below the word which you can click if this is the case.";											

	/*
	 * onCreate method
	 * 
	 * Load all data for the game. Such as collocations, 
	 * summary report, score etc. Display collocations
	 * game in which ever state the user last left it.
	 * i.e. new, incomplete, complete.
	 * 
	 * (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	protected void onCreate(Bundle savedInstanceState) {
		
		// Initialize game screen
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_screen);
		game = this;
		
		// Load game data based on the unique activity id
		uid = this.getIntent().getIntExtra("uniqueId", -1);	
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
	 * Update all aspects of the game when ever the game is paused.
	 * i.e. the game will pasue when the orientation is changed.
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
		
//		// If the game is complete, hide the 'Check Answer' menu item
//		if(GameItem.getActivityStatus().equals("complete")){
//			MenuItem checkAnswer = menu.findItem(R.id.check_answer);
//			checkAnswer.setVisible(false);
//		}
		
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
            	help.displayHelpDialog(HELP_MESSAGE);
                return true;
                
            // Menu Item -- how to play pressed ...
            case R.id.how_to_play:
            	
            	// Display How to Play Dialog
            	DialogHowToPlay d = new DialogHowToPlay(context);
            	d.displayHowToPlayDialog(HOW_TO_PLAY_MESSAGE);
            	return true;
            	
            // Menu Item -- check answer pressed ...
//            case R.id.check_answer:
//            	
//            	// Display Check Answer Dialog
//            	GameItem.setEndTime();
//            	gameEngine.checkAnswer();
//            	
//            	// Re-display score
//            	TextView tvScore = (TextView)findViewById(SCORE_ID);
//    			tvScore.setText("Score: " + GameItem.getScore() + " / " + GameItem.getPossibleScore());
//            	return true;
            	
            // Menu Item -- restart game pressed ...
            case R.id.restart_game:
            	
            	// Restart Collocation Dominoes Game
            	displayRestartGameDialog();
            	return true;
            
            // Menu Item -- summary report pressed ...
            case R.id.summary_report:
              	
              	// Display Summary Report Dialog
               	DialogSummaryReport s = new DialogSummaryReport(context);
               	s.displaySummaryReportDialog(GameItem.getScore(), GameItem.getPossibleScore(), 
            	GameItem.getStartTime(), GameItem.getEndTime(), GameItem.getAttempts());
               	return true;
               	
            // Menu Item -- summary report pressed ...
//            case R.id.enable_sample:
//                	
//              	// Display Sample Dialog
//            	sampleDialog = new DialogEnableSamples(context);
//        		sampleDialog.loadSampleSettings();
//            	sampleDialog.displaySampleDialog();
//               	return true;
            
            default:
                return super.onOptionsItemSelected(item);
        }
    } 
    
    /*
	 * displayRestartGameDialog method
	 * 
	 * Displays the restart game dialog
	 */
	public void displayRestartGameDialog(){
				
		// Instantiate alert dialog builder
		AlertDialog.Builder alertDialogBuilder 					= new AlertDialog.Builder(context);		
		
		// set dialog title
		alertDialogBuilder.setTitle("Restart Game");
		
		// set dialog content
		alertDialogBuilder.setMessage("Would you like to restart this game?")	
		
		// set dialog save button
		.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				// Restart game
				gameEngine.restartGame();
				dialog.cancel();
		}})
	
		// set dialog Done button
		.setNegativeButton("No",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				dialog.cancel();
		}});
		
		// create alert dialog and display it
		AlertDialog alertDialog 								= alertDialogBuilder.create();
		alertDialog.show();
	}
} // end of class
