package flax.baseview;

import java.sql.SQLException;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.View.OnClickListener;

import com.j256.ormlite.dao.Dao;

/**
 * A placeholder fragment containing a simple view.
 * Fragments of game page must extends this class
 */
public abstract class BasePageFragment<PAGE> extends Fragment implements OnClickListener {
	public static final String TAG = "GamePageFragment";
	protected PAGE mItem;
	protected Dao<PAGE, ?> mItemDao;
	protected OnPageAnswerCheckedListener<PAGE> mListener;
	
	public void setPageData(PAGE item, Dao<PAGE, ?> itemDao) {
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
	
	@SuppressWarnings("unchecked")
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		// Setup listener in order to dispatch event to activity.
		this.mListener = (OnPageAnswerCheckedListener<PAGE>) activity;
	}
	
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
	public int updateItem(PAGE item){
		int count = 0;
		try {
			count = mItemDao.update(item);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return count;
	}
	
	/**
	 * Use this interface interact with activity.
	 * @author Nan Wu
	 */
	public static interface OnPageAnswerCheckedListener<P> {
		public void onPageAnswerChecked(P itme);
	}
}