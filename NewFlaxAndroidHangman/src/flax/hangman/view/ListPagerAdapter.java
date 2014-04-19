package flax.hangman.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class ListPagerAdapter<E> extends FragmentPagerAdapter {

	private List<E> mItems;
	public ListPagerAdapter(FragmentManager fm, Collection<E> items) {
		super(fm);
		mItems = new ArrayList<E>(items);
	}

	@Override
	public Fragment getItem(int position) {
		// getItem is called to instantiate the fragment for the given page.
		// Return a PlaceholderFragment (defined as a static inner class
		// below).
		
		return GamePageFragment.newInstance(mItems.get(position));
	}

	@Override
	public int getCount() {
		return mItems.size();
	}
}