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

import flax.entity.base.BaseEntity;

@DatabaseTable(tableName="hangman_response")
@Root(name = "response")
public class HangmanResponse extends BaseEntity{
	@DatabaseField(id=true)
	private String uniqueUrl;
	
	@DatabaseField
	@Attribute
	@Path("player")
	private String hints;

	@DatabaseField
	@Attribute(name = "from")
	private String from;
	
	@ForeignCollectionField(eager = false,foreignFieldName="response")
	@Path("player")
	@ElementList(inline=true,entry="word")
    private Collection<Word> words;

	/** Constructor */
	public HangmanResponse(){}

	/**
	 * Build one-to-many relation, prepare for orm.
	 */
	@Commit
	private void buildRelation(){
		for (Word word : words) {
			word.setResponse(this);
		}
	}
	
	/** Get/Set Methods */
	public String getHints() {
		return hints;
	}

	public void setHints(String hints) {
		this.hints = hints;
	}

	public String getUniqueUrl() {
		return uniqueUrl;
	}

	public void setUniqueUrl(String uniqueUrl) {
		this.uniqueUrl = uniqueUrl;
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

