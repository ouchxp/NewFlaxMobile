package flax.template.entity;

import static flax.utils.GlobalConstants.*;

import java.util.Collection;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.core.Commit;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import flax.entity.base.BaseExerciseDetail;

/**
 * The data entity of exercise detail (corresponding response tag in xml)
 * @author Nan Wu
 */
//TODO: Define corresponding database table name in @DatabaseTable annotation
@DatabaseTable(tableName = "template_exercise")
//TODO: Define corresponding xml tag name in @Root annotation
@Root(name = "response")
//The data entity of exercise detial must extends BaseExerciseDetail class.
public class TempleteExerciseDetail extends BaseExerciseDetail {

	//TODO: Declare data fields here 

	/** 
	 * This is an example, recommend use private field, and implement get/set methods to use
	 * these fields
	 * @see http://simple.sourceforge.net/download/stream/doc/tutorial/tutorial.php#deserialize 
	 * @see http://ormlite.com/javadoc/ormlite-core/doc-files/ormlite_2.html#Using
	 */
	/*
	@DatabaseField
	@Attribute(name = "from")
	private String from;
	
	@DatabaseField
	@Attribute
	@Path("player")
	private String hints;

	@ForeignCollectionField(eager = true, maxEagerLevel = MAX_EAGER_LEVEL)
	@Path("player")
	@ElementList(inline = true, entry = "word")
	private Collection<TemplatePage> words;
	*/
	
	/** Constructor (Entity class must have a empty public constructor)*/
	public TempleteExerciseDetail() {
	}

	/**
	 * Build one-to-many relation, prepare for orm.
	 * this method will be called when this object's xml parsing process is done.
	 * @see http://simple.sourceforge.net/download/stream/doc/tutorial/tutorial.php#callback
	 */
	@Commit
	private void buildRelation() {
		//TODO: build one-to-many relation here
		
		// This is an example
		/*
		for (TemplatePage word : words) {
			word.setExercise(this);
		}
		*/
	}

	/** Get/Set Methods */
	/*
	public String getHints() {
		return hints;
	}

	public void setHints(String hints) {
		this.hints = hints;
	}

	public Collection<TemplatePage> getWords() {
		return words;
	}

	public void setWords(Collection<TemplatePage> words) {
		this.words = words;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}
	*/
}
