package flax.database;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;

import flax.entity.base.BaseEntity;
import flax.utils.FlaxUtil;

/**
 * Help save hierarchical nested entity. Only use for uncertain type of entity.
 * Should not use this save regular entity due to low efficiency.
 * 
 * @author Nan Wu
 * 
 */
public class DatabaseNestedObjectHelper {

	/**
	 * Save nested entity.
	 * 
	 * @param rootEntity
	 * @param helper
	 * @param entityClasses
	 * @throws IllegalAccessException
	 * @throws SQLException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void save(BaseEntity rootEntity, DatabaseDaoHelper helper,
			Class<? extends BaseEntity>... entityClasses) throws IllegalAccessException {
		if (rootEntity == null)
			return;
		List<BaseEntity> entities = extractAllEntities(rootEntity);

		// Caching Daos
		Map<String, FlaxDao> daoMap = buildDaoMap(helper, entityClasses);

		// Save individual object
		for (Object entity : entities) {
			FlaxDao dao = daoMap.get(entity.getClass().getName());
			dao.create(entity);
		}
	}

	/**
	 * Save nested without helper.
	 * 
	 * @param rootEntity
	 * @param entityClasses
	 * @throws IllegalAccessException
	 * @throws SQLException
	 */
	public static void save(BaseEntity rootEntity, Class<? extends BaseEntity>... entityClasses)
			throws IllegalAccessException {

		// Get database helper
		DatabaseDaoHelper helper = OpenHelperManager.getHelper(FlaxUtil.getApplication(), DatabaseDaoHelper.class);
		save(rootEntity, helper, entityClasses);
		OpenHelperManager.releaseHelper();

	}

	/**
	 * Save multiple hierarchical entity without helper.
	 * 
	 * @param rootEntity
	 * @param helper
	 * @param entityClasses
	 * @throws IllegalAccessException
	 * @throws SQLException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void delete(BaseEntity rootEntity, DatabaseDaoHelper helper,
			Class<? extends BaseEntity>... entityClasses) throws IllegalAccessException {
		if (rootEntity == null)
			return;
		List<BaseEntity> entities = extractAllEntities(rootEntity);

		// Caching Daos
		Map<String, FlaxDao> daoMap = buildDaoMap(helper, entityClasses);

		// Save individual object
		for (Object entity : entities) {
			FlaxDao dao = daoMap.get(entity.getClass().getName());
			dao.delete(entity);
		}
	}

	/**
	 * Delete multiple hierarchical entity without helper.
	 * 
	 * @param rootEntity
	 * @param entityClasses
	 * @throws IllegalAccessException
	 * @throws SQLException
	 */
	public static void delete(BaseEntity rootEntity, Class<? extends BaseEntity>... entityClasses)
			throws IllegalAccessException {

		// Get database helper
		DatabaseDaoHelper helper = OpenHelperManager.getHelper(FlaxUtil.getApplication(), DatabaseDaoHelper.class);
		delete(rootEntity, helper, entityClasses);
		OpenHelperManager.releaseHelper();

	}

	/** Analysis object tree using stack */
	private static List<BaseEntity> extractAllEntities(BaseEntity rootEntity) throws IllegalAccessException {
		UniqueStack<BaseEntity> entityStack = new UniqueStack<BaseEntity>();
		entityStack.push(rootEntity);

		/** Analysis object tree using stack */
		BaseEntity currEntity;
		while ((currEntity = entityStack.pop()) != null) {
			Class<?> root = currEntity.getClass();
			for (Field f : root.getDeclaredFields()) {
				if (isForeignCollection(f)) {
					f.setAccessible(true);

					// Get foreign collection
					Collection<?> entityCollection = (Collection<?>) f.get(currEntity);
					if (entityCollection == null)
						continue;

					// Reverse, add this to keep right saving order
					List<?> list = new ArrayList<Object>(entityCollection);
					Collections.reverse(list);

					for (Object object : list) {
						entityStack.push((BaseEntity) object);
					}
				} else if (isForeignField(f)) {
					f.setAccessible(true);
					Object object = f.get(currEntity);
					if (object == null)
						continue;
					entityStack.push((BaseEntity) object);
				}
			}

		}

		/** Store object tree in bottom-up order */
		// Reverse the records, top-down to bottom-up
		Collections.reverse(entityStack.records);
		return entityStack.records;
	}

	/**
	 * Build Dao map for further usage
	 * 
	 * @param helper
	 * @param entityClasses
	 * @return
	 * @throws SQLException
	 */
	@SuppressWarnings("rawtypes")
	private static Map<String, FlaxDao> buildDaoMap(DatabaseDaoHelper helper,
			Class<? extends BaseEntity>... entityClasses) {
		Map<String, FlaxDao> daoMap = new HashMap<String, FlaxDao>();
		for (Class<? extends BaseEntity> entityClass : entityClasses) {
			daoMap.put(entityClass.getName(), helper.getFlaxDao(entityClass));
		}
		return daoMap;
	}

	/**
	 * Is the class an derived class of BaseEntity class.
	 * 
	 * @param c
	 * @return
	 */
	private static boolean isBaseEntity(final Class<?> c) {
		return c.isAssignableFrom(BaseEntity.class);
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
			// Do not add same object
			if (records.contains(object))
				return null;
			records.add(object);
			return super.push(object);
		}

		@Override
		public synchronized E pop() {
			if (elementCount == 0)
				return null;
			return super.pop();
		}

	}
}
