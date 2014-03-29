package flax.data.exercise;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * Represent "categoryList" Tag in XML.
 * @author ouchxp
 *
 */
public class CategoryList {
	
	@XStreamAsAttribute
	String c;
	@XStreamAsAttribute
	String flax_server_domain;
	@XStreamAsAttribute
	String type;
	Category category;

	/* Constructors */

	public CategoryList(){}
	public CategoryList(String c, String flax_server_domain, String type,
			Category category) {
		this.c = c;
		this.flax_server_domain = flax_server_domain;
		this.type = type;
		this.category = category;
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
