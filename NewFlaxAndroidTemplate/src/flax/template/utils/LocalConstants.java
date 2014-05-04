package flax.template.utils;

import flax.core.ExerciseType;
import flax.template.R;
import flax.template.converter.TemplateUrlConverter;
import flax.template.entity.TempleteExerciseDetail;
import flax.template.entity.TemplatePage;

public class LocalConstants {
	@SuppressWarnings("unchecked")
	//TODO: define exercise type
	/** define exercise type */
	public static final ExerciseType HANGMAN = ExerciseType
			.newInstance("HANGMAN", "HANGMAN", TemplateUrlConverter.class, TempleteExerciseDetail.class, TemplatePage.class,
					TempleteExerciseDetail.class, TemplatePage.class);

	//TODO: declare constants for download
	/** constants for download */
	public static final String HANGMAN_URL = "?a=pr&o=xml&ro=1&rt=r&s=Hangman&c=password&s1.service=11";

	
	//TODO: declare constants
	/** declare constants */
	/*
	// Keys for extra
	public static final String PRESSED_KEYS = "pressedKeys";
	public static final String MOVES = "moves";

	// for Hangman game
	public static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";
	public static final int[] BUTTON_GROUPS = { R.id.btns_a2i, R.id.btns_j2r, R.id.btns_s2z };
	public static final int[] HANGMAN_PICS = { R.drawable.img_hang_0, R.drawable.img_hang_1, R.drawable.img_hang_2,
			R.drawable.img_hang_3, R.drawable.img_hang_4, R.drawable.img_hang_5, R.drawable.img_hang_6,
			R.drawable.img_hang_7, R.drawable.img_hang_8, R.drawable.img_hang_9, R.drawable.img_hang_10 };
	*/
}
