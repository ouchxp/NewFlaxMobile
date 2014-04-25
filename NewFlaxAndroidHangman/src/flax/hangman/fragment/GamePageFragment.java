package flax.hangman.fragment;

import static flax.hangman.utils.LocalConstants.*;
import static flax.utils.GlobalConstants.*;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import flax.baseview.BasePageFragment;
import flax.entity.hangman.Word;
import flax.hangman.R;

public class GamePageFragment extends BasePageFragment<Word>{

	/** Default Constructor */
	public GamePageFragment() {}

	private View mRootView;
	private List<Button> mBtnList;
	private TextView mWordTextView;
	private ImageView mHangmanImage;
	
	private OnPageEventListener listener;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Keep the fragment object when activity recreate.
		setRetainInstance(true);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		// Setup listener in order to dispatch event to activity.
		this.listener = (OnPageEventListener) activity;
	}

	/**
	 * Initiate View
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.game_screen_fragment, container, false);
		
		// Initialize text view
		mWordTextView = (TextView) mRootView.findViewById(R.id.guessing_word);
		mWordTextView.setText(getMaskWord());

		// Initialize image view
		mHangmanImage = (ImageView) mRootView.findViewById(R.id.hangman);
		setUpImageView();
		
		// Get and hold all buttons for future use
		mBtnList = getAllButtons();
		setUpAllButtons();
		
		return mRootView;
	}

	/**
	 * Setup image view, decide which image should be shown. 
	 */
	private void setUpImageView() {
		int pageStatus = mItem.getPageStatus();
		switch (pageStatus) {
		case PAGE_WIN:
			mHangmanImage.setImageResource(R.drawable.face_smile);
			break;
		case PAGE_FAIL:
			mHangmanImage.setImageResource(R.drawable.face_worried);
			break;
		default:
			int moves = mItem.getIntExtra(MOVES);
			mHangmanImage.setImageResource(HANGMAN_PICS[moves]);
			break;
		}
	}
	
	/**
	 * Setup buttons' listener, enable etc.
	 */
	private void setUpAllButtons() {
		// Page status is fail or win means page is done.
		boolean isPageDone = (mItem.getPageStatus() == PAGE_FAIL || mItem.getPageStatus() == PAGE_WIN);
		String pressedKeys = mItem.getExtra(PRESSED_KEYS, "");
		for (Button btn : mBtnList) {
			String letter = btn.getText().toString().toLowerCase(ENGLISH);
			
			// Disable pressed buttons, and when page is done, disable all buttons.
			if(isPageDone || pressedKeys.contains(letter)){
				btn.setEnabled(false);
				continue;
			}
			
			// Set event listener for available buttons
			btn.setOnClickListener(this);
		}
	}

	/**
	 * Initiate button list
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
	 * Will be called when user click any letter button.
	 */
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
		
		// Dispatch event to activity, in order to update summary's start/end time 
		listener.onPageInteracted(mItem);
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

	/**
	 * Fragments of game page must implement this method.
	 * This method will be called when user choose "Check Answer" in menu.
	 */
	@Override
	public void checkAnswer() {
		String currMaskWord = mWordTextView.getText().toString();
		String rightMaskWord = mItem.getWord().replaceAll(".", "$0 ").trim();
		int moves = mItem.getIntExtra(MOVES);
		final boolean isWin = rightMaskWord.equals(currMaskWord);
		boolean isOutOfMove = moves == HANGMAN_PICS.length - 1;

		// Game Over, Win or out of move
		if (isWin || isOutOfMove) {
			
			// Disable all buttons
			setUpAllButtons();

			// Show smile face or worried face after 300ms.
			mHangmanImage.postDelayed(new Runnable() {
				@Override
				public void run() {
					mHangmanImage.setImageResource(isWin ? R.drawable.face_smile : R.drawable.face_worried);
				}
			}, 300);
			
			mItem.setPageStatus(isWin ? PAGE_WIN : PAGE_FAIL);
			
			// Save the status for score calculation
			updateItem(mItem);
		}
		
		// Call activity to calculate score
		if(isWin){
			listener.onPageWin(mItem);
		}
	}
	
	/**
	 * Use this interface interactive with activity.
	 * @author Nan Wu
	 */
	public interface OnPageEventListener {
		public void onPageWin(Word itme);
		public void onPageInteracted(Word itme);
	}
}
