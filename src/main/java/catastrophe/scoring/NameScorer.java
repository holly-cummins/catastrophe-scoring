package catastrophe.scoring;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class NameScorer {

	public int getScore(String realName, String guess) {

		// Rather unsophisticated algorithm :)
		return 70;
	}

}
