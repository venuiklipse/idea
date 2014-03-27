package za.co.idea.ip.ws;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import za.co.idea.ip.orm.bean.IpGroup;
import za.co.idea.ip.orm.bean.IpUser;
import za.co.idea.ip.ws.wrapper.CreateStatus;
import za.co.idea.ip.ws.wrapper.UpdateStatus;

@Path(value = "/as")
public class AdminService {
	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public IpUser login(String uName, String password) {
		return null;
	}

	@POST
	@Path("/createUser")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public CreateStatus createUser(IpUser user) {
		return null;
	}

	@POST
	@Path("/updateUser")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public UpdateStatus updateUser(IpUser user) {
		return null;
	}

	@POST
	@Path("/createGroup")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public CreateStatus createGroup(IpGroup group) {
		return null;
	}

	@POST
	@Path("/updateGroup")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public UpdateStatus updateGroup(IpGroup group) {
		return null;
	}
}
