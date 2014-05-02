package flax.baseview;

import static flax.utils.GlobalConstants.*;

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

import flax.baseview.BasePageFragment.OnPageAnswerCheckedListener;
import flax.core.ExerciseType;
import flax.database.DatabaseDaoHelper;
import flax.database.FlaxDao;
import flax.dialog.DialogHelper;
import flax.entity.base.BaseExerciseDetail;
import flax.entity.base.BasePage;
import flax.entity.exerciselist.Exercise;
import flax.library.R;

/**
 * 
 * @author Nan Wu
 * 
 * @param <EXEC>
 *            corresponding to exercise detail class
 *            (ExerciseTypeEnum.exerciseEntityClass)
 * @param <PAGE>
 *            corresponding to game page class
 *            (ExerciseTypeEnum.pageEntityClass)
 */
public abstract class BaseGameScreenActivity<EXEC extends BaseExerciseDetail, PAGE extends BasePage> extends
		FragmentActivity implements OnPageAnswerCheckedListener<PAGE> {

	public static final String TAG = "GameScreen";

	/** Ormlite database helper, use getDBHelper method to get a instance */
	private DatabaseDaoHelper mDaoHelper = null;
	private FlaxDao<Exercise, String> mExerciseDao = null;
	private FlaxDao<EXEC, String> mExerciseDetailDao = null;
	private FlaxDao<PAGE, String> mPageDao = null;

	@SuppressWarnings("rawtypes")
	protected GamePagerAdapter mPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the page contents.
	 */
	protected ViewPager mViewPager;

	protected final ExerciseType EXERCISE_TYPE = getExerciseType();
	/**
	 * This is the item which be used to show exercise list, it contains
	 * exercise status.
	 */
	protected Exercise mExercise;
	/** This is the actual exercise detail */
	protected EXEC mExerciseDetail;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_screen);

		/** Load exercise data from database */
		mExercise = loadExercise();
		mExerciseDetail = loadExerciseDetail();

		/** Setup exercise */
		setUpExercise();

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

	public void updateTitle() {
		setTitle(mExercise.getName());
	}

	public void setUpExercise() {
		// Set possible score for exercise.
		mExerciseDetail.setPossibleScore(calculatePossibleScore());

		// Set start time for summary.
		if (mExerciseDetail.getStartTime() == null || mExercise.getStatus() == EXERCISE_NEW) {
			String date = DATE_FORMATTER.format(new Date());
			mExerciseDetail.setStartTime(date);
		}
	}

	public void setUpPageIndicator() {
		CirclePageIndicator indicator = (CirclePageIndicator) findViewById(R.id.indicator);
		indicator.setViewPager(mViewPager);
	}

	/** Methods that sub class have to implement */

	public abstract ExerciseType getExerciseType();

	public abstract void setUpListPagerAdapter();

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
		return getExerciseDao().queryForId(exerciseId);
	}

	/**
	 * Load game content data using ormlite
	 */
	private EXEC loadExerciseDetail() {
		String exerciseId = this.getIntent().getStringExtra(EXERCISE_ID);
		return getExerciseDetailDao().queryForId(exerciseId);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game_screen, menu);
		return true;
	}

	/**
	 * Display help, how to play or summary report dialog depending on which
	 * menu item has been selected
	 * 
	 * @see http://tools.android.com/tips/non-constant-fields
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		if (itemId == R.id.help) {
			// Display Help Dialog
			DialogHelper help = new DialogHelper(this);
			help.displayHelpDialog(getHelpMessage());
			return true;
		} else if (itemId == R.id.how_to_play) {
			// Display How to Play Dialog
			DialogHelper d = new DialogHelper(this);
			d.displayHowToPlayDialog(getHowToPlayMessage());
			return true;
		} else if (itemId == R.id.check_answer) {
			getCurrentFragment().checkAnswer();
			return true;
		} else if (itemId == R.id.restart_game) {
			// Restart Game
			DialogHelper dh = new DialogHelper(this);
			dh.displayRestartGameDialog(new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					restartGame();
				}
			});
			return true;
		} else if (itemId == R.id.summary_report) {
			// Display Summary Report Dialog
			DialogHelper s = new DialogHelper(this);
			s.displaySummaryReportDialog(mExerciseDetail.getScore(), mExerciseDetail.getPossibleScore(),
					mExerciseDetail.getStartTime(), mExerciseDetail.getEndTime(), mExerciseDetail.getAttempts());
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}

	public void restartGame() {

		// Reset exercise status
		mExercise.setStatus(EXERCISE_NEW);
		getExerciseDao().update(mExercise);

		// Reset exercise score start time etc.
		mExerciseDetail.resetExercise();
		getExerciseDetailDao().update(mExerciseDetail);

		// Reset page information
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

		// Set up exercise again.
		setUpExercise();
	}

	/**
	 * This method will be invoke when after any page's checkAnswer method have
	 * been called. Update information for status change.
	 */
	@Override
	public void onPageAnswerChecked(PAGE item) {
		// Update start time for summary if this exercise is a new exercise.
		if (mExercise.getStatus() == EXERCISE_NEW) {
			mExercise.setStatus(EXERCISE_INCOMPLETE);
		}

		// Update end time for summary
		String date = DATE_FORMATTER.format(new Date());
		mExerciseDetail.setEndTime(date);

		// Update score after (one page) game win.
		if (item.getPageStatus() == PAGE_WIN) {
			mExerciseDetail.setScore(calculateScore());
			// Update exercise status to complete if exercise is done.
			if (mExerciseDetail.isComplete()) {
				mExercise.setStatus(EXERCISE_COMPLETE);
			}
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		// Save information
		getExerciseDao().update(mExercise);
		getExerciseDetailDao().update(mExerciseDetail);
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

	protected FlaxDao<Exercise, String> getExerciseDao() {
		if (mExerciseDao == null) {
			mExerciseDao = getDaoHelper().getFlaxDao(Exercise.class);
		}
		return mExerciseDao;
	}

	protected FlaxDao<PAGE, String> getPageDao() {
		if (mPageDao == null) {
			mPageDao = getDaoHelper().getFlaxDao(EXERCISE_TYPE.getPageEntityClass());
		}
		return mPageDao;
	}

	protected FlaxDao<EXEC, String> getExerciseDetailDao() {
		if (mExerciseDetailDao == null) {
			mExerciseDetailDao = getDaoHelper().getFlaxDao(EXERCISE_TYPE.getExerciseEntityClass());
		}
		return mExerciseDetailDao;
	}

	/**
	 * Generate DatabaseDaoHelper for database operation.
	 */
	protected DatabaseDaoHelper getDaoHelper() {
		if (mDaoHelper == null) {
			mDaoHelper = OpenHelperManager.getHelper(this, DatabaseDaoHelper.class);
		}
		return mDaoHelper;
	}
}
