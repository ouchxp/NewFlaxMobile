package flax.hangman.entity;

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
	
	@DatabaseField(foreign = true, foreignAutoRefresh = true, columnName="response_foreign_id") 
	private HangmanExerciseDetail exercise;
	
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

	public HangmanExerciseDetail getExercise() {
		return exercise;
	}

	public void setExercise(HangmanExerciseDetail exercise) {
		this.exercise = exercise;
	}
}
