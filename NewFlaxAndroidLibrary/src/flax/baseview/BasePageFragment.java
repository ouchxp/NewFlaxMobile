package flax.baseview;

import android.app.Activity;
import android.support.v4.app.Fragment;
import flax.database.FlaxDao;

/**
 * A placeholder fragment containing a simple view.
 * Fragments of game page must extends this class
 */
public abstract class BasePageFragment<PAGE> extends Fragment {
	public static final String TAG = "GamePageFragment";
	protected PAGE mItem;
	protected FlaxDao<PAGE, ?> mItemDao;
	protected OnPageAnswerCheckedListener<PAGE> mListener;
	
	public void setPageData(PAGE item, FlaxDao<PAGE, ?> itemDao) {
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
		return mItemDao.update(item);
	}
	
	/**
	 * Use this interface interact with activity.
	 * @author Nan Wu
	 */
	public static interface OnPageAnswerCheckedListener<P> {
		public void onPageAnswerChecked(P itme);
	}
}