package flax.hangman.view;

import static flax.utils.GlobalConstants.*;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.Callable;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.viewpagerindicator.CirclePageIndicator;

import flax.activity.ExerciseTypeEnum;
import flax.database.DatabaseDaoHelper;
import flax.dialog.DialogHelper;
import flax.entity.base.BaseEntity;
import flax.entity.base.BaseExercise;
import flax.entity.base.BasePage;
import flax.entity.exerciselist.Exercise;
import flax.entity.hangman.HangmanExerciseDetail;
import flax.entity.hangman.Word;
import flax.hangman.R;
import flax.hangman.view.GamePageFragment.OnPageEventListener;

public class PagerGameScreenActivity extends FragmentActivity implements OnPageEventListener {

	private static final String TAG = "GameScreen";
	private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat(SUMMARY_DATE_FORMAT, ENGLISH);

	/** Ormlite database helper, use getDBHelper method to get a instance */
	private DatabaseDaoHelper mDaoHelper = null;
	private Dao<Exercise, String> mExerciseDao = null;
	private Dao<BaseEntity, String> mExerciseDetailDao = null;
	private Dao<Word, String> mWordDao = null;

	@SuppressWarnings("rawtypes")
	protected ListPagerAdapter mPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the page contents.
	 */
	protected ViewPager mViewPager;

	protected ExerciseTypeEnum EXERCISE_TYPE;
	/** This is the item which be used to show exercise list, it contains exercise status.*/
	protected Exercise mExercise;
	/** This is the actual exercise detail */
	protected HangmanExerciseDetail mExerciseDetail;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_screen_pager);

		/** Get exercise data */
		EXERCISE_TYPE = getExerciseType();

		/** Load exercise data from database */
		mExercise = loadExercise();
		mExerciseDetail = (HangmanExerciseDetail) loadExerciseDetail();

		/** Setup exercise */
		mExerciseDetail.setPossibleScore(calculatePossibleScore(mExerciseDetail));

		/** Set title */
		updateTitle();

		/** Setup PagerAdapter */
		setUpListPagerAdapter();

		/** Setup the ViewPager with the PagerAdapter. */
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mPagerAdapter);

		/** Setup page indicator */ 
		setUpPageIndicator();

	}

	public ExerciseTypeEnum getExerciseType() {
		return ExerciseTypeEnum.HANGMAN;
	}

	private void setUpPageIndicator() {
		CirclePageIndicator indicator = (CirclePageIndicator) findViewById(R.id.indicator);
		indicator.setViewPager(mViewPager);
	}

	private void setUpListPagerAdapter() {
		mPagerAdapter = new ListPagerAdapter<Word, GamePageFragment>(getSupportFragmentManager(),
				getPageItemList(), getWordDao(), GamePageFragment.class);
	}

	private void updateTitle() {
		setTitle("Score: " + mExerciseDetail.getScore() + "/" + mExerciseDetail.getPossibleScore());
	}

	private int calculatePossibleScore(BaseExercise exercise) {
		return ((HangmanExerciseDetail) exercise).getWords().size();
	}

	private int calculateScore() {
		int score = 0;
		for (BasePage page : getPageItemList()) {
			if (PAGE_WIN == page.getPageStatus()) {
				score++;
			}
		}
		return score;
	}

	public String getHowToPlayMessage() {
		return getString(R.string.how_to_play_message);
	}

	public String getHelpMessage() {
		return getString(R.string.game_screen_help_message);
	}

	public Collection<Word> getPageItemList() {
		return mExerciseDetail.getWords();
	}

	/**
	 * Load game data using ormlite
	 */
	private Exercise loadExercise() {

		String exerciseId = this.getIntent().getStringExtra(EXERCISE_ID);
		try {
			return getExerciseDao().queryForId(exerciseId);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Load game content data using ormlite
	 */
	private BaseEntity loadExerciseDetail() {
		String exerciseId = this.getIntent().getStringExtra(EXERCISE_ID);
		try {
			return getExerciseDetailDao().queryForId(exerciseId);
		} catch (SQLException e) {
			throw new RuntimeException(e);
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
		switch (item.getItemId()) {
		case R.id.help:
			// Display Help Dialog
			DialogHelper help = new DialogHelper(this);
			help.displayHelpDialog(getHelpMessage());
			return true;

		case R.id.how_to_play:
			// Display How to Play Dialog
			DialogHelper d = new DialogHelper(this);
			d.displayHowToPlayDialog(getHowToPlayMessage());
			return true;

			// Menu Item -- check answer pressed ...
		case R.id.check_answer:
			String date = DATE_FORMATTER.format(new Date());
			mExerciseDetail.setEndTime(date);
			GamePageFragment f = getCurrentFragment();
			f.checkAnswer();
			return true;

		case R.id.restart_game:
			// Restart Game
			DialogHelper dh = new DialogHelper(this);
			dh.displayRestartGameDialog(new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					restartGame();
				}
			});
			return true;

		case R.id.summary_report:
			// Display Summary Report Dialog
			DialogHelper s = new DialogHelper(this);
			s.displaySummaryReportDialog(mExerciseDetail.getScore(), mExerciseDetail.getPossibleScore(), mExerciseDetail.getStartTime(),
					mExerciseDetail.getEndTime(), mExerciseDetail.getAttempts());
			return true;

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

	private void restartGame() {
		try {
			mExercise.setStatus(EXERCISE_INCOMPLETE);
			getExerciseDao().update(mExercise);

			mExerciseDetail.resetExercise();
			getExerciseDetailDao().update(mExerciseDetail);

			getWordDao().callBatchTasks(new Callable<Void>() {
				@SuppressWarnings("unchecked")
				@Override
				public Void call() throws Exception {
					Collection<Word> words = getPageItemList();
					for (Word word : words) {
						word.resetPage();
						getWordDao().update(word);
					}

					// Update dataSet if necessary, and call
					// notifyDataSetChanged.
					mPagerAdapter.updateDataSet(words);
					return null;
				}
			});

			updateTitle();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
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
	 * Update score after (one page) game finished.
	 */
	@Override
	public void onPageFinished(Word itme, boolean isWin) {
		if (isWin) {
			mExerciseDetail.setScore(calculateScore());
			updateTitle();
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
			if (mExerciseDetail.isComplete()) {
				mExercise.setStatus(EXERCISE_COMPLETE);
			}
			getExerciseDao().update(mExercise);
			getExerciseDetailDao().update(mExerciseDetail);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		// Release DatabaseDaoHelper
		if (mDaoHelper != null) {
			OpenHelperManager.releaseHelper();
			mDaoHelper = null;
		}
	}
	
	protected GamePageFragment getCurrentFragment() {
		return (GamePageFragment) mPagerAdapter.getFragment(mViewPager.getCurrentItem());
	}

	protected Dao<Word, String> getWordDao() {
		if (mWordDao == null) {
			try {
				mWordDao = getDBHelper().getDao(Word.class);
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
		return mWordDao;
	}

	protected Dao<Exercise, String> getExerciseDao() throws SQLException {
		if (mExerciseDao == null) {
			mExerciseDao = getDBHelper().getDao(Exercise.class);
		}
		return mExerciseDao;
	}

	protected Dao<BaseEntity, String> getExerciseDetailDao() throws SQLException {
		if (mExerciseDetailDao == null) {
			mExerciseDetailDao = getDBHelper().getDao(EXERCISE_TYPE.getRootEntityClass());
		}
		return mExerciseDetailDao;
	}

	/**
	 * Generate DatabaseDaoHelper for database operation.
	 */
	protected DatabaseDaoHelper getDBHelper() {
		if (mDaoHelper == null) {
			mDaoHelper = OpenHelperManager.getHelper(this, DatabaseDaoHelper.class);
		}
		return mDaoHelper;
	}
}
