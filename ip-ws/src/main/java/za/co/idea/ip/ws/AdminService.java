package za.co.idea.ip.ws;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.Md5Crypt;

import za.co.idea.ip.orm.bean.IpFunction;
import za.co.idea.ip.orm.bean.IpFunctionConfig;
import za.co.idea.ip.orm.bean.IpGroup;
import za.co.idea.ip.orm.bean.IpLogin;
import za.co.idea.ip.orm.bean.IpUser;
import za.co.idea.ip.orm.dao.IpFunctionConfigDAO;
import za.co.idea.ip.orm.dao.IpFunctionDAO;
import za.co.idea.ip.orm.dao.IpGroupDAO;
import za.co.idea.ip.orm.dao.IpLoginDAO;
import za.co.idea.ip.orm.dao.IpUserDAO;
import za.co.idea.ip.ws.bean.FunctionMessage;
import za.co.idea.ip.ws.bean.GroupMessage;
import za.co.idea.ip.ws.bean.ResponseMessage;
import za.co.idea.ip.ws.bean.UserMessage;

@SuppressWarnings("rawtypes")
@Path(value = "/as")
public class AdminService {
	private IpGroupDAO ipGroupDAO;
	private IpUserDAO ipUserDAO;
	private IpLoginDAO ipLoginDAO;
	private IpFunctionDAO ipFunctionDAO;
	private IpFunctionConfigDAO ipFunctionConfigDAO;

	@POST
	@Path("/group/add")
	@Consumes("application/json")
	@Produces("application/json")
	public ResponseMessage createGroup(GroupMessage group) {
		IpGroup ipGroup = new IpGroup();
		ipGroup.setGroupId(group.getgId());
		ipGroup.setGroupEmail(group.getGeMail());
		ipGroup.setGroupName(group.getgName());
		ipGroup.setGroupStatus(((group.getIsActive() != null && group.getIsActive()) ? "y" : "n"));
		if (group.getpGrpId() != null && group.getpGrpId().longValue() != 0)
			ipGroup.setIpGroup(ipGroupDAO.findById(group.getpGrpId()));
		if (group.getAdmUserId() != null && group.getAdmUserId().longValue() != 0)
			ipGroup.setIpUser(ipUserDAO.findById(group.getAdmUserId()));
		try {
			ipGroupDAO.save(ipGroup);
			return ResponseMessage.createSuccess();
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseMessage.createException(e);
		}
	}

	@PUT
	@Path("/group/modify")
	@Consumes("application/json")
	@Produces("application/json")
	public ResponseMessage updateGroup(GroupMessage group) {
		IpGroup ipGroup = new IpGroup();
		ipGroup.setGroupId(group.getgId());
		ipGroup.setGroupEmail(group.getGeMail());
		ipGroup.setGroupName(group.getgName());
		ipGroup.setGroupStatus(((group.getIsActive() != null && group.getIsActive()) ? "y" : "n"));
		if (group.getpGrpId() != null && group.getpGrpId().longValue() != 0)
			ipGroup.setIpGroup(ipGroupDAO.findById(group.getpGrpId()));
		if (group.getAdmUserId() != null && group.getAdmUserId().longValue() != 0)
			ipGroup.setIpUser(ipUserDAO.findById(group.getAdmUserId()));
		try {
			ipGroupDAO.merge(ipGroup);
			return ResponseMessage.createSuccess();
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseMessage.createException(e);
		}
	}

	@GET
	@Path("/group/list")
	@Produces("application/json")
	public List<GroupMessage> listGroup() {
		List<GroupMessage> ret = new ArrayList<GroupMessage>();
		try {
			List groups = ipGroupDAO.findAll();
			for (Object object : groups) {
				IpGroup ipGroup = (IpGroup) object;
				GroupMessage group = new GroupMessage();
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
	@Path("/func/list")
	@Produces("application/json")
	public List<FunctionMessage> listFunction() {
		List<FunctionMessage> ret = new ArrayList<FunctionMessage>();
		try {
			List functions = ipFunctionDAO.findAll();
			for (Object object : functions) {
				IpFunction ipFunction = (IpFunction) object;
				FunctionMessage function = new FunctionMessage();
				function.setFuncId(ipFunction.getFuncId());
				function.setFuncName(ipFunction.getFuncName());
				List<UserMessage> users = new ArrayList<UserMessage>();
				List<GroupMessage> groups = new ArrayList<GroupMessage>();
				for (Object obj : ipFunction.getIpFunctionConfigs()) {
					IpFunctionConfig config = (IpFunctionConfig) obj;
					if (config.getIpGroup() != null && config.getIpGroup().getGroupId() != null) {
						GroupMessage group = new GroupMessage();
						group.setgId(config.getIpGroup().getGroupId());
						group.setgName(config.getIpGroup().getGroupName());
						groups.add(group);
					}
					if (config.getIpUser() != null && config.getIpUser().getUserId() != null) {
						UserMessage user = new UserMessage();
						user.setuId(config.getIpUser().getUserId());
						user.setScName(config.getIpUser().getUserScreenName());
						users.add(user);
					}
				}
				function.setGroupList(groups);
				function.setUserList(users);
				ret.add(function);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	@GET
	@Path("/func/get/{id}")
	@Produces("application/json")
	public FunctionMessage getFunctionById(@PathParam("id") Long id) {
		FunctionMessage function = new FunctionMessage();
		try {
			IpFunction ipFunction = ipFunctionDAO.findById(id);
			function.setFuncId(ipFunction.getFuncId());
			function.setFuncName(ipFunction.getFuncName());
			List<UserMessage> users = new ArrayList<UserMessage>();
			List<GroupMessage> groups = new ArrayList<GroupMessage>();
			for (Object obj : ipFunction.getIpFunctionConfigs()) {
				IpFunctionConfig config = (IpFunctionConfig) obj;
				GroupMessage group = new GroupMessage();
				group.setgId(config.getIpGroup().getGroupId());
				group.setgName(config.getIpGroup().getGroupName());
				groups.add(group);
				UserMessage user = new UserMessage();
				user.setuId(config.getIpUser().getUserId());
				user.setScName(config.getIpUser().getUserScreenName());
				users.add(user);
			}
			function.setGroupList(groups);
			function.setUserList(users);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return function;
	}

	@GET
	@Path("/group/get/{id}")
	@Produces("application/json")
	public GroupMessage getGroupById(@PathParam("id") Long id) {
		GroupMessage group = new GroupMessage();
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
	@Produces("application/json")
	public ResponseMessage createUser(UserMessage user) {
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
			ipUser.setUserStatus(((user.getIsActive() != null && user.getIsActive()) ? "y" : "n"));
			if (user.getFbHandle() != null && user.getFbHandle().length() > 0)
				ipUser.setUserFbHandle(user.getFbHandle());
			if (user.getAvatar() != null && user.getAvatar().length() > 0)
				ipUser.setUserAvatar(user.getAvatar());
			if (user.getBio() != null && user.getBio().length() > 0)
				ipUser.setUserBio(user.getBio());
			if (user.getmName() != null && user.getmName().length() > 0)
				ipUser.setUserMName(user.getmName());
			if (user.getTwHandle() != null && user.getTwHandle().length() > 0)
				ipUser.setUserTwHandle(user.getTwHandle());
			ipUserDAO.save(ipUser);
			IpLogin ipLogin = new IpLogin();
			ipLogin.setIpUser(ipUser);
			ipLogin.setLoginName(ipUser.getUserScreenName());
			ipLogin.setLoginId(user.getuId());
			ipLogin.setLoginPwd(Base64.encodeBase64URLSafeString(DigestUtils.md5(user.getPwd().getBytes())));
			try {
				ipLoginDAO.save(ipLogin);
			} catch (Exception e) {
				ipUserDAO.delete(ipUser);
				throw new RuntimeException("Cannot create user :: " + e.getMessage());
			}
			return ResponseMessage.createSuccess();
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseMessage.createException(e);
		}
	}

	@POST
	@Path("/func/add")
	@Consumes("application/json")
	@Produces("application/json")
	public ResponseMessage createFunction(FunctionMessage function) {
		try {
			IpFunction ipFunction = new IpFunction();
			ipFunction.setFuncId(function.getFuncId());
			ipFunction.setFuncName(function.getFuncName());
			Set<IpFunctionConfig> configs = new HashSet<IpFunctionConfig>();
			for (Long grpId : function.getGroupIdList()) {
				IpFunctionConfig config = new IpFunctionConfig();
				config.setFcId(UUID.randomUUID().toString());
				config.setIpFunction(ipFunction);
				config.setIpGroup(ipGroupDAO.findById(grpId));
				configs.add(config);
			}
			for (Long usrId : function.getUserIdList()) {
				IpFunctionConfig config = new IpFunctionConfig();
				config.setFcId(UUID.randomUUID().toString());
				config.setIpFunction(ipFunction);
				config.setIpUser(ipUserDAO.findById(usrId));
				configs.add(config);
			}
			ipFunction.setIpFunctionConfigs(configs);
			ipFunctionDAO.save(ipFunction);
			return ResponseMessage.createSuccess();
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseMessage.createException(e);
		}
	}

	@PUT
	@Path("/func/modify")
	@Consumes("application/json")
	@Produces("application/json")
	public ResponseMessage updateFunction(FunctionMessage function) {
		try {
			ipFunctionConfigDAO.deleteByFunctionId(function.getFuncId());
			IpFunction ipFunction = new IpFunction();
			ipFunction.setFuncId(function.getFuncId());
			ipFunction.setFuncName(function.getFuncName());
			Set<IpFunctionConfig> configs = new HashSet<IpFunctionConfig>();
			for (Long grpId : function.getGroupIdList()) {
				IpFunctionConfig config = new IpFunctionConfig();
				config.setFcId(UUID.randomUUID().toString());
				config.setIpFunction(ipFunction);
				config.setIpGroup(ipGroupDAO.findById(grpId));
				configs.add(config);
			}
			for (Long usrId : function.getUserIdList()) {
				IpFunctionConfig config = new IpFunctionConfig();
				config.setFcId(UUID.randomUUID().toString());
				config.setIpFunction(ipFunction);
				config.setIpUser(ipUserDAO.findById(usrId));
				configs.add(config);
			}
			ipFunction.setIpFunctionConfigs(configs);
			ipFunctionDAO.merge(ipFunction);
			return ResponseMessage.createSuccess();
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseMessage.createException(e);
		}
	}

	@PUT
	@Path("/user/modify")
	@Consumes("application/json")
	@Produces("application/json")
	public ResponseMessage updateUser(UserMessage user) {
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
			ipUser.setUserStatus(((user.getIsActive() != null && user.getIsActive()) ? "y" : "n"));
			if (user.getFbHandle() != null && user.getFbHandle().length() > 0)
				ipUser.setUserFbHandle(user.getFbHandle());
			if (user.getAvatar() != null && user.getAvatar().length() > 0)
				ipUser.setUserAvatar(user.getAvatar());
			if (user.getBio() != null && user.getBio().length() > 0)
				ipUser.setUserBio(user.getBio());
			if (user.getmName() != null && user.getmName().length() > 0)
				ipUser.setUserMName(user.getmName());
			if (user.getTwHandle() != null && user.getTwHandle().length() > 0)
				ipUser.setUserTwHandle(user.getTwHandle());
			ipUserDAO.merge(ipUser);
			IpLogin ipLogin = new IpLogin();
			ipLogin.setIpUser(ipUser);
			ipLogin.setLoginName(ipUser.getUserScreenName());
			ipLogin.setLoginId(user.getuId());
			ipLogin.setLoginPwd(Md5Crypt.md5Crypt(user.getPwd().getBytes()));
			try {
				ipLoginDAO.merge(ipLogin);
			} catch (Exception e) {
				throw new RuntimeException("Cannot merge login :: " + e.getMessage());
			}
			return ResponseMessage.createSuccess();
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseMessage.createException(e);
		}
	}

	@GET
	@Path("/user/list")
	@Produces("application/json")
	public List<UserMessage> listUser() {
		List<UserMessage> ret = new ArrayList<UserMessage>();
		try {
			List users = ipUserDAO.findAll();
			for (Object object : users) {
				IpUser ipUser = (IpUser) object;
				UserMessage user = new UserMessage();
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
	@Produces("application/json")
	public UserMessage getUserById(@PathParam("id") Long id) {
		UserMessage user = new UserMessage();
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

	@GET
	@Path("/user/check/screenName/{sc}")
	@Produces("application/json")
	public Boolean checkScreenName(@PathParam("sc") String sc) {
		List usersByScName = ipUserDAO.findByUserScreenName(sc);
		Boolean ret = (usersByScName != null && usersByScName.size() > 0);
		return ret;
	}

	@GET
	@Path("/user/login/{login}/{pwd}")
	@Produces("application/json")
	public UserMessage login(@PathParam("login") String login, @PathParam("pwd") String pwd) {
		List logins = ipLoginDAO.verifyLogin(login, pwd);
		if (logins.size() == 0)
			return null;
		else {
			UserMessage user = new UserMessage();
			try {
				IpLogin ipLogin = (IpLogin) logins.get(0);
				IpUser ipUser = ipLogin.getIpUser();
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

	public IpLoginDAO getIpLoginDAO() {
		return ipLoginDAO;
	}

	public void setIpLoginDAO(IpLoginDAO ipLoginDAO) {
		this.ipLoginDAO = ipLoginDAO;
	}

	public IpFunctionDAO getIpFunctionDAO() {
		return ipFunctionDAO;
	}

	public void setIpFunctionDAO(IpFunctionDAO ipFunctionDAO) {
		this.ipFunctionDAO = ipFunctionDAO;
	}

	public IpFunctionConfigDAO getIpFunctionConfigDAO() {
		return ipFunctionConfigDAO;
	}

	public void setIpFunctionConfigDAO(IpFunctionConfigDAO ipFunctionConfigDAO) {
		this.ipFunctionConfigDAO = ipFunctionConfigDAO;
	}
}
