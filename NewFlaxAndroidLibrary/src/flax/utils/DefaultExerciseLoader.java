package flax.utils;

import static flax.utils.GlobalConstants.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.content.Context;
import android.content.res.AssetManager;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.table.TableUtils;

import flax.core.ExerciseType;
import flax.database.DatabaseDaoHelper;
import flax.database.DatabaseObjectSaver;
import flax.entity.base.BaseEntity;
import flax.entity.exerciselist.Category;
import flax.entity.exerciselist.Exercise;
import flax.entity.exerciselist.ExerciseListResponse;

public class DefaultExerciseLoader {
	public static final String TAG = "DefaultExerciseLoader";
	private Context mContext;
	private ExerciseType mExerciseType;
	private AssetManager mAssetManager;

	public DefaultExerciseLoader(Context context, ExerciseType exerciseType) {
		this.mContext = context;
		this.mExerciseType = exerciseType;
		this.mAssetManager = mContext.getAssets();
	}

	/**
	 * Load default exercises if the app's first run.
	 * @param exerciseListfiles
	 * @return
	 */
	public boolean loadDefaultExercises(String... exerciseListfiles) {
		boolean isFirstTime = isFirstTime();
		if(!isFirstTime) return isFirstTime;
		
		final OrmLiteSqliteOpenHelper helper = OpenHelperManager.getHelper(mContext, DatabaseDaoHelper.class);
		createTableIfNotExist(helper);
		try {
			for (String fileName : exerciseListfiles) {
				ExerciseListResponse response = XmlParser
						.fromAsset(fileName, mAssetManager, ExerciseListResponse.class);

				List<String> savedExercises = saveExerciseList(helper, response);
				for (String execFileName : savedExercises) {
					BaseEntity exercise = XmlParser.fromAsset(execFileName, mAssetManager, mExerciseType.getExerciseEntityClass());
					DatabaseObjectSaver.save(exercise, helper, mExerciseType.getEntityClasses());
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		return isFirstTime;
	}

	/** create database table if the table is not exist yet. */
	private void createTableIfNotExist(final OrmLiteSqliteOpenHelper helper){
		try {
			for(Class<?> clazz : mExerciseType.getEntityClasses()){
				TableUtils.createTableIfNotExists(helper.getConnectionSource(), clazz);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Save exercise list to database
	 * @param helper
	 * @param response
	 * @return
	 * @throws SQLException
	 */
	private List<String> saveExerciseList(final OrmLiteSqliteOpenHelper helper, ExerciseListResponse response)
			throws SQLException {
		List<String> savedExercises = new ArrayList<String>();
		
		// Get daos
		Dao<ExerciseListResponse, String> responseDao = helper.getDao(ExerciseListResponse.class);
		Dao<Category, String> categoryDao = helper.getDao(Category.class);
		Dao<Exercise, String> exerciseItemDao = helper.getDao(Exercise.class);
		
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
}
