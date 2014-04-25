package flax.baseview;

import java.sql.SQLException;

import android.support.v4.app.Fragment;
import android.view.View.OnClickListener;

import com.j256.ormlite.dao.Dao;

/**
 * A placeholder fragment containing a simple view.
 * Fragments of game page must extends this class
 */
public abstract class BasePageFragment<P> extends Fragment implements OnClickListener {
	public static final String TAG = "GamePageFragment";
	protected P mItem;
	protected Dao<P, ?> mItemDao;

	public void setPageData(P item, Dao<P, ?> itemDao) {
		mItem = item;
		if (itemDao != null) {
			mItemDao = itemDao;
		}
	}

	/**
	 * Fragments of game page must implement this method.
	 * This method will be called when user choose "Check Answer" in menu.
	 */
	public abstract void checkAnswer();
	
	@Override
	public void onStop() {
		super.onStop();
		// Save information when page become not visible.
		updateItem(mItem);
	}
	
	/**
	 * Update single item to database.
	 * @param item
	 * @return recode numbers updated.
	 */
	public int updateItem(P item){
		int count = 0;
		try {
			count = mItemDao.update(item);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return count;
	}
}