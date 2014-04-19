package flax.hangman.view;

import java.sql.SQLException;

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
import flax.entity.base.BaseEntity;
import flax.entity.exercise.Exercise;
import flax.entity.hangman.HangmanResponse;
import flax.entity.hangman.Word;
import flax.hangman.R;

public class PagerGameScreenActivity extends FragmentActivity implements OnPageChangeListener{
	
	private static final String TAG = "GameScreen";

	/** Ormlite database helper, use getDBHelper method to get a instance */
	private DatabaseDaoHelper databaseHelper = null;
	
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
	protected HangmanResponse mExerciseContent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_screen_pager);
		
		/** Get exercise data */
		EXERCISE_TYPE = getExerciseType();
		
		// Load exercise data from database
		mExercise = loadExercise();
		mExerciseContent = (HangmanResponse)loadExerciseContent();
		
		/** Set up pager */
		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		mPagerAdapter = new ListPagerAdapter<Word>(getSupportFragmentManager(),mExerciseContent.getWords());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mPagerAdapter);
		mViewPager.setOnPageChangeListener(this);
	}
	
	/**
	 * Load game data using ormlite
	 * @return
	 */
	private Exercise loadExercise(){
		
		String exerciseId = this.getIntent().getStringExtra("exerciseId");
		try {
			Dao<Exercise, String> execDao = getDBHelper().getDao(Exercise.class);
			return execDao.queryForId(exerciseId);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Load game content data using ormlite
	 * @return
	 */
	private BaseEntity loadExerciseContent(){
		String exerciseId = this.getIntent().getStringExtra("exerciseId");
		try {
			Dao<BaseEntity, String> dao = getDBHelper().getDao(EXERCISE_TYPE.getRootEntityClass());
			return dao.queryForId(exerciseId);
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
//		if (id == R.id.action_settings) {
//			return true;
//		}
		return super.onOptionsItemSelected(item);
	}




	
	protected String getHowToPlayMessage(){
		return getString(R.string.how_to_play_message);
	}

	protected String getHelpMessage(){
		return getString(R.string.game_screen_help_message);
	}
	
	protected ExerciseTypeEnum getExerciseType(){
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
	


	@Override
	public void onPageSelected(int position) {
		Log.i(TAG, "onPageSelected");
		this.setTitle("Page:" + (position + 1) + "/" + mExerciseContent.getWords().size());
	}

	
	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
	}
	
	@Override
	public void onPageScrollStateChanged(int state) {
	}
}
