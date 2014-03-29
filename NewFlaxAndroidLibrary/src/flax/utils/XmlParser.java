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

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.KXml2Driver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;

import flax.network.Downloader;

/**
 * NetworkXmlParser Class
 *
 * This class is used to parse a given input stream (containing xml data)
 * and extract activity information.
 * 
 * @author Nan Wu
 */
public class XmlParser {

	/* NetworkXmlParser class constructor */
    public XmlParser(){
    	
    }
    
	@SuppressWarnings("unchecked")
	public static <T> T fromUrl(String url,Class<T> resultType) {
		InputStream is = null;
		try {
			is = Downloader.downloadUrl(url);
			XStream stream = new XStream(new KXml2Driver(new XmlFriendlyNameCoder("_-", "_")));
			stream.processAnnotations(resultType);
			return (T) stream.fromXML(is);
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
    /*
     * parse method
     * 
     * Parses the given input stream (xml content) and
     * saves in a list of ActivityItem objects
     * 
     * @param in, the input stream containing the xml content
     * @return exercises, the list that holds the activity details
     */
//    public List<ActivityItem> parse(InputStream in) {
//    	
//    	Response response = fromStream(in,Response.class);
//    	List<Exercise> exercises = response.getCategoryList().getCategory().getExercises();
//    	List<ActivityItem> result = new ArrayList<ActivityItem>();
//    	for (Exercise exec : exercises) {
//			ActivityItem activityItem = new ActivityItem(0, exec.getId(), exec.getCategory_id(), exec.getName(), exec.getType(), exec.getUrl(), "new", 0, 0);
//			result.add(activityItem);
//		}
//    	return result;
//    }
} // end of class
