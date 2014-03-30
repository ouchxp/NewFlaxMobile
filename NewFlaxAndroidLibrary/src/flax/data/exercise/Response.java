package flax.data.exercise;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import flax.data.base.BaseData;

/**
 * Represent "response" Tag in XML(root tag).
 * @author ouchxp
 *
 */
@DatabaseTable(tableName="exercises_response")
@XStreamAlias("response")
public class Response implements BaseData{

	@DatabaseField(id=true)
	@XStreamAsAttribute
	private String from;
	@DatabaseField(foreign = true,foreignAutoRefresh=true, columnName = "categoryList_id")
	private CategoryList categoryList;

	/* Constructors */
	public Response(){}

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
