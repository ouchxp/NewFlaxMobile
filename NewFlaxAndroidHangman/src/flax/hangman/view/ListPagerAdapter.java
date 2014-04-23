package flax.hangman.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.j256.ormlite.dao.Dao;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class ListPagerAdapter<E,ID> extends FragmentPagerAdapter {
	private FragmentManager mFragmentManager;
	private List<E> mItems;
	private Dao<E, ID> mItemDao;
	public ListPagerAdapter(FragmentManager fm, Collection<E> items, Dao<E, ID> itemDao) {
		super(fm);
		mFragmentManager = fm;
		mItems = new ArrayList<E>(items);
		this.mItemDao = itemDao;
	}

	@Override
	public Fragment getItem(int position) {
		// getItem is called to instantiate the fragment for the given page.
		// Return a PlaceholderFragment (defined as a static inner class
		// below).
		return new GamePageFragment();
		//return GamePageFragment.newInstance(mItems.get(position), mItemDao);
	}

	@Override
	public int getCount() {
		return mItems.size();
	}
	
	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}
	
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		GamePageFragment f = (GamePageFragment) super.instantiateItem(container, position);
		f.updatePageData(mItems.get(position), mItemDao);
		return f;
	}
	
	public void updateDataSet(Collection<E> items) {
		mItems = new ArrayList<E>(items);
		notifyDataSetChanged();
	}
	
//	public Fragment getActiveFragment(ViewPager container, int position) {
//	    String name = makeFragmentName(container.getId(), position);
//		return  mFragmentManager.findFragmentByTag(name);
//	}
//
//	private static String makeFragmentName(int viewId, int index) {
//	    return "android:switcher:" + viewId + ":" + index;
//	}
}