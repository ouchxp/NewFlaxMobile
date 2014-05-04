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
package flax.template.view;

import static flax.template.utils.LocalConstants.*;
import static flax.utils.GlobalConstants.*;

import java.util.ArrayList;
import java.util.Collection;

import android.view.Menu;
import android.view.MenuItem;
import flax.baseview.BaseGameScreenActivity;
import flax.baseview.GamePagerAdapter;
import flax.core.ExerciseType;
import flax.entity.base.BasePage;
import flax.template.R;
import flax.template.entity.TempleteExerciseDetail;
import flax.template.entity.TemplatePage;
import flax.template.fragment.GamePageFragment;

/**
 * GameScreen Class
 * 
 * This class is the Game Screen Activity, it handles the game display for the
 * activity.
 * 
 * Note: Areas of code that need modifying are highlighted with a todo tag.
 * 
 * @author Nan Wu
 */
//TODO: The game screen must extends BaseGameScreenActivity<EXEC, PAGE>
// EXEC is the exercise detail entity class which extends BaseExerciseDetail,
// and PAGE is the page entity class which extends BasePage.
public class GameScreen extends BaseGameScreenActivity<TempleteExerciseDetail, TemplatePage> {

	@Override
	public String getHowToPlayMessage() {
		//TODO: return how to play message string for game screen
		return getString(R.string.how_to_play_message);
	}

	@Override
	public String getHelpMessage() {
		//TODO: return help message string for game screen
		return getString(R.string.default_game_screen_help_message);
	}

	@Override
	public ExerciseType getExerciseType() {
		//TODO: return the exercise type constant.
		return HANGMAN;
	}

	@Override
	public void setUpListPagerAdapter() {
		//TODO: set up page adapter
		// The adapter type must be GamePagerAdapter<FRG, PAGE> or sub class of GamePagerAdapter
		// FRG is the page fragment class which extends BasePageFragment,
		// and PAGE is the page entity class which extends BasePage.
		// First argument of the constructor is fragment manager, 
		// Second argument is the page data list (call getPageItemList()),  
		// third argument is Dao for page class (call getPageDao())
		// last argument is the Fragment class same as FRG.
		mPagerAdapter = new GamePagerAdapter<GamePageFragment, TemplatePage>(getSupportFragmentManager(), getPageItemList(),
				getPageDao(), GamePageFragment.class);
	}

	@Override
	public int calculatePossibleScore() {
		//TODO: calculate possible score
		/** this is an example*/
		//return mExerciseDetail.getWords().size();
		return 1;
	}

	@Override
	public int calculateScore() {
		//TODO: calculate possible score
		/** this is an example*/
		/*
		int score = 0;
		for (BasePage page : getPageItemList()) {
			if (PAGE_WIN == page.getPageStatus()) {
				score++;
			}
		}
		return score;
		*/
		return 0;
	}

	@Override
	public Collection<TemplatePage> getPageItemList() {
		//TODO: return the page item list
		/** this is an example*/
		//return mExerciseDetail.getWords();
		return new ArrayList<TemplatePage>();
	}

	//
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		boolean dispalyMenu = super.onCreateOptionsMenu(menu);

		// Hangman exercise doesn't need checkAnswer menu.
		// So this menu can be disabled as below.
		//MenuItem checkAnswerMenu = menu.findItem(R.id.check_answer);
		//checkAnswerMenu.setVisible(false);

		return dispalyMenu;
	}
} // end of class
