package za.co.idea.ip.ws;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import za.co.idea.ip.orm.bean.IpIdea;
import za.co.idea.ip.orm.bean.IpTag;
import za.co.idea.ip.ws.wrapper.CreateStatus;
import za.co.idea.ip.ws.wrapper.UpdateStatus;

@Path(value = "/is")
public class IdeaService {

	@POST
	@Path("/createIdea")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public CreateStatus createIdea(IpIdea idea) {
		return null;
	}

	@POST
	@Path("/updateIdea")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public UpdateStatus updateIdea(IpIdea idea) {
		return null;
	}

	@GET
	@Path("/getIdeas")
	@Produces(MediaType.APPLICATION_JSON)
	public IpIdea[] getIdeas() {
		return null;
	}

	@POST
	@Path("/createTag")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public CreateStatus createTag(IpTag tag) {
		return null;
	}

}
