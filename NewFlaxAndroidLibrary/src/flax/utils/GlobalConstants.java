package flax.utils;

import java.util.Locale;


public class GlobalConstants {
	public static final String DEFAULT_SERVER_PATH = "http://flax.nzdl.org/greenstone3/flax";
	public static final String CHECK_FIRST_KEY = "checkFirst";
	public static final String DOWNLOAD_STATUS_KEY  = "downloadStatus";
	public static final String SERVER_PATH_KEY  = "server";
	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "NewFlax.db";
	
	public static final Locale ENGLISH = Locale.ENGLISH;
	public static final String SUMMARY_DATE_FORMAT = "E MMM d HH:mm:ss";
	
	//Exercise
	public static final String EXERCISE_ID  = "exericseId";
	
	//Exercise status
	public static final String EXERCISE_STATUS  = "exericseStatus";
	public static final String EXERCISE_NEW  = "New";
	public static final String EXERCISE_INCOMPLETE  = "Incomplete";
	public static final String EXERCISE_COMPLETE  = "Complete";
	
	//Page status
	public static final int PAGE_WIN = 1;
	public static final int PAGE_INCOMPLETE = 0;
	public static final int PAGE_FAIL = -1;

}
