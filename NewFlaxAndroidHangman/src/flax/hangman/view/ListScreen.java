/*
 * File: flax.collocationdominoes.view.ListScreen
 *
 * License: Greenstone 3 (GSDL3)
 * Copyright (C) 2003 New Zealand Digital Libraries, University Of Waikato
 * Greenstone3 comes with ABSOLUTELY NO WARRANTY; for details see LICENSE.txt
 * This is free software, and you are welcome to redistribute it
 * 
 * Reference: http://developer.android.com
 * 
 * Notes: This file is the List Screen Activity. This screen displays a list of all
 * existing activities. It is from this screen that the user can select which game they 
 * would like to play. 
 */
package flax.hangman.view;

import flax.activity.ExerciseTypeEnum;
import flax.hangman.R;


/**
 * ListScreen
 * @author ouchxp
 *
 */
public class ListScreen extends BaseListScreenActivity {

	@Override
	protected ExerciseTypeEnum getExerciseType() {
		return ExerciseTypeEnum.HANGMAN;
	}

	@Override
	protected String getHelpMessage() {
		return context.getString(R.string.home_screen_help_message);
	}
}