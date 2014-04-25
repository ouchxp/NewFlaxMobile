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

import static flax.utils.GlobalConstants.*;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;

import android.view.Menu;
import android.view.MenuItem;
import flax.activity.ExerciseTypeEnum;
import flax.baseview.BaseGameScreenActivity;
import flax.baseview.ListPagerAdapter;
import flax.entity.base.BasePage;
import flax.entity.hangman.HangmanExerciseDetail;
import flax.entity.hangman.Word;
import flax.hangman.R;
import flax.hangman.fragment.GamePageFragment;
import flax.hangman.fragment.GamePageFragment.OnPageEventListener;

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
public class GameScreen extends BaseGameScreenActivity<HangmanExerciseDetail, Word> implements OnPageEventListener {

	@Override
	public String getHowToPlayMessage() {
		return getString(R.string.how_to_play_message);
	}

	@Override
	public String getHelpMessage() {
		return getString(R.string.default_game_screen_help_message);
	}

	@Override
	public ExerciseTypeEnum getExerciseType() {
		return ExerciseTypeEnum.HANGMAN;
	}

	@Override
	public void setUpListPagerAdapter() {
		try {
			mPagerAdapter = new ListPagerAdapter<GamePageFragment, Word>(getSupportFragmentManager(),
					getPageItemList(), getPageDao(), GamePageFragment.class);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void updateTitle() {
		setTitle("Score: " + mExerciseDetail.getScore() + "/" + mExerciseDetail.getPossibleScore());
	}

	@Override
	public int calculatePossibleScore() {
		return mExerciseDetail.getWords().size();
	}

	@Override
	public int calculateScore() {
		int score = 0;
		for (BasePage page : getPageItemList()) {
			if (PAGE_WIN == page.getPageStatus()) {
				score++;
			}
		}
		return score;
	}

	@Override
	public Collection<Word> getPageItemList() {
		return mExerciseDetail.getWords();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		boolean dispalyMenu = super.onCreateOptionsMenu(menu);
		
		// Hangman exercise doesn't need checkAnswer menu.
		MenuItem checkAnswerMenu = menu.findItem(R.id.check_answer);
		checkAnswerMenu.setVisible(false);

		return dispalyMenu;
	}

	/**
	 * This method will be invoke when any page have interaction, In this case,
	 * button pressed.
	 * 
	 * Update Incomplete status for first interaction
	 */
	@Override
	public void onPageInteracted(Word itme) {
		// Update end time for summary
		String date = DATE_FORMATTER.format(new Date());
		mExerciseDetail.setEndTime(date);

		// Update start time for summary
		if (EXERCISE_NEW.equals(mExercise.getStatus())) {
			mExercise.setStatus(EXERCISE_INCOMPLETE);
			// Add Summary start time
			mExerciseDetail.setStartTime(date);
		}
	}

	/**
	 * Update score after (one page) game win.
	 */
	@Override
	public void onPageWin(Word itme) {
		mExerciseDetail.setScore(calculateScore());
		updateTitle();
	}

} // end of class
