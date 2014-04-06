package flax.entity.hangman;

import java.util.Collection;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;

@Root(name = "response")
class Response {
	
	@Attribute
	@Path("player")
	private String hints;
	
	@Path("player")
	@ElementList(inline=true,entry="word")
    private Collection<String> player;

	@Attribute(name = "from")
	private String from;
	
	/* Constructors */
	public Response(){}

	/** Get/Set Methods */
	public String getHints() {
		return hints;
	}

	public void setHints(String hints) {
		this.hints = hints;
	}

	public Collection<String> getPlayer() {
		return player;
	}

	public void setPlayer(Collection<String> player) {
		this.player = player;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}
}

