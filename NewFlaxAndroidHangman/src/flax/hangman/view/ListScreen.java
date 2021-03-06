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

import static flax.hangman.utils.LocalConstants.*;
import flax.baseview.BaseListScreenActivity;
import flax.core.ExerciseType;

/**
 * ListScreen
 * 
 * @author Nan Wu
 * 
 */
public class ListScreen extends BaseListScreenActivity {

	public ExerciseType getExerciseType() {
		return HANGMAN;
	}

	/**
	 * Return the class of next activity for building Intent.
	 */
	public Class<?> getNextActivityClass() {
		return GameScreen.class;
	}
}