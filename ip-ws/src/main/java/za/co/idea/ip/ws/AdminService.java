package za.co.idea.ip.ws;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import za.co.idea.ip.orm.bean.IpGroup;
import za.co.idea.ip.orm.bean.IpUser;
import za.co.idea.ip.orm.dao.IpGroupDAO;
import za.co.idea.ip.orm.dao.IpUserDAO;
import za.co.idea.ip.ws.bean.GroupBean;
import za.co.idea.ip.ws.bean.UserBean;

@Path(value = "/as")
@Produces("application/json")
public class AdminService {
	private IpGroupDAO ipGroupDAO;
	private IpUserDAO ipUserDAO;

	@POST
	@Path("/group/add")
	@Consumes("application/json")
	public Response createGroup(GroupBean group) {
		IpGroup ipGroup = new IpGroup();
		ipGroup.setGroupId(group.getpGrpId());
		ipGroup.setGroupEmail(group.getGeMail());
		ipGroup.setGroupName(group.getgName());
		ipGroup.setGroupStatus((group.getIsActive() ? "y" : "n"));
		if (group.getpGrpId() != null || group.getpGrpId().longValue() != 0)
			ipGroup.setIpGroup(ipGroupDAO.findById(group.getpGrpId()));
		if (group.getAdmUserId() != null || group.getAdmUserId().longValue() != 0)
			ipGroup.setIpUser(ipUserDAO.findById(group.getAdmUserId()));
		try {
			ipGroupDAO.save(ipGroup);
			return Response.ok().build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}

	@PUT
	@Path("/group/modify")
	@Consumes("application/json")
	public Response updateGroup(GroupBean group) {
		IpGroup ipGroup = new IpGroup();
		ipGroup.setGroupId(group.getpGrpId());
		ipGroup.setGroupEmail(group.getGeMail());
		ipGroup.setGroupName(group.getgName());
		ipGroup.setGroupStatus((group.getIsActive() ? "y" : "n"));
		if (group.getpGrpId() != null || group.getpGrpId().longValue() != 0)
			ipGroup.setIpGroup(ipGroupDAO.findById(group.getpGrpId()));
		if (group.getAdmUserId() != null || group.getAdmUserId().longValue() != 0)
			ipGroup.setIpUser(ipUserDAO.findById(group.getAdmUserId()));
		try {
			ipGroupDAO.merge(ipGroup);
			return Response.ok().build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}

	@SuppressWarnings("rawtypes")
	@GET
	@Path("/group/list")
	public List<GroupBean> listGroup() {
		List<GroupBean> ret = new ArrayList<GroupBean>();
		try {
			List groups = ipGroupDAO.findAll();
			for (Object object : groups) {
				IpGroup ipGroup = (IpGroup) object;
				GroupBean group = new GroupBean();
				group.setGeMail(ipGroup.getGroupEmail());
				group.setgId(ipGroup.getGroupId());
				group.setgName(ipGroup.getGroupName());
				group.setIsActive(ipGroup.getGroupStatus().equalsIgnoreCase("y"));
				if (ipGroup.getIpGroup() != null)
					group.setpGrpId(ipGroup.getIpGroup().getGroupId());
				if (ipGroup.getIpUser() != null)
					group.setAdmUserId(ipGroup.getIpUser().getUserId());
				ret.add(group);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	@GET
	@Path("/group/get/{id}")
	public GroupBean getGroup(@PathParam("id") Long id) {
		GroupBean group = new GroupBean();
		try {
			IpGroup ipGroup = ipGroupDAO.findById(id);
			group.setGeMail(ipGroup.getGroupEmail());
			group.setgId(ipGroup.getGroupId());
			group.setgName(ipGroup.getGroupName());
			group.setIsActive(ipGroup.getGroupStatus().equalsIgnoreCase("y"));
			if (ipGroup.getIpGroup() != null)
				group.setpGrpId(ipGroup.getIpGroup().getGroupId());
			if (ipGroup.getIpUser() != null)
				group.setAdmUserId(ipGroup.getIpUser().getUserId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return group;
	}

	@POST
	@Path("/user/add")
	@Consumes("application/json")
	public Response createUser(UserBean user) {
		try {
			IpUser ipUser = new IpUser();
			ipUser.setUserId(user.getuId());
			ipUser.setUserContact(user.getContact());
			ipUser.setUserEmail(user.geteMail());
			ipUser.setUserFName(user.getfName());
			ipUser.setUserIdNum(user.getIdNum());
			ipUser.setUserLName(user.getlName());
			ipUser.setUserScreenName(user.getScName());
			ipUser.setUserSkills(user.getSkills());
			ipUser.setUserStatus((user.getIsActive() ? "y" : "n"));
			if (user.getFbHandle() != null && user.getFbHandle().length() > 0)
				ipUser.setUserFbHandle(user.getFbHandle());
			if (user.getAvatar() != null && user.getAvatar().length() > 0)
				ipUser.setUserAvatar(user.getAvatar());
			if (user.getBio() != null && user.getBio().length() > 0)
				ipUser.setUserBio(user.getBio());
			if (user.getmName() != null && user.getAvatar().length() > 0)
				ipUser.setUserMName(user.getmName());
			if (user.getTwHandle() != null && user.getTwHandle().length() > 0)
				ipUser.setUserTwHandle(user.getTwHandle());
			ipUserDAO.save(ipUser);
			return Response.ok().build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}

	@PUT
	@Path("/user/modify")
	@Consumes("application/json")
	public Response updateUser(UserBean user) {
		try {
			IpUser ipUser = new IpUser();
			ipUser.setUserId(user.getuId());
			ipUser.setUserContact(user.getContact());
			ipUser.setUserEmail(user.geteMail());
			ipUser.setUserFName(user.getfName());
			ipUser.setUserIdNum(user.getIdNum());
			ipUser.setUserLName(user.getlName());
			ipUser.setUserScreenName(user.getScName());
			ipUser.setUserSkills(user.getSkills());
			ipUser.setUserStatus((user.getIsActive() ? "y" : "n"));
			if (user.getFbHandle() != null && user.getFbHandle().length() > 0)
				ipUser.setUserFbHandle(user.getFbHandle());
			if (user.getAvatar() != null && user.getAvatar().length() > 0)
				ipUser.setUserAvatar(user.getAvatar());
			if (user.getBio() != null && user.getBio().length() > 0)
				ipUser.setUserBio(user.getBio());
			if (user.getmName() != null && user.getAvatar().length() > 0)
				ipUser.setUserMName(user.getmName());
			if (user.getTwHandle() != null && user.getTwHandle().length() > 0)
				ipUser.setUserTwHandle(user.getTwHandle());
			ipUserDAO.merge(ipUser);
			return Response.ok().build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}

	@SuppressWarnings("rawtypes")
	@GET
	@Path("/user/list")
	public List<UserBean> listUser() {
		List<UserBean> ret = new ArrayList<UserBean>();
		try {
			List users = ipUserDAO.findAll();
			for (Object object : users) {
				IpUser ipUser = (IpUser) object;
				UserBean user = new UserBean();
				user.setuId(ipUser.getUserId());
				user.setContact(ipUser.getUserContact());
				user.seteMail(ipUser.getUserEmail());
				user.setfName(ipUser.getUserFName());
				user.setIdNum(ipUser.getUserIdNum());
				user.setlName(ipUser.getUserLName());
				user.setScName(ipUser.getUserScreenName());
				user.setSkills(ipUser.getUserSkills());
				user.setIsActive(ipUser.getUserStatus().equalsIgnoreCase("y"));
				if (ipUser.getUserFbHandle() != null && ipUser.getUserFbHandle().length() > 0)
					user.setFbHandle(ipUser.getUserFbHandle());
				if (ipUser.getUserAvatar() != null && ipUser.getUserAvatar().length() > 0)
					user.setAvatar(ipUser.getUserAvatar());
				if (ipUser.getUserBio() != null && ipUser.getUserBio().length() > 0)
					user.setBio(ipUser.getUserBio());
				if (ipUser.getUserMName() != null && ipUser.getUserMName().length() > 0)
					user.setmName(ipUser.getUserMName());
				if (ipUser.getUserTwHandle() != null && ipUser.getUserTwHandle().length() > 0)
					user.setTwHandle(ipUser.getUserTwHandle());
				ret.add(user);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	@GET
	@Path("/user/get/{id}")
	public UserBean getUser(@PathParam("id") Long id) {
		UserBean user = new UserBean();
		try {
			IpUser ipUser = ipUserDAO.findById(id);
			user.setuId(ipUser.getUserId());
			user.setContact(ipUser.getUserContact());
			user.seteMail(ipUser.getUserEmail());
			user.setfName(ipUser.getUserFName());
			user.setIdNum(ipUser.getUserIdNum());
			user.setlName(ipUser.getUserLName());
			user.setScName(ipUser.getUserScreenName());
			user.setSkills(ipUser.getUserSkills());
			user.setIsActive(ipUser.getUserStatus().equalsIgnoreCase("y"));
			if (ipUser.getUserFbHandle() != null && ipUser.getUserFbHandle().length() > 0)
				user.setFbHandle(ipUser.getUserFbHandle());
			if (ipUser.getUserAvatar() != null && ipUser.getUserAvatar().length() > 0)
				user.setAvatar(ipUser.getUserAvatar());
			if (ipUser.getUserBio() != null && ipUser.getUserBio().length() > 0)
				user.setBio(ipUser.getUserBio());
			if (ipUser.getUserMName() != null && ipUser.getUserMName().length() > 0)
				user.setmName(ipUser.getUserMName());
			if (ipUser.getUserTwHandle() != null && ipUser.getUserTwHandle().length() > 0)
				user.setTwHandle(ipUser.getUserTwHandle());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}

	public IpGroupDAO getIpGroupDAO() {
		return ipGroupDAO;
	}

	public void setIpGroupDAO(IpGroupDAO ipGroupDAO) {
		this.ipGroupDAO = ipGroupDAO;
	}

	public IpUserDAO getIpUserDAO() {
		return ipUserDAO;
	}

	public void setIpUserDAO(IpUserDAO ipUserDAO) {
		this.ipUserDAO = ipUserDAO;
	}
}
