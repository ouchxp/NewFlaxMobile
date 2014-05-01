package flax.asynctask;

import static flax.utils.GlobalConstants.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.util.Log;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import flax.core.ExerciseType;
import flax.database.DatabaseDaoHelper;
import flax.database.DatabaseNestedObjectHelper;
import flax.database.FlaxDao;
import flax.entity.base.BaseEntity;
import flax.entity.exerciselist.Category;
import flax.entity.exerciselist.Exercise;
import flax.entity.exerciselist.ExerciseListResponse;
import flax.utils.SpHelper;
import flax.utils.XmlParser;

/**
 * DefaultExercisesLoader Class load default exercise from asset folder.
 * 
 * @author Nan Wu
 */
public class DefaultExercisesLoader extends AsyncTask<String, Void, Void> {
	public static final String TAG = "DefaultExerciseLoader";
	private Context mContext;
	private ExerciseType mExerciseType;
	private AssetManager mAssetManager;

	public DefaultExercisesLoader(Context context, ExerciseType exerciseType) {
		this.mContext = context;
		this.mExerciseType = exerciseType;
		this.mAssetManager = mContext.getAssets();
	}

	/**
	 * doInBackground method Async task that connects to the server and
	 * Retrieves the required xml file using the NetworkHttpRequest class
	 */
	@Override
	protected Void doInBackground(final String... exerciseListfiles) {
		boolean isFirstTime = isFirstTime();
		if (!isFirstTime)
			return null;

		Log.i(TAG, "Loading default exercises");
		final DatabaseDaoHelper helper = OpenHelperManager.getHelper(mContext, DatabaseDaoHelper.class);

		// Creating table
		helper.createTablesIfNotExist(mExerciseType.getEntityClasses());

		// Load and save data
		loadDefaultExercisesInBatch(helper, exerciseListfiles);

		// Release helper
		OpenHelperManager.releaseHelper();
		return null;
	}

	/**
	 * Call loadDefaultExercises in batch task to gain better performance.
	 * 
	 * @param helper
	 * @param exerciseListfiles
	 */
	public void loadDefaultExercisesInBatch(final DatabaseDaoHelper helper, final String... exerciseListfiles) {
		Callable<Void> batchTasks = new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				loadDefaultExercises(helper, exerciseListfiles);
				return null;
			}
		};
		helper.callBatchTasks(batchTasks);
	}

	/**
	 * Load default exercises if the app's first run.
	 * 
	 * @param exerciseListfiles
	 * @return
	 * @throws IllegalAccessException
	 */
	public void loadDefaultExercises(final DatabaseDaoHelper helper, final String... exerciseListfiles)
			throws IllegalAccessException {

		for (String fileName : exerciseListfiles) {

			// Get exercise list data
			ExerciseListResponse response = XmlParser.fromAsset(fileName, mAssetManager, ExerciseListResponse.class);

			// Save exercise list and get exercise urls
			List<String> savedExercises = saveExerciseList(helper, response);

			// Get all exercises' detail
			for (String execFileName : savedExercises) {
				Log.i(TAG, "loading " + execFileName);

				// Get exercise detail
				BaseEntity exerciseDetail = XmlParser.fromAsset(execFileName, mAssetManager,
						mExerciseType.getExerciseEntityClass());

				// Save to database
				DatabaseNestedObjectHelper.save(exerciseDetail, helper, mExerciseType.getEntityClasses());
			}
		}
	}

	/**
	 * Save exercise list to database
	 * 
	 * @param helper
	 * @param response
	 * @return
	 */
	private List<String> saveExerciseList(final DatabaseDaoHelper helper, ExerciseListResponse response) {
		List<String> savedExercises = new ArrayList<String>();

		// Get daos
		FlaxDao<ExerciseListResponse, String> responseDao = helper.getFlaxDao(ExerciseListResponse.class);
		FlaxDao<Category, String> categoryDao = helper.getFlaxDao(Category.class);
		FlaxDao<Exercise, String> exerciseItemDao = helper.getFlaxDao(Exercise.class);

		// Save response
		responseDao.create(response);

		// Get categoryList
		Collection<Category> categoryList = response.getCategoryList();
		for (Category category : categoryList) {
			// Save categories
			categoryDao.create(category);

			// Get exercise items.
			Collection<Exercise> exerciseItems = category.getExercises();

			// Process each exercise item
			for (Exercise exercise : exerciseItems) {
				// Save exercise that not exist
				exerciseItemDao.create(exercise);

				savedExercises.add(exercise.getUrl());
			}
		}

		return savedExercises;
	}

	/**
	 * isFirstTime method Check is first time execution, and set first time flag
	 * automatically
	 */
	public static boolean isFirstTime() {
		// Retrieve value from shared pref
		boolean isFirstTime = SpHelper.getBoolean(CHECK_FIRST_KEY, true);

		// After check, set first time flag as false
		SpHelper.putSingleBoolean(CHECK_FIRST_KEY, false);
		return isFirstTime;
	}

} // end of async task