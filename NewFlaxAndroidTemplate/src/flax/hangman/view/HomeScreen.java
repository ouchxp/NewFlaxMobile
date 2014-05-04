package flax.hangman.view;

import static flax.hangman.utils.LocalConstants.*;
import flax.baseview.BaseHomeScreenActivity;
import flax.core.ExerciseType;
import flax.utils.FlaxUtil;

/**
 * HomeScreen
 * @author Nan Wu
 *
 */
public class HomeScreen extends BaseHomeScreenActivity{

	public ExerciseType getExerciseType() {
		return HANGMAN;
	}

	public String[] getUrls() {
		String url = FlaxUtil.getServerPath() + HANGMAN_URL;
		return new String[]{url};
	}

	/**
	 * Return the class of next activity for building Intent.
	 */
	public Class<?> getNextActivityClass() {
		return ListScreen.class;
	}
}
