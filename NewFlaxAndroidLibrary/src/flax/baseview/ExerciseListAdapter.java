/*
 * File: flax.activity.ActivityCustomAdapter
 *
 * License: Greenstone 3 (GSDL3)
 * Copyright (C) 2003 New Zealand Digital Libraries, University Of Waikato
 * Greenstone3 comes with ABSOLUTELY NO WARRANTY; for details see LICENSE.txt
 * This is free software, and you are welcome to redistribute it
 * 
 * Reference: http://developer.android.com
 * 
 * Notes: This file is used to create a custom adapter so that the 
 * activities can be displayed in a certain way in the list view.
 */
package flax.baseview;

import static flax.utils.GlobalConstants.*;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;
import flax.entity.exerciselist.Category;
import flax.entity.exerciselist.Exercise;
import flax.library.R;

/**
 * CustomAdapter Class
 * 
 * This class is used to create a custom adapter so that the activities can be
 * displayed in a certain way in the list view.
 * 
 * @author Nan Wu
 */
public class ExerciseListAdapter extends BaseExpandableListAdapter {
	public static final String TAG = "ExpandableExerciseListAdapter";
	private static final String NONE_CATEGORY = "none";
	
	// Declare variables for adapter
	private Context mContext;
	private LayoutInflater mInflater;
	private List<Category> mCategories;
	private List<List<Exercise>> mExerciseGroups;

	/* Class constructor */
	public ExerciseListAdapter(Context context, List<Category> categories, List<List<Exercise>> exerciseGroups) {
		mInflater = LayoutInflater.from(context);
		this.mContext = context;
		this.mCategories = categories;
		this.mExerciseGroups = exerciseGroups;
	}

	/**
	 * Set up group view
	 */
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		// Get category name
		String name = mCategories.get(groupPosition).getName();
		
		// Set up "none" group view (not show "none" group view, but keep the children)
		if(NONE_CATEGORY.equals(name) || "".equals(name)){
			return new FrameLayout(mContext);
		}
		
		// Setup regular group view
		ViewHolder holder;
		if (convertView == null || convertView instanceof FrameLayout) {
			holder = new ViewHolder();
			// Create view from inflater
			convertView = mInflater.inflate(R.layout.list_screen_group, parent, false);
			// Get the text view from the group row View
			holder.labelView = (TextView) convertView.findViewById(R.id.group_name);
			// Set text style
			holder.labelView.setTextColor(R.drawable.style_text_selector);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		// Set text for textView
		holder.labelView.setText(name);

		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
			ViewGroup parent) {

		ViewHolder holder;
		if (convertView == null) {
			// Create view from inflater
			convertView = mInflater.inflate(R.layout.list_screen_item, parent, false);
			holder = new ViewHolder();

			// Get the two text view from the rowView
			holder.labelView = (TextView) convertView.findViewById(R.id.label);
			holder.valueView = (TextView) convertView.findViewById(R.id.value);

			// Set text style
			holder.labelView.setTextColor(R.drawable.style_text_selector);
			holder.valueView.setTextColor(R.drawable.style_text_selector);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Exercise exercise = mExerciseGroups.get(groupPosition).get(childPosition);

		// Set text for textView
		holder.labelView.setText(exercise.getName());

		try {
			holder.valueView.setText(EXERCISE_STATUS_NAME[exercise.getStatus()]);
		} catch (ArrayIndexOutOfBoundsException e) {
			Log.e(TAG, "GlobalConstants.EXERCISE_STATUS_NAME does not contains a name for status " + exercise.getStatus(), e);
			holder.valueView.setText(String.valueOf(exercise.getStatus()));
		}

		// return convertView
		return convertView;
	}

	static class ViewHolder {
		TextView labelView;
		TextView valueView;
	}

	@Override
	public int getGroupCount() {
		return mCategories.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return mExerciseGroups.get(groupPosition).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return mCategories.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return mExerciseGroups.get(groupPosition).get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}
	
	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
} // end of class
