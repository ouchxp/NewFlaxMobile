package flax.entity.base;

import com.j256.ormlite.field.DatabaseField;

public class BaseExerciseDetail extends BaseEntity{
	@DatabaseField(id=true)
	private String uniqueUrl;
	
	@DatabaseField(canBeNull=true)
	private int possibleScore;
	
	/** Summary fields Start */
	@DatabaseField(canBeNull=true)
	private int score;
	
	@DatabaseField(canBeNull=true)
	private String startTime;
	
	@DatabaseField(canBeNull=true)
	private String endTime;
	
	@DatabaseField(canBeNull=true)
	private int attempts;
	/** Summary fields End */
	
	/** Constructor */
	public BaseExerciseDetail(){}

	/** Get/Set Methods */
	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getPossibleScore() {
		return possibleScore;
	}

	public void setPossibleScore(int possibleScore) {
		this.possibleScore = possibleScore;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public int getAttempts() {
		return attempts;
	}

	public void setAttempts(int attempts) {
		this.attempts = attempts;
	}
	
	public String getUniqueUrl() {
		return uniqueUrl;
	}

	/**
	 * Will be use in XMLParser.setUniqueUrl, do not modify this method rashly.
	 * @param uniqueUrl
	 */
	public void setUniqueUrl(String uniqueUrl) {
		this.uniqueUrl = uniqueUrl;
	}
	
	/**
	 * Reset exercise, clear summary information and extra.
	 * e.g. when user restart game.
	 */
	public void resetExercise(){
		score = 0;
		startTime = null;
		endTime = null;
		attempts = 0;
		clearExtra();
	}
	
	/**
	 * Check if this exercise is complete.
	 * @return
	 */
	public boolean isComplete(){
		return score == possibleScore;
	}
}
