package flax.hangman.view;

import static flax.utils.GlobalConstants.*;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.MenuItem;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import flax.activity.ExerciseTypeEnum;
import flax.database.DatabaseDaoHelper;
import flax.dialog.DialogHelp;
import flax.dialog.DialogHowToPlay;
import flax.dialog.DialogSummaryReport;
import flax.entity.base.BaseEntity;
import flax.entity.base.BaseExercise;
import flax.entity.exerciselist.ExerciseListItem;
import flax.entity.hangman.HangmanExercise;
import flax.entity.hangman.Word;
import flax.hangman.R;
import flax.hangman.view.GamePageFragment.OnPageEventListener;

public class PagerGameScreenActivity extends FragmentActivity implements OnPageChangeListener, OnPageEventListener {

	private static final String TAG = "GameScreen";
	
	private static final SimpleDateFormat dateFormatter = new SimpleDateFormat(SUMMARY_DATE_FORMAT, ENGLISH);
	
	/** Ormlite database helper, use getDBHelper method to get a instance */
	private DatabaseDaoHelper databaseHelper = null;
	private Dao<ExerciseListItem, String> exerciseListItemDao = null;
	private Dao<BaseEntity, String> exerciseDao = null;
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
	protected ExerciseListItem mExerciseListItem;
	protected HangmanExercise mExercise;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_screen_pager);

		/** Get exercise data */
		EXERCISE_TYPE = getExerciseType();

		/** Load exercise data from database */
		mExerciseListItem = loadExerciseListItem();
		mExercise = (HangmanExercise) loadExercise();
		
		/** Setup exercise */
		mExercise.setPossibleScore(calculatePossibleScore(mExercise));

		/** Set title */
		updateTitle();

		/** Setup pager */
		mPagerAdapter = new ListPagerAdapter<Word,String>(getSupportFragmentManager(), getPageItemList(), getWordDao());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mPagerAdapter);
		mViewPager.setOnPageChangeListener(this);
	}
	
	private int calculatePossibleScore(BaseExercise exercise){
		return ((HangmanExercise)exercise).getWords().size();
	}

	private void updateTitle() {
		setTitle("Score:" + mExercise.getScore() + "/" + mExercise.getPossibleScore());
	}

	private Collection<Word> getPageItemList() {
		return mExercise.getWords();
	}

	/**
	 * Load game data using ormlite
	 * 
	 * @return
	 */
	private ExerciseListItem loadExerciseListItem() {

		String exerciseId = this.getIntent().getStringExtra("exerciseId");
		try {
			return getExerciseListItemDao().queryForId(exerciseId);
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
	private BaseEntity loadExercise() {
		String exerciseId = this.getIntent().getStringExtra("exerciseId");
		try {
			return getExerciseDao().queryForId(exerciseId);
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
		switch (item.getItemId()) {
		// Help icon pressed ...
		case R.id.help:

			// Display Help Dialog
			DialogHelp help = new DialogHelp(this);
			help.displayHelpDialog(getHelpMessage());
			return true;

			// Menu Item -- how to play pressed ...
		case R.id.how_to_play:

			// Display How to Play Dialog
			DialogHowToPlay d = new DialogHowToPlay(this);
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
			//displayRestartGameDialog();
			return true;

			// Menu Item -- summary report pressed ...
		case R.id.summary_report:

			// Display Summary Report Dialog
			DialogSummaryReport s = new DialogSummaryReport(this);
			s.displaySummaryReportDialog(mExercise.getScore(), mExercise.getPossibleScore(), mExercise.getStartTime(), mExercise.getEndTime(), mExercise.getAttempts());
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

	protected Dao<ExerciseListItem, String> getExerciseListItemDao() throws SQLException {
		if (exerciseListItemDao == null) {
			exerciseListItemDao = getDBHelper().getDao(ExerciseListItem.class);
		}
		return exerciseListItemDao;
	}
	
	protected Dao<BaseEntity, String> getExerciseDao() throws SQLException {
		if (exerciseDao == null) {
			exerciseDao = getDBHelper().getDao(EXERCISE_TYPE.getRootEntityClass());
		}
		return exerciseDao;
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
		if (isWin) {
			mExercise.setScore(mExercise.getScore()+1);
			updateTitle();
		}

	}
	

	/**
	 * This method will be invoke when any page have interaction,
	 * In this case, button pressed.
	 * 
	 * Update Incomplete status for first interaction
	 */
	@Override
	public void onPageInteracted(Word itme) {
		// Update end time for summary
		String date = dateFormatter.format(new Date());
		mExercise.setEndTime(date);
		
		// Update start time for summary
		if (EXERCISE_NEW.equals(mExerciseListItem.getStatus())) {
			mExerciseListItem.setStatus(EXERCISE_INCOMPLETE);
			// Add Summary start time
			mExercise.setStartTime(date);
		}
		
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		
		// Do not save anything when no interaction (Still New)
		if (EXERCISE_NEW.equals(mExerciseListItem.getStatus())) {
			return;
		}
		
		// Save information
		try {
			if (mExercise.isComplete()) {
				mExerciseListItem.setStatus(EXERCISE_COMPLETE);
			}
			getExerciseListItemDao().update(mExerciseListItem);
			getExerciseDao().update(mExercise);
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
