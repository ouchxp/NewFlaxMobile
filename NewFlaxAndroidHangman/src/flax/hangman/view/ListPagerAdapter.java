package flax.hangman.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.j256.ormlite.dao.Dao;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class ListPagerAdapter<E,ID> extends FragmentPagerAdapter {

	private List<E> mItems;
	private Dao<E, ID> mItemDao;
	public ListPagerAdapter(FragmentManager fm, Collection<E> items, Dao<E, ID> itemDao) {
		super(fm);
		mItems = new ArrayList<E>(items);
		this.mItemDao = itemDao;
	}

	@Override
	public Fragment getItem(int position) {
		// getItem is called to instantiate the fragment for the given page.
		// Return a PlaceholderFragment (defined as a static inner class
		// below).
		
		return GamePageFragment.newInstance(mItems.get(position), mItemDao);
	}

	@Override
	public int getCount() {
		return mItems.size();
	}
}