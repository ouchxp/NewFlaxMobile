package flax.data.hangman;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

public class Player {
	@XStreamAsAttribute
	private String hints;

	@XStreamImplicit(itemFieldName = "word")
	private List<String> words;

	/* Constructors */
	public Player(){}
	public Player(String hints,
			List<String> words) {
		this.hints = hints;
		this.words = words;
	}

	/* Get/Set Methods */
	public String getHints() {
		return hints;
	}

	public void setHints(String hints) {
		this.hints = hints;
	}

	public List<String> getWords() {
		return words;
	}

	public void setWords(List<String> words) {
		this.words = words;
	}

}