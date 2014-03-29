/*
 * File: flax.collocation.CollocationSampleDownload
 *
 * License: Greenstone 3 (GSDL3)
 * Copyright (C) 2003 New Zealand Digital Libraries, University Of Waikato
 * Greenstone3 comes with ABSOLUTELY NO WARRANTY; for details see LICENSE.txt
 * This is free software, and you are welcome to redistribute it
 * 
 * Reference: http://developer.android.com
 * 
 * Notes: This file uses an http post request and CollocationSampleXmlParser to 
 * retrieve the sample sentence information from the server and display it. 
 */
package flax.collocation;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.widget.Toast;

import flax.collocation.CollocationItem;
import flax.dialog.DialogSampleSentences;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlPullParserException;

/**
 * CollocationSampleDownload class
 * 
 * This file uses an http post request and CollocationSampleXmlParser to 
 * retrieve the sample sentence information from the server and display it. 
 * 
 * Note: All classes relating to collocations have been stored in the FlaxAndroidLibrary
 * as they may be used by multiple games. ie. Collocation Dominoes, Collocation Matching etc.
 * 
 * @author Jemma Konig
 */
public class CollocationSampleDownload extends AsyncTask<String, Void, String>{
		
	// Declare variables for download
	protected String aValue;
	protected String rtValue;
	protected String sendIdValue;
	protected String colloTextValue;
	protected String dbNameValue;
	protected String cValue;
	protected String sValue;
	protected String server;
	protected static int colloIndex;
	
	// Declare constants for download
	protected final String A_KEY 						= "a";
	protected final String RT_KEY 						= "rt";
	protected final String SEND_ID_KEY 					= "s1.sentIDs";
	protected final String COLLO_TEXT_KEY 				= "s1.colloText";
	protected final String DB_NAME_KEY 					= "s1.dbName";
	protected final String C_KEY 						= "c";
	protected final String S_KEY 						= "s";
	protected final String SERVER_DEFAULT				= "http://flax.nzdl.org/greenstone3/flax";
	
	// Declare classes for download
	protected final Context context;
    protected ProgressDialog progress 					= null;
	protected CollocationSampleXmlParser colloSampleXml;

	// Declare Lists for download
	protected ArrayList<String> sampleList 				= new ArrayList<String>();
	protected ArrayList<String> formatList 				= new ArrayList<String>();
	protected ArrayList<CollocationItem> collocations 	= new ArrayList<CollocationItem>();
	
	/* 
	 * CollocationSampleDownload Class Constructor
	 */
	public CollocationSampleDownload(Context c, ArrayList<CollocationItem> colloList, int index){
		
		// set context and list
		this.context 								= c;
		this.collocations							= colloList;
			
		// set post request parameters
		colloIndex									= index;
		aValue 										= "g";
		rtValue 									= "r";
		sendIdValue									= getSendId();
		colloTextValue 								= getColloText();
		dbNameValue									= "Wikipedia";
		cValue 										= "collocations";
		sValue 										= "CollocationSampleSentenceRetrieve";
	}
	
	/*
	 * loadServerPath method
	 * 
	 * Uses shared preferences to load the 
	 * Users chosen server path
	 */
	public void loadServerPath(){
		
		// Declare shared preference manager
		SharedPreferences sharedPref 				= PreferenceManager
													.getDefaultSharedPreferences(context);
        // Retrieve value from shared pref
		server	 									= sharedPref.getString("server", SERVER_DEFAULT);
	}
	
    /* 
     * onPreExecute method
     * 
     * Before the async task starts, 
     * prepare the progress bar
     * @see android.os.AsyncTask#onPreExecute()
     */
    @Override
    protected void onPreExecute() {
    	// Prepare progress bar
    	progress  									= new ProgressDialog (context);
    	progress.setMessage("looking for collocation samples ...");
    	progress.show();
    }

    /*
     * doInBackground method
     * 
     * Sends an http post request to the server and parses the
     * resulting xml.
     * 
     * @see android.os.AsyncTask#doInBackground(Params[])
     */
	@Override
	protected String doInBackground(String... arg0) {
		
		// Load server path
		loadServerPath();

		// Set required variables
		InputStream is 								= null;
		String address 								= server;
				
		try {
	    	// Sleep app for one second to show progress
			Thread.sleep(1000);
			
			// Set http post request
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(address);
			
			// Set parameters for post
			List<NameValuePair> pairs = new ArrayList<NameValuePair>();
			pairs.add(new BasicNameValuePair(A_KEY, aValue));
			pairs.add(new BasicNameValuePair(RT_KEY, rtValue));
			pairs.add(new BasicNameValuePair(SEND_ID_KEY, sendIdValue));
			pairs.add(new BasicNameValuePair(COLLO_TEXT_KEY, colloTextValue));
			pairs.add(new BasicNameValuePair(DB_NAME_KEY, dbNameValue));
			pairs.add(new BasicNameValuePair(C_KEY, cValue));
			pairs.add(new BasicNameValuePair(S_KEY, sValue));
			post.setEntity(new UrlEncodedFormEntity(pairs));
			
			// Send http post request
			HttpResponse response = client.execute(post);
			
			// Retrieve xml response
			HttpEntity entity = response.getEntity();
			
			// Parse xml response
			if (entity != null) {
			    is = entity.getContent();
		    	colloSampleXml = new CollocationSampleXmlParser();
		    	sampleList = colloSampleXml.parse(is);
			}
			return "successful";

		} catch (UnsupportedEncodingException e) {
			return "Error: Unsupported Encoding Exception";
		} catch (ClientProtocolException e) {
			return "Error: Client Protocol Exception";
		} catch (IOException e) {
			return "Error: IO Exception";
		} catch (XmlPullParserException e) {
			return "Error: Xml Pull Parser Exception";
		} catch (InterruptedException e) {
			return "Error: Interrupted Exception";
		}
	}
	
	/*
	 * onPostExecute method
	 * 
	 * this method is called once the async task is complete.
	 * It will display the sample sentences dialog.
	 * 
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
    protected void onPostExecute(String result) {
    	
		// Display Sample Sentences
    	if(result.equals("successful")){
        	if(sampleList.size() > 0){
        		formatList = formatSampleSentences(sampleList);
        		DialogSampleSentences sampleDialog = new DialogSampleSentences(context);
        		sampleDialog.displaySampleDialog(formatList);
        	}
        	else{
        		Toast.makeText(context, "no collocation samples found", Toast.LENGTH_SHORT).show();
        	}
    	} else {
    		Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
    	}
    	// Stop progress bar
    	progress.dismiss();
    }
    
    /*
     * getSendId method
     * @return sendId
     */
	private String getSendId() {
		CollocationItem collo 	= collocations.get(colloIndex);
		String sendId 			= collo.getSendId();
		return sendId;
	}
	
	/*
	 * getColloText method
	 * 
	 * Takes the collocation word and replaces any ' ' with '%20'
	 * @return colloText
	 */
	private String getColloText(){
		CollocationItem collo 	= collocations.get(colloIndex);
		String tmp 				= collo.getCollocationWord();
		String[] arr 			= tmp.split(" ");
		String colloText 		= "";
		for(int i = 0; i < arr.length -1; i++){
			colloText 			+= (arr[i] + "%20");
		}
		colloText 				+= arr[arr.length -1];
		return colloText;
	}
	
	/*
	 * formatSampleSentences method
	 * 
	 * This method runs through all of the sample sentences for a specific collocation
	 * and converts any instances of the collocation to upper case
	 * 
	 * @param list, list of sample sentences
	 * @return formatList, the list of formatted sample sentences
	 */
	private ArrayList<String> formatSampleSentences(ArrayList<String> list){
		
		CollocationItem collo 	= collocations.get(colloIndex);
		String word = collo.getCollocationWord();
		String[] colloArr = word.split(" ");
		
		ArrayList<String> formatList 				= new ArrayList<String>();

		// for each entry in the list
		for(String s : list){
			String tmp = "";
			String[] sampleArr = s.split(" ");
					
			// compare each word in the entry against the collocation
			for(int i = 0; i < sampleArr.length; i++){
								
				// If the sample word matches the first word in the collocation
				if(sampleArr[i].startsWith(colloArr[0])){
					boolean match = true;
					int index = i;
					
					// Check the rest of the collocation matches
					for(String c : colloArr){
						if(index < sampleArr.length){
							if(!sampleArr[index].startsWith(c)){
								match = false;
								break;
							}
							else{
								index++;
							}

						}
					}
					
					// If collocation is found, set it to capitals
					if(match == true){
						for(int j = 0; j < colloArr.length; j++){
							if(i+j < sampleArr.length){
								sampleArr[i + j] = sampleArr[i + j].toUpperCase(Locale.US);
							}
						}
					}
				}
				tmp += (sampleArr[i] + " ");
			}
			formatList.add(tmp);
		}
		return formatList;
	}
} // end of class

