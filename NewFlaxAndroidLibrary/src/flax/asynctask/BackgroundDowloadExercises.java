package flax.asynctask;

import static flax.utils.GlobalConstants.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import flax.core.ExerciseType;
import flax.database.DatabaseDaoHelper;
import flax.database.DatabaseNestedObjectHelper;
import flax.database.FlaxDao;
import flax.entity.base.BaseEntity;
import flax.entity.base.BaseExerciseDetail;
import flax.entity.exerciselist.Category;
import flax.entity.exerciselist.Exercise;
import flax.entity.exerciselist.ExerciseListResponse;
import flax.library.R;
import flax.utils.GlobalConstants;
import flax.utils.IUrlConverter;
import flax.utils.SpHelper;
import flax.utils.XmlParser;

/**
 * BackgroundDowloadExercises Class Download and retrieve exercises from
 * specific activity. Done in background to take the load off of the HomeScreen
 * Activity
 * 
 * @author Nan Wu
 */
public class BackgroundDowloadExercises extends AsyncTask<String, Void, Collection<Exercise>> {
	public static final String TAG = "BackgroundDowloadExercises";
	private ExerciseType EXERCISE_TYPE;
	private Context mContext;
	private ProgressDialog mProgress;

	public BackgroundDowloadExercises(Context context, ExerciseType type) {
		this.mContext = context;
		this.EXERCISE_TYPE = type;
	}

	/**
	 * Before the async task starts, prepare the progress bar
	 */
	@Override
	protected void onPreExecute() {
		mProgress = new ProgressDialog(mContext);
		mProgress.setMessage(mContext.getString(R.string.download_process_dialog_message));
		mProgress.show();
	}

	/**
	 * doInBackground method Async task that connects to the server and
	 * Retrieves the required xml file using the NetworkHttpRequest class
	 */
	@Override
	protected Collection<Exercise> doInBackground(String... urls) {
		final long startTime = System.currentTimeMillis();
		Collection<Exercise> newExecs;
		try {

			// Begin parsing the xml from url
			final ExerciseListResponse response = XmlParser.fromUrl(urls[0], ExerciseListResponse.class);

			// If something wrong then return null.
			if (response == null) {
				sleepForAWhile(startTime);
				return null;
			}

			// Get database helper
			final DatabaseDaoHelper helper = OpenHelperManager.getHelper(mContext, DatabaseDaoHelper.class);

			// Define batch task for all database operation
			final Callable<Collection<Exercise>> batchTask = new Callable<Collection<Exercise>>() {
				@Override
				public Collection<Exercise> call() throws Exception {

					// "new" exercises, which doesn't exist in database.
					final Collection<Exercise> newExecs = downloadAndSaveExerciseList(response, helper);

					// Delete old exercises
					final List<String> deletedIds = deleteOldExercises(response, helper);

					// Check whether there are deleted exercises in new exercise
					// list (only
					// happen if more than 10 exercises in a category in XML)
					checkNewExercise(newExecs, deletedIds);

					// If new exercise exist, download and save exercise detail
					// to
					// database.
					if (!newExecs.isEmpty()) {
						downloadAndSaveContent(newExecs, helper);
					}

					return newExecs;
				}
			};

			// Get new exercises by execute batch task
			newExecs = helper.callBatchTasks(batchTask);

			// have to release helper after use
			OpenHelperManager.releaseHelper();
			
		} catch (Exception e) {
			Log.w(TAG, e);
			return null;
		}
		
		// If time spent less than 1 sec, then sleep until 1 sec.
		sleepForAWhile(startTime);
		return newExecs;
	}

	private Collection<Exercise> downloadAndSaveExerciseList(ExerciseListResponse response,
			final DatabaseDaoHelper helper) {
		final Collection<Exercise> newExecs = new ArrayList<Exercise>();

		// Get daos
		FlaxDao<ExerciseListResponse, String> responseDao = helper.getFlaxDao(ExerciseListResponse.class);
		FlaxDao<Category, String> categoryDao = helper.getFlaxDao(Category.class);
		FlaxDao<Exercise, String> exerciseItemDao = helper.getFlaxDao(Exercise.class);

		// Save response
		responseDao.createIfNotExists(response);

		// Get categoryList
		Collection<Category> categoryList = response.getCategoryList();
		for (Category category : categoryList) {
			// Save categories
			categoryDao.createIfNotExists(category);

			// Get exercise items.
			Collection<Exercise> exerciseItems = category.getExercises();

			// format exercise url
			formatExerciseUrl(exerciseItems);

			// Process each exercise item
			for (Exercise exercise : exerciseItems) {
				// Save exercise that not exist
				if (!exerciseItemDao.idExists(exerciseItemDao.extractId(exercise))) {
					exerciseItemDao.create(exercise);
					newExecs.add(exercise);
				}
			}
		}

		return newExecs;
	}

	/**
	 * Delete old exercises from database
	 * 
	 * @param response
	 * @param helper
	 * @return
	 * @throws IllegalAccessException
	 */
	private List<String> deleteOldExercises(ExerciseListResponse response, final DatabaseDaoHelper helper)
			throws IllegalAccessException {

		// Get daos
		FlaxDao<ExerciseListResponse, String> responseDao = helper.getFlaxDao(ExerciseListResponse.class);
		FlaxDao<Exercise, String> exerciseItemDao = helper.getFlaxDao(Exercise.class);
		FlaxDao<BaseEntity, String> exerciseDetailDao = helper.getFlaxDao(EXERCISE_TYPE.getExerciseEntityClass());

		// Load new data from database
		responseDao.refresh(response);

		// old exercises that exceeds MAX_EXEC_PER_CATEGORY in category should
		// be delete.
		List<Exercise> execToBeDeleted = new ArrayList<Exercise>();
		for (Category category : response.getCategoryList()) {

			// Warp exercises in order to get a subList
			List<Exercise> exercises = new ArrayList<Exercise>(category.getExercises());
			if (exercises.size() > MAX_EXEC_PER_CATEGORY) {
				execToBeDeleted.addAll(exercises.subList(0, exercises.size() - MAX_EXEC_PER_CATEGORY));
			}
		}

		// Delete exercise details if exist
		List<String> deletedIds = new ArrayList<String>();
		for (Exercise exercise : execToBeDeleted) {
			String exerciseId = exercise.getUrl();
			deletedIds.add(exerciseId);

			BaseEntity exerciseDetail = exerciseDetailDao.queryForId(exerciseId);
			if (exerciseDetail == null) {
				continue;
			}
			DatabaseNestedObjectHelper.delete(exerciseDetail, helper, EXERCISE_TYPE.getEntityClasses());
		}

		// Delete all old exercises
		int count = exerciseItemDao.delete(execToBeDeleted);

		Log.i(TAG, count + " old exercises deleted");
		return deletedIds;
	}

	private void checkNewExercise(final Collection<Exercise> newExecs, final List<String> deletedIds) {
		// Check whether items were deleted.
		List<Exercise> exercisesDeleted = new ArrayList<Exercise>();
		for (Exercise exercise : newExecs) {
			if (deletedIds.contains(exercise.getUrl())) {
				exercisesDeleted.add(exercise);
			}
		}
		newExecs.removeAll(exercisesDeleted);
	}

	/**
	 * Download and save exercise content
	 * 
	 * @param execs
	 * @param helper
	 * @return
	 * @throws IllegalAccessException
	 */
	private void downloadAndSaveContent(Collection<Exercise> execs, DatabaseDaoHelper helper)
			throws IllegalAccessException {
		// Go through each new exercise and download corresponding exercise
		// detail
		for (Exercise exec : execs) {
			BaseExerciseDetail exerciseDetail = XmlParser
					.fromUrl(exec.getUrl(), EXERCISE_TYPE.getExerciseEntityClass());
			// Save multiple hierarchical entity, with object tree analyze.
			DatabaseNestedObjectHelper.save(exerciseDetail, helper, EXERCISE_TYPE.getEntityClasses());
		}
	}

	private void sleepForAWhile(final long startTime) {
		final long timeSpent = System.currentTimeMillis() - startTime;
		if (timeSpent < 1000) {
			// Sleep app for one second to show progress
			try {
				Thread.sleep(1000 - timeSpent);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * onPostExecute method
	 * 
	 * After activities have been downloaded, call CollocationProcess class
	 * which saves new exercises in db and downloads their content.
	 */
	@Override
	protected void onPostExecute(Collection<Exercise> result) {
		// If there is trouble with the server connection
		if (getDownloadStatus() == false) {
			Toast.makeText(mContext, R.string.downloading_error_message, Toast.LENGTH_LONG).show();
		} else if (result == null) {
			Toast.makeText(mContext, R.string.downloading_timeout_message, Toast.LENGTH_SHORT).show();
		} else if (result.isEmpty()) {
			Toast.makeText(mContext, R.string.downloading_nonew_message, Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(mContext, R.string.downloading_success_message, Toast.LENGTH_SHORT).show();
		}
		mProgress.dismiss();
	}

	/**
	 * The url retrieved from the activity xml returns the html layout rather
	 * than the needed xml. This url needs to be altered to return the correctly
	 * formatted xml
	 */
	private Collection<Exercise> formatExerciseUrl(Collection<Exercise> downloadedExercises) {

		// invoke urlConverter that alters the exercise content URL to get the
		// correctly formatted
		// xml.
		IUrlConverter urlConverter = null;
		try {
			urlConverter = EXERCISE_TYPE.getUrlConvertClass().newInstance();
			for (Exercise exec : downloadedExercises) {
				exec.setUrl(urlConverter.convert(exec.getUrl()));
			}
		} catch (InstantiationException e) {
			Log.e(TAG, EXERCISE_TYPE.getUrlConvertClass().getName() + " can not be instantiate.", e);
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			Log.e(TAG, EXERCISE_TYPE.getUrlConvertClass().getName()
					+ " can not be instantiate. Make sure it has a default no argument constructor.", e);
			throw new RuntimeException(e);
		}
		return downloadedExercises;
	}

	/**
	 * getDownloadStatus method get if download is done
	 */
	private boolean getDownloadStatus() {
		return SpHelper.getBoolean(GlobalConstants.DOWNLOAD_STATUS_KEY, false);
	}
} // end of async task