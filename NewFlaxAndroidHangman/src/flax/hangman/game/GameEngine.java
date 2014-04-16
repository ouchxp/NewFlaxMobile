/*
 * File: flax.collocationdominoes.game.GameEngine
 *
 * License: Greenstone 3 (GSDL3)
 * Copyright (C) 2003 New Zealand Digital Libraries, University Of Waikato
 * Greenstone3 comes with ABSOLUTELY NO WARRANTY; for details see LICENSE.txt
 * This is free software, and you are welcome to redistribute it
 * 
 * Reference: http://developer.android.com
 * 
 * Notes: This file holds all of the methods for playing the activity.
 * It holds the method to load data, display the game, check the answer, 
 * drag and drop on the screen and update the data in the internal database.
 */
package flax.hangman.game;

import java.util.ArrayList;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Color;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnClickListener;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import flax.activity.ActivityItem;
import flax.collocation.CollocationDatabaseManager;
import flax.collocation.CollocationItem;
import flax.dialog.DialogCheckAnswer;
import flax.hangman.R;
import flax.hangman.view.GameScreen;

/**
 * GameEngine Class
 * 
 * This class holds all of the methods for playing the activity.
 * It holds the method to load data, display the game, check the answer, 
 * drag and drop on the screen and update the data in the internal database.
 * 
 * Note: Areas of code that need modifying are highlighted with a TODO tag.
 * 
 * @author Jemma Konig
 */
public class GameEngine {
	
	// Declare variables for the game engine
	protected int uid;
	protected int index;
	protected double rowCount;
	protected int steps;
	
	// Declare classes used for the game engine
	protected Menu menu;
	protected GameScreen game;
	protected Context context;
	protected ActivityItem activity;
	protected GameEngine gameEngine;
	protected TableLayout answerDisplay;
	protected static final int[] HANGMAN_PICS = {R.drawable.hang0,R.drawable.hang1,R.drawable.hang2,R.drawable.hang3,R.drawable.hang4,
		R.drawable.hang5,R.drawable.hang6,R.drawable.hang7,R.drawable.hang8,R.drawable.hang9,R.drawable.hang10};
	// Declare log tag for the game engine
	protected static final String TAG					= "logging game";
		
	// Declare constants for the game engine
	protected static final int START_TIME				= 1;
	protected static final int END_TIME					= 2;
	protected static final int ATTEMPTS					= 3;
	protected static final int SCORE					= 4;

	protected static final int SCORE_ID 				= 1000;
	
	// TODO Change to the name of the current activity. Should match the activity specified in the xml retrieved from the server.
	protected static final String ACT_TYPE				= "Hangman";					
		
	protected static final String ACT_STATUS_NEW		= "new";
	protected static final String ACT_STATUS_INCOMPLETE	= "incomplete";
	protected static final String ACT_STATUS_COMPLETE	= "complete";
	protected static final String ACT_STATUS_REMOVED	= "removed";
	
	protected static final String TITLE_CORRECT_FULL	= "Well done!";
	protected static final String TITLE_INCORRECT		= "Try again";
	protected static final String BODY_CORRECT_FULL		= "Your answer is correct";
	protected static final String BODY_INCORRECT		= "Your answer is not correct";
	
	// Declare lists used by the game engine
	protected ArrayList<String> summary 				= new ArrayList<String>();
	
	// TODO This array list is used for activities involving collocations. It will not be needed for activities that do not use collocations.
	protected ArrayList<CollocationItem> collocations 	= new ArrayList<CollocationItem>();

	/* Class constructor */
	public GameEngine(Context c, GameScreen g){
		context 		= c;
		game 			= g;
	}
	
	/* setMenu method */
	public void setMenu(Menu m){
		menu = m;
	}
	
	/*
	 * showText method
	 * 
	 * Changes the color of the text in a textView to black and sets the 
	 * onTouchListener so that the textView is clickable again
	 */
	public void showText(TextView view){
		view.setTextColor(Color.BLACK);
		view.setOnTouchListener(new ActivityTouchListener());
	}
	
	/*
	 * hideText method
	 * 
	 * Changes the color of the text in a textView to grey and disables the 
	 * onTouchListener so that the textView is no longer clickable
	 */
	public void hideText(TextView view){
		view.setTextColor(Color.GRAY);
		view.setOnTouchListener(null);
	}
	
	/*
	 * restartGame method
	 * 
	 * Clears any user added data from the game and
	 * restarts it with original activity data.
	 * 
	 * TODO: Will need to reset any further data that is added for
	 * each specific activity.
	 */
	public void restartGame(){
		
		// Reset the GameItem data
		GameItem.setActivityStatus(ACT_STATUS_NEW);
		GameItem.setStartTime("");
		GameItem.setEndTime("");
		GameItem.setAttempts(0);
		GameItem.setScore(0);
		
		for(CollocationItem c : collocations){
			c.status="";
			c.statusFinal="";
		}
		for (WordItem w:GameItem.collocationWords) {
			w.status="";
			w.statusFinal="";
		}
		
		
		// Update the internal database
		updateDatabase();
		
		// Reload the game data from the database
		loadGameData(GameItem.activityId);
		
		// Redisplay the game now that it has been restarted
		game.setContentView(R.layout.game_screen);
		displayGameData();
	}
	
	/*
	 * loadGameData method
	 * 
	 * This method loads the game data from the internal database
	 * and stores it in the GameItem object to be used during game play
	 * 
	 * TODO: If there is additional data that needs to be loaded from the
	 * internal database, add loading instructions here.
	 * 
	 * @param uid, the unique id of the game.
	 */
	public void loadGameData(String uid){
		
		// get activity and corresponding collocations from internal database
		CollocationDatabaseManager dbManagerCollo 
						= new CollocationDatabaseManager(context);
		activity 		= dbManagerCollo.selectActivity(uid);
		collocations 	= dbManagerCollo.selectGivenCollocations(uid);
		
		// Save activity details
		GameItem.setActivityId(String.valueOf(activity.uniqueId));
		GameItem.setGameTitle(activity.activityName);
		GameItem.setActivityStatus(activity.activityStatus);
		if(GameItem.getActivityStatus().equals(ACT_STATUS_NEW)){
			GameItem.setActivityStatus(ACT_STATUS_INCOMPLETE);
		}
		
		// Set summary report details from summary table in db
		summary 		= dbManagerCollo.selectSummary(uid);
		GameItem.setAttempts(Integer.parseInt(summary.get(ATTEMPTS)));
		GameItem.setScore(Integer.parseInt(summary.get(SCORE)));
		GameItem.setStartTime(summary.get(START_TIME));
		GameItem.setEndTime(summary.get(END_TIME));
		GameItem.setPossibleScore(collocations.size());
		
		// If the game hasn't been started before (i.e. is new), set start time.
		if(GameItem.getStartTime().equals("")){
			GameItem.setStartTime();
		}

		// TODO: This section is only needed if the activity uses collocations
		
		// Clear any existing items from the collocationWords list
		GameItem.clearCollocationWords();
		GameItem.attempts =0;
		// Save collocations
		for(CollocationItem c : collocations){
			String middle 	= "";
			String tmp 		= c.word;
			String[] arr 	= tmp.split(" ");
			for(int t = 0; t < arr.length; t++){
				if(t != 0 && t != arr.length -1){
					middle 	+= " " + arr[t];
				}
			}
			middle 			+= " ";
			WordItem word	 = new WordItem(arr[0], middle, arr[arr.length - 1], c.status, 
								c.statusFinal, c.uniqueId);
			GameItem.addCollocationWord(word);
		}

	}
	
	/*
	 * displayGameData method
	 * 
	 * 
	 * TODO: displayGameData only displays the activity details, user score and 'Check Answer' button.
	 * Code will need to be added to display the specific activity.
	 */
	public void displayGameData(){
		
		WordItem wordItem = GameItem.getCollocationWords().get(GameItem.pageNumber);
		TextView actText = (TextView) game.findViewById(R.id.activity_template_text);
		if (wordItem.getStatusFinal().equals("done")) {
			wordItem.setMiddle(wordItem.getFirst().replaceAll("[a-z]", "$0 "));
			
			setAllBtnEnable((LinearLayout)game.findViewById(R.id.atoi),false);
			setAllBtnEnable((LinearLayout)game.findViewById(R.id.jtor),false);
			setAllBtnEnable((LinearLayout)game.findViewById(R.id.stoz),false);
		}else {
			
			wordItem.setMiddle(wordItem.getFirst().replaceAll("[a-z]", "_ "));
			setAllBtnEnable((LinearLayout)game.findViewById(R.id.atoi),true);
			setAllBtnEnable((LinearLayout)game.findViewById(R.id.jtor),true);
			setAllBtnEnable((LinearLayout)game.findViewById(R.id.stoz),true);
		}
		actText.setText(wordItem.getMiddle());
//		actText.setText("Activity Name: " + GameItem.getGameTitle() + "\n" +
//						"Activity Status: " + GameItem.getActivityStatus() + "\n" +
//						"Game Attempts: " + GameItem.getAttempts() + "\n" +
//						"Game Score: " + GameItem.getScore() + "\n");
		
		// Display the score and check answer button in a table layout so that they scale well with differing screen widths.
    	rowCount = 1;
    	answerDisplay = (TableLayout) game.findViewById(R.id.answer_table);

    	index = 0;
    	TextView tvscore;
    	if((tvscore = (TextView)game.findViewById(SCORE_ID)) == null){
        for (int i = 0; i < rowCount; i++) {
            TableRow tableRow = new TableRow(context);
		    TableLayout.LayoutParams TRParams = new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		    tableRow.setLayoutParams(TRParams);
		    
		    // Display the text view that holds the users score
			TextView textViewScore = new TextView(context);
			textViewScore.setPadding(8, 3, 8, 3);
			textViewScore.setGravity(Gravity.LEFT);
			textViewScore.setTextColor(R.drawable.style_text_selector);
			textViewScore.setText("Score: " + GameItem.getScore() + " / " + GameItem.getPossibleScore() + "\nPage: " + (GameItem.pageNumber +1) + " of "+GameItem.getCollocationWords().size());
			textViewScore.setLayoutParams(new TableRow.LayoutParams(0, LayoutParams.WRAP_CONTENT));
			textViewScore.setId(SCORE_ID);
			tableRow.addView(textViewScore);

//			// Display the 'Check Answer' button
//			TextView buttonCheckAns = new TextView(context);
//			buttonCheckAns.setId(ANS_BTN_ID);
//			buttonCheckAns.setPadding(8, 3, 8, 3);
//			buttonCheckAns.setGravity(Gravity.CENTER);
//			buttonCheckAns.setTextColor(R.drawable.style_text_selector);
//			buttonCheckAns.setBackgroundResource(R.drawable.home_style_button_selector);
//			buttonCheckAns.setText("Check Answer");
//			buttonCheckAns.setLayoutParams(new TableRow.LayoutParams(0, LayoutParams.WRAP_CONTENT));
//			buttonCheckAns.setClickable(!GameItem.getActivityStatus().equals(ACT_STATUS_COMPLETE));
//			buttonCheckAns.setOnClickListener(new View.OnClickListener() {
//	            @Override
//	            public void onClick(View v) {
//	            	GameItem.setEndTime();
//	            	checkAnswer();
//	            	TextView tvScore = (TextView)game.findViewById(SCORE_ID);
//	    			tvScore.setText("Score: " + GameItem.getScore() + " / " + GameItem.getPossibleScore());
//	            }
//	        });	
//			tableRow.addView(buttonCheckAns);
			
            answerDisplay.addView(tableRow);
        } 
    	}else{
    		tvscore.setText("Score: " + GameItem.getScore() + " / " + GameItem.getPossibleScore() + "\nPage: " + (GameItem.pageNumber +1) + " of "+GameItem.getCollocationWords().size());
    	}
    	
		
		ImageView hangman = (ImageView)game.findViewById(R.id.hangman);
		if (GameItem.getCollocationWords().get(GameItem.pageNumber).statusFinal.equals("done")) {
			hangman.setImageDrawable(game.getResources().getDrawable(R.drawable.face_smile));
		}else {
			steps =0;
			hangman.setImageDrawable(game.getResources().getDrawable(HANGMAN_PICS[0]));
		}
	}
	
	public void onLetterClick(View view) {
		
		Button btn =(Button)view;
		WordItem wordItem = GameItem.getCollocationWords().get(GameItem.pageNumber);
		String word = wordItem.getFirst().toLowerCase();
		String mask = wordItem.getMiddle().toLowerCase();
		TextView actText = (TextView) game.findViewById(R.id.activity_template_text);
		char c = btn.getText().toString().toLowerCase().charAt(0);
		
		int index =-1;
		while ((index = word.indexOf(c,index+1)) !=-1) {
			mask = mask.substring(0,2*index) + c + " " + mask.substring(2*index+2,mask.length());
		}
		if(mask.equals(wordItem.getMiddle())){
			steps++;
		}
		wordItem.setMiddle(mask);
		actText.setText(mask);
		btn.setEnabled(false);
		
		/**pass*/
		if(mask.replaceAll(" ", "").equals(word)){
			GameItem.setEndTime();
			checkAnswer();
		}
		ImageView hangman = (ImageView)game.findViewById(R.id.hangman);
		hangman.setImageDrawable(game.getResources().getDrawable(HANGMAN_PICS[steps]));
		
		/**fail*/
		if (steps == HANGMAN_PICS.length-1) {
			GameItem.setEndTime();
			GameItem.attempts++;
			Toast.makeText(game, TITLE_INCORRECT, Toast.LENGTH_SHORT).show();
			steps = 0;
			
			setAllBtnEnable((LinearLayout)game.findViewById(R.id.atoi),false);
			setAllBtnEnable((LinearLayout)game.findViewById(R.id.jtor),false);
			setAllBtnEnable((LinearLayout)game.findViewById(R.id.stoz),false);
			
			hangman.postDelayed(new Runnable(){  
				public void run(){  
					ImageView hangman = (ImageView)game.findViewById(R.id.hangman);
					hangman.setImageDrawable(game.getResources().getDrawable(R.drawable.face_worried));
				}  
			}, 300);

			hangman.postDelayed(new Runnable(){  
				public void run(){  
					displayGameData();
				}  
			}, 2000);
		}
	}
	
	public void onClickLeftArrow(View view) {
		//Button btn =(Button)view;
		GameItem.pageNumber--;
		if(GameItem.pageNumber<0){
			GameItem.pageNumber = GameItem.getCollocationWords().size()-1;
		}
		displayGameData();
	}
	
	public void onClickRightArrow(View view) {
		//Button btn =(Button)view;
		GameItem.pageNumber++;
		if(GameItem.pageNumber>=GameItem.getCollocationWords().size()){
			GameItem.pageNumber = 0;
		}
		
		displayGameData();
		//Log.i("Click",  btn.getText().toString());
	}
	
	/*
	 * checkAnswer method
	 * 
	 * TODO: Create a method that checks the answer for the activity. 
	 * This method is called when the user selects the 'Check Answer' 
	 * button at the bottom of the screen or when they select 
	 * 'Check Answer' from the options menu.
	 */
	public void checkAnswer(){
		setAllBtnEnable((LinearLayout)game.findViewById(R.id.atoi),false);
		setAllBtnEnable((LinearLayout)game.findViewById(R.id.jtor),false);
		setAllBtnEnable((LinearLayout)game.findViewById(R.id.stoz),false);

		
		WordItem wordItem = GameItem.getCollocationWords().get(GameItem.pageNumber);
		wordItem.status = "full";
		wordItem.statusFinal = "done";
		for (CollocationItem c :collocations) {
			if (c.word.equals(wordItem.first)) {
				c.status= "full";
				c.statusFinal = "done";
			}
		}
		GameItem.score++;
		Toast.makeText(game, TITLE_CORRECT_FULL, Toast.LENGTH_SHORT).show();
		ImageView hangman = (ImageView)game.findViewById(R.id.hangman);
		hangman.postDelayed(new Runnable(){  
			public void run(){  
				ImageView hangman = (ImageView)game.findViewById(R.id.hangman);
				hangman.setImageDrawable(game.getResources().getDrawable(R.drawable.face_smile));
			}  
		}, 300);
		
		if(GameItem.getScore() >= GameItem.possibleScore){
			GameItem.score = GameItem.possibleScore;
			GameItem.setActivityStatus(ACT_STATUS_COMPLETE);
//			MenuItem checkAnswer = menu.findItem(R.id.check_answer);
//			checkAnswer.setVisible(false);
        	DialogCheckAnswer c = new DialogCheckAnswer(context);
        	c.displayCheckAnswerDialog(TITLE_CORRECT_FULL, BODY_CORRECT_FULL);

		}else {
			GameItem.pageNumber++;
		}
		
		
		hangman.postDelayed(new Runnable(){  
			public void run(){  
				displayGameData();
			}  
		}, 2000);
		
//		else {
//        	DialogCheckAnswer c = new DialogCheckAnswer(context);
//        	c.displayCheckAnswerDialog(TITLE_INCORRECT, BODY_INCORRECT);
//		}
	}
	
	private void setAllBtnEnable(ViewGroup layout,boolean enable){
		for (int i = 0; i < layout.getChildCount(); i++) {
			layout.getChildAt(i).setEnabled(enable);
		}
	}
	
	/*
	 * updateDatabase method
	 * 
	 * This method updates all data for an individual game in
	 * the internal database and only keeps the last ten completed
	 * activities. 
	 * 
	 * (Note: Collocations related to old activities are deleted from
	 * the database but the general activity data is kept to compare to
	 * new activities. Activity status is set to 'remove' so that it isn't
	 * included in the List Screen when displaying current activities.)
	 * 
	 * TODO: Any additional data needs to be updated in the database.
	 */
	public void updateDatabase(){
		
		// Instantiate database manager
		CollocationDatabaseManager dbManagerCollo = new CollocationDatabaseManager(context);
		
		// Update summary report details in database
		dbManagerCollo.updateSummary(GameItem.getStartTime(), GameItem.getEndTime(),
				GameItem.getAttempts(), GameItem.getScore(), GameItem.getActivityId());
		
		// Update activity status in database
		dbManagerCollo.updateActivity(GameItem.getActivityStatus(), GameItem.getActivityId());
		
		
		// Update collocations in database
		for(int i = 0; i < GameItem.getCollocationWords().size(); i++){
			String status = GameItem.getCollocationWords().get(i).getStatus();
			String statusFinal = GameItem.getCollocationWords().get(i).getStatusFinal();
			int id = GameItem.getCollocationWords().get(i).getId();
			String userWord = GameItem.getCollocationWords().get(i).getFirst() + 
								GameItem.getCollocationWords().get(i).getMiddle() +
								GameItem.getCollocationWords().get(i).getLast();
			dbManagerCollo.updateCollocation(id, status, statusFinal, userWord);
		}
		
    	// Only keep the ten most recently completed activities 
		if(GameItem.activityStatus.equals(ACT_STATUS_COMPLETE)){
			ArrayList<ActivityItem> completeActivities = dbManagerCollo.selectCompleteActivities(ACT_TYPE);
			if(completeActivities.size() > 10){
				for(int i = 0; i < completeActivities.size() - 10; i++){
					int uid = completeActivities.get(i).getUniqueId();
					
					// Change activity status to 'removed' in db
					dbManagerCollo.updateActivity(ACT_STATUS_REMOVED, String.valueOf(uid));

					// Remove collocations from db
					dbManagerCollo.deleteCollocation(uid);
				}
			}
		}	
	}
	
	/**
	 * ActivityOnClick Class
	 * 
	 * This class is used to handle on-click events
     * Note: If using this method, remove @SuppressWarnings("unused")
	 */
    @SuppressWarnings("unused")
	private class ActivityOnClick implements OnClickListener {

    	/*
    	 * onClick method
    	 * 
    	 * TODO: Add any on-click events here.
    	 * @see android.view.View.OnClickListener#onClick(android.view.View)
    	 */
		@Override
		public void onClick(View arg0) {
			// on-click events ... 
		}		
    } // end of onClick class

    /**
     * ActivityTouchListener  class
     * 
     * This class will handle touch events on draggable textviews
     */
    final class ActivityTouchListener implements OnTouchListener {
    	
    	/*
    	 * onTouch method
    	 * 
    	 * This method shows the text as it's being dragged
    	 * 
    	 * (non-Javadoc)
    	 * @see android.view.View.OnTouchListener#onTouch(android.view.View, android.view.MotionEvent)
    	 */
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                ClipData data = ClipData.newPlainText("", "");
                DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                view.startDrag(data, shadowBuilder, view, 0);
                return true;
            } else {
                return false;
            }
        }
    }  // end of onTouch class

    /**
     * ActivityDragListener class
     * 
     * This class will handle the textViews being dragged and dropped.
     * Note: If using this method, remove @SuppressWarnings("unused")
     */
    @SuppressWarnings("unused")
	private class ActivityDragListener implements OnDragListener {

    	/*
    	 * OnDrag method 
    	 * 
    	 * TODO: Add any drag and drop events needed.
    	 * @see android.view.View.OnDragListener#onDrag(android.view.View, android.view.DragEvent)
    	 */
        @Override
        public boolean onDrag(View v, DragEvent event) {
            switch (event.getAction()) {
            
            // CASE: Drag started -- No action
            case DragEvent.ACTION_DRAG_STARTED:
                break;
                
            // CASE: Drag entered -- No action
            case DragEvent.ACTION_DRAG_ENTERED:
                break;
                
            // CASE: Drag exited -- No action
            case DragEvent.ACTION_DRAG_EXITED:        
            	break;
            		
            // CASE: Drag dropped -- No action
            case DragEvent.ACTION_DROP:
            	break;
            
            // CASE: Drag started -- No action
            case DragEvent.ACTION_DRAG_ENDED:
                break;
                
            // CASE: Default -- No action
            default:
                break;
            }
            return true;
        }
    }
} // end of class