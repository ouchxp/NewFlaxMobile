package flax.template.fragment;

import static flax.template.utils.LocalConstants.*;
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
import flax.template.R;
import flax.template.entity.TemplatePage;

public class GamePageFragment extends BasePageFragment<TemplatePage> {

	/** Default Constructor */
	public GamePageFragment() {
	}

	private View mRootView;
	
	//TODO: Declare views and field variables here
	/**This is an example*/
	/*
	private List<Button> mBtnList;
	private TextView mWordTextView;
	private ImageView mHangmanImage;
	*/
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Set RetainInstance to retain status.
		setRetainInstance(true);
	}

	/**
	 * Initiate View
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.fragment_game_page, container, false);

		//TODO: Initialize views and do some initialization operation.
		/**This is an example*/
		/*
		// Initialize text view
		mWordTextView = (TextView) mRootView.findViewById(R.id.guessing_word);
		mWordTextView.setText(getMaskWord());

		// Initialize image view
		mHangmanImage = (ImageView) mRootView.findViewById(R.id.hangman);
		setUpImageView();

		// Get and hold all buttons for future use
		mBtnList = getAllButtons();
		setUpAllButtons();
		*/

		return mRootView;
	}

	/**
	 * Fragments of game page must implement this method. This method will be
	 * called when user choose "Check Answer" in menu.
	 */
	@Override
	public void checkAnswer() {
		
		// TODO: perform check answer operation, set win/fail status for this page item
		// and save data change of this page 
		
		/**This is an example*/
		/*
		String currMaskWord = mWordTextView.getText().toString();
		String rightMaskWord = mItem.getWord().replaceAll(".", "$0 ").trim();
		int moves = mItem.getIntExtra(MOVES);
		final boolean isWin = rightMaskWord.equals(currMaskWord);
		boolean isOutOfMove = moves == HANGMAN_PICS.length - 1;

		// Game Over, Win or out of move
		if (isWin || isOutOfMove) {
			mItem.setPageStatus(isWin ? PAGE_WIN : PAGE_FAIL);

			// Show smile face or worried face after 300ms.
			mHangmanImage.setImageResource(isWin ? R.drawable.img_face_smile : R.drawable.img_face_worried);

			// Set up buttons depends on item status, in this case Disable all buttons
			setUpAllButtons();

			// Save the status to database for score calculation
			updateItem(mItem);
		}
		*/

		// call this method when checkAnswer process finished in order to
		// dispatch event to activity, in order to update summary's start/end
		// time and calculate score
		/** Must call this method when checkAnswer process finished */
		mListener.onPageAnswerChecked(mItem);
	}

}
