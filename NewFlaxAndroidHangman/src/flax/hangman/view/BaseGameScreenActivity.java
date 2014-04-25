package flax.hangman.view;

import static flax.utils.GlobalConstants.*;
import java.sql.SQLException;
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
import flax.entity.base.BaseExerciseDetail;
import flax.entity.base.BasePage;
import flax.entity.exerciselist.Exercise;
import flax.hangman.R;

/**
 * 
 * @author Nan Wu
 *
 * @param <E extends BaseExerciseDetail> corresponding to the type 
 * @param <PAGE>
 */
public abstract class BaseGameScreenActivity<EXEC extends BaseExerciseDetail, PAGE extends BasePage> extends FragmentActivity{

	public static final String TAG = "GameScreen";

	/** Ormlite database helper, use getDBHelper method to get a instance */
	private DatabaseDaoHelper mDaoHelper = null;
	private Dao<Exercise, String> mExerciseDao = null;
	private Dao<EXEC, String> mExerciseDetailDao = null;
	private Dao<PAGE, String> mPageDao = null;

	@SuppressWarnings("rawtypes")
	public ListPagerAdapter mPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the page contents.
	 */
	public ViewPager mViewPager;

	public ExerciseTypeEnum EXERCISE_TYPE;
	/** This is the item which be used to show exercise list, it contains exercise status.*/
	public Exercise mExercise;
	/** This is the actual exercise detail */
	public EXEC mExerciseDetail;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_screen_pager);

		/** Get exercise data */
		EXERCISE_TYPE = getExerciseType();

		/** Load exercise data from database */
		mExercise = loadExercise();
		mExerciseDetail = loadExerciseDetail();

		/** Setup exercise */
		mExerciseDetail.setPossibleScore(calculatePossibleScore());

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

	public void setUpPageIndicator() {
		CirclePageIndicator indicator = (CirclePageIndicator) findViewById(R.id.indicator);
		indicator.setViewPager(mViewPager);
	}
	
	/** Methods that sub class have to implement */

	public abstract ExerciseTypeEnum getExerciseType();
	
	public abstract void setUpListPagerAdapter();

	public abstract void updateTitle();

	public abstract int calculatePossibleScore();

	public abstract int calculateScore();

	public abstract String getHowToPlayMessage();
	
	public abstract String getHelpMessage();

	public abstract Collection<PAGE> getPageItemList();

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
	private EXEC loadExerciseDetail() {
		String exerciseId = this.getIntent().getStringExtra(EXERCISE_ID);
		try {
			return (EXEC) getExerciseDetailDao().queryForId(exerciseId);
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
			getCurrentFragment().checkAnswer();
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

	public void restartGame() {
		try {
			mExercise.setStatus(EXERCISE_INCOMPLETE);
			getExerciseDao().update(mExercise);

			mExerciseDetail.resetExercise();
			getExerciseDetailDao().update(mExerciseDetail);

			getPageDao().callBatchTasks(new Callable<Void>() {
				@SuppressWarnings("unchecked")
				@Override
				public Void call() throws Exception {
					Collection<PAGE> pages = getPageItemList();
					for (PAGE page : pages) {
						page.resetPage();
						getPageDao().update(page);
					}

					// Update dataSet if necessary, and call
					// notifyDataSetChanged.
					mPagerAdapter.updateDataSet(pages);
					return null;
				}
			});

			updateTitle();
		} catch (Exception e) {
			throw new RuntimeException(e);
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
	
	@SuppressWarnings("unchecked")
	protected BasePageFragment<PAGE> getCurrentFragment() {
		return mPagerAdapter.getFragment(mViewPager.getCurrentItem());
	}

	protected Dao<PAGE, String> getPageDao() throws SQLException{
		if (mPageDao == null) {
			mPageDao = getDBHelper().getDao(EXERCISE_TYPE.getPageEntityClass());
		}
		return mPageDao;
	}

	protected Dao<Exercise, String> getExerciseDao() throws SQLException {
		if (mExerciseDao == null) {
			mExerciseDao = getDBHelper().getDao(Exercise.class);
		}
		return mExerciseDao;
	}

	protected Dao<EXEC, String> getExerciseDetailDao() throws SQLException {
		if (mExerciseDetailDao == null) {
			mExerciseDetailDao = getDBHelper().getDao(EXERCISE_TYPE.getExerciseEntityClass());
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
