package za.co.idea.ip.ws;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import za.co.idea.ip.orm.bean.IpTag;
import za.co.idea.ip.orm.dao.IpChallengeDAO;
import za.co.idea.ip.orm.dao.IpIdeaDAO;
import za.co.idea.ip.orm.dao.IpSolutionDAO;
import za.co.idea.ip.orm.dao.IpTagDAO;
import za.co.idea.ip.orm.dao.IpTagEntityTypeDAO;
import za.co.idea.ip.orm.dao.IpTagTypeDAO;
import za.co.idea.ip.orm.dao.IpUserDAO;
import za.co.idea.ip.ws.bean.ResponseMessage;
import za.co.idea.ip.ws.bean.TagMessage;

@Path(value = "/ts")
public class TagService {
	private IpTagDAO ipTagDAO;
	private IpTagEntityTypeDAO ipTagEntityTypeDAO;
	private IpTagTypeDAO ipTagTypeDAO;
	private IpUserDAO ipUserDAO;
	private IpIdeaDAO ipIdeaDAO;
	private IpChallengeDAO ipChallengeDAO;
	private IpSolutionDAO ipSolutionDAO;

	@SuppressWarnings("unchecked")
	@GET
	@Path("/tag/get/{entityId}/{teId}/{ttId}")
	@Produces("application/json")
	public List<TagMessage> getTag(@PathParam("entityId") Long entityId, @PathParam("teId") Integer teId, @PathParam("ttId") Integer ttId) {
		List<TagMessage> ret = new ArrayList<TagMessage>();
		try {
			List<IpTag> tags = ipTagDAO.getTagByFilterA(entityId, teId, ttId);
			for (IpTag ipTag : tags) {
				TagMessage message = new TagMessage();
				message.setEntityId(entityId);
				message.setTeId(teId);
				message.setTtId(ttId);
				message.setTagText(ipTag.getTagText());
				message.setUsrScreenName(ipTag.getIpUser().getUserScreenName());
				message.setUserFullName(ipTag.getIpUser().getUserFName() + " " + ((ipTag.getIpUser().getUserMName() != null && ipTag.getIpUser().getUserMName().length() > 0) ? (ipTag.getIpUser().getUserMName() + " ") : "") + ipTag.getIpUser().getUserLName());
				message.setUserId(ipTag.getIpUser().getUserId());
				message.setTagId(ipTag.getTagId());
				ret.add(message);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	@SuppressWarnings("unchecked")
	@GET
	@Path("/tag/getByUser/{entityId}/{teId}/{ttId}/{userId}")
	@Produces("application/json")
	public List<TagMessage> getTagByUser(@PathParam("entityId") Long entityId, @PathParam("teId") Integer teId, @PathParam("ttId") Integer ttId, @PathParam("userId") Long userId) {
		List<TagMessage> ret = new ArrayList<TagMessage>();
		try {
			List<IpTag> tags = ipTagDAO.getTagByFilterB(entityId, teId, ttId, userId);
			for (IpTag ipTag : tags) {
				TagMessage message = new TagMessage();
				message.setEntityId(entityId);
				message.setTeId(teId);
				message.setTtId(ttId);
				message.setTagText(ipTag.getTagText());
				message.setUsrScreenName(ipTag.getIpUser().getUserScreenName());
				message.setUserFullName(ipTag.getIpUser().getUserFName() + " " + ((ipTag.getIpUser().getUserMName() != null && ipTag.getIpUser().getUserMName().length() > 0) ? (ipTag.getIpUser().getUserMName() + " ") : "") + ipTag.getIpUser().getUserLName());
				message.setUserId(ipTag.getIpUser().getUserId());
				message.setTagId(ipTag.getTagId());
				ret.add(message);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	@POST
	@Path("/tag/add")
	@Consumes("application/json")
	@Produces("application/json")
	public ResponseMessage createTag(TagMessage tag) {
		try {
			if (!tag.isDuplicate() && ipTagDAO.getTagByFilterB(tag.getEntityId(), tag.getTeId(), tag.getTtId(), tag.getUserId()).size() != 0) {
				return ResponseMessage.createMessage("Tag Already exists. Cannot Create Duplicate");
			} else {
				IpTag ipTag = new IpTag();
				ipTag.setIpTagEntityType(ipTagEntityTypeDAO.findById(tag.getTeId()));
				ipTag.setIpTagType(ipTagTypeDAO.findById(tag.getTtId()));
				ipTag.setIpUser(ipUserDAO.findById(tag.getUserId()));
				ipTag.setTagEntityId(tag.getEntityId());
				ipTag.setTagId(tag.getTagId());
				ipTag.setTagText(tag.getTagText());
				ipTagDAO.save(ipTag);
				return ResponseMessage.createSuccess();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseMessage.createException(e);
		}
	}

	@POST
	@Path("/tag/del")
	@Consumes("application/json")
	@Produces("application/json")
	public ResponseMessage deleteTag(Long tagId) {
		try {
			ipTagDAO.delete(ipTagDAO.findById(tagId));
			return ResponseMessage.createSuccess();
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseMessage.createException(e);
		}
	}

	public IpTagDAO getIpTagDAO() {
		return ipTagDAO;
	}

	public void setIpTagDAO(IpTagDAO ipTagDAO) {
		this.ipTagDAO = ipTagDAO;
	}

	public IpTagEntityTypeDAO getIpTagEntityTypeDAO() {
		return ipTagEntityTypeDAO;
	}

	public void setIpTagEntityTypeDAO(IpTagEntityTypeDAO ipTagEntityTypeDAO) {
		this.ipTagEntityTypeDAO = ipTagEntityTypeDAO;
	}

	public IpTagTypeDAO getIpTagTypeDAO() {
		return ipTagTypeDAO;
	}

	public void setIpTagTypeDAO(IpTagTypeDAO ipTagTypeDAO) {
		this.ipTagTypeDAO = ipTagTypeDAO;
	}

	public IpUserDAO getIpUserDAO() {
		return ipUserDAO;
	}

	public void setIpUserDAO(IpUserDAO ipUserDAO) {
		this.ipUserDAO = ipUserDAO;
	}

	public IpIdeaDAO getIpIdeaDAO() {
		return ipIdeaDAO;
	}

	public void setIpIdeaDAO(IpIdeaDAO ipIdeaDAO) {
		this.ipIdeaDAO = ipIdeaDAO;
	}

	public IpChallengeDAO getIpChallengeDAO() {
		return ipChallengeDAO;
	}

	public void setIpChallengeDAO(IpChallengeDAO ipChallengeDAO) {
		this.ipChallengeDAO = ipChallengeDAO;
	}

	public IpSolutionDAO getIpSolutionDAO() {
		return ipSolutionDAO;
	}

	public void setIpSolutionDAO(IpSolutionDAO ipSolutionDAO) {
		this.ipSolutionDAO = ipSolutionDAO;
	}

}
