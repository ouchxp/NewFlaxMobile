package flax.activity;

import flax.converters.HangmanURLConverter;
import flax.entity.base.BaseEntity;
import flax.entity.hangman.HangmanResponse;
import flax.entity.hangman.Word;
import flax.utils.IURLConverter;

/**
 * Define different types of exercises. Provide information for creating
 * database table, XML parser etc.
 * 
 * @author ouchxp
 * 
 */
public enum ExerciseTypeEnum {
	@SuppressWarnings("unchecked")
	HANGMAN("Hangman", "HANGMAN", HangmanURLConverter.class, HangmanResponse.class, HangmanResponse.class, Word.class);// ,
	// COLLOCATION_MATCHING("collocationMatching");

	private String name;
	private String title;

	private Class<? extends IURLConverter> urlConvertClass;
	private Class<? extends BaseEntity> rootEntityClass;
	private Class<? extends BaseEntity>[] entityClasses;

	private ExerciseTypeEnum(String name, String title, Class<? extends IURLConverter> urlConvertClass,
			Class<? extends BaseEntity> rootEntityClass, Class<? extends BaseEntity>... entityClasses) {
		this.name = name;
		this.title = title;
		this.urlConvertClass = urlConvertClass;
		this.rootEntityClass = rootEntityClass;
		this.entityClasses = entityClasses;
	}

	public Class<? extends IURLConverter> getUrlConvertClass() {
		return urlConvertClass;
	}

	public void setUrlConvertClass(Class<? extends IURLConverter> urlConvertClass) {
		this.urlConvertClass = urlConvertClass;
	}

	public Class<? extends BaseEntity>[] getEntityClasses() {
		return entityClasses;
	}

	public void setEntityClasses(Class<? extends BaseEntity>... entityClasses) {
		this.entityClasses = entityClasses;
	}

	public Class<? extends BaseEntity> getRootEntityClass() {
		return rootEntityClass;
	}

	public void setRootEntityClass(Class<? extends BaseEntity> rootEntityClass) {
		this.rootEntityClass = rootEntityClass;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
