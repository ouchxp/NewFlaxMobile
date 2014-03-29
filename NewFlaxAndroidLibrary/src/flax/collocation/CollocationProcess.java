/*
 * File: flax.collocation.CollocationActivityProcess
 *
 * License: Greenstone 3 (GSDL3)
 * Copyright (C) 2003 New Zealand Digital Libraries, University Of Waikato
 * Greenstone3 comes with ABSOLUTELY NO WARRANTY; for details see LICENSE.txt
 * This is free software, and you are welcome to redistribute it
 * 
 * Reference: http://developer.android.com
 * 
 * Notes: This file is used to compare existing and new activities.
 * If new activities are found, download corresponding collocations
 * through the use of CollocationNetworkDownload class.
 */
package flax.collocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.widget.Toast;
import flax.activity.ActivityItem;
import flax.activity.ActivityProcess;
import flax.asynctask.BackgroundDownloadExercisesContent;
import flax.data.exercise.Exercise;
import flax.database.DatabaseManager;
import flax.utils.GlobalConstants;
import flax.utils.SPHelper;

/**
 * CollocationActivityProcess Class
 * 
 * Extends ActivityProcess class. This class is used to compare existing and new
 * activities. Any new activities are saved into the internal database. If new
 * activities are found, download corresponding collocations through the use of
 * CollocationNetworkDownload class.
 * 
 * Note: All classes relating to collocations have been stored in the
 * FlaxAndroidLibrary as they may be used by multiple games. ie. Collocation
 * Dominoes, Collocation Matching etc.
 * 
 * @author Jemma Konig
 */
public class CollocationProcess extends ActivityProcess {

	// Declare context for process
	protected final Context context;
	// Declare variables for process
	protected DatabaseManager dbManager;
	protected List<Exercise> downloadedExercises;

	/*
	 * CollocationActivityProcess class constructor
	 * 
	 * @param c - context
	 * 
	 * @param n - listArray of new activities
	 * 
	 * @param e - listArray of existing activities
	 */
	public CollocationProcess(List<Exercise> d, Context c) {
		// super(n, c);
		// this.activityType = type;
		context = c;
		downloadedExercises = d;
	}

	/*
	 * processNewActivities method
	 * 
	 * This method calls checkNewActivities() and displays a toast message
	 * depending on the outcome.
	 */
	public void processNewExercises() {

		// download new activities
		downloadNewExerciseContent();

		// If there is trouble with the server connection
		if (getDownloadStatus() == false) {
			Toast.makeText(
					context,
					"There has been an error in your connection, "
							+ "if you have used a custom server path, please check that it is correct.",
					Toast.LENGTH_LONG).show();
		}

		// If there are no new activities ...
		else if (downloadedExercises.isEmpty()) {
			Toast.makeText(context, "No new exercises. " + "Press 'Play' to see existing exercises", Toast.LENGTH_SHORT)
					.show();
		}
		// If there are new activities ...
		else {
			Toast.makeText(context, "New exercises saved. " + "Press 'Play' to see all exercises", Toast.LENGTH_SHORT)
					.show();
		}
	}

	/**
	 * getDownloadStatus method get if download is done
	 */
	public boolean getDownloadStatus() {
		return SPHelper.getBoolean(GlobalConstants.DOWNLOAD_STATUS_KEY, false);
	}

	/**
	 * downloadNewExerciseContent method
	 * 
	 * downloads content for any new exercises.
	 * 
	 */
	public void downloadNewExerciseContent() {
		// get "new"(doesn't exist in db) exercises.
		List<Exercise> newExecs = getNewExercises(downloadedExercises);

		if (!newExecs.isEmpty()) {
			// otherwise save new exercises
			saveNewExercises(newExecs);

			// call async download new exercises content
			BackgroundDownloadExercisesContent download = new BackgroundDownloadExercisesContent(context);
			// TODO:change xmlParser
			download.execute(newExecs.toArray(new Exercise[] {}));
		} else {
			// If there are no new exercises, clear exercises list
			downloadedExercises.clear();
		}
	}

	/**
	 * getNewExercises method
	 * 
	 * get "new" exercises, which doesn't exist in db from downloaded exercises.
	 */
	private List<Exercise> getNewExercises(List<Exercise> execs) {
		// Declare db for check
		dbManager = new DatabaseManager(context);
		// Store new Items in holder with url as key
		Map<String, Exercise> newItemHolder = new HashMap<String, Exercise>();
		for (Exercise ne : execs) {
			newItemHolder.put(ne.getUrl(), ne);
		}

		// get the new urls which is already existing urls in the db
		List<String> existingUrls = dbManager.selectExistUrls(newItemHolder.keySet().toArray(new String[] {}));

		// remove the existing items (keep the new items)
		for (String url : existingUrls) {
			newItemHolder.remove(url);
		}

		return new ArrayList<Exercise>(newItemHolder.values());
	}

	/**
	 * saveNewExercises method
	 * 
	 * This method takes the downloaded exercise list and compares it to the
	 * existing exercises, saving any new exercises in the database.
	 */
	private void saveNewExercises(List<Exercise> newExercises) {
		long rowId = 0;
		// add new items to db
		for (Exercise ne : newExercises) {
			rowId = dbManager.addActivity(ne.getId(), ne.getCategory_id(), ne.getType(), ne.getName(),
					ne.getUrl(), ne.getStatus(), (int) rowId, 0);
			ne.setUniqueId((int) rowId);
		}

	}

	// protected String getDataClassPath() {
	// return "flax.data." + this.activityType.replaceAll("_",
	// "").toLowerCase(Locale.ENGLISH) + ".";
	// }
	//
	// protected Class<?> getDataClass() {
	// Class<?> c = null;
	// try {
	// c = Class.forName(getDataClassPath() + "Response");
	// } catch (ClassNotFoundException e) {
	// e.printStackTrace();
	// }
	// return c;
	// }

	// end of async task
} // end of class
