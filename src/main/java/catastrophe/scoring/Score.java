package catastrophe.scoring;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class Score {

	private final int score;
	private final String bestGuess;

	public Score(int score, String bestGuess) {
		this.score = score;
		this.bestGuess = bestGuess;
	}

	public int getScore() {
		return Math.min(score, 100);
	}

	public String getScoringAlgorithm() {
		return "IBM Watson Visual Recognition";
	}

	public String getBestGuess() {
		return bestGuess;
	}
}
