package flax.hangman.entity;

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

@DatabaseTable(tableName = "hangman_exercise")
@Root(name = "response")
public class HangmanExerciseDetail extends BaseExerciseDetail {

	@DatabaseField
	@Attribute
	@Path("player")
	private String hints;

	@DatabaseField
	@Attribute(name = "from")
	private String from;

	@ForeignCollectionField(eager = true, maxEagerLevel = MAX_EAGER_LEVEL)
	@Path("player")
	@ElementList(inline = true, entry = "word")
	private Collection<Word> words;

	/** Constructor */
	public HangmanExerciseDetail() {
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
