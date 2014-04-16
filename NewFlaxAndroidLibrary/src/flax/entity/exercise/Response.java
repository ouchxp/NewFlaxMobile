package flax.entity.exercise;

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

/**
 * Represent "response" Tag in XML(root tag).
 * @author ouchxp
 *
 */
@Root(name = "response")
@DatabaseTable(tableName="exercises_response")
public class Response extends BaseEntity{
	@DatabaseField(id=true)
	private String uniqueUrl;
	
	@DatabaseField
	@Attribute
	private String from;
	
	@DatabaseField
	@Attribute
	@Path("categoryList")
	private String c;
	
	@DatabaseField
	@Attribute(name="flax_server_domain")
	@Path("categoryList")
	private String flaxServerDomain;
	
	@DatabaseField
	@Attribute
	@Path("categoryList")
	private String type;
	
	@ForeignCollectionField(eager = false,foreignFieldName="response")
	@ElementList(inline=true,entry="category")
	@Path("categoryList")
	private Collection<Category> categoryList;
	
	/** Constructor */
	public Response(){}
	
	/**
	 * Build one-to-many relation, prepare for orm.
	 */
	@Commit
	private void buildRelation(){
		for (Category category : categoryList) {
			category.setResponse(this);
		}
	}

	/** Get/Set Methods */
	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getC() {
		return c;
	}

	public void setC(String c) {
		this.c = c;
	}

	public String getFlaxServerDomain() {
		return flaxServerDomain;
	}

	public void setFlaxServerDomain(String flaxServerDomain) {
		this.flaxServerDomain = flaxServerDomain;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Collection<Category> getCategoryList() {
		return categoryList;
	}

	public void setCategoryList(Collection<Category> categoryList) {
		this.categoryList = categoryList;
	}

	public String getUniqueUrl() {
		return uniqueUrl;
	}

	public void setUniqueUrl(String uniqueUrl) {
		this.uniqueUrl = uniqueUrl;
	}
}
