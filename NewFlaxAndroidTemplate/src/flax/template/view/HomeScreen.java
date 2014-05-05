package flax.template.view;

import static flax.template.utils.LocalConstants.*;
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
		//TODO: return the exercise type constant.
		return TEMPLATE;
	}

	public String[] getUrls() {
		//return url of spicific activity's exercise list.
		String url = FlaxUtil.getServerPath() + TEMPLATE_URL;
		return new String[]{url};
	}

	/**
	 * Return the class of next activity for building Intent.
	 */
	public Class<?> getNextActivityClass() {
		// return class of next screen
		return ListScreen.class;
	}
}
