/*
 * File: flax.network.NetworkConnection
 *
 * License: Greenstone 3 (GSDL3)
 * Copyright (C) 2003 New Zealand Digital Libraries, University Of Waikato
 * Greenstone3 comes with ABSOLUTELY NO WARRANTY; for details see LICENSE.txt
 * This is free software, and you are welcome to redistribute it
 * 
 * Reference: http://developer.android.com
 * 
 * Notes: This file is used to check whether there is Internet connection.
 * Compares user preferences ("wi-fi" or "any network") against connectivity.
 */
package flax.network;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;

/**
 * NetworkConnection Class
 *
 * This class is used to check whether there is Internet connection.
 * Compares user preferences ("wi-fi" or "any network") against connectivity.
 * 
 * @author Jemma Konig
 */
public class NetworkConnection {

	// Declare constants for connection
	protected final Context context;
	protected static final String WIFI 			= "Wi-Fi";
    protected static final String ANY 			= "Any";
    
    // Declare variables for connection
    protected static String networkPref 		= null;
    protected static boolean connect 			= false;
	
	/* NetworkConnection class constructor */
	public NetworkConnection(Context c){
		context 						= c;
		loadNetworkPreferences();
	}
	
	/*
	 * loadNetworkPreferences method
	 * 
	 * Used to load the user preferences
	 * Connect to server with Wi-Fi only, or any connection
	 */
	protected void loadNetworkPreferences(){
		SharedPreferences sharedPref = PreferenceManager
				.getDefaultSharedPreferences(context);
		networkPref 	= sharedPref.getString("network", "Wi-Fi");
	}
	
	/*
	 * checkNetworkConnection method
	 * 
	 * Check if the user preferences and the
	 * network connection match.
	 * 
	 * @return connect, boolean true if connection is okay.
	 */
	public boolean checkNetworkConnection(){
		// Load user preferences
		loadNetworkPreferences();
		
		// Check the  connection status of the device
        ConnectivityManager conMan 	= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInf 			= conMan.getActiveNetworkInfo();
        
        // Set 'connect = true' if preferences and network status match
        if (WIFI.equals(networkPref) && netInf != null
                && netInf.getType() == ConnectivityManager.TYPE_WIFI) {
        	connect 				= true;
        } else if (ANY.equals(networkPref) && netInf != null) {
        	connect 				= true;
        } else {
        	connect 				= false;
        }
        return connect;
	}	
} // end of class
