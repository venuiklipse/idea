package za.co.idea.web.ui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.ws.rs.core.MediaType;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.jaxrs.provider.json.JSONProvider;

import za.co.idea.ip.ws.bean.FunctionMessage;
import za.co.idea.ip.ws.bean.GroupMessage;
import za.co.idea.ip.ws.bean.ResponseMessage;
import za.co.idea.ip.ws.bean.UserMessage;
import za.co.idea.ip.ws.util.NumericCounter;
import za.co.idea.web.ui.bean.FunctionBean;
import za.co.idea.web.ui.bean.GroupBean;
import za.co.idea.web.ui.bean.UserBean;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class AdminController implements Serializable {
	private static final long serialVersionUID = 1441325880500732566L;
	private static final Log LOG = LogFactory.getLog(AdminController.class);

	private UserBean userBean;
	private GroupBean groupBean;
	private FunctionBean functionBean;
	private List<FunctionBean> functions;
	private List<GroupBean> pGrps;
	private List<UserBean> admUsers;
	private List<UserBean> viewUsers;
	private List<GroupBean> viewGroups;
	private boolean available;
	private static final NumericCounter COUNTER = new NumericCounter();

	public String login() {
		List providers = new ArrayList();
		JSONProvider provider = new JSONProvider();
		ArrayList<String> mediaTypes = new ArrayList<String>();
		mediaTypes.add(MediaType.APPLICATION_JSON);
		provider.setProduceMediaTypes(mediaTypes);
		providers.add(provider);
		WebClient loginClient = WebClient.create("http://127.0.0.1:8080/ip-ws/ip/as/user/login/" + userBean.getScName() + "/" + Base64.encodeBase64URLSafeString(DigestUtils.md5(userBean.getPwd().getBytes())), providers);
		loginClient.header("Content-Type", "application/json");
		loginClient.header("Accept", "application/json");
		UserMessage userMessage = loginClient.accept(MediaType.APPLICATION_JSON).get(UserMessage.class);
		if (userMessage == null) {
			FacesMessage exceptionMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid User Name Password", "Invalid User Name Password");
			FacesContext.getCurrentInstance().addMessage(null, exceptionMessage);
			return "";
		} else {
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
			bean.setIsActive(userMessage.getIsActive());
			bean.setuId(userMessage.getuId());
			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("user", bean);
			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("userId", bean.getuId());
			return "home";
		}
	}

	public String logout() {
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		return "login";
	}

	public String showViewUsers() {
		try {
			viewUsers = fetchAllUsers();
			return "admvu";
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e, e);
			FacesMessage exceptionMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, exceptionMessage);
			return "";
		}
	}

	public String showEditUser() {
		try {
			userBean.setcPw(userBean.getPwd());
			return "admeu";
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e, e);
			FacesMessage exceptionMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, exceptionMessage);
			return "";
		}
	}

	public String showCreateUser() {
		try {
			userBean = new UserBean();
			return "admcu";
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e, e);
			FacesMessage exceptionMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, exceptionMessage);
			return "";
		}
	}

	public String showViewGroups() {
		try {
			viewGroups = fetchAllGroups();
			pGrps = fetchAllGroups();
			admUsers = fetchAllUsers();
			return "admvg";
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e, e);
			FacesMessage exceptionMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, exceptionMessage);
			return "";
		}
	}

	public String showEditGroup() {
		try {
			pGrps = fetchAllGroups();
			admUsers = fetchAllUsers();
			return "admeg";
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e, e);
			FacesMessage exceptionMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, exceptionMessage);
			return "";
		}
	}

	public String showCreateGroup() {
		try {
			groupBean = new GroupBean();
			pGrps = fetchAllGroups();
			admUsers = fetchAllUsers();
			return "admcg";
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e, e);
			FacesMessage exceptionMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, exceptionMessage);
			return "";
		}
	}

	public String showCreateFunction() {
		try {
			admUsers = fetchAllUsers();
			pGrps = fetchAllGroups();
			return "admcf";
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e, e);
			FacesMessage exceptionMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, exceptionMessage);
			return "";
		}
	}

	public String showViewFunction() {
		try {
			functions = fetchAllFunctions();
			admUsers = fetchAllUsers();
			pGrps = fetchAllGroups();
			return "admvf";
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e, e);
			FacesMessage exceptionMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, exceptionMessage);
			return "";
		}
	}

	public String showEditFunction() {
		try {
			admUsers = fetchAllUsers();
			pGrps = fetchAllGroups();
			return "admef";
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e, e);
			FacesMessage exceptionMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, exceptionMessage);
			return "";
		}
	}

	public String checkAvailability() {
		List providers = new ArrayList();
		JSONProvider provider = new JSONProvider();
		ArrayList<String> mediaTypes = new ArrayList<String>();
		mediaTypes.add(MediaType.APPLICATION_JSON);
		provider.setProduceMediaTypes(mediaTypes);
		providers.add(provider);
		WebClient checkAvailablityClient = WebClient.create("http://127.0.0.1:8080/ip-ws/ip/as/user/check/screenName/" + userBean.getScName(), providers);
		checkAvailablityClient.header("Content-Type", "application/json");
		checkAvailablityClient.header("Accept", "application/json");
		Boolean avail = checkAvailablityClient.accept(MediaType.APPLICATION_JSON).get(Boolean.class);
		available = avail.booleanValue();
		if (available) {
			FacesMessage exceptionMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Screen Name Not Available", "Screen Name Not Available");
			FacesContext.getCurrentInstance().addMessage("txtSCName", exceptionMessage);
		} else {
			FacesMessage exceptionMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "Screen Name Available", "Screen Name Available");
			FacesContext.getCurrentInstance().addMessage("txtSCName", exceptionMessage);
		}
		return "";
	}

	public String saveUser() {
		try {
			if (available) {
				FacesMessage exceptionMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Screen Name Not Available", "Screen Name Not Available");
				FacesContext.getCurrentInstance().addMessage(null, exceptionMessage);
				return "";
			}
			List providers = new ArrayList();
			JSONProvider provider = new JSONProvider();
			ArrayList<String> mediaTypes = new ArrayList<String>();
			mediaTypes.add(MediaType.APPLICATION_JSON);
			provider.setProduceMediaTypes(mediaTypes);
			providers.add(provider);
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
			bean.setuId(COUNTER.nextLong());
			ResponseMessage response = addUserClient.accept(MediaType.APPLICATION_JSON).post(bean, ResponseMessage.class);
			if (response.getStatusCode() == 0)
				return "home";
			else {
				return "";
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e, e);
			FacesMessage exceptionMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, exceptionMessage);
			return "";
		}
	}

	public String updateUser() {
		try {
			List providers = new ArrayList();
			JSONProvider provider = new JSONProvider();
			ArrayList<String> mediaTypes = new ArrayList<String>();
			mediaTypes.add(MediaType.APPLICATION_JSON);
			provider.setProduceMediaTypes(mediaTypes);
			providers.add(provider);
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
			bean.setIsActive(userBean.getIsActive());
			bean.setuId(userBean.getuId());
			ResponseMessage response = updateUserClient.accept(MediaType.APPLICATION_JSON).put(bean, ResponseMessage.class);
			if (response.getStatusCode() == 0)
				return "home";
			else {
				return "";
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e, e);
			FacesMessage exceptionMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, exceptionMessage);
			return "";
		}
	}

	public String saveGroup() {
		try {
			List providers = new ArrayList();
			JSONProvider provider = new JSONProvider();
			ArrayList<String> mediaTypes = new ArrayList<String>();
			mediaTypes.add(MediaType.APPLICATION_JSON);
			provider.setProduceMediaTypes(mediaTypes);
			providers.add(provider);
			WebClient addGroupClient = WebClient.create("http://127.0.0.1:8080/ip-ws/ip/as/group/add", providers);
			addGroupClient.header("Content-Type", "application/json");
			addGroupClient.header("Accept", "application/json");
			GroupMessage groupMessage = new GroupMessage();
			groupMessage.setAdmUserId(groupBean.getSelAdmUser());
			groupMessage.setGeMail(groupBean.getGeMail());
			groupMessage.setgId(COUNTER.nextLong());
			groupMessage.setgName(groupBean.getgName());
			groupMessage.setIsActive(true);
			groupMessage.setpGrpId(groupBean.getSelPGrp());
			ResponseMessage response = addGroupClient.accept(MediaType.APPLICATION_JSON).post(groupMessage, ResponseMessage.class);
			if (response.getStatusCode() == 0)
				return "home";
			else {
				return "";
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e, e);
			FacesMessage exceptionMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, exceptionMessage);
			return "";
		}
	}

	public String saveFunction() {
		try {
			List providers = new ArrayList();
			JSONProvider provider = new JSONProvider();
			ArrayList<String> mediaTypes = new ArrayList<String>();
			mediaTypes.add(MediaType.APPLICATION_JSON);
			provider.setProduceMediaTypes(mediaTypes);
			providers.add(provider);
			WebClient addFunctionClient = WebClient.create("http://127.0.0.1:8080/ip-ws/ip/as/func/add", providers);
			addFunctionClient.header("Content-Type", "application/json");
			addFunctionClient.header("Accept", "application/json");
			FunctionMessage functionMessage = new FunctionMessage();
			functionMessage.setFuncId(COUNTER.nextLong());
			functionMessage.setFuncName(functionBean.getFuncName());
			functionMessage.setGroupIdList(functionBean.getGroupIdList());
			functionMessage.setUserIdList(functionBean.getUserIdList());
			ResponseMessage response = addFunctionClient.accept(MediaType.APPLICATION_JSON).post(functionMessage, ResponseMessage.class);
			if (response.getStatusCode() == 0)
				return "home";
			else {
				return "";
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e, e);
			FacesMessage exceptionMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, exceptionMessage);
			return "";
		}
	}

	public String updateGroup() {
		try {
			List providers = new ArrayList();
			JSONProvider provider = new JSONProvider();
			ArrayList<String> mediaTypes = new ArrayList<String>();
			mediaTypes.add(MediaType.APPLICATION_JSON);
			provider.setProduceMediaTypes(mediaTypes);
			providers.add(provider);
			WebClient updateGroupClient = WebClient.create("http://127.0.0.1:8080/ip-ws/ip/as/group/modify", providers);
			updateGroupClient.header("Content-Type", "application/json");
			updateGroupClient.header("Accept", "application/json");
			GroupMessage groupMessage = new GroupMessage();
			groupMessage.setAdmUserId(groupBean.getSelAdmUser());
			groupMessage.setGeMail(groupBean.getGeMail());
			groupMessage.setgId(groupBean.getgId());
			groupMessage.setgName(groupBean.getgName());
			groupMessage.setIsActive(groupBean.getIsActive());
			groupMessage.setpGrpId(groupBean.getSelPGrp());
			ResponseMessage response = updateGroupClient.accept(MediaType.APPLICATION_JSON).put(groupMessage, ResponseMessage.class);
			if (response.getStatusCode() == 0)
				return "home";
			else {
				return "";
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e, e);
			FacesMessage exceptionMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, exceptionMessage);
			return "";
		}
	}

	public String updateFunction() {
		try {
			List providers = new ArrayList();
			JSONProvider provider = new JSONProvider();
			ArrayList<String> mediaTypes = new ArrayList<String>();
			mediaTypes.add(MediaType.APPLICATION_JSON);
			provider.setProduceMediaTypes(mediaTypes);
			providers.add(provider);
			WebClient updateFunctionClient = WebClient.create("http://127.0.0.1:8080/ip-ws/ip/as/func/modify", providers);
			updateFunctionClient.header("Content-Type", "application/json");
			updateFunctionClient.header("Accept", "application/json");
			FunctionMessage functionMessage = new FunctionMessage();
			functionMessage.setFuncId(functionBean.getFuncId());
			functionMessage.setFuncName(functionBean.getFuncName());
			functionMessage.setGroupIdList(functionBean.getGroupIdList());
			functionMessage.setUserIdList(functionBean.getUserIdList());
			ResponseMessage response = updateFunctionClient.accept(MediaType.APPLICATION_JSON).put(functionMessage, ResponseMessage.class);
			if (response.getStatusCode() == 0)
				return "home";
			else {
				return "";
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e, e);
			FacesMessage exceptionMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, exceptionMessage);
			return "";
		}
	}

	private List<UserBean> fetchAllUsers() {
		List<UserBean> ret = new ArrayList<UserBean>();
		List providers = new ArrayList();
		JSONProvider provider = new JSONProvider();
		ArrayList<String> mediaTypes = new ArrayList<String>();
		mediaTypes.add(MediaType.APPLICATION_JSON);
		provider.setProduceMediaTypes(mediaTypes);
		providers.add(provider);
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
			bean.setIsActive(userMessage.getIsActive());
			bean.setuId(userMessage.getuId());
			ret.add(bean);
		}
		return ret;
	}

	private List<GroupBean> fetchAllGroups() {
		List<GroupBean> ret = new ArrayList<GroupBean>();
		List providers = new ArrayList();
		JSONProvider provider = new JSONProvider();
		ArrayList<String> mediaTypes = new ArrayList<String>();
		mediaTypes.add(MediaType.APPLICATION_JSON);
		provider.setProduceMediaTypes(mediaTypes);
		providers.add(provider);
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

	private List<FunctionBean> fetchAllFunctions() {
		List<FunctionBean> ret = new ArrayList<FunctionBean>();
		List providers = new ArrayList();
		JSONProvider provider = new JSONProvider();
		ArrayList<String> mediaTypes = new ArrayList<String>();
		mediaTypes.add(MediaType.APPLICATION_JSON);
		provider.setProduceMediaTypes(mediaTypes);
		providers.add(provider);
		WebClient viewFunctionsClient = WebClient.create("http://127.0.0.1:8080/ip-ws/ip/as/func/list", providers);
		viewFunctionsClient.header("Content-Type", "application/json");
		viewFunctionsClient.header("Accept", "application/json");
		List<FunctionMessage> functions = new ArrayList<FunctionMessage>(viewFunctionsClient.accept(MediaType.APPLICATION_JSON).getCollection(FunctionMessage.class));
		for (FunctionMessage functionMessage : functions) {
			FunctionBean bean = new FunctionBean();
			bean.setFuncId(functionMessage.getFuncId());
			bean.setFuncName(functionMessage.getFuncName());
			bean.getGroupList().clear();
			for (GroupMessage msg : functionMessage.getGroupList()) {
				GroupBean gBean = new GroupBean();
				gBean.setgId(msg.getgId());
				gBean.setgName(msg.getgName());
				bean.getGroupList().add(gBean);
			}
			bean.getUserList().clear();
			for (UserMessage msg : functionMessage.getUserList()) {
				UserBean uBean = new UserBean();
				uBean.setuId(msg.getuId());
				uBean.setScName(msg.getScName());
				bean.getUserList().add(uBean);
			}
			ret.add(bean);
		}
		return ret;
	}

	public GroupBean getParentGroup(Long pGrpId) {
		GroupBean bean = new GroupBean();
		List providers = new ArrayList();
		JSONProvider provider = new JSONProvider();
		ArrayList<String> mediaTypes = new ArrayList<String>();
		mediaTypes.add(MediaType.APPLICATION_JSON);
		provider.setProduceMediaTypes(mediaTypes);
		providers.add(provider);
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
		JSONProvider provider = new JSONProvider();
		ArrayList<String> mediaTypes = new ArrayList<String>();
		mediaTypes.add(MediaType.APPLICATION_JSON);
		provider.setProduceMediaTypes(mediaTypes);
		providers.add(provider);
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

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	public FunctionBean getFunctionBean() {
		if (functionBean == null)
			functionBean = new FunctionBean();
		return functionBean;
	}

	public void setFunctionBean(FunctionBean functionBean) {
		this.functionBean = functionBean;
	}

	public List<FunctionBean> getFunctions() {
		if (functions == null)
			functions = new ArrayList<FunctionBean>();
		return functions;
	}

	public void setFunctions(List<FunctionBean> functions) {
		this.functions = functions;
	}

}
