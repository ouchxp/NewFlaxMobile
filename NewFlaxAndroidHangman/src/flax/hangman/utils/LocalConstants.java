package flax.hangman.utils;

import flax.activity.ExerciseType;
import flax.hangman.R;
import flax.hangman.converter.HangmanUrlConverter;
import flax.hangman.entity.HangmanExerciseDetail;
import flax.hangman.entity.Word;

public class LocalConstants {
	@SuppressWarnings("unchecked")
	public static final ExerciseType HANGMAN = ExerciseType
			.newInstance("HANGMAN", "HANGMAN", HangmanUrlConverter.class, HangmanExerciseDetail.class, Word.class,
					HangmanExerciseDetail.class, Word.class);

	/** for home screen */
	public static final String HANGMAN_URL = "?a=pr&o=xml&ro=1&rt=r&s=Hangman&c=password&s1.service=11";

	/** for game page fragment */
	// Keys for extra
	public static final String PRESSED_KEYS = "pressedKeys";
	public static final String MOVES = "moves";

	// for Hangman game
	public static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";
	public static final int[] BUTTON_GROUPS = { R.id.btns_a2i, R.id.btns_j2r, R.id.btns_s2z };
	public static final int[] HANGMAN_PICS = { R.drawable.img_hang_0, R.drawable.img_hang_1, R.drawable.img_hang_2,
			R.drawable.img_hang_3, R.drawable.img_hang_4, R.drawable.img_hang_5, R.drawable.img_hang_6,
			R.drawable.img_hang_7, R.drawable.img_hang_8, R.drawable.img_hang_9, R.drawable.img_hang_10 };

}
