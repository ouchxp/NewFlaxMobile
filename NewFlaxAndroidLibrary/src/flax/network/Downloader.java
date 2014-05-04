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
	private Downloader(){
	}
    
    /*
     * downloadUrl method
     * 
     * Set up a http connection and get the response
 	 * @param urlString, the string used in the http request
 	 * @return is, the input stream containing the http response
     */
    public static InputStream downloadUrl(String urlString) throws IOException {
		
		boolean downloadStatus 				= false;
		//Save to sharedPreference
		SpHelper.putSingleBoolean(GlobalConstants.DOWNLOAD_STATUS_KEY, downloadStatus);
		
    	InputStream is 				= getHttpResponse(urlString);
    	
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
