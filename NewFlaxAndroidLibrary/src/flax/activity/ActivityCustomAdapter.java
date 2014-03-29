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

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import flax.activity.Item;
import flax.library.R;

import java.util.ArrayList;
 
/**
 * CustomAdapter Class
 * 
 * This class is used to create a custom adapter so that the 
 * activities can be displayed in a certain way in the list view.
 * 
 * @author Jemma Konig
 */
public class ActivityCustomAdapter extends ArrayAdapter<Item> {
 
		// Declare variables for adapter
        private final Context context;
        private final ArrayList<Item> itemsArrayList;
 
        /* Class constructor */
        public ActivityCustomAdapter(Context context, ArrayList<Item> itemsArrayList) {
            super(context, R.layout.list_row, itemsArrayList);
            this.context 					= context;
            this.itemsArrayList 			= itemsArrayList;
        }
 
        /*
         * getView method
         * 
         * This method sets the layout of the rows that the adapter creates
         * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
        	//TODO: To be optimized
            // Create inflater 
            LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
 
            // Get rowView from inflater
            View rowView = inflater.inflate(R.layout.list_row, parent, false);
            
            // Get the two text view from the rowView
            TextView labelView = (TextView) rowView.findViewById(R.id.label);
            TextView valueView = (TextView) rowView.findViewById(R.id.value);
            labelView.setTextColor(R.drawable.style_text_selector);
            valueView.setTextColor(R.drawable.style_text_selector);
 
            // Set the text for textView 
            labelView.setText(itemsArrayList.get(position).getTitle());
            valueView.setText(itemsArrayList.get(position).getStatus());
 
            // return rowView
            return rowView;
        }
} // end of class
