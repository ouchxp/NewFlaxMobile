package flax.collocation;

import java.util.List;

import android.content.Context;
import android.os.AsyncTask;
import flax.data.exercise.Exercise;

// TODO: get Class converter
/**
 * BackgroundDownloadExercises class
 * 
 * Async task, downloads collocations for each new activity.
 */
public class BackgroundDownloadExercisesContent extends AsyncTask<Exercise, Void, String> {
	private final Context context;
	public BackgroundDownloadExercisesContent(Context context){
		this.context = context;
	}

	/*
	 * doInBackground method
	 * 
	 * Async task that connects to the server and Retrieves the required xml
	 * file using the NetworkHttpRequest class
	 * 
	 * @param urls, first item in stringArray holds the url
	 * 
	 * @return result, passed into the onPostExecute method
	 * 
	 * @see android.os.AsyncTask#doInBackground(Params[])
	 */
	@Override
	protected String doInBackground(Exercise... execs) {
		try {
			// Go through each new activity and download corresponding
			// collocations
			for (Exercise e : execs) {
				int i = 0;

				// Download collocations
				String url = e.getUrl();
				CollocationNetworkDownload collocationDownload = new CollocationNetworkDownload(context);
				collocationDownload.downloadCollocations(url);
				List<CollocationItem> collocations = collocationDownload.getCollocationList();

				// Set database manager
				CollocationDatabaseManager dbManager = new CollocationDatabaseManager(context);
				
				// Add collocations to db - note i = index (collocation
				// order)
				for (CollocationItem c : collocations) {
					dbManager.addCollocation(c.collocationId, i, c.type, c.fre, c.sendId, c.word, "none",
							"none", "none", c.getBaseWord(), e.getUniqueId());
					i++;
				}

				// Save word count
				dbManager.updateActivityWordCount(collocations.size(), e.getUniqueId());

				// Add initial entry to summary report table in db
				dbManager.addSummary("", "", 0, 0, e.getUniqueId());

			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}