package flax.hangman.view;

import flax.activity.ExerciseTypeEnum;
import flax.hangman.R;
import flax.hangman.utils.LocalConstants;
import flax.utils.FlaxUtil;

/**
 * HomeScreen
 * @author ouchxp
 *
 */
public class HomeScreen extends BaseHomeScreenActivity{

	@Override
	protected ExerciseTypeEnum getExerciseType() {
		return ExerciseTypeEnum.HANGMAN;
	}

	@Override
	protected String getHelpMessage() {
		return context.getString(R.string.home_screen_help_message);
	}

	@Override
	protected String[] getUrls() {
		String url = FlaxUtil.getServerPath() + LocalConstants.HANGMAN_URL;
		return new String[]{url};
	}

}
