package flax.activity;

import flax.converters.HangmanURLConverter;
import flax.entity.base.BaseEntity;
import flax.entity.base.BaseExerciseDetail;
import flax.entity.base.BasePage;
import flax.entity.hangman.HangmanExerciseDetail;
import flax.entity.hangman.Word;
import flax.utils.IURLConverter;

/**
 * Define different types of exercises. Provide information for creating
 * database table, XML parser etc.
 * 
 * @author Nan Wu
 * 
 */
public enum ExerciseTypeEnum {
	@SuppressWarnings("unchecked")
	HANGMAN("HANGMAN", "HANGMAN", HangmanURLConverter.class, HangmanExerciseDetail.class, Word.class,
			HangmanExerciseDetail.class, Word.class);// ,
	// COLLOCATION_MATCHING("collocationMatching");

	private String name;
	private String title;

	private Class<? extends IURLConverter> urlConvertClass;
	private Class<? extends BaseExerciseDetail> exerciseEntityClass;
	private Class<? extends BasePage> pageEntityClass;
	private Class<? extends BaseEntity>[] entityClasses;

	private ExerciseTypeEnum(String name, String title, Class<? extends IURLConverter> urlConvertClass,
			Class<? extends BaseExerciseDetail> exerciseEntityClass, Class<? extends BasePage> pageEntityClass,
			Class<? extends BaseEntity>... entityClasses) {
		this.name = name;
		this.title = title;
		this.urlConvertClass = urlConvertClass;
		this.exerciseEntityClass = exerciseEntityClass;
		this.pageEntityClass = pageEntityClass;
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

	public Class<? extends BaseExerciseDetail> getExerciseEntityClass() {
		return exerciseEntityClass;
	}

	public void setExerciseEntityClass(Class<? extends BaseExerciseDetail> exerciseEntityClass) {
		this.exerciseEntityClass = exerciseEntityClass;
	}

	public Class<? extends BasePage> getPageEntityClass() {
		return pageEntityClass;
	}

	public void setPageEntityClass(Class<? extends BasePage> pageEntityClass) {
		this.pageEntityClass = pageEntityClass;
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
