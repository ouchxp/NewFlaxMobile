package flax.activity;

/**
 * Define different types of exercises. Provide information 
 * for creating database table, XML parser etc.
 * @author ouchxp
 *
 */
public enum ExerciseEnum {
	HANGMAN("hangman"),
	COLLOCATION_MATCHING("collocationMatching");

		private String name;
		
		private ExerciseEnum(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
}
