package za.co.idea.web.ui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.client.WebClient;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.codehaus.jackson.map.ObjectMapper;

import za.co.idea.ip.ws.bean.GroupMessage;
import za.co.idea.ip.ws.bean.UserMessage;
import za.co.idea.web.ui.bean.GroupBean;
import za.co.idea.web.ui.bean.UserBean;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class AdminController implements Serializable {
	private static final long serialVersionUID = 1441325880500732566L;

	/**
	 * Create and Edit Beans
	 */
	private UserBean userBean;
	private GroupBean groupBean;
	private List<GroupBean> pGrps;
	private List<UserBean> admUsers;

	/**
	 * View Beans
	 */
	private List<UserBean> viewUsers;
	private List<GroupBean> viewGroups;

	public String showViewUsers() {
		viewUsers = fetchAllUsers();
		return "admvu";
	}

	public String showEditUser() {
		userBean.setcPw(userBean.getPwd());
		return "admeu";
	}

	public String showCreateUser() {
		userBean = new UserBean();
		return "admcu";
	}

	public String saveUser() {
		List providers = new ArrayList();
		providers.add(new JacksonJsonProvider(new ObjectMapper()));
		WebClient addUserClient = WebClient.create("http://127.0.0.1:8080/ip-ws/ip/as/user/add", providers);
		addUserClient.header("Content-Type", "application/json");
		addUserClient.header("Accept", "application/json");
		UserMessage bean = new UserMessage();
		bean.setAvatar(userBean.getAvatar());
		bean.setBio(userBean.getBio());
		bean.setContact(userBean.getContact());
		bean.seteMail(userBean.geteMail());
		bean.setFbHandle(userBean.getFbHandle());
		bean.setfName(userBean.getfName());
		bean.setIdNum(userBean.getIdNum());
		bean.setIsActive(userBean.getIsActive());
		bean.setlName(userBean.getlName());
		bean.setmName(userBean.getmName());
		bean.setPwd(userBean.getPwd());
		bean.setScName(userBean.getScName());
		bean.setSkills(userBean.getSkills());
		bean.setTwHandle(userBean.getTwHandle());
		bean.setIsActive(true);
		bean.setuId(System.currentTimeMillis());
		Response response = addUserClient.accept(MediaType.APPLICATION_JSON).post(bean);
		if (response.getStatus() == Response.Status.OK.getStatusCode())
			return "home";
		else {
			return "";
		}
	}

	public String updateUser() {
		List providers = new ArrayList();
		providers.add(new JacksonJsonProvider(new ObjectMapper()));
		WebClient updateUserClient = WebClient.create("http://127.0.0.1:8080/ip-ws/ip/as/user/modify", providers);
		updateUserClient.header("Content-Type", "application/json");
		updateUserClient.header("Accept", "application/json");
		UserMessage bean = new UserMessage();
		bean.setAvatar(userBean.getAvatar());
		bean.setBio(userBean.getBio());
		bean.setContact(userBean.getContact());
		bean.seteMail(userBean.geteMail());
		bean.setFbHandle(userBean.getFbHandle());
		bean.setfName(userBean.getfName());
		bean.setIdNum(userBean.getIdNum());
		bean.setIsActive(userBean.getIsActive());
		bean.setlName(userBean.getlName());
		bean.setmName(userBean.getmName());
		bean.setPwd(userBean.getPwd());
		bean.setScName(userBean.getScName());
		bean.setSkills(userBean.getSkills());
		bean.setTwHandle(userBean.getTwHandle());
		bean.setIsActive(true);
		bean.setuId(userBean.getuId());
		Response response = updateUserClient.accept(MediaType.APPLICATION_JSON).put(bean);
		if (response.getStatus() == Response.Status.OK.getStatusCode())
			return "home";
		else {
			return "";
		}
	}

	public String showViewGroups() {
		viewGroups = fetchAllGroups();
		pGrps = fetchAllGroups();
		admUsers = fetchAllUsers();
		return "admvg";
	}

	public String showEditGroup() {
		pGrps = fetchAllGroups();
		admUsers = fetchAllUsers();
		return "admeg";
	}

	public String showCreateGroup() {
		groupBean = new GroupBean();
		pGrps = fetchAllGroups();
		admUsers = fetchAllUsers();
		return "admcg";
	}

	public String saveGroup() {
		List providers = new ArrayList();
		providers.add(new JacksonJsonProvider(new ObjectMapper()));
		WebClient addGroupClient = WebClient.create("http://127.0.0.1:8080/ip-ws/ip/as/group/add", providers);
		addGroupClient.header("Content-Type", "application/json");
		addGroupClient.header("Accept", "application/json");
		GroupMessage groupMessage = new GroupMessage();
		groupMessage.setAdmUserId(groupBean.getSelAdmUser());
		groupMessage.setGeMail(groupBean.getGeMail());
		groupMessage.setgId(System.currentTimeMillis());
		groupMessage.setgName(groupBean.getgName());
		groupMessage.setIsActive(true);
		groupMessage.setpGrpId(groupBean.getSelPGrp());
		Response response = addGroupClient.accept(MediaType.APPLICATION_JSON).post(groupMessage);
		if (response.getStatus() == Response.Status.OK.getStatusCode())
			return "home";
		else {
			return "";
		}
	}

	public String updateGroup() {
		List providers = new ArrayList();
		providers.add(new JacksonJsonProvider(new ObjectMapper()));
		WebClient updateGroupClient = WebClient.create("http://127.0.0.1:8080/ip-ws/ip/as/group/modify", providers);
		updateGroupClient.header("Content-Type", "application/json");
		updateGroupClient.header("Accept", "application/json");
		GroupMessage groupMessage = new GroupMessage();
		groupMessage.setAdmUserId(groupBean.getSelAdmUser());
		groupMessage.setGeMail(groupBean.getGeMail());
		groupMessage.setgId(groupBean.getgId());
		groupMessage.setgName(groupBean.getgName());
		groupMessage.setIsActive(true);
		groupMessage.setpGrpId(groupBean.getSelPGrp());
		Response response = updateGroupClient.accept(MediaType.APPLICATION_JSON).put(groupMessage);
		if (response.getStatus() == Response.Status.OK.getStatusCode())
			return "home";
		else {
			return "";
		}
	}

	private List<UserBean> fetchAllUsers() {
		List<UserBean> ret = new ArrayList<UserBean>();
		List providers = new ArrayList();
		providers.add(new JacksonJsonProvider(new ObjectMapper()));
		WebClient viewUsersClient = WebClient.create("http://127.0.0.1:8080/ip-ws/ip/as/user/list", providers);
		viewUsersClient.header("Content-Type", "application/json");
		viewUsersClient.header("Accept", "application/json");
		List<UserMessage> users = new ArrayList<UserMessage>(viewUsersClient.accept(MediaType.APPLICATION_JSON).getCollection(UserMessage.class));
		for (UserMessage userMessage : users) {
			UserBean bean = new UserBean();
			bean.setAvatar(userMessage.getAvatar());
			bean.setBio(userMessage.getBio());
			bean.setContact(userMessage.getContact());
			bean.seteMail(userMessage.geteMail());
			bean.setFbHandle(userMessage.getFbHandle());
			bean.setfName(userMessage.getfName());
			bean.setIdNum(userMessage.getIdNum());
			bean.setIsActive(userMessage.getIsActive());
			bean.setlName(userMessage.getlName());
			bean.setmName(userMessage.getmName());
			bean.setPwd(userMessage.getPwd());
			bean.setScName(userMessage.getScName());
			bean.setSkills(userMessage.getSkills());
			bean.setTwHandle(userMessage.getTwHandle());
			bean.setIsActive(true);
			bean.setuId(userMessage.getuId());
			ret.add(bean);
		}
		return ret;
	}

	private List<GroupBean> fetchAllGroups() {
		List<GroupBean> ret = new ArrayList<GroupBean>();
		List providers = new ArrayList();
		providers.add(new JacksonJsonProvider(new ObjectMapper()));
		WebClient viewGroupsClient = WebClient.create("http://127.0.0.1:8080/ip-ws/ip/as/group/list", providers);
		viewGroupsClient.header("Content-Type", "application/json");
		viewGroupsClient.header("Accept", "application/json");
		List<GroupMessage> groups = new ArrayList<GroupMessage>(viewGroupsClient.accept(MediaType.APPLICATION_JSON).getCollection(GroupMessage.class));
		for (GroupMessage groupMessage : groups) {
			GroupBean bean = new GroupBean();
			bean.setgId(groupMessage.getgId());
			bean.setGeMail(groupMessage.getGeMail());
			bean.setgName(groupMessage.getgName());
			bean.setIsActive(groupMessage.getIsActive());
			bean.setSelAdmUser(groupMessage.getAdmUserId());
			bean.setSelPGrp(groupMessage.getpGrpId());
			ret.add(bean);
		}
		return ret;
	}

	public GroupBean getParentGroup(Long pGrpId) {
		GroupBean bean = new GroupBean();
		List providers = new ArrayList();
		providers.add(new JacksonJsonProvider(new ObjectMapper()));
		WebClient groupByIdClient = WebClient.create("http://127.0.0.1:8080/ip-ws/ip/as/group/get/" + pGrpId, providers);
		groupByIdClient.header("Content-Type", "application/json");
		groupByIdClient.header("Accept", "application/json");
		GroupMessage groupMessage = groupByIdClient.accept(MediaType.APPLICATION_JSON).get(GroupMessage.class);
		bean.setgId(groupMessage.getgId());
		bean.setGeMail(groupMessage.getGeMail());
		bean.setgName(groupMessage.getgName());
		bean.setIsActive(groupMessage.getIsActive());
		bean.setSelAdmUser(groupMessage.getAdmUserId());
		bean.setSelPGrp(groupMessage.getpGrpId());
		return bean;
	}

	public UserBean getUserById(Long id) {
		UserBean bean = new UserBean();
		List providers = new ArrayList();
		providers.add(new JacksonJsonProvider(new ObjectMapper()));
		WebClient userByIdClient = WebClient.create("http://127.0.0.1:8080/ip-ws/ip/as/user/get/" + id, providers);
		userByIdClient.header("Content-Type", "application/json");
		userByIdClient.header("Accept", "application/json");
		UserMessage userMessage = userByIdClient.accept(MediaType.APPLICATION_JSON).get(UserMessage.class);
		bean.setAvatar(userMessage.getAvatar());
		bean.setBio(userMessage.getBio());
		bean.setContact(userMessage.getContact());
		bean.seteMail(userMessage.geteMail());
		bean.setFbHandle(userMessage.getFbHandle());
		bean.setfName(userMessage.getfName());
		bean.setIdNum(userMessage.getIdNum());
		bean.setIsActive(userMessage.getIsActive());
		bean.setlName(userMessage.getlName());
		bean.setmName(userMessage.getmName());
		bean.setPwd(userMessage.getPwd());
		bean.setScName(userMessage.getScName());
		bean.setSkills(userMessage.getSkills());
		bean.setTwHandle(userMessage.getTwHandle());
		bean.setIsActive(true);
		bean.setuId(userMessage.getuId());
		return bean;
	}

	public UserBean getUserBean() {
		if (userBean == null)
			userBean = new UserBean();
		return userBean;
	}

	public void setUserBean(UserBean userBean) {
		this.userBean = userBean;
	}

	public GroupBean getGroupBean() {
		if (groupBean == null)
			groupBean = new GroupBean();
		return groupBean;
	}

	public void setGroupBean(GroupBean groupBean) {
		this.groupBean = groupBean;
	}

	public List<GroupBean> getpGrps() {
		if (pGrps == null)
			pGrps = new ArrayList<GroupBean>();
		return pGrps;
	}

	public void setpGrps(List<GroupBean> pGrps) {
		this.pGrps = pGrps;
	}

	public List<UserBean> getAdmUsers() {
		if (admUsers == null)
			admUsers = new ArrayList<UserBean>();
		return admUsers;
	}

	public void setAdmUsers(List<UserBean> admUsers) {
		this.admUsers = admUsers;
	}

	public List<UserBean> getViewUsers() {
		if (viewUsers == null)
			viewUsers = new ArrayList<UserBean>();
		return viewUsers;
	}

	public void setViewUsers(List<UserBean> viewUsers) {
		this.viewUsers = viewUsers;
	}

	public List<GroupBean> getViewGroups() {
		if (viewGroups == null)
			viewGroups = new ArrayList<GroupBean>();
		return viewGroups;
	}

	public void setViewGroups(List<GroupBean> viewGroups) {
		this.viewGroups = viewGroups;
	}
}
