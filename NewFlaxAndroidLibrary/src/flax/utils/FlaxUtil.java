package flax.utils;

import static flax.utils.GlobalConstants.*;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import flax.core.FlaxApplication;

public class FlaxUtil {
	/**
	 * getServerPath method Get server path from shared pref
	 */
	public static String getServerPath() {
		return SpHelper.getString(SERVER_PATH_KEY, DEFAULT_SERVER_PATH);
	}

	/**
	 * setServerPath method set server path to shared pref
	 */
	public static void setServerPath(String serverPath) {
		SpHelper.putSingleString(SERVER_PATH_KEY, serverPath);
	}

	/**
	 * getServerPath method Get server path from shared pref
	 */
	public static String getNetworkSetting() {
		return SpHelper.getString(SERVER_PATH_KEY, DEFAULT_NETWORK_SETTING);
	}

	/**
	 * setServerPath method set server path to shared pref
	 */
	public static void setNetworkSetting(String networkSetting) {
		SpHelper.putSingleString(SERVER_PATH_KEY, networkSetting);
	}

	/**
	 * isConnected method Gets the connection status.
	 */
	public static boolean isConnected() {

		boolean connect;
		// Load user preferences
		String networkPref = getNetworkSetting();

		// Check the connection status of the device
		ConnectivityManager conMan = (ConnectivityManager) getApplication().getSystemService(
				Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInf = conMan.getActiveNetworkInfo();

		// Set 'connect = true' if preferences and network status match
		if (NETWORK_SETTING_WIFI.equals(networkPref) && netInf != null
				&& netInf.getType() == ConnectivityManager.TYPE_WIFI) {
			connect = true;
		} else if (NETWORK_SETTING_ANY.equals(networkPref) && netInf != null) {
			connect = true;
		} else {
			connect = false;
		}
		return connect;
	}

	/**
	 * getApplication method Get application object, can be used as global
	 * context
	 */
	public static SharedPreferences getSharedPreferences() {
		return PreferenceManager.getDefaultSharedPreferences(getApplication());
	}

	/**
	 * getApplication method Get application object, can be used as global
	 * context
	 */
	public static Application getApplication() {
		return FlaxApplication.getInstance();
	}
}
