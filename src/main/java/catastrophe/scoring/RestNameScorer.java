package catastrophe.scoring;

import java.io.IOException;

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
	public Score getScore(@QueryParam("encodedImage") String encodedImage) throws IOException {
		return new ImageScorer().getScore(encodedImage);
	}

	// Useful for testing
	@Path("scoringalgorithm")
	@Produces(MediaType.APPLICATION_JSON)
	@GET
	public String getScoringAlgorithm() {
		return new ImageScorer().getScoringAlgorithm();
	}
}
