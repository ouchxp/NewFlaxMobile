/*
 * File: flax.network.NetworkDownload
 *
 * License: Greenstone 3 (GSDL3)
 * Copyright (C) 2003 New Zealand Digital Libraries, University Of Waikato
 * Greenstone3 comes with ABSOLUTELY NO WARRANTY; for details see LICENSE.txt
 * This is free software, and you are welcome to redistribute it
 * 
 * Reference: http://developer.android.com
 * 
 * Notes: This file uses NetworkHttpRequest and NetworkXmlParser to 
 * retrieve the activity information from the server and save it. 
 * For each custom application, create a new NetworkDownload class for
 * methods to manage further downloading. ie download collocations 
 */
package flax.network;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import flax.utils.GlobalConstants;
import flax.utils.Mock;
import flax.utils.SpHelper;

/**
 * NetworkDownload Class
 *
 * This class uses NetworkHttpRequest and NetworkXmlParser to 
 * retrieve the activity information from the server and store
 * it in a list of of ActivityExercise objects.
 * 
 * @author Jemma Konig
 */
public class Downloader {

	// Declare variables for download
	
	/* NetworkDowload class constructor */
	public Downloader(){
	}
	
/*	
	 * saveServerPath method
	 * 
	 * Uses shared preferences to save the 
	 * Users chosen server path
	 
	public void saveDownloadStatus(){
		
		// Declare shared preference manager
        SharedPreferences sharedPref 					= PreferenceManager
        												.getDefaultSharedPreferences(context);
        // Declare shared preference editor
        Editor editor 									= sharedPref.edit();
        
        // Save shared preference value for "server"
        editor.putBoolean("downloadStatus", downloadStatus);
        editor.commit();
	}
	
	
	 * loadServerPath method
	 * 
	 * Uses shared preferences to load the 
	 * Users chosen server path
	 
	public void loadDownloadStatus(){
		
		// Declare shared preference manager
		SharedPreferences sharedPref 					= PreferenceManager
														.getDefaultSharedPreferences(context);
        // Retrieve value from shared pref
		downloadStatus	 									= sharedPref.getBoolean("downloadStatus", false);
	}*/
    
    /* 
     * getExerciseList method
     * 
     * Returns the list of ActivityExercise objects
     * @return arr, list containing activities
     */
//    public List<ActivityItem> getExerciseList(String url) throws IOException {
//    	
//
//    }
	
	
 	/* 
 	 * dowloadActivities method
 	 * 
 	 * Downloads the xml file for activities, parses it and 
 	 * stores it in a list of of ActivityExercise objects.
 	 * 
 	 * @param urlString, the string used in the http request
 	 */
//    public List<ActivityItem> xmlParse(InputStream stream) throws IOException {
//        
//        // Declare and initialize new xml parser
//        
//    }
    
    /*
     * downloadUrl method
     * 
     * Set up a http connection and get the response
     * 
 	 * @param urlString, the string used in the http request
 	 * @return is, the input stream containing the http response
     */
    public static InputStream downloadUrl(String urlString) throws IOException {
		
		boolean downloadStatus 				= false;
		//Save to sharedPreference
		SpHelper.putSingleBoolean(GlobalConstants.DOWNLOAD_STATUS_KEY, downloadStatus);
		
    	//TODO:Mocking to be removed
    	InputStream is 				= urlString.equals("http://flax.nzdl.org/greenstone3/flax?a=pr&o=xml&ro=1&rt=r&s=Hangman&c=password&s1.service=11")?Mock.getExercises():Mock.getWords();//getHttpResponse(urlString);
    	
    	downloadStatus				= true;
    	//Save to sharedPreference
    	SpHelper.putSingleBoolean(GlobalConstants.DOWNLOAD_STATUS_KEY, downloadStatus);
    	
    	return is;
    }
    
    public static InputStream getHttpResponse(String urlString) throws IOException {

    	// Set needed parameters for http request
        URL url 						= new URL(urlString);
        HttpURLConnection conn 			= (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000);
        conn.setConnectTimeout(15000);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        
        // Start the http query
        conn.connect();
        InputStream stream 				= conn.getInputStream();
        return stream;
    }
    

} // end of class
