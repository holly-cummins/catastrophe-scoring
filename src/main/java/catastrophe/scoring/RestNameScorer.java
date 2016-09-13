package catastrophe.scoring;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("scoring")
public class RestNameScorer {

	@Path("score")
	@Produces(MediaType.APPLICATION_JSON)
	@GET
	public int getScore(@QueryParam("realName") String realName,
			@QueryParam("guess") String guess) {
		// Rather unsophisticated algorithm :)
		return 70;
	}
}
