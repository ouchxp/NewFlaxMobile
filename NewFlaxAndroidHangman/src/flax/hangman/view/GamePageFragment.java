package flax.hangman.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import flax.entity.hangman.Word;
import flax.hangman.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class GamePageFragment extends Fragment {
	
	private Word mItem;

	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	public static GamePageFragment newInstance(Object item) {
		GamePageFragment fragment = new GamePageFragment();
		fragment.mItem = (Word)item;
		return fragment;
	}

	public GamePageFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.game_screen_fragment, container, false);
		TextView textView = (TextView) rootView.findViewById(R.id.activity_template_text);
		textView.setText(mItem.getWord());
		return rootView;
	}
}