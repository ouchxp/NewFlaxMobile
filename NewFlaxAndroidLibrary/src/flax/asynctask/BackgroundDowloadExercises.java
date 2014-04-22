package flax.asynctask;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;

import flax.activity.ExerciseTypeEnum;
import flax.collocation.CollocationDatabaseManager;
import flax.collocation.CollocationItem;
import flax.database.DatabaseDaoHelper;
import flax.database.DatabaseManager;
import flax.database.DatabaseObjectHelper;
import flax.entity.base.BaseEntity;
import flax.entity.exercise.Category;
import flax.entity.exercise.Exercise;
import flax.entity.exercise.ExerciseListResponse;
import flax.entity.hangman.HangmanExercise;
import flax.entity.hangman.Word;
import flax.utils.GlobalConstants;
import flax.utils.IURLConverter;
import flax.utils.SPHelper;
import flax.utils.XMLParser;

/**
 * BackgroundDowloadExercises Class
 * 
 * Download and retrieve exercises from specific activity. Done in background to
 * take the load off of the HomeScreen Activity
 */
public class BackgroundDowloadExercises extends AsyncTask<String, Void, Collection<Exercise>> {

	private Context context;
	// Convert url to correct format.
	private ExerciseTypeEnum EXERCISE_TYPE;
	private DatabaseManager dbManager;
	// Declare progress bar
	private ProgressDialog progress;

	public BackgroundDowloadExercises(Context context, ExerciseTypeEnum type) {
		this.context = context;
		this.EXERCISE_TYPE = type;
	}

	/**
	 * onPreExecute method Before the async task starts, prepare the progress
	 * bar
	 */
	@Override
	protected void onPreExecute() {
		// Prepare progress bar
		progress = new ProgressDialog(context);
		progress.setMessage("looking for new activities ...");
		progress.show();
	}

	/**
	 * doInBackground method Async task that connects to the server and
	 * Retrieves the required xml file using the NetworkHttpRequest class
	 */
	@Override
	protected Collection<Exercise> doInBackground(String... urls) {

		// Sleep app for one second to show progress
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// Begin parsing the xml from url
		ExerciseListResponse response = XMLParser.fromUrl(urls[0], ExerciseListResponse.class);
		// If something wrong then return null.
		if (response == null) {
			return null;
		}

		// Get exercises and format urls for exercises
		Collection<Exercise> exercises = new ArrayList<Exercise>();
		for (Category category : response.getCategoryList()) {
			formatExerciseUrl(category.getExercises());
			exercises.addAll(category.getExercises());
		}

		// TODO: status should be separate
		for (Exercise exercise : exercises) {
			exercise.setStatus("New");
		}

		// Get "new" exercises, which doesn't exist in db from downloaded
		// exercises.
		Collection<Exercise> newExecs = getNewExercises(exercises);

		// TODO: Change to Ormlite version after ListScreen Done
		OrmLiteSqliteOpenHelper helper = OpenHelperManager.getHelper(context, DatabaseDaoHelper.class);
		try {

			Collection<Category> categoryList = response.getCategoryList();
			// Category category = categoryList.getCategory();

			Dao<ExerciseListResponse, String> dao1 = helper.getDao(ExerciseListResponse.class);
			Dao<Category, String> dao3 = helper.getDao(Category.class);
			Dao<Exercise, String> dao4 = helper.getDao(Exercise.class);
			dao1.createIfNotExists(response);
			for (Category category : categoryList) {
				dao3.createIfNotExists(category);
				Collection<Exercise> exercises1 = category.getExercises();
				formatExerciseUrl(exercises1);
				for (Exercise exercise : exercises1) {
					dao4.createIfNotExists(exercise);
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		// If no new exercises, return empty array.
		if (newExecs.isEmpty()) {
			exercises.clear();
			return exercises;
		}

		// otherwise save new exercises
		saveNewExercises(newExecs);

		// call async download new exercises content
		downloadAndSaveContent(newExecs,helper);

		// have to release helper after use
		OpenHelperManager.releaseHelper();
		
		return exercises;
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
			Toast.makeText(
					context,
					"There has been an error in your connection, if you have used a custom server path, please check that it is correct.",
					Toast.LENGTH_LONG).show();
		} else if (result == null) {
			Toast.makeText(context, "Please try again to download the new activities.", Toast.LENGTH_SHORT).show();
		} else if (result.isEmpty()) {
			Toast.makeText(context, "No new exercises. " + "Press 'Play' to see existing exercises", Toast.LENGTH_SHORT)
					.show();
		} else {
			Toast.makeText(context, "New exercises saved. " + "Press 'Play' to see all exercises", Toast.LENGTH_SHORT)
					.show();
		}
		// save, and download the content of new exercises
		// CollocationProcess colloProcess = new CollocationProcess( result,
		// context);
		// colloProcess.processNewExercises();
		// Stop progress bar
		progress.dismiss();
	}

	private String downloadAndSaveContent(Collection<Exercise> execs, OrmLiteSqliteOpenHelper helper) {
		try {
			// Go through each new exercise and download corresponding exercise
			// content
			for (Exercise e : execs) {
				int i = 0;

				// TODO: Change Data Type
				// Download exercise content
				BaseEntity exerciseContent = XMLParser.fromUrl(e.getUrl(), EXERCISE_TYPE.getRootEntityClass());
				DatabaseObjectHelper.save(exerciseContent, helper, EXERCISE_TYPE.getEntityClasses());

				// TODO: Change to ormlite version after GameScreen Done.
				// Set collocations list to be null
				List<CollocationItem> collocations = new ArrayList<CollocationItem>();

				for (Word word : ((HangmanExercise)exerciseContent).getWords()) {
					collocations.add(new CollocationItem(0, word.getWord(), word.getWord(), 0, word.getWord(), word
							.getWord(), "none", EXERCISE_TYPE.getName(), "none", "none", word.getWord(), 0));
				}

				// Set database manager
				CollocationDatabaseManager dbManager = new CollocationDatabaseManager(context);

				// Add collocations to db - note i = index (collocation
				// order)
				for (CollocationItem c : collocations) {
					dbManager.addCollocation(c.collocationId, i, c.type, c.fre, c.sendId, c.word, "none", "none",
							"none", c.getBaseWord(), e.getUniqueId());
					i++;
				}

				// Save word count
				dbManager.updateActivityWordCount(collocations.size(), e.getUniqueId());

				// Add initial entry to summary report table in db
				dbManager.addSummary("", "", 0, 0, e.getUniqueId());
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/*
	 * formatActivityUrl method
	 * 
	 * The url retrieved from the activity xml returns the html layout rather
	 * than the needed xml. This url needs to be altered to return the correctly
	 * formatted xml
	 */
	private Collection<Exercise> formatExerciseUrl(Collection<Exercise> downloadedExercises) {

		// Create algorithm that alters the url to get the correctly formatted
		// xml.
		IURLConverter urlConverter = null;
		try {
			urlConverter = EXERCISE_TYPE.getUrlConvertClass().newInstance();
			for (Exercise a : downloadedExercises) {
				a.setUrl(urlConverter.convert(a.getUrl()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return downloadedExercises;
	}

	/**
	 * getNewExercises method
	 * 
	 * get "new" exercises, which doesn't exist in db from downloaded exercises.
	 */
	private Collection<Exercise> getNewExercises(Collection<Exercise> execs) {
		// Declare db for check
		dbManager = new DatabaseManager(context);
		// Store new Items in holder with url as key
		Map<String, Exercise> newItemHolder = new HashMap<String, Exercise>();
		for (Exercise ne : execs) {
			newItemHolder.put(ne.getUrl(), ne);
		}

		// get the new urls which is already existing urls in the db
		List<String> existingUrls = dbManager.selectExistUrls(newItemHolder.keySet().toArray(new String[] {}));

		// remove the existing items (keep the new items)
		for (String url : existingUrls) {
			newItemHolder.remove(url);
		}

		return new ArrayList<Exercise>(newItemHolder.values());
	}

	/**
	 * getDownloadStatus method get if download is done
	 */
	private boolean getDownloadStatus() {
		return SPHelper.getBoolean(GlobalConstants.DOWNLOAD_STATUS_KEY, false);
	}

	/**
	 * saveNewExercises method
	 * 
	 * This method takes the downloaded exercise list and compares it to the
	 * existing exercises, saving any new exercises in the database.
	 */
	private void saveNewExercises(Collection<Exercise> newExercises) {
		long rowId = 0;
		// add new items to db
		for (Exercise ne : newExercises) {
			rowId = dbManager.addActivity(ne.getId(), ne.getCategoryId(), ne.getType(), ne.getName(), ne.getUrl(),
					ne.getStatus(), (int) rowId, 0);
			ne.setUniqueId((int) rowId);
		}

	}

} // end of async task