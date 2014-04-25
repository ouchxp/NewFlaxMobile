package flax.hangman.utils;

import flax.hangman.R;

public class LocalConstants {
	/** for home screen */
	public static final String HANGMAN_URL = "?a=pr&o=xml&ro=1&rt=r&s=Hangman&c=password&s1.service=11";
	
	/** for game page fragment */
	// Keys for extra
	public static final String PRESSED_KEYS = "pressedKeys";
	public static final String MOVES = "moves";
	
	// for Hangman game 
	public static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";
	public static final int[] BUTTON_GROUPS = { R.id.btns_a2i, R.id.btns_j2r, R.id.btns_s2z };
	public static final int[] HANGMAN_PICS = { R.drawable.hang0, R.drawable.hang1, R.drawable.hang2, R.drawable.hang3,
			R.drawable.hang4, R.drawable.hang5, R.drawable.hang6, R.drawable.hang7, R.drawable.hang8, R.drawable.hang9,
			R.drawable.hang10 };
	
	
}
