package flax.entity.exercise;

import java.util.Collection;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.core.Commit;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import flax.entity.base.BaseEntity;

/**
 * Represent "category" Tag in XML.
 * @author ouchxp
 *
 */
@DatabaseTable(tableName="exercises_category")
public class Category extends BaseEntity{
	
	
	/** Composite Id Start */
	
	/**
	 * Currently ormlite doesn't support Composite Id(Multiple primary keys), 
	 * but we can simulate that, use an extra field which returns a combination 
	 * of all primary keys as unique id. In order to do this, useGetSet=true is
	 * necessary.
	 */
	@DatabaseField(id=true,useGetSet=true)
	private String uniqueId;
	
	public String getUniqueId() {
		return id + "@" + response.getUniqueUrl();
	}

	/**
	 * Strongly <B>NOT</B> recommend to use this method.
	 * This method should <B>ONLY</B> be called by ORM framwork.
	 * @param uniqueId
	 */
	@Deprecated
	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}
	/** Composite Id End */

	@Element
	@DatabaseField
	private String categorysummary;

	@DatabaseField
	@Attribute
	private String id;
	@DatabaseField
	@Attribute
	private String name;
	
	@ForeignCollectionField(eager = false)
	@ElementList(inline=true,entry="exercise")
	private Collection<Exercise> exercises;
	
	@DatabaseField(foreign = true, foreignAutoRefresh = true,columnName="response_foreign_id") 
	private Response response;
	
	/** Constructors */
	public Category(){}
	
	/**
	 * Build one-to-many relation, prepare for orm.
	 */
	@Commit
	private void buildRelation(){
		for (Exercise exercise : exercises) {
			exercise.setCategory(this);
		}
	}

	/** Get/Set Methods */
	public String getCategorysummary() {
		return categorysummary;
	}

	public void setCategorysummary(String categorysummary) {
		this.categorysummary = categorysummary;
	}

	public Collection<Exercise> getExercises() {
		return exercises;
	}

	public void setExercises(Collection<Exercise> exercises) {
		this.exercises = exercises;
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

	public Response getResponse() {
		return response;
	}

	public void setResponse(Response response) {
		this.response = response;
	}
	
}


