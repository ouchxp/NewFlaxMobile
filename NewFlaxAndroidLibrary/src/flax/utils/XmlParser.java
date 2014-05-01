/*
 * File: flax.utils.XMLParser
 *
 * License: Greenstone 3 (GSDL3)
 * Copyright (C) 2003 New Zealand Digital Libraries, University Of Waikato
 * Greenstone3 comes with ABSOLUTELY NO WARRANTY; for details see LICENSE.txt
 * This is free software, and you are welcome to redistribute it
 * 
 * Reference: http://developer.android.com
 * 
 * Notes: This file is used to parse a given input stream (containing xml data)
 * and extract activity information.
 */
package flax.utils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import android.content.res.AssetManager;
import android.util.Log;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;

import flax.entity.base.BaseEntity;
import flax.network.Downloader;

/**
 * XMLParser Class
 * 
 * This class is used to parse a given input stream (containing xml data) and
 * extract activity information.
 * @see http://simple.sourceforge.net/
 * 
 * @author Nan Wu
 */
public class XmlParser {
	private static final String TAG = "XMLParser";
	private static final String UNIQUE_URL_SETTING_METHOD = "setUniqueUrl";
	/* XmlParser class constructor */
	private XmlParser() {}

	/**
	 * Parse XML from URL
	 * @see http://simple.sourceforge.net/download/stream/doc/tutorial/tutorial.php#deserialize
	 * 
	 * @param url
	 * @param resultType
	 * @return
	 */
	public static <T> T fromUrl(String url, Class<T> resultType) {
		InputStream is = null;
		try {
			// Downloading
			is = Downloader.downloadUrl(url);
			
			// Parsing
			Serializer serializer = new Persister();
			T result = serializer.read(resultType, is);
			
			// Setting unique url
			setUniqueUrl(result,url);
			
			// Set up foreign relation @Deprecated
			// prepareForOrm(result);
			
			return result;
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
			throw new RuntimeException();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					Log.e(TAG, e.getMessage());
					throw new RuntimeException();
				}
			}
		}
	}
	
	/**
	 * Parse XML from URL
	 * @see http://simple.sourceforge.net/download/stream/doc/tutorial/tutorial.php#deserialize
	 * 
	 * @param url
	 * @param resultType
	 * @return
	 */
	public static <T> T fromAsset(String fileName, AssetManager assetManager, Class<T> resultType) {
		InputStream is = null;
		try {
			// Downloading
			is = assetManager.open(fileName);
			
			// Parsing
			Serializer serializer = new Persister();
			T result = serializer.read(resultType, is);
			
			// Setting unique url
			setUniqueUrl(result,fileName);
			
			// Set up foreign relation @Deprecated
			// prepareForOrm(result);
			
			return result;
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
			throw new RuntimeException();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					Log.e(TAG, e.getMessage());
					throw new RuntimeException();
				}
			}
		}
	}

	/**
	 * Automatically set download url as unique id for response.
	 * 
	 * @param response
	 * @param url
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException 
	 */
	private static final <T> void setUniqueUrl(T response, String url) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		if (response == null || !(response instanceof BaseEntity))
			return;

		Class<?> clazz = response.getClass();
		for (Method m : clazz.getMethods()) {
			if (UNIQUE_URL_SETTING_METHOD.equals(m.getName())) {
				m.invoke(response, url);
				break;
			}
		}
	}

	/**
	 * Automatically set foreign id for foreign tables.
	 * Currently not used, because user can manipulate object relation
	 * using @Commit annotation in entity classes.
	 * 
	 * @deprecated should use @Commit in entity class to build relation.
	 * @param <T>
	 * 
	 * @param primaryRow
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	@SuppressWarnings("unused")
	@Deprecated
	private static <T> void prepareForOrm(T primaryRow) throws IllegalArgumentException, IllegalAccessException {

		if (primaryRow == null || !(primaryRow instanceof BaseEntity))
			return;
		Class<?> primaryClass = primaryRow.getClass();
		for (Field f : primaryClass.getDeclaredFields()) {
			f.setAccessible(true);
			if (isForeignField(f)) {// Foreign field
				Object foreignRow = f.get(primaryRow);// Foreign Table Row
				for (Field subf : foreignRow.getClass().getDeclaredFields()) {
					if (isForeignKey(subf, primaryClass)) {// Foreign Key
						subf.setAccessible(true);
						if (subf.get(foreignRow) == null) {
							subf.set(foreignRow, primaryRow);
							// Process Foreign Rows
							prepareForOrm(foreignRow);
						}
					}
				}

			} else if (isForeignCollection(f)) {// ForeignCollection
				Collection<?> foreignTable = (Collection<?>) f.get(primaryRow);
				for (Object foreignRow : foreignTable) {// Foreign Table Row
					for (Field subf : foreignRow.getClass().getDeclaredFields()) {
						if (isForeignKey(subf, primaryClass)) {// Foreign Key
							subf.setAccessible(true);
							if (subf.get(foreignRow) == null) {
								subf.set(foreignRow, primaryRow);
								// Process Foreign Rows
								prepareForOrm(foreignRow);
							}
						}
					}
				}
			}
		}
	}


	/**
	 * Is the class an implement of BaseData interface.
	 * 
	 * @param c
	 * @return
	 */
	private static boolean isBaseData(final Class<?> c) {
		for (Class<?> i : c.getInterfaces()) {
			if (i.equals(BaseEntity.class)) {
				return true;
			}
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
		if (!isBaseData(f.getType())) {
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

	/**
	 * Is the field a foreign colloction
	 * 
	 * @param f
	 * @return
	 */
	private static boolean isForeignKey(final Field f, final Class<?> primaryClass) {
		return isForeignField(f) && f.getType().equals(primaryClass);
	}

} // end of class
