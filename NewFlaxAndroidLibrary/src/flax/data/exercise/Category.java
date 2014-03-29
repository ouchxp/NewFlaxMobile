package flax.data.exercise;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * Represent "category" Tag in XML.
 * @author ouchxp
 *
 */
public class Category {

	private String categorysummary;
	@XStreamImplicit(itemFieldName = "exercise")
	private List<Exercise> exercises;
	@XStreamAsAttribute
	private String id;
	@XStreamAsAttribute
	private String name;

	/* Constructors */
	public Category(){}
	
	public Category(String id, String name, String categorysummary,
			List<Exercise> exercises) {
		this.id = id;
		this.name = name;
		this.categorysummary = categorysummary;
		this.exercises = exercises;
	}

	/* Get/Set Methods */
	public String getCategorysummary() {
		return categorysummary;
	}

	public void setCategorysummary(String categorysummary) {
		this.categorysummary = categorysummary;
	}

	public List<Exercise> getExercises() {
		return exercises;
	}

	public void setExercises(List<Exercise> exercises) {
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


