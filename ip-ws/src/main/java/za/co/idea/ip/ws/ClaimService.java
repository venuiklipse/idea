package za.co.idea.ip.ws;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import za.co.idea.ip.orm.dao.IpClaimDAO;
import za.co.idea.ip.orm.dao.IpClaimStatusDAO;
import za.co.idea.ip.orm.dao.IpUserDAO;
import za.co.idea.ip.ws.bean.ClaimMessage;
import za.co.idea.ip.ws.bean.MetaDataMessage;
import za.co.idea.ip.ws.bean.ResponseMessage;

@Path(value = "/cls")
public class ClaimService {
	private IpUserDAO ipUserDAO;
	private IpClaimDAO ipClaimDAO;
	private IpClaimStatusDAO ipClaimStatusDAO;

	@POST
	@Path("/claim/add")
	@Consumes("application/json")
	@Produces("application/json")
	private ResponseMessage createClaim(ClaimMessage claim) {
		try {
			ResponseMessage message = new ResponseMessage();
			message.setStatusCode(0);
			message.setStatusDesc("Success");
			return message;
		} catch (Exception e) {
			e.printStackTrace();
			ResponseMessage message = new ResponseMessage();
			message.setStatusCode(1);
			message.setStatusDesc(e.getMessage());
			return message;
		}
	}

	@POST
	@Path("/claim/modify")
	@Consumes("application/json")
	@Produces("application/json")
	private ResponseMessage updateClaim(ClaimMessage claim) {
		try {
			ResponseMessage message = new ResponseMessage();
			message.setStatusCode(0);
			message.setStatusDesc("Success");
			return message;
		} catch (Exception e) {
			e.printStackTrace();
			ResponseMessage message = new ResponseMessage();
			message.setStatusCode(1);
			message.setStatusDesc(e.getMessage());
			return message;
		}
	}

	@GET
	@Path("/claim/list")
	@Produces("application/json")
	public <T extends ClaimMessage> List<T> listClaim() {
		List<T> ret = new ArrayList<T>();
		try {

		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	@GET
	@Path("/claim/get/{id}")
	@Produces("application/json")
	public ClaimMessage getClaimById(@PathParam("id") Long id) {
		ClaimMessage claim = new ClaimMessage();
		try {
		} catch (Exception e) {
			e.printStackTrace();
		}
		return claim;
	}

	@GET
	@Path("/claim/list/{id}")
	@Produces("application/json")
	public <T extends ClaimMessage> List<T> listClaimByStatus(@PathParam("id") Integer id) {
		List<T> ret = new ArrayList<T>();
		try {

		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	@GET
	@Path("/claim/list/{id}")
	@Produces("application/json")
	public <T extends ClaimMessage> List<T> listClaimByUser(@PathParam("id") Long id) {
		List<T> ret = new ArrayList<T>();
		try {

		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	@GET
	@Path("/claim/status/list")
	@Produces("application/json")
	public <T extends MetaDataMessage> List<T> listClaimStatus() {
		List<T> ret = new ArrayList<T>();
		try {
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	@GET
	@Path("/claim/status/get/{id}")
	@Produces("application/json")
	public MetaDataMessage getClaimStatusById(@PathParam("id") Integer id) {
		MetaDataMessage message = new MetaDataMessage();
		try {
		} catch (Exception e) {
			e.printStackTrace();
		}
		return message;
	}

	public IpUserDAO getIpUserDAO() {
		return ipUserDAO;
	}

	public void setIpUserDAO(IpUserDAO ipUserDAO) {
		this.ipUserDAO = ipUserDAO;
	}

	public IpClaimDAO getIpClaimDAO() {
		return ipClaimDAO;
	}

	public void setIpClaimDAO(IpClaimDAO ipClaimDAO) {
		this.ipClaimDAO = ipClaimDAO;
	}

	public IpClaimStatusDAO getIpClaimStatusDAO() {
		return ipClaimStatusDAO;
	}

	public void setIpClaimStatusDAO(IpClaimStatusDAO ipClaimStatusDAO) {
		this.ipClaimStatusDAO = ipClaimStatusDAO;
	}

}
