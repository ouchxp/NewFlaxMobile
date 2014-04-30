package flax.activity;

import flax.entity.base.BaseEntity;
import flax.entity.base.BaseExerciseDetail;
import flax.entity.base.BasePage;
import flax.utils.IUrlConverter;

/**
 * Define different types of exercises. Provide information for creating
 * database table, XML parser etc.
 * 
 * @author Nan Wu
 * 
 */
public class ExerciseType {
	
// newInstance method can be used like this in LocalConstat class to define your own exercise type
//	@SuppressWarnings("unchecked")
//	public static final ExerciseType HANGMAN = newInstance("HANGMAN", "HANGMAN", HangmanUrlConverter.class,
//			HangmanExerciseDetail.class, Word.class, HangmanExerciseDetail.class, Word.class);

	public static final ExerciseType newInstance(String name, String title,
			Class<? extends IUrlConverter> urlConvertClass, Class<? extends BaseExerciseDetail> exerciseEntityClass,
			Class<? extends BasePage> pageEntityClass, Class<? extends BaseEntity>... entityClasses) {
		return new ExerciseType(name, title, urlConvertClass, exerciseEntityClass, pageEntityClass, entityClasses);
	}

	private String name;
	private String title;

	private Class<? extends IUrlConverter> urlConvertClass;
	private Class<? extends BaseExerciseDetail> exerciseEntityClass;
	private Class<? extends BasePage> pageEntityClass;
	private Class<? extends BaseEntity>[] entityClasses;

	private ExerciseType(String name, String title, Class<? extends IUrlConverter> urlConvertClass,
			Class<? extends BaseExerciseDetail> exerciseEntityClass, Class<? extends BasePage> pageEntityClass,
			Class<? extends BaseEntity>... entityClasses) {
		this.name = name;
		this.title = title;
		this.urlConvertClass = urlConvertClass;
		this.exerciseEntityClass = exerciseEntityClass;
		this.pageEntityClass = pageEntityClass;
		this.entityClasses = entityClasses;
	}

	public String getName() {
		return name;
	}

	public String getTitle() {
		return title;
	}

	public Class<? extends IUrlConverter> getUrlConvertClass() {
		return urlConvertClass;
	}

	public Class<? extends BaseExerciseDetail> getExerciseEntityClass() {
		return exerciseEntityClass;
	}

	public Class<? extends BasePage> getPageEntityClass() {
		return pageEntityClass;
	}

	public Class<? extends BaseEntity>[] getEntityClasses() {
		return entityClasses;
	}
}
