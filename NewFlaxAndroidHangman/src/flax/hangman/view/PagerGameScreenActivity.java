package flax.hangman.view;

import static flax.utils.GlobalConstants.*;

import java.sql.SQLException;
import java.util.Collection;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import flax.activity.ExerciseTypeEnum;
import flax.database.DatabaseDaoHelper;
import flax.database.DatabaseObjectHelper;
import flax.entity.base.BaseEntity;
import flax.entity.exercise.Exercise;
import flax.entity.hangman.HangmanExercise;
import flax.entity.hangman.Word;
import flax.hangman.R;
import flax.hangman.view.GamePageFragment.OnPageEventListener;

public class PagerGameScreenActivity extends FragmentActivity implements OnPageChangeListener, OnPageEventListener {

	private static final String TAG = "GameScreen";
	private static final String SCORE = "Score";
	/** Ormlite database helper, use getDBHelper method to get a instance */
	private DatabaseDaoHelper databaseHelper = null;
	private Dao<Exercise, String> exerciseDao = null;
	private Dao<BaseEntity, String> exerciseContentDao = null;
	private Dao<Word, String> wordDao = null;

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a {@link FragmentPagerAdapter}
	 * derivative, which will keep every loaded fragment in memory. If this
	 * becomes too memory intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	protected FragmentPagerAdapter mPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	protected ViewPager mViewPager;

	protected ExerciseTypeEnum EXERCISE_TYPE;
	protected Exercise mExercise;
	protected HangmanExercise mExerciseContent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_screen_pager);

		/** Get exercise data */
		EXERCISE_TYPE = getExerciseType();

		// Load exercise data from database
		mExercise = loadExercise();
		mExerciseContent = (HangmanExercise) loadExerciseContent();

		/** Set title */
		setTitle("Score:" + mExerciseContent.getIntExtra(SCORE) + "/" + mExerciseContent.getWords().size());

		/** Setup pager */
		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		mPagerAdapter = new ListPagerAdapter<Word,String>(getSupportFragmentManager(), getPageItemList(), getWordDao());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mPagerAdapter);
		mViewPager.setOnPageChangeListener(this);
	}

	private Collection<Word> getPageItemList() {
		return mExerciseContent.getWords();
	}

	/**
	 * Load game data using ormlite
	 * 
	 * @return
	 */
	private Exercise loadExercise() {

		String exerciseId = this.getIntent().getStringExtra("exerciseId");
		try {
			return getExerciseDao().queryForId(exerciseId);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Load game content data using ormlite
	 * 
	 * @return
	 */
	private BaseEntity loadExerciseContent() {
		String exerciseId = this.getIntent().getStringExtra("exerciseId");
		try {
			return getExerciseContentDao().queryForId(exerciseId);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game_screen, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		// if (id == R.id.action_settings) {
		// return true;
		// }
		return super.onOptionsItemSelected(item);
	}

	protected String getHowToPlayMessage() {
		return getString(R.string.how_to_play_message);
	}

	protected String getHelpMessage() {
		return getString(R.string.game_screen_help_message);
	}

	protected ExerciseTypeEnum getExerciseType() {
		return ExerciseTypeEnum.HANGMAN;
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

	protected Dao<Exercise, String> getExerciseDao() throws SQLException {
		if (exerciseDao == null) {
			exerciseDao = getDBHelper().getDao(Exercise.class);
		}
		return exerciseDao;
	}
	
	protected Dao<BaseEntity, String> getExerciseContentDao() throws SQLException {
		if (exerciseContentDao == null) {
			exerciseContentDao = getDBHelper().getDao(EXERCISE_TYPE.getRootEntityClass());
		}
		return exerciseContentDao;
	}
	
	protected Dao<Word, String> getWordDao() {
		if (wordDao == null) {
			try {
				wordDao = getDBHelper().getDao(Word.class);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return wordDao;
	}

	/**
	 * Update score after (one page) game finished.
	 */
	@Override
	public void onPageFinished(Word itme, boolean isWin) {
		int score = mExerciseContent.getIntExtra(SCORE) + 1;
		if (isWin) {
			mExerciseContent.putIntExtra(SCORE, score);
			setTitle("Score:" + score + "/" + mExerciseContent.getWords().size());
		}

	}
	

	/**
	 * Update Incomplete status for first interaction
	 */
	@Override
	public void onPageInteracted(Word itme) {
		if (EXERCISE_NEW.equals(mExercise.getStatus())) {
			mExercise.setStatus(EXERCISE_INCOMPLETE);
		}
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		
		// Do not save anything when no interaction (Still New)
		if (EXERCISE_NEW.equals(mExercise.getStatus())) {
			return;
		}
		
		// Save information
		try {
			int score = mExerciseContent.getIntExtra(SCORE);
			if (score == mExerciseContent.getWords().size()) {
				mExercise.setStatus(EXERCISE_DONE);
			}
			getExerciseDao().update(mExercise);
			getExerciseContentDao().update(mExerciseContent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onPageSelected(int position) {
	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
	}

	@Override
	public void onPageScrollStateChanged(int state) {
	}


}
