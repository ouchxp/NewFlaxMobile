package flax.baseview;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.util.SparseArray;
import android.view.ViewGroup;
import flax.database.FlaxDao;
import flax.entity.base.BasePage;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to one
 * of the sections/tabs/pages.
 */
public class ListPagerAdapter<FRG extends BasePageFragment<PAGE>, PAGE extends BasePage> extends FragmentPagerAdapter {
	private static final String TAG = "ListPagerAdapter";
	private SparseArray<BasePageFragment<PAGE>> mFrags = new SparseArray<BasePageFragment<PAGE>>();
	private List<PAGE> mItems;
	private FlaxDao<PAGE, ?> mItemDao;
	private Class<FRG> mFragmentClass;

	public ListPagerAdapter(FragmentManager fm, Collection<PAGE> pageItems, FlaxDao<PAGE, ?> itemDao, Class<FRG> fragmentClass) {
		super(fm);
		mItems = new ArrayList<PAGE>(pageItems);
		this.mItemDao = itemDao;
		this.mFragmentClass = fragmentClass;
	}

	/**
	 * getItem should only be used by adapter it self, create simple fragment
	 * object in order to initiate viewpager. Get fragment objects should use
	 * getFragment method.
	 */
	@Override
	public Fragment getItem(int position) {
		Fragment f = null;
		try {
			f = (BasePageFragment<PAGE>) mFragmentClass.newInstance();
		} catch (InstantiationException e) {
			Log.e(TAG, "fragment class must have a default non-argument constructor.");
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			Log.e(TAG, "fragment class must have a default non-argument constructor.");
			throw new RuntimeException(e);
		} catch (ClassCastException e) {
			Log.e(TAG, "fragment class must extends GamePageFragment");
			throw e;
		}
		return f;
	}

	@Override
	public int getCount() {
		return mItems.size();
	}

	/**
	 * Ensure that when dataSet changed fragment will be recreate.
	 */
	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}

	/**
	 * instantiateItem is the place where should pass object arguments to
	 * fragments.
	 */
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		@SuppressWarnings("unchecked")
		BasePageFragment<PAGE> f = (BasePageFragment<PAGE>) super.instantiateItem(container, position);
		f.setPageData(mItems.get(position), mItemDao);
		mFrags.put(position, f);
		return f;
	}

	/**
	 * Update dataSet of this adapter, and then notifyDataSetChanged will be
	 * called to recreate fragments.
	 * 
	 * @param items
	 */
	public void updateDataSet(Collection<PAGE> items) {
		if (items != null) {
			mItems = new ArrayList<PAGE>(items);
		}
		super.notifyDataSetChanged();
	}

	/**
	 * When distoryItem remove item from fragment list.
	 */
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		mFrags.remove(position);
		super.destroyItem(container, position, object);
	}

	/**
	 * This allow use to get fragment object in specific position.
	 * 
	 * @param position
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public FRG getFragment(int position) {
		return (FRG) mFrags.get(position);
	}

	/**
	 * <b>Not recommend</b> to use this method, should use updateDataSet
	 * instead. Cause updateDataSet provide an argument to allow updating
	 * dataSet.<br>
	 * This is because of when using <b>lazy collection</b> in OrmLite, retrieve
	 * element from lazy collection multiple times will return different objects
	 * represent database records of retrieving time point. <br>
	 * That means the feature of "pass by reference" is hard to gain on dataSet.
	 * Modify different objects won't affect dataSet's value.<br>
	 * That could be problematic when using <b>lazy collection</b>, so It's
	 * better to reset dataSet using updateDataSet method.
	 * 
	 * @see http
	 *      ://ormlite.com/javadoc/ormlite-core/doc-files/ormlite_2.html#Foreign
	 *      -Collection
	 */
	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	}
}