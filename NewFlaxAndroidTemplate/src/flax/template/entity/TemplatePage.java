package flax.template.entity;

import org.simpleframework.xml.Text;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import flax.entity.base.BasePage;

/**
 * The data entity of one page of game. (corresponding xxx tag in xml)
 * @author Nan Wu
 */
//TODO: Define corresponding database table name in @DatabaseTable annotation
@DatabaseTable(tableName="hangman_word")
//The data entity of one page of game must extends BasePage class.
public class TemplatePage extends BasePage{
	
	/** 
	 * This is an example, recommend use private field, and implement get/set methods to use
	 * these fields
	 * @see http://simple.sourceforge.net/download/stream/doc/tutorial/tutorial.php#deserialize 
	 * @see http://ormlite.com/javadoc/ormlite-core/doc-files/ormlite_2.html#Using
	 */
	/*
	@DatabaseField(generatedId=true)
	private int id;
	
	@Text
	@DatabaseField
	private String word;
	
	@DatabaseField(foreign = true, foreignAutoRefresh = true,columnName="response_foreign_id") 
	private TempleteExerciseDetail exercise;
	*/
	
	/** Constructor (Entity class must have a empty public constructor)*/
	public TemplatePage() {}

	/** Get/Set Methods */
	/*
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

	public TempleteExerciseDetail getExercise() {
		return exercise;
	}

	public void setExercise(TempleteExerciseDetail exercise) {
		this.exercise = exercise;
	}
	*/
}
