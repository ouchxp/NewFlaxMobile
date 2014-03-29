package flax.data.exercise;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

/**
 * Represent "exercise" Tag in XML.
 * @author ouchxp
 *
 */
@DatabaseTable
public class Exercise {
	@DatabaseField
	@XStreamAsAttribute
	private String category_id;
	@DatabaseField(id=true)
	@XStreamAsAttribute
	private String id;
	@DatabaseField
	@XStreamAsAttribute
	private String name;
	@DatabaseField
	@XStreamAsAttribute
	private String type;
	@DatabaseField
	private String url;
	@DatabaseField
	private String summary;
	@DatabaseField(defaultValue="new")
	@XStreamOmitField
	private String status;
	@DatabaseField
	@XStreamOmitField
	private int uniqueId;

	/* Constructors */
	public Exercise(){}
	public Exercise(String category_id, String id, String name, String type,
			String url, String summary, String status) {
		super();
		this.category_id = category_id;
		this.id = id;
		this.name = name;
		this.type = type;
		this.url = url;
		this.summary = summary;
		this.status = status;
	}

	/* Get/Set Methods */
	public String getCategory_id() {
		return category_id;
	}

	public void setCategory_id(String category_id) {
		this.category_id = category_id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public int getUniqueId() {
		return uniqueId;
	}
	
	public void setUniqueId(int uniqueId) {
		this.uniqueId = uniqueId;
	}
	
}