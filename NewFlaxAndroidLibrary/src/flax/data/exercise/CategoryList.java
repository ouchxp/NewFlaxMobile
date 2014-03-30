package flax.data.exercise;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import flax.data.base.BaseData;

/**
 * Represent "categoryList" Tag in XML.
 * 
 * @author ouchxp
 * 
 */
@DatabaseTable(tableName = "exercises_category_list")
public class CategoryList implements BaseData{
	@DatabaseField(id=true)
	@XStreamAsAttribute
	private String c;
	@DatabaseField
	@XStreamAsAttribute
	private String flax_server_domain;
	@DatabaseField
	@XStreamAsAttribute
	private String type;
	@DatabaseField(foreign = true, foreignAutoRefresh=true,columnName = "category_id")
	private Category category;

	/* Constructors */

	public CategoryList() {
	}

	/* Get/Set Methods */
	public String getC() {
		return c;
	}

	public void setC(String c) {
		this.c = c;
	}

	public String getFlax_server_domain() {
		return flax_server_domain;
	}

	public void setFlax_server_domain(String flax_server_domain) {
		this.flax_server_domain = flax_server_domain;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}
}
