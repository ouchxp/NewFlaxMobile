package flax.database;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import flax.entity.exerciselist.Category;
import flax.entity.exerciselist.Exercise;
import flax.entity.exerciselist.ExerciseListResponse;
import flax.entity.hangman.HangmanExerciseDetail;
import flax.entity.hangman.Word;
import flax.utils.GlobalConstants;

public class DatabaseDaoHelper extends OrmLiteSqliteOpenHelper {
	
	public DatabaseDaoHelper(Context context) {
		super(context, GlobalConstants.DATABASE_NAME, null, GlobalConstants.DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
		try {
			//Create tables
            TableUtils.createTable(connectionSource, Exercise.class);
            TableUtils.createTable(connectionSource, ExerciseListResponse.class);
            TableUtils.createTable(connectionSource, Category.class);
            
            TableUtils.createTable(connectionSource, HangmanExerciseDetail.class);
            TableUtils.createTable(connectionSource, Word.class);
        } catch (SQLException e) {
        	throw new RuntimeException(e);
        }
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
        	//Upgrade table version
            
            TableUtils.dropTable(connectionSource, Exercise.class, true);
            TableUtils.dropTable(connectionSource, ExerciseListResponse.class, true);
            TableUtils.dropTable(connectionSource, Category.class, true);
            
            TableUtils.dropTable(connectionSource, HangmanExerciseDetail.class, true);
            TableUtils.dropTable(connectionSource, Word.class, true);
            onCreate(db, connectionSource);
        } catch (SQLException e) {
        	throw new RuntimeException(e);
        }
	}

}
