package flax.data.exercise;

import java.util.Collection;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import flax.data.base.BaseData;

/**
 * Represent "category" Tag in XML.
 * @author ouchxp
 *
 */
@DatabaseTable(tableName="exercises_category")
public class Category implements BaseData{
	@DatabaseField
	private String categorysummary;

	@DatabaseField(id = true)
	@XStreamAsAttribute
	private String id;
	@DatabaseField
	@XStreamAsAttribute
	private String name;
	@ForeignCollectionField(eager = false)
	@XStreamImplicit(itemFieldName = "exercise")
	private Collection<Exercise> exercises;
	

	/* Constructors */
	public Category(){}

	/* Get/Set Methods */
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


