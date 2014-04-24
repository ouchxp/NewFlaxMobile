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

import flax.activity.ExerciseTypeEnum;
import flax.hangman.R;




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
public class GameScreen extends BaseGameScreenActivity {

	@Override
	public String getHowToPlayMessage() {
		return getString(R.string.how_to_play_message);
	}

	@Override
	public String getHelpMessage() {
		return getString(R.string.game_screen_help_message);
	}

	@Override
	public ExerciseTypeEnum getExerciseType() {
		return ExerciseTypeEnum.HANGMAN;
	}
	
	
} // end of class
