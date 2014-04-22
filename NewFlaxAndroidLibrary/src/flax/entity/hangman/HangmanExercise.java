package flax.entity.hangman;

import java.util.Collection;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.core.Commit;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import flax.entity.base.BaseExercise;

@DatabaseTable(tableName = "hangman_exercise")
@Root(name = "response")
public class HangmanExercise extends BaseExercise {
	
	@DatabaseField
	@Attribute
	@Path("player")
	private String hints;

	@DatabaseField
	@Attribute(name = "from")
	private String from;

	@ForeignCollectionField(eager = false, foreignFieldName = "exercise")
	@Path("player")
	@ElementList(inline = true, entry = "word")
	private Collection<Word> words;

	/** Constructor */
	public HangmanExercise() {
	}

	/**
	 * Build one-to-many relation, prepare for orm.
	 */
	@Commit
	private void buildRelation() {
		for (Word word : words) {
			word.setExercise(this);
		}
	}

	/** Get/Set Methods */
	public String getHints() {
		return hints;
	}

	public void setHints(String hints) {
		this.hints = hints;
	}

	public Collection<Word> getWords() {
		return words;
	}

	public void setWords(Collection<Word> words) {
		this.words = words;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}
}
