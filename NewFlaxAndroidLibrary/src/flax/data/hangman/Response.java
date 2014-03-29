package flax.data.hangman;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("response")
class Response {

	@XStreamAsAttribute
	private String from;

	private Player player;
	
	/* Constructors */
	public Response(){}
	public Response(String from, Player player) {
		super();
		this.from = from;
		this.player = player;
	}

	/* Get/Set Methods */
	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}
	
}

