package flax.data.exercise;

import java.util.Collection;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import flax.data.base.BaseData;

/**
 * Represent "category" Tag in XML.
 * @author ouchxp
 *
 */
@DatabaseTable(tableName="exercises_category")
public class Category implements BaseData{
	@Element
	@DatabaseField
	private String categorysummary;

	@DatabaseField(id = true)
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
	
	/* Constructors */
	public Category(){}

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


	
}


