/*
 * File: flax.collocation.CollocationNetworkDownload
 *
 * License: Greenstone 3 (GSDL3)
 * Copyright (C) 2003 New Zealand Digital Libraries, University Of Waikato
 * Greenstone3 comes with ABSOLUTELY NO WARRANTY; for details see LICENSE.txt
 * This is free software, and you are welcome to redistribute it
 * 
 * Reference: http://developer.android.com
 * 
 * Notes: This file uses NetworkHttpRequest and CollocationNetworkXmlParser to 
 * retrieve the collocation information from the server and save it. 
 */
package flax.collocation;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import flax.utils.Mock;

/**
 * CollocationNetworkDownload class
 * 
 * This class uses NetworkHttpRequest and CollocationNetworkXmlParser to 
 * retrieve the collocation information from the server and store
 * it in a list of CollocationItem objects.
 * 
 * Note: All classes relating to collocations have been stored in the FlaxAndroidLibrary
 * as they may be used by multiple games. ie. Collocation Dominoes, Collocation Matching etc.
 * 
 * @author Jemma Konig
 */
public class CollocationNetworkDownload{
	
	// Declare variables for download
	public int wordCount								= 0;
	public static Context context;
	protected CollocationDatabaseManager dbManager;
	
	// Declare list of CollocationItem objects
	public static List<CollocationItem> collocations 	= null;
	
	/* CollocationNetworkDownload class constructor */
	public CollocationNetworkDownload(Context c) {
		context = c;
	}
	
	/* 
 	 * dowloadCollocations method
 	 * 
 	 * Downloads the xml file for the collocations, parses it and 
 	 * stores it in the list 'collocations'. 
 	 * 
 	 * @param urlString, the string used in the http request
 	 */
    public void downloadCollocations(String urlString) throws XmlPullParserException, IOException {
    	
    	// Declare input stream used for http response 
        InputStream stream 			= null;
        
        // Declare and initialize new xml parser
        CollocationNetworkXmlParser colloXmlParser 	= new CollocationNetworkXmlParser();
        
        // Set collocations list to be null
        collocations				= null;
        
        try {
        	// Retrieve input stream from http response
            stream 					= downloadUrl(urlString);      
        	
        	// Parse the input stream and save in list
            collocations			= colloXmlParser.parse(stream);
            
        // Close the input stream now that we have finished with it
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
    }
    
    /*
     * downloadUrl method
     * 
     * Set up a http connection and get the response
     * 
 	 * @param urlString, the string used in the http request
 	 * @return is, the input stream containing the http response
     */
    private InputStream downloadUrl(String urlString) throws IOException {
		//TODO: Mocking to be removed
    	InputStream is 				= Mock.getWords();//Downloader.getHttpResponse(urlString);   	
    	return is;
    }
    
    /* 
     * getCollocationList method
     * 
     * Returns the list of CollocationActivitiyExercise objects
     * @return collocations, list containing new collocations
     */
    public ArrayList<CollocationItem> getCollocationList(){
    	ArrayList<CollocationItem> arr = new ArrayList<CollocationItem>();
    	for(CollocationItem c : collocations){
    		arr.add(c);
    	}
    	return arr;
    }
} // end of class
