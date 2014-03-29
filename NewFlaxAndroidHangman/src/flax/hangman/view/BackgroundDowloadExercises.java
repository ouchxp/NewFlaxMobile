package flax.hangman.view;

import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;
import flax.collocation.CollocationProcess;
import flax.data.exercise.Exercise;
import flax.data.exercise.Response;
import flax.utils.IURLConverter;
import flax.utils.XmlParser;

/**
 * BackgroundDowloadExercises Class
 * 
 * Download and retrieve exercises from specific activity. Done in background to
 * take the load off of the HomeScreen Activity
 */
public class BackgroundDowloadExercises extends AsyncTask<String, Void, List<Exercise>> {

	private Context context;
	// Convert url to correct format.
	private IURLConverter urlConverter;

	public BackgroundDowloadExercises(Context context, IURLConverter urlConverter) {
		this.context = context;
		this.urlConverter = urlConverter;
	}

	// Declare progress bar
	private ProgressDialog progress;

	/**
	 * onPreExecute method Before the async task starts, prepare the progress
	 * bar
	 */
	@Override
	protected void onPreExecute() {
		// Prepare progress bar
		progress = new ProgressDialog(context);
		progress.setMessage("looking for new activities ...");
		progress.show();
	}

	/**
	 * doInBackground method Async task that connects to the server and
	 * Retrieves the required xml file using the NetworkHttpRequest class
	 */
	@Override
	protected List<Exercise> doInBackground(String... urls) {

		// Sleep app for one second to show progress
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// Begin parsing the xml from url
		Response response = XmlParser.fromUrl(urls[0], Response.class);

		if (response != null) {
			List<Exercise> exercises = response.getCategoryList().getCategory().getExercises();
			return exercises;
		} else {
			return null;
		}
	}

	/**
	 * onPostExecute method
	 * 
	 * After activities have been downloaded, call CollocationProcess class
	 * which saves new exercises in db and downloads their content.
	 */
	@Override
	protected void onPostExecute(List<Exercise> result) {

		if (result != null) {
			// Process activities
			formatActivityUrl(result);

			// save, and download the content of new exercises
			CollocationProcess colloProcess = new CollocationProcess( result, context);
			colloProcess.processNewExercises();
		} else {
			Toast.makeText(context, "Please try again to download the new activities.", Toast.LENGTH_SHORT).show();
		}
		// Stop progress bar
		progress.dismiss();
	}

	/*
	 * formatActivityUrl method
	 * 
	 * The url retrieved from the activity xml returns the html layout rather
	 * than the needed xml. This url needs to be altered to return the correctly
	 * formatted xml
	 */
	public List<Exercise> formatActivityUrl(List<Exercise> downloadedExercises) {

		// Create algorithm that alters the url to get the correctly formatted
		// xml.
		for (Exercise a : downloadedExercises) {
			a.setUrl(urlConverter.convert(a.getUrl()));
		}
		return downloadedExercises;
	}

} // end of async task