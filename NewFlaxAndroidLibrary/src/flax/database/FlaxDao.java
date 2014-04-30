package flax.database;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;

/**
 * "Dao" stands for "Database Access Object". This class extends
 * RuntimeExceptionDao that instead of throw SQLException it 
 * wraps each Exception and rethrow as RuntimeException. 
 * 
 * Library developer can customize some common database operations here.
 * 
 * @author Nan Wu
 * 
 * @param <T>
 * @param <ID>
 */
public class FlaxDao<T, ID> extends RuntimeExceptionDao<T, ID> {
	private Dao<T, ID> dao;
	public FlaxDao(Dao<T, ID> dao) {
		super(dao);
		this.dao = dao;
	}
}
