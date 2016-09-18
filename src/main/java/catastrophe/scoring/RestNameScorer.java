package catastrophe.scoring;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("scoring")
public class RestNameScorer {

	@Path("score")
	@Produces(MediaType.APPLICATION_JSON)
	@GET
	public NameScorer getScore(@QueryParam("realName") String realName, @QueryParam("guess") String guess) {
		return new NameScorer(realName, guess);
	}

	// Useful for testing
	@Path("scoringalgorithm")
	@Produces(MediaType.APPLICATION_JSON)
	@GET
	public String getScoringAlgorithm() {
		return new NameScorer(null, null).getScoringAlgorithm();
	}
}
