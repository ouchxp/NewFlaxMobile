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
package flax.activity;

import static flax.utils.GlobalConstants.*;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
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
public class ExerciseListAdapter extends ArrayAdapter<Exercise> {
	public static final String TAG = "ExerciseListAdapter";

	// Declare variables for adapter
	private LayoutInflater mInflater;

	/* Class constructor */
	public ExerciseListAdapter(Context context, List<Exercise> exercises) {
		super(context, R.layout.list_screen_item, exercises);
		mInflater = LayoutInflater.from(context);
	}

	/*
	 * getView method
	 * 
	 * This method sets the layout of the rows that the adapter creates
	 * 
	 * @see android.widget.ArrayAdapter#getView(int, android.view.View,
	 * android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
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

		Exercise exercise = this.getItem(position);

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
} // end of class
