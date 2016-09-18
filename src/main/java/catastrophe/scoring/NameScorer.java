package catastrophe.scoring;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class NameScorer {

	private final String realName;
	private final String guess;

	public NameScorer(String realName, String guess) {
		this.realName = realName;
		this.guess = guess;
	}

	public int getScore() {
		// Rather unsophisticated algorithm :)
		return 70;
	}

	public String getScoringAlgorithm() {
		return "an invariant number";
	}

}
