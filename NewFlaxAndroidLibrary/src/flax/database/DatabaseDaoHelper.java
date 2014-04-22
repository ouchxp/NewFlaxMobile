package flax.database;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import flax.entity.exerciselist.Category;
import flax.entity.exerciselist.ExerciseListItem;
import flax.entity.exerciselist.ExerciseListResponse;
import flax.entity.hangman.HangmanExercise;
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
            TableUtils.createTable(connectionSource, ExerciseListItem.class);
            TableUtils.createTable(connectionSource, ExerciseListResponse.class);
            TableUtils.createTable(connectionSource, Category.class);
            
            TableUtils.createTable(connectionSource, HangmanExercise.class);
            TableUtils.createTable(connectionSource, Word.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
        	//Upgrade table version
            
            TableUtils.dropTable(connectionSource, ExerciseListItem.class, true);
            TableUtils.dropTable(connectionSource, ExerciseListResponse.class, true);
            TableUtils.dropTable(connectionSource, Category.class, true);
            
            TableUtils.dropTable(connectionSource, HangmanExercise.class, true);
            TableUtils.dropTable(connectionSource, Word.class, true);
            onCreate(db, connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
	}

}
