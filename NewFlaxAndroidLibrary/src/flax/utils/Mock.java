package flax.utils;
import java.io.InputStream;

import android.content.res.Resources;
import flax.library.R;

/**
 * This class is just for testing
 * @author Nan Wu
 *
 */
public class Mock {
	public static Resources r;
	public static InputStream getExercises() {
		return r.openRawResource(R.raw.exercises);
	}
	
	public static InputStream getWords() {
		return r.openRawResource(R.raw.words);
	}
}
