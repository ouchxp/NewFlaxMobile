package flax.utils;

import java.util.Map;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.PreferenceManager;

/**
 * SharedPreferences Helper class. Simplify the get/put operations.
 * @author ouchxp
 *
 */
public class SpHelper {
	private static SpHelper instances;
	private static SpHelper getInstance() {
		if (instances == null) {
			instances = new SpHelper();
		}
		return instances;
	}
	
	private final SharedPreferences sp;
	private SpHelper(){
		sp = PreferenceManager.getDefaultSharedPreferences(FlaxUtil.getApplication());
	}
	
	/* Enhanced method*/
    /**
     * Put single value into SharedPreferences and commit.
     */
	public static boolean putSingleString(String key,String value) {
		return edit().putString(key, value).commit();
	}
    /**
     * Put single value into SharedPreferences and commit.
     */
	public static boolean putSingleInt(String key,int value) {
		return edit().putInt(key, value).commit();
	}
    /**
     * Put single value into SharedPreferences and commit.
     */
	public static boolean putSingleBoolean(String key,boolean value) {
		return edit().putBoolean(key, value).commit();
	}
    /**
     * Put single value into SharedPreferences and commit.
     */
	public static boolean putSingleLong(String key,long value) {
		return edit().putLong(key, value).commit();
	}
    /**
     * Put single value into SharedPreferences and commit.
     */
	public static boolean putSingleFloat(String key,float value) {
		return edit().putFloat(key, value).commit();
	}
	
	/* Original methods*/
	public static Map<String, ?> getAll() {
		return getInstance().sp.getAll();
	}
	public static String getString(String key, String defValue) {
		return getInstance().sp.getString(key, defValue);
	}
	public static int getInt(String key, int defValue) {
		return getInstance().sp.getInt(key, defValue);
	}
	public static long getLong(String key, long defValue) {
		return getInstance().sp.getLong(key, defValue);
	}
	public static float getFloat(String key, float defValue) {
		return getInstance().sp.getFloat(key, defValue);
	}
	public static boolean getBoolean(String key, boolean defValue) {
		return getInstance().sp.getBoolean(key, defValue);
	}
	public static boolean contains(String key) {
		return getInstance().sp.contains(key);
	}
	public static Editor edit() {
		return getInstance().sp.edit();
	}
	public static void registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {
		getInstance().sp.registerOnSharedPreferenceChangeListener(listener);
	}
	public static void unregisterOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {
		getInstance().sp.unregisterOnSharedPreferenceChangeListener(listener);
	}
}