package flax.hangman.view;

import flax.activity.ExerciseTypeEnum;
import flax.hangman.utils.LocalConstants;
import flax.utils.FlaxUtil;

/**
 * HomeScreen
 * @author Nan Wu
 *
 */
public class HomeScreen extends BaseHomeScreenActivity{

	public ExerciseTypeEnum getExerciseType() {
		return ExerciseTypeEnum.HANGMAN;
	}

	public String[] getUrls() {
		String url = FlaxUtil.getServerPath() + LocalConstants.HANGMAN_URL;
		return new String[]{url};
	}

	/**
	 * Return the class of next activity for building Intent.
	 */
	public Class<?> getNextActivityClass() {
		return ListScreen.class;
	}
}
