package flax.utils;

import static flax.utils.GlobalConstants.*;
import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import flax.core.FlaxApplication;
import flax.network.NetworkConnection;

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
	 * isConnected method Gets the connection status.
	 */
	public static boolean isConnected() {
		// get connection status
		return new NetworkConnection(getApplication()).checkNetworkConnection();
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
