/*
 * File: flax.network.NetworkXmlParser
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
import java.util.Collection;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.KXml2Driver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;

import flax.data.base.BaseData;
import flax.network.Downloader;

/**
 * NetworkXmlParser Class
 * 
 * This class is used to parse a given input stream (containing xml data) and
 * extract activity information.
 * 
 * @author Nan Wu
 */
public class XmlParser {

	/* NetworkXmlParser class constructor */
	public XmlParser() {

	}

	/**
	 * Parse XML from URL
	 * 
	 * @param url
	 * @param resultType
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T fromUrl(String url, Class<T> resultType) {
		InputStream is = null;
		try {
			is = Downloader.downloadUrl(url);
			XStream stream = new XStream(new KXml2Driver(new XmlFriendlyNameCoder("_-", "_")));
			stream.processAnnotations(resultType);
			T result = (T) stream.fromXML(is);
			// Automatically set foreign id for foreign tables.
			prepareForOrm(result);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Automatically set foreign id for foreign tables.
	 * 
	 * @param primaryRow
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	private static void prepareForOrm(Object primaryRow) throws IllegalArgumentException, IllegalAccessException {

		if (primaryRow == null || !(primaryRow instanceof BaseData))
			return;
		Class primaryClass = primaryRow.getClass();
		for (Field f : primaryClass.getDeclaredFields()) {
			f.setAccessible(true);
			if (isForeignField(f)) {// Foreign field
				Object foreignRow = f.get(primaryRow);// Foreign Table Row
				prepareForOrm(foreignRow);
			} else if (isForeignCollection(f)) {// ForeignCollection
				Collection foreignTable = (Collection) f.get(primaryRow);
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
			if (i.equals(BaseData.class)) {
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
	private static boolean isForeignKey(final Field f, final Class primaryClass) {
		return isForeignField(f) && f.getType().equals(primaryClass);
	}

} // end of class
