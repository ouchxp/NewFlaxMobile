package flax.utils;

import java.text.SimpleDateFormat;
import java.util.Locale;

import flax.library.R;

public class GlobalConstants {
	/** Locale */
	public static final Locale ENGLISH = Locale.ENGLISH;
	
	/** Server Path Setting */
	public static final String SERVER_PATH_KEY = "server";
	public static final String DEFAULT_SERVER_PATH = "http://flax.nzdl.org/greenstone3/flax";
	public static final String VALID_SERVER_PREFIX = "http://";
	public static final String VALID_SERVER_SUFFIX = "/greenstone3/flax";
	
	/** Network Setting */
	public static final String NETWORK_SETTING_KEY = "network";
	public static final String NETWORK_SETTING_WIFI = "Wi-Fi";
	public static final String NETWORK_SETTING_ANY = "Any";
	public static final String DEFAULT_NETWORK_SETTING = NETWORK_SETTING_WIFI;

	/** Downloading */
	public static final String DOWNLOAD_STATUS_KEY = "downloadStatus";
	public static final String DEFAULT_EXERCISE_LIST_FILE = "default_exercise_list.xml";

	/** Database */
	public static final String DATABASE_NAME = "NewFlax.db";
	public static final int DATABASE_VERSION = 1;
	public static final int MAX_EAGER_LEVEL = 99;

	/** Exercise */
	public static final int MAX_EXEC_PER_CATEGORY = 10;
	public static final String EXERCISE_ID = "exericseId";
	public static final String EXERCISE_STATUS = "exericseStatus";
	public static final int EXERCISE_NEW = 0;
	public static final int EXERCISE_INCOMPLETE = 1;
	public static final int EXERCISE_COMPLETE = 2;
	public static final int[] EXERCISE_STATUS_NAME = { R.string.exercise_new, R.string.exercise_incomplete,
			R.string.exercise_complete };

	/** Page */
	public static final int PAGE_WIN = 1;
	public static final int PAGE_INCOMPLETE = 0;
	public static final int PAGE_FAIL = -1;
	
	/** Summary Date Format */
	public static final String SUMMARY_DATE_FORMAT = "E MMM d HH:mm:ss";
	public static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat(SUMMARY_DATE_FORMAT, ENGLISH);

}
