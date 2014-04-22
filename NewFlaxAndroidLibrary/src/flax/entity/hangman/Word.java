package flax.entity.hangman;

import org.simpleframework.xml.Text;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import flax.entity.base.BasePage;

@DatabaseTable(tableName="hangman_word")
public class Word extends BasePage{
	@DatabaseField(generatedId=true)
	private int id;
	
	@Text
	@DatabaseField
	private String word;
	
	@DatabaseField(foreign = true, foreignAutoRefresh = true,columnName="response_foreign_id") 
	private HangmanExercise exercise;
	
	/** Constructor */
	public Word() {}

	/** Get/Set Methods */
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public HangmanExercise getExercise() {
		return exercise;
	}

	public void setExercise(HangmanExercise exercise) {
		this.exercise = exercise;
	}
}
