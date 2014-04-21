package flax.hangman.view;

import static flax.utils.GlobalConstants.*;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import flax.entity.hangman.Word;
import flax.hangman.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class GamePageFragment extends Fragment implements OnClickListener {
	private static final String TAG = "GamePageFragment";
	private static final String PRESSED_KEYS = "pressedKeys";
	private static final String PAGE_STATUS = "pageStatus";
	private static final String PAGE_WIN = "pageWin";
	private static final String PAGE_FAIL = "pageFail";

	private static final String MOVES = "moves";
	private static final int[] BUTTON_GROUPS = { R.id.btns_a2i, R.id.btns_j2r, R.id.btns_s2z };
	private static final int[] HANGMAN_PICS = { R.drawable.hang0, R.drawable.hang1, R.drawable.hang2, R.drawable.hang3,
			R.drawable.hang4, R.drawable.hang5, R.drawable.hang6, R.drawable.hang7, R.drawable.hang8, R.drawable.hang9,
			R.drawable.hang10 };
	private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";

	private OnPageFinishedListener listener;
	private View mRootView;
	private Word mItem;
	private List<Button> mBtnList;
	private TextView mWordTextView;
	private ImageView mHangmanImage;

	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	public static GamePageFragment newInstance(Object item) {
		Log.i(TAG, "newInstance");
		GamePageFragment fragment = new GamePageFragment();
		fragment.mItem = (Word) item;
		return fragment;
	}

	public GamePageFragment() {
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		Log.i(TAG, "onAttach");
		this.listener = (OnPageFinishedListener) activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.i(TAG, "onCreateView");
		mRootView = inflater.inflate(R.layout.game_screen_fragment, container, false);
		
		// Initialize text view
		mWordTextView = (TextView) mRootView.findViewById(R.id.guessing_word);
		mWordTextView.setText(getMaskWord());

		// Initialize image view
		mHangmanImage = (ImageView) mRootView.findViewById(R.id.hangman);
		setupImageView();
		
		// Get and hold all buttons for future use
		mBtnList = getAllButtons();
		setupAllButtons();
		
		return mRootView;
	}

	private void setupImageView() {
		String pageStatus = mItem.getExtra(PAGE_STATUS);
		if(PAGE_WIN.equals(pageStatus)){
			mHangmanImage.setImageResource(R.drawable.face_smile);
		}else if(PAGE_FAIL.equals(pageStatus)) {
			mHangmanImage.setImageResource(R.drawable.face_worried);
		}else{
			int moves = mItem.getIntExtra(MOVES);
			mHangmanImage.setImageResource(HANGMAN_PICS[moves]);
		}
	}
	
	/**
	 * Setup buttons' listener, enable etc.
	 */
	private void setupAllButtons() {
		boolean isPageDone = mItem.getExtra(PAGE_STATUS) != null;
		String pressedKeys = mItem.getExtra(PRESSED_KEYS, "");
		for (Button btn : mBtnList) {
			String key = btn.getText().toString().toLowerCase(ENGLISH);
			
			// Disable pressed keys
			if(isPageDone || pressedKeys.contains(key)){
				btn.setEnabled(false);
				continue;
			}
			
			// Set event listener for available buttons
			btn.setOnClickListener(this);
		}
	}

	@Override
	public void onClick(View v) {

		String letter = ((Button) v).getText().toString().toLowerCase(ENGLISH);
		// Update pressed keys
		String pressedKeys = mItem.getExtra(PRESSED_KEYS, "") + letter;
		mItem.putExtra(PRESSED_KEYS, pressedKeys);

		// Update mask word
		String maskWord = getMaskWord();
		mWordTextView.setText(maskWord);

		// Update moves and image if the letter doesn't exist in this word.
		if (!maskWord.toLowerCase(ENGLISH).contains(letter)) {
			int moves = mItem.getIntExtra(MOVES) + 1;
			mItem.putIntExtra(MOVES, moves);
			mHangmanImage.setImageResource(HANGMAN_PICS[moves]);
		}

		// Disable pressed button
		v.setEnabled(false);

		checkAnswer();
	}

	private String getMaskWord() {
		String pressedKeys = mItem.getExtra(PRESSED_KEYS, "");

		// Add space between each letter.
		String maskWord = mItem.getWord().replaceAll(".", "$0 ").trim();

		// Replace the letter which has not been selected to "_".
		for (char letter : ALPHABET.toCharArray()) {
			// Skip letters which word does not contain or the key has been
			// pressed.
			if (maskWord.indexOf(letter) == -1 || pressedKeys.indexOf(letter) != -1)
				continue;

			// Change letters to "_". (?i) case insensitive.
			maskWord = maskWord.replaceAll("(?i)" + letter, "_");
		}

		return maskWord;
	}

	public void checkAnswer() {
		String rightMaskWord = mItem.getWord().replaceAll(".", "$0 ").trim();
		int moves = mItem.getIntExtra(MOVES);
		final boolean isWin = rightMaskWord.equals(mWordTextView.getText().toString());
		boolean isOutOfMove = moves == HANGMAN_PICS.length - 1;

		// Game Over, Win or out of move
		if (isWin || isOutOfMove) {

			// Show smile face or worried face after 300ms.
			mHangmanImage.postDelayed(new Runnable() {
				@Override
				public void run() {
					mHangmanImage.setImageResource(isWin ? R.drawable.face_smile : R.drawable.face_worried);
				}
			}, 300);

			listener.onPageFinished(mItem, isWin);
			
			mItem.putExtra(PAGE_STATUS, isWin ? PAGE_WIN : PAGE_FAIL);
			
			// Disable all buttons
			setupAllButtons();
		}
	}

	/**
	 * Initiate button list
	 * 
	 * @return
	 */
	private List<Button> getAllButtons() {
		List<Button> btnList = new ArrayList<Button>();
		for (int groupId : BUTTON_GROUPS) {
			LinearLayout btnGroup = (LinearLayout) mRootView.findViewById(groupId);
			for (int i = 0; i < btnGroup.getChildCount(); i++) {
				btnList.add((Button) btnGroup.getChildAt(i));
			}
		}
		return btnList;
	}

	/**
	 * Use this interface interactive with activity.
	 * @author Nan Wu
	 */
	public interface OnPageFinishedListener {
		public void onPageFinished(Word itme, boolean isWin);
	}

}