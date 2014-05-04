package flax.entity.exerciselist;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import flax.entity.base.BaseEntity;

/**
 * Represent "exercise" Tag in XML.
 * 
 * @author Nan Wu
 * 
 */
@DatabaseTable(tableName = "exerciselist_item")
public class Exercise extends BaseEntity {
	@DatabaseField
	@Attribute(name = "category_id")
	private String categoryId;

	@DatabaseField
	@Attribute
	private String id;

	@DatabaseField
	@Attribute
	private String name;

	@DatabaseField
	@Attribute
	private String type;

	// Using URL as unique id
	@DatabaseField(id = true)
	@Element
	private String url;

	@DatabaseField
	@Element
	private String summary;

	@DatabaseField(defaultValue = "0")
	private int status;

	@DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "category_foreign_id")
	private Category category;

	/** Constructors */
	public Exercise() {
	}

	/** Get/Set Methods */

	public String getId() {
		return id;
	}

	/**
	 * @return the categoryId
	 */
	public String getCategoryId() {
		return categoryId;
	}

	/**
	 * @param categoryId
	 *            the categoryId to set
	 */
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

}