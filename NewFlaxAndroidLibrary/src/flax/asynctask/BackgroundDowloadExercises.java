package flax.asynctask;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Callable;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.misc.TransactionManager;

import flax.activity.ExerciseTypeEnum;
import flax.database.DatabaseDaoHelper;
import flax.database.DatabaseObjectHelper;
import flax.entity.base.BaseEntity;
import flax.entity.exerciselist.Category;
import flax.entity.exerciselist.ExerciseListItem;
import flax.entity.exerciselist.ExerciseListResponse;
import flax.utils.GlobalConstants;
import flax.utils.IURLConverter;
import flax.utils.SPHelper;
import flax.utils.XMLParser;

/**
 * BackgroundDowloadExercises Class
 * Download and retrieve exercises from specific activity. Done in background to
 * take the load off of the HomeScreen Activity
 * @author Nan Wu
 */
public class BackgroundDowloadExercises extends AsyncTask<String, Void, Collection<ExerciseListItem>> {
	
	private ExerciseTypeEnum EXERCISE_TYPE;
	private Context mContext;
	private ProgressDialog mProgress;

	public BackgroundDowloadExercises(Context context, ExerciseTypeEnum type) {
		this.mContext = context;
		this.EXERCISE_TYPE = type;
	}

	/**
	 * Before the async task starts, prepare the progress bar
	 */
	@Override
	protected void onPreExecute() {
		mProgress = new ProgressDialog(mContext);
		mProgress.setMessage("Looking for new activities ...");
		mProgress.show();
	}

	/**
	 * doInBackground method Async task that connects to the server and
	 * Retrieves the required xml file using the NetworkHttpRequest class
	 */
	@Override
	protected Collection<ExerciseListItem> doInBackground(String... urls) {
		final long startTime = System.currentTimeMillis();

		// Begin parsing the xml from url
		ExerciseListResponse response = XMLParser.fromUrl(urls[0], ExerciseListResponse.class);
		
		// If something wrong then return null.
		if (response == null) {
			sleepForAWhile(startTime);
			return null;
		}

		// "new" exercises, which doesn't exist in database.
		final Collection<ExerciseListItem> newExecs = new ArrayList<ExerciseListItem>();
		
		// Get database helper
		final OrmLiteSqliteOpenHelper helper = OpenHelperManager.getHelper(mContext, DatabaseDaoHelper.class);
		try {

			// Get daos
			Dao<ExerciseListResponse, String> responseDao = helper.getDao(ExerciseListResponse.class);
			Dao<Category, String> categoryDao = helper.getDao(Category.class);
			Dao<ExerciseListItem, String> exerciseItemDao = helper.getDao(ExerciseListItem.class);
			
			// Save response
			responseDao.createIfNotExists(response);
			
			// Get categoryList
			Collection<Category> categoryList = response.getCategoryList();
			for (Category category : categoryList) {
				// Save categories
				categoryDao.createIfNotExists(category);
				
				// Get exercise items.
				Collection<ExerciseListItem> exerciseItems = category.getExercises();
				
				// format exercise url
				formatExerciseUrl(exerciseItems);
				
				// Process each exercise item
				for (ExerciseListItem exercise : exerciseItems) {
					// Save exercise that not exist
					if(!exerciseItemDao.idExists(exerciseItemDao.extractId(exercise))){
						exerciseItemDao.create(exercise);
						newExecs.add(exercise);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		// If new exercise exist, download and save exercise detail to database.
		if (!newExecs.isEmpty()) {
			downloadAndSaveContentInTrans(newExecs,helper);
		}

		// have to release helper after use
		OpenHelperManager.releaseHelper();
		
		// If time spent less than 1 sec, then sleep until 1 sec.
		sleepForAWhile(startTime);
		return newExecs;
	}
	
	private void sleepForAWhile(final long startTime){
		final long timeSpent = System.currentTimeMillis() - startTime;
		if(timeSpent < 1000){
			// Sleep app for one second to show progress
			try {
				Thread.sleep(1000-timeSpent);
			} catch (InterruptedException e) {
				e.printStackTrace();
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
	protected void onPostExecute(Collection<ExerciseListItem> result) {
		// If there is trouble with the server connection
		if (getDownloadStatus() == false) {
			Toast.makeText(
					mContext,
					"There has been an error in your connection, if you have used a custom server path, please check that it is correct.",
					Toast.LENGTH_LONG).show();
		} else if (result == null) {
			Toast.makeText(mContext, "Please try again to download the new activities.", Toast.LENGTH_SHORT).show();
		} else if (result.isEmpty()) {
			Toast.makeText(mContext, "No new exercises. " + "Press 'Play' to see existing exercises", Toast.LENGTH_SHORT)
					.show();
		} else {
			Toast.makeText(mContext, "New exercises saved. " + "Press 'Play' to see all exercises", Toast.LENGTH_SHORT)
					.show();
		}

		mProgress.dismiss();
	}

	
	/**
	 * Call downloadAndSaveContent in Transaction to gain better performance
	 * @see http://ormlite.com/javadoc/ormlite-core/doc-files/ormlite_5.html#index-database-transactions
	 * @see http://ormlite.com/javadoc/ormlite-core/doc-files/ormlite_5.html#callBatchTasks
	 * @param execs
	 * @param helper
	 */
	private void downloadAndSaveContentInTrans(final Collection<ExerciseListItem> execs, final OrmLiteSqliteOpenHelper helper) {
		try {
			TransactionManager.callInTransaction(helper.getConnectionSource(), new Callable<Void>() {
				@Override
				public Void call() throws Exception {
					downloadAndSaveContent(execs,helper);
					return null;
				}
			});
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Download and save exercise content
	 * @param execs
	 * @param helper
	 * @return
	 */
	private String downloadAndSaveContent(Collection<ExerciseListItem> execs, OrmLiteSqliteOpenHelper helper) {
		try {
			// Go through each new exercise and download corresponding exercise detail
			for (ExerciseListItem e : execs) {
				BaseEntity exerciseContent = XMLParser.fromUrl(e.getUrl(), EXERCISE_TYPE.getRootEntityClass());
				// Save multiple hierarchical entity, with object tree analyse.
				DatabaseObjectHelper.save(exerciseContent, helper, EXERCISE_TYPE.getEntityClasses());
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * The url retrieved from the activity xml returns the html layout rather
	 * than the needed xml. This url needs to be altered to return the correctly
	 * formatted xml
	 */
	private Collection<ExerciseListItem> formatExerciseUrl(Collection<ExerciseListItem> downloadedExercises) {

		// invoke urlConverter that alters the exercise content URL to get the correctly formatted
		// xml.
		IURLConverter urlConverter = null;
		try {
			urlConverter = EXERCISE_TYPE.getUrlConvertClass().newInstance();
			for (ExerciseListItem a : downloadedExercises) {
				a.setUrl(urlConverter.convert(a.getUrl()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return downloadedExercises;
	}

	/**
	 * getDownloadStatus method get if download is done
	 */
	private boolean getDownloadStatus() {
		return SPHelper.getBoolean(GlobalConstants.DOWNLOAD_STATUS_KEY, false);
	}
} // end of async task