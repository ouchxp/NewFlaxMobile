package flax.data.exercise;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * Represent "response" Tag in XML(root tag).
 * @author ouchxp
 *
 */
@XStreamAlias("response")
public class Response {

	@XStreamAsAttribute
	private String from;

	private CategoryList categoryList;

	/* Constructors */
	public Response(){}
	public Response(String from, CategoryList categoryList) {
		super();
		this.from = from;
		this.categoryList = categoryList;
	}

	/* Get/Set Methods */
	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public CategoryList getCategoryList() {
		return categoryList;
	}

	public void setCategoryList(CategoryList categoryList) {
		this.categoryList = categoryList;
	}

}
