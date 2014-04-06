package flax.database;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import flax.entity.exercise.Category;
import flax.entity.exercise.Exercise;
import flax.entity.exercise.Response;
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
            TableUtils.createTable(connectionSource, Response.class);
            TableUtils.createTable(connectionSource, Category.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
        	//Upgrade table version
            
            TableUtils.dropTable(connectionSource, Exercise.class, true);
            TableUtils.dropTable(connectionSource, Response.class, true);
            TableUtils.dropTable(connectionSource, Category.class, true);
            onCreate(db, connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
	}

}
