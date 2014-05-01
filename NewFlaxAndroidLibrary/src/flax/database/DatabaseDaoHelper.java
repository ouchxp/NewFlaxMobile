package flax.database;

import java.sql.SQLException;
import java.util.concurrent.Callable;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import flax.entity.exerciselist.Category;
import flax.entity.exerciselist.Exercise;
import flax.entity.exerciselist.ExerciseListResponse;
import flax.utils.GlobalConstants;

public class DatabaseDaoHelper extends OrmLiteSqliteOpenHelper {
	public static final String TAG = "DatabaseDaoHelper";

	public DatabaseDaoHelper(Context context) {
		super(context, GlobalConstants.DATABASE_NAME, null, GlobalConstants.DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
		try {
			// Create tables
			TableUtils.createTable(connectionSource, Exercise.class);
			TableUtils.createTable(connectionSource, ExerciseListResponse.class);
			TableUtils.createTable(connectionSource, Category.class);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
		try {
			// Upgrade table version
			TableUtils.dropTable(connectionSource, Exercise.class, true);
			TableUtils.dropTable(connectionSource, ExerciseListResponse.class, true);
			TableUtils.dropTable(connectionSource, Category.class, true);
			onCreate(db, connectionSource);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Simple warp of calling batch task
	 * 
	 * @param callable
	 * @param helper
	 * @return T
	 */
	public <T> T callBatchTasks(Callable<T> callable) {
		T result;
		try {
			Dao<ExerciseListResponse, String> dao = getDao(ExerciseListResponse.class);
			result = dao.callBatchTasks(callable);
		} catch (Exception e) {
			Log.i(TAG, "Exception occured when calling batch task");
			throw new RuntimeException(e);
		}
		return result;
	}

	/**
	 * Get FlaxDao
	 * 
	 * @param clazz
	 * @return
	 */
	public <D extends FlaxDao<?, ?>, T> D getFlaxDao(Class<T> clazz) {
		try {
			Dao<T, ?> dao = getDao(clazz);
			@SuppressWarnings({ "unchecked", "rawtypes" })
			D castDao = (D) new FlaxDao(dao);
			return castDao;
		} catch (SQLException e) {
			throw new RuntimeException("Could not create FlaxDao for class " + clazz, e);
		}
	}

	/**
	 * create database tables if the table is not exist yet.
	 * 
	 * @param <T>
	 */
	public <T> void createTablesIfNotExist(Class<?>... classes) {
		try {
			for (Class<?> clazz : classes) {
				TableUtils.createTableIfNotExists(getConnectionSource(), clazz);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
