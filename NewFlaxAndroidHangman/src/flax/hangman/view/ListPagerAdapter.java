package flax.hangman.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.j256.ormlite.dao.Dao;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class ListPagerAdapter<E,ID> extends FragmentPagerAdapter {
	private SparseArray<Fragment> mFrags = new SparseArray<Fragment>();
	private List<E> mItems;
	private Dao<E, ID> mItemDao;
	public ListPagerAdapter(FragmentManager fm, Collection<E> items, Dao<E, ID> itemDao) {
		super(fm);
		mItems = new ArrayList<E>(items);
		this.mItemDao = itemDao;
	}

	@Override
	public Fragment getItem(int position) {
		return new GamePageFragment();
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
		mFrags.put(position, f);
		return f;
	}
	
	public void updateDataSet(Collection<E> items) {
		mItems = new ArrayList<E>(items);
		notifyDataSetChanged();
	}
	
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        mFrags.remove(position);
        super.destroyItem(container, position, object);
    }

    public Fragment getFragment(int position) {
        return mFrags.get(position);
    }
}