package flax.hangman.fragment;

import static flax.hangman.utils.LocalConstants.*;
import static flax.utils.GlobalConstants.*;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import flax.baseview.BasePageFragment;
import flax.hangman.R;
import flax.hangman.entity.Word;

public class GamePageFragment extends BasePageFragment<Word> {

	/** Default Constructor */
	public GamePageFragment() {
	}

	private View mRootView;
	private List<Button> mBtnList;
	private TextView mWordTextView;
	private ImageView mHangmanImage;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}

	/**
	 * Initiate View
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.fragment_game_page, container, false);

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
			mHangmanImage.setImageResource(R.drawable.img_face_smile);
			break;
		case PAGE_FAIL:
			mHangmanImage.setImageResource(R.drawable.img_face_worried);
			break;
		default:
			int moves = mItem.getIntExtra(MOVES);
			mHangmanImage.setImageResource(HANGMAN_PICS[moves]);
			break;
		}
	}

	/**
	 * Set up buttons depends on item status, so should call this after item
	 * status changed, not before. Setup buttons' listener, enable etc.
	 */
	private void setUpAllButtons() {
		// Page status is fail or win means page is done.
		boolean isPageDone = (mItem.getPageStatus() == PAGE_FAIL || mItem.getPageStatus() == PAGE_WIN);
		String pressedKeys = mItem.getExtra(PRESSED_KEYS, "");
		for (Button btn : mBtnList) {
			String letter = btn.getText().toString().toLowerCase(ENGLISH);

			// Disable pressed buttons, and when page is done, disable all
			// buttons.
			if (isPageDone || pressedKeys.contains(letter)) {
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
	}

	private String getMaskWord() {
		String pressedKeys = mItem.getExtra(PRESSED_KEYS, "");

		// Add space between each letter.
		String maskWord = mItem.getWord().replaceAll(".", "$0 ").trim();

		// Replace the letter which has not been selected to "_".
		for (char letter : ALPHABET.toCharArray()) {
			// Skip letters which word does not contain
			if (maskWord.toLowerCase(ENGLISH).indexOf(letter) == -1)
				continue;
			
			 //Skip letters which the key has been pressed.
			if ((pressedKeys.indexOf(letter) != -1))
				continue;

			// Change letters to "_". (?i) case insensitive.
			maskWord = maskWord.replaceAll("(?i)" + letter, "_");
		}

		return maskWord;
	}

	/**
	 * Fragments of game page must implement this method. This method will be
	 * called when user choose "Check Answer" in menu.
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
			mItem.setPageStatus(isWin ? PAGE_WIN : PAGE_FAIL);

			// Show smile face or worried face after 300ms.
			mHangmanImage.postDelayed(new Runnable() {
				@Override
				public void run() {
					mHangmanImage.setImageResource(isWin ? R.drawable.img_face_smile : R.drawable.img_face_worried);
				}
			}, 300);

			// Set up buttons depends on item status, in this case Disable all
			// buttons
			setUpAllButtons();

			// Save the status to database for score calculation
			updateItem(mItem);
		}

		// call this method when checkAnswer process finished in order to
		// dispatch event to activity, in order to update summary's start/end
		// time and calculate score
		/** Must call this method when checkAnswer process finished */
		mListener.onPageAnswerChecked(mItem);
	}

}
