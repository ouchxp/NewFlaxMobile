package flax.database;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;

import flax.entity.base.BaseEntity;
import flax.utils.FlaxUtil;

/**
 * Help save multiple hierarchical entity.
 * @author Nan Wu
 *
 */
public class DatabaseObjectHelper {
	
	/**
	 * Save multiple hierarchical entity without helper.
	 * @param rootEntity
	 * @param helper
	 * @param entityClasses
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws SQLException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void save(BaseEntity rootEntity, OrmLiteSqliteOpenHelper helper, Class<? extends BaseEntity>... entityClasses)
			throws IllegalAccessException, IllegalArgumentException, SQLException {
		if (rootEntity == null)
			return;
		UniqueStack<BaseEntity> entityStack = new UniqueStack<BaseEntity>();
		entityStack.push(rootEntity);

		/** Analysis object tree using stack */
		BaseEntity currEntity;
		while ((currEntity = entityStack.pop()) != null) {
			Class<?> root = currEntity.getClass();
			for (Field f : root.getDeclaredFields()) {
				if (isForeignCollection(f)) {
					f.setAccessible(true);
					Collection<?> entityCollection = (Collection<?>) f.get(currEntity);
					if (entityCollection == null) continue;
					for (Object object : entityCollection) {
						entityStack.push((BaseEntity) object);
					}
				} else if (isForeignField(f)) {
					f.setAccessible(true);
					Object object = f.get(currEntity);
					if (object == null) continue;
					entityStack.push((BaseEntity) object);
				}
			}
		}

		/** Store object tree in bottom-up order */
		
		// Reverse the records, top-down to bottom-up
		Collections.reverse(entityStack.records);

		// Create daos for individual class
		Map<String, Dao> daoMap = new HashMap<String, Dao>();
		for (Class<? extends BaseEntity> entityClass : entityClasses) {
			daoMap.put(entityClass.getName(), helper.getDao(entityClass));
		}

		// Save individual object
		for (Object entity : entityStack.records) {
			Dao dao = daoMap.get(entity.getClass().getName());
			dao.create(entity);
		}
	}
	
	/**
	 * Save multiple hierarchical entity without helper.
	 * @param rootEntity
	 * @param entityClasses
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws SQLException
	 */
	public static void save(BaseEntity rootEntity, Class<? extends BaseEntity>... entityClasses)
			throws IllegalAccessException, IllegalArgumentException, SQLException {

		// Get database helper
		OrmLiteSqliteOpenHelper helper = OpenHelperManager.getHelper(FlaxUtil.getApplication(), DatabaseDaoHelper.class);
		save(rootEntity, helper, entityClasses);
		OpenHelperManager.releaseHelper();
		
	}

	/**
	 * Is the class an derived class of BaseEntity class.
	 * 
	 * @param c
	 * @return
	 */
	private static boolean isBaseEntity(final Class<?> c) {
		Class<?> superClass = c.getSuperclass();
		if (superClass == null) {
			return false;
		} else if (c.getSuperclass().equals(BaseEntity.class)) {
			return true;
		}
		return false;
	}

	/**
	 * Is the field a foreign field.
	 * 
	 * @param f
	 * @return
	 */
	private static boolean isForeignField(final Field f) {
		if (!isBaseEntity(f.getType())) {
			return false;
		}
		DatabaseField annotation = f.getAnnotation(DatabaseField.class);
		return annotation != null && annotation.foreign();
	}

	/**
	 * Is the field a foreign colloction
	 * 
	 * @param f
	 * @return
	 */
	private static boolean isForeignCollection(final Field f) {
		if (!f.getType().equals(Collection.class)) {
			return false;
		}
		ForeignCollectionField annotation = f.getAnnotation(ForeignCollectionField.class);
		return annotation != null;
	}

	@SuppressWarnings("serial")
	private static class UniqueStack<E> extends Stack<E> {
		// Recording all objects have been pushed in. 
		List<E> records = new ArrayList<E>();

		@Override
		public E push(E object) {
			if (records.contains(object))
				return null;
			records.add(object);
			return super.push(object);
		}
		
		@Override
		public synchronized E pop() {
			if (elementCount == 0) return null;
			return super.pop();
		}

	}
}
