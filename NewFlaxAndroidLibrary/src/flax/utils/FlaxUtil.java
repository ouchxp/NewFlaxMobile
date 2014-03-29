package flax.utils;

import static flax.utils.GlobalConstants.*;
import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import flax.core.FlaxApplication;
import flax.network.NetworkConnection;

public class FlaxUtil {
	/**
	 * getServerPath method Get sercer path from shared pref
	 */
	public static String getServerPath() {
		return SPHelper.getString(SERVER_PATH_KEY, DEFAULT_SERVER_PATH);
	}

	/**
	 * isConnected method Gets the connection status.
	 */
	public static boolean isConnected() {
		// get connection status
		return new NetworkConnection(getApplication()).checkNetworkConnection();
	}

	/**
	 * isFirstTime method Check is first time execution, and set first time flag
	 * automatically
	 */
	public static boolean isFirstTime() {
		// Retrieve value from shared pref
		boolean isFirstTime = SPHelper.getBoolean(CHECK_FIRST_KEY, true);

		// After check, set first time flag as false
		SPHelper.putSingleBoolean(CHECK_FIRST_KEY, false);
		return isFirstTime;
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
