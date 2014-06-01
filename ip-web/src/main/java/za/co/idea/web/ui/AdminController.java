package za.co.idea.web.ui;

import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.ws.rs.core.MediaType;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import za.co.idea.ip.jaxws.document.Document;
import za.co.idea.ip.jaxws.document.DocumentService;
import za.co.idea.ip.jaxws.document.DownloadDocumentRq;
import za.co.idea.ip.jaxws.document.DownloadDocumentRs;
import za.co.idea.ip.jaxws.document.UploadDocumentRq;
import za.co.idea.ip.jaxws.document.UploadDocumentRs;
import za.co.idea.ip.ws.bean.FunctionMessage;
import za.co.idea.ip.ws.bean.GroupMessage;
import za.co.idea.ip.ws.bean.ResponseMessage;
import za.co.idea.ip.ws.bean.UserMessage;
import za.co.idea.ip.ws.util.CustomObjectMapper;
import za.co.idea.web.ui.bean.FunctionBean;
import za.co.idea.web.ui.bean.GroupBean;
import za.co.idea.web.ui.bean.UserBean;
import za.co.idea.web.util.IdNumberGen;

public class AdminController implements Serializable {
	private static final long serialVersionUID = 1441325880500732566L;

	private UserBean userBean;
	private GroupBean groupBean;
	private FunctionBean functionBean;
	private List<FunctionBean> functions;
	private List<GroupBean> pGrps;
	private List<UserBean> admUsers;
	private List<UserBean> viewUsers;
	private List<GroupBean> viewGroups;
	private boolean available;
	private String secA;
	private String secQ;
	private StreamedContent image;
	private String fileName;
	private String contentType;
	private String uploadSrc;
	private boolean show;
	private boolean showDef;
	private boolean enableUpload;
	private static final IdNumberGen COUNTER = new IdNumberGen();

	private WebClient createCustomClient(String url) {
		WebClient client = WebClient.create(url, Collections.singletonList(new JacksonJsonProvider(new CustomObjectMapper())));
		client.header("Content-Type", "application/json");
		client.header("Accept", "application/json");
		return client;
	}

	public String login() {
		WebClient loginClient = createCustomClient("http://127.0.0.1:38080/ip-ws/ip/as/user/login/" + userBean.getScName() + "/" + Base64.encodeBase64URLSafeString(DigestUtils.md5(userBean.getPwd().getBytes())));
		UserMessage userMessage = loginClient.accept(MediaType.APPLICATION_JSON).get(UserMessage.class);
		if (userMessage == null) {
			FacesMessage exceptionMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid User Name Password", "Invalid User Name Password");
			FacesContext.getCurrentInstance().addMessage(null, exceptionMessage);
			return "";
		} else {
			UserBean bean = new UserBean();
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
			bean.setLastLoginDt(userMessage.getLastLoginDt());
			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("user", bean);
			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("userId", bean.getuId());
			try {
				DownloadDocumentRq rq = new DownloadDocumentRq();
				rq.setEntityId(userMessage.getuId().toString());
				rq.setEntityTableName("ip_user");
				DocumentService service = new DocumentService();
				DownloadDocumentRs rs = service.getDocumentSOAP().downloadDocument(rq);
				if (rs.getFileContent() == null || rs.getFileContent().length == 0)
					throw new Exception("Profile Image Not Avavilable");
				this.image = new DefaultStreamedContent(new ByteArrayInputStream(rs.getFileContent()));
				show = true;
				showDef = false;
			} catch (Exception e) {
				FacesMessage exceptionMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Cannot Load Profile image", e.getMessage());
				FacesContext.getCurrentInstance().addMessage(null, exceptionMessage);
				show = false;
				showDef = true;
			}
			return "home";
		}
	}

	public String verifyLogin() {
		WebClient loginClient = createCustomClient("http://127.0.0.1:38080/ip-ws/ip/as/user/verify/" + userBean.getScName());
		UserMessage userMessage = loginClient.accept(MediaType.APPLICATION_JSON).get(UserMessage.class);
		if (userMessage == null) {
			FacesMessage exceptionMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid User Name Password", "Invalid User Name Password");
			FacesContext.getCurrentInstance().addMessage(null, exceptionMessage);
			return "";
		} else {
			userBean.setScName(userMessage.getScName());
			userBean.setSecQ(userMessage.getSecQ());
			userBean.setSecA(userMessage.getSecA());
			return "";
		}
	}

	public String resetPassword() {
		if (Base64.encodeBase64URLSafeString(DigestUtils.md5(secA.getBytes())).equalsIgnoreCase(userBean.getSecA())) {
			WebClient loginClient = createCustomClient("http://127.0.0.1:38080/ip-ws/ip/as/user/rpw/");
			ResponseMessage response = loginClient.accept(MediaType.APPLICATION_JSON).put(new String[] { userBean.getScName(), Base64.encodeBase64URLSafeString(DigestUtils.md5(userBean.getPwd().getBytes())) }, ResponseMessage.class);
			if (response.getStatusCode() == 0) {
				FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
				userBean = new UserBean();
				return "login";
			} else {
				FacesMessage exceptionMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, response.getStatusCode() + " :: " + response.getStatusDesc(), response.getStatusCode() + " :: " + response.getStatusDesc());
				FacesContext.getCurrentInstance().addMessage(null, exceptionMessage);
				return "";
			}
		} else {
			FacesMessage exceptionMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid Security Answer", "Invalid Security Answer");
			FacesContext.getCurrentInstance().addMessage(null, exceptionMessage);
			return "";
		}

	}

	public String resetSecurity() {
		if (secQ == null || secQ.length() == 0) {
			FacesMessage exceptionMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Security Question is Mandatory", "Security Question is Mandatory");
			FacesContext.getCurrentInstance().addMessage("txtSCName", exceptionMessage);
			RequestContext.getCurrentInstance().openDialog("dlgSecUpdate");
			return "";
		}
		if (secA == null || secA.length() == 0) {
			FacesMessage exceptionMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Security Answer is Mandatory", "Security Answer is Mandatory");
			FacesContext.getCurrentInstance().addMessage("txtSCName", exceptionMessage);
			RequestContext.getCurrentInstance().openDialog("dlgSecUpdate");
			return "";
		}
		WebClient loginClient = createCustomClient("http://127.0.0.1:38080/ip-ws/ip/as/user/rsec");
		ResponseMessage response = loginClient.accept(MediaType.APPLICATION_JSON).put(new String[] { userBean.getScName(), secQ, secA }, ResponseMessage.class);
		this.userBean.setSecA(secA);
		this.userBean.setSecQ(secQ);
		if (response.getStatusCode() != 0) {
			FacesMessage exceptionMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, response.getStatusCode() + " :: " + response.getStatusDesc(), response.getStatusCode() + " :: " + response.getStatusDesc());
			FacesContext.getCurrentInstance().addMessage(null, exceptionMessage);
			RequestContext.getCurrentInstance().closeDialog("dlgSecUpdate");
			return "";
		}
		return "";
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
			e.printStackTrace(System.out);
			FacesMessage exceptionMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, exceptionMessage);
			return "";
		}
	}

	public String showRPw() {
		return "rpw";
	}

	public String redirectHome() {
		return "home";
	}

	public String showEditUser() {
		try {
			userBean.setcPw(userBean.getPwd());
			return "admeu";
		} catch (Exception e) {
			e.printStackTrace(System.out);
			FacesMessage exceptionMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, exceptionMessage);
			return "";
		}
	}

	public String showEditProfile() {
		try {
			userBean = (UserBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");
			userBean.setcPw(userBean.getPwd());
			return "admeu";
		} catch (Exception e) {
			e.printStackTrace(System.out);
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
			e.printStackTrace(System.out);
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
			e.printStackTrace(System.out);
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
			e.printStackTrace(System.out);
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
			e.printStackTrace(System.out);
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
			e.printStackTrace(System.out);
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
			e.printStackTrace(System.out);
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
			e.printStackTrace(System.out);
			FacesMessage exceptionMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, exceptionMessage);
			return "";
		}
	}

	public String checkAvailability() {
		if (userBean.getScName() == null || userBean.getScName().length() == 0) {
			FacesMessage exceptionMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Enter Screen Name to Check Availability", "Enter Screen Name to Check Availability");
			FacesContext.getCurrentInstance().addMessage("txtSCName", exceptionMessage);
			return "";
		}
		WebClient checkAvailablityClient = createCustomClient("http://127.0.0.1:38080/ip-ws/ip/as/user/check/screenName/" + userBean.getScName());
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
			WebClient addUserClient = createCustomClient("http://127.0.0.1:38080/ip-ws/ip/as/user/add");
			UserMessage bean = new UserMessage();
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
			bean.setuId(COUNTER.getNextId("ip_user"));
			bean.setLastLoginDt(new Date());
			bean.setSecQ(userBean.getSecQ());
			bean.setSecA(userBean.getSecA());
			ResponseMessage response = addUserClient.accept(MediaType.APPLICATION_JSON).post(bean, ResponseMessage.class);
			if (response.getStatusCode() == 0) {
				try {
					Document document = new Document();
					document.setContentType(contentType);
					document.setEntityId(bean.getuId().toString());
					document.setEntityTableName("ip_user");
					document.setFileContent(IOUtils.toByteArray(image.getStream()));
					document.setFileName(fileName);
					DocumentService service = new DocumentService();
					UploadDocumentRq rq = new UploadDocumentRq();
					rq.setDocument(document);
					UploadDocumentRs rs = service.getDocumentSOAP().uploadDocument(rq);
					if (Integer.valueOf(rs.getResponse().getRespCode()) != 0) {
						FacesMessage exceptionMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Unable to upload attachment. Please update later", rs.getResponse().getRespDesc());
						FacesContext.getCurrentInstance().addMessage(null, exceptionMessage);
					}
				} catch (Exception e) {
					FacesMessage exceptionMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Unable to upload attachment. Please update later", e.getMessage());
					FacesContext.getCurrentInstance().addMessage(null, exceptionMessage);
				}
				return "home";
			} else {
				FacesMessage exceptionMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, response.getStatusDesc(), response.getStatusDesc());
				FacesContext.getCurrentInstance().addMessage(null, exceptionMessage);
				return "";
			}
		} catch (Exception e) {
			e.printStackTrace(System.out);
			FacesMessage exceptionMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, exceptionMessage);
			return "";
		}
	}

	public String updateUser() {
		try {
			WebClient updateUserClient = createCustomClient("http://127.0.0.1:38080/ip-ws/ip/as/user/modify");
			UserMessage bean = new UserMessage();
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
			bean.setLastLoginDt(userBean.getLastLoginDt());
			ResponseMessage response = updateUserClient.accept(MediaType.APPLICATION_JSON).put(bean, ResponseMessage.class);
			if (response.getStatusCode() == 0)
				try {
					Document document = new Document();
					document.setContentType(contentType);
					document.setEntityId(bean.getuId().toString());
					document.setEntityTableName("ip_user");
					document.setFileContent(IOUtils.toByteArray(image.getStream()));
					document.setFileName(fileName);
					DocumentService service = new DocumentService();
					UploadDocumentRq rq = new UploadDocumentRq();
					rq.setDocument(document);
					UploadDocumentRs rs = service.getDocumentSOAP().uploadDocument(rq);
					if (Integer.valueOf(rs.getResponse().getRespCode()) != 0) {
						FacesMessage exceptionMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Unable to upload attachment. Please update later", rs.getResponse().getRespDesc());
						FacesContext.getCurrentInstance().addMessage(null, exceptionMessage);
					}
				} catch (Exception e) {
					FacesMessage exceptionMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Unable to upload attachment. Please update later", e.getMessage());
					FacesContext.getCurrentInstance().addMessage(null, exceptionMessage);
				}
			return "home";
		} catch (Exception e) {
			e.printStackTrace(System.out);
			FacesMessage exceptionMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, exceptionMessage);
			return "";
		}
	}

	public String saveGroup() {
		try {
			WebClient addGroupClient = createCustomClient("http://127.0.0.1:38080/ip-ws/ip/as/group/add");
			GroupMessage groupMessage = new GroupMessage();
			groupMessage.setAdmUserId(groupBean.getSelAdmUser());
			groupMessage.setGeMail(groupBean.getGeMail());
			groupMessage.setgId(COUNTER.getNextId("ip_group"));
			groupMessage.setgName(groupBean.getgName());
			groupMessage.setIsActive(true);
			groupMessage.setpGrpId(groupBean.getSelPGrp());
			ResponseMessage response = addGroupClient.accept(MediaType.APPLICATION_JSON).post(groupMessage, ResponseMessage.class);
			if (response.getStatusCode() == 0)
				return "home";
			else {
				FacesMessage exceptionMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, response.getStatusDesc(), response.getStatusDesc());
				FacesContext.getCurrentInstance().addMessage(null, exceptionMessage);
				return "";
			}
		} catch (Exception e) {
			e.printStackTrace(System.out);
			FacesMessage exceptionMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, exceptionMessage);
			return "";
		}
	}

	public String saveFunction() {
		try {
			WebClient addFunctionClient = createCustomClient("http://127.0.0.1:38080/ip-ws/ip/as/func/add");
			FunctionMessage functionMessage = new FunctionMessage();
			functionMessage.setFuncId(COUNTER.getNextId("ip_function"));
			functionMessage.setFuncName(functionBean.getFuncName());
			functionMessage.setGroupIdList(toIdArray(functionBean.getGroupIdList()));
			functionMessage.setUserIdList(toIdArray(functionBean.getUserIdList()));
			ResponseMessage response = addFunctionClient.accept(MediaType.APPLICATION_JSON).post(functionMessage, ResponseMessage.class);
			if (response.getStatusCode() == 0)
				return "home";
			else {
				FacesMessage exceptionMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, response.getStatusDesc(), response.getStatusDesc());
				FacesContext.getCurrentInstance().addMessage(null, exceptionMessage);
				return "";
			}
		} catch (Exception e) {
			e.printStackTrace(System.out);
			FacesMessage exceptionMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, exceptionMessage);
			return "";
		}
	}

	public String updateGroup() {
		try {
			WebClient updateGroupClient = createCustomClient("http://127.0.0.1:38080/ip-ws/ip/as/group/modify");
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
				FacesMessage exceptionMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, response.getStatusDesc(), response.getStatusDesc());
				FacesContext.getCurrentInstance().addMessage(null, exceptionMessage);
				return "";
			}
		} catch (Exception e) {
			e.printStackTrace(System.out);
			FacesMessage exceptionMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, exceptionMessage);
			return "";
		}
	}

	public String updateFunction() {
		try {
			WebClient updateFunctionClient = createCustomClient("http://127.0.0.1:38080/ip-ws/ip/as/func/modify");
			FunctionMessage functionMessage = new FunctionMessage();
			functionMessage.setFuncId(functionBean.getFuncId());
			functionMessage.setFuncName(functionBean.getFuncName());
			functionMessage.setGroupIdList(toIdArray(functionBean.getGroupIdList()));
			functionMessage.setUserIdList(toIdArray(functionBean.getUserIdList()));
			ResponseMessage response = updateFunctionClient.accept(MediaType.APPLICATION_JSON).put(functionMessage, ResponseMessage.class);
			if (response.getStatusCode() == 0)
				return "home";
			else {
				FacesMessage exceptionMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, response.getStatusDesc(), response.getStatusDesc());
				FacesContext.getCurrentInstance().addMessage(null, exceptionMessage);
				return "";
			}
		} catch (Exception e) {
			e.printStackTrace(System.out);
			FacesMessage exceptionMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, exceptionMessage);
			return "";
		}
	}

	public String updateImage() {
		try {
			Document document = new Document();
			document.setContentType(contentType);
			document.setEntityId(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userId").toString());
			document.setEntityTableName("ip_user");
			document.setFileContent(IOUtils.toByteArray(image.getStream()));
			document.setFileName(fileName);
			DocumentService service = new DocumentService();
			UploadDocumentRq rq = new UploadDocumentRq();
			rq.setDocument(document);
			UploadDocumentRs rs = service.getDocumentSOAP().uploadDocument(rq);
			if (Integer.valueOf(rs.getResponse().getRespCode()) != 0) {
				FacesMessage exceptionMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Unable to upload attachment. Please update later", rs.getResponse().getRespDesc());
				FacesContext.getCurrentInstance().addMessage(null, exceptionMessage);
			}
		} catch (Exception e) {
			FacesMessage exceptionMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Unable to upload attachment. Please update later", e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, exceptionMessage);
		}
		return "";
	}

	private Long[] toIdArray(List<Long> ids) {
		Long[] ret = new Long[ids.size()];
		for (int i = 0; i < ids.size(); i++) {
			Object obj = ids.get(i);
			ret[i] = new Long(obj.toString());
		}
		return ret;
	}

	private List<UserBean> fetchAllUsers() {
		List<UserBean> ret = new ArrayList<UserBean>();
		WebClient viewUsersClient = createCustomClient("http://127.0.0.1:38080/ip-ws/ip/as/user/list");
		Collection<? extends UserMessage> users = new ArrayList<UserMessage>(viewUsersClient.accept(MediaType.APPLICATION_JSON).getCollection(UserMessage.class));
		for (UserMessage userMessage : users) {
			UserBean bean = new UserBean();
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
		WebClient viewGroupsClient = createCustomClient("http://127.0.0.1:38080/ip-ws/ip/as/group/list");
		Collection<? extends GroupMessage> groups = new ArrayList<GroupMessage>(viewGroupsClient.accept(MediaType.APPLICATION_JSON).getCollection(GroupMessage.class));
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
		WebClient viewFunctionsClient = createCustomClient("http://127.0.0.1:38080/ip-ws/ip/as/func/list");
		Collection<? extends FunctionMessage> functions = new ArrayList<FunctionMessage>(viewFunctionsClient.accept(MediaType.APPLICATION_JSON).getCollection(FunctionMessage.class));
		for (FunctionMessage functionMessage : functions) {
			FunctionBean bean = new FunctionBean();
			bean.setFuncId(functionMessage.getFuncId());
			bean.setFuncName(functionMessage.getFuncName());
			bean.getGroupIdList().clear();
			for (Long id : functionMessage.getGroupIdList())
				if (id != null)
					bean.getGroupIdList().add(id);
			bean.getUserIdList().clear();
			for (Long id : functionMessage.getUserIdList())
				if (id != null)
					bean.getUserIdList().add(id);
			ret.add(bean);
		}
		return ret;
	}

	public GroupBean getParentGroup(Long pGrpId) {
		GroupBean bean = new GroupBean();
		WebClient groupByIdClient = createCustomClient("http://127.0.0.1:38080/ip-ws/ip/as/group/get/" + pGrpId);
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
		WebClient userByIdClient = createCustomClient("http://127.0.0.1:38080/ip-ws/ip/as/user/get/" + id);
		UserMessage userMessage = userByIdClient.accept(MediaType.APPLICATION_JSON).get(UserMessage.class);
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

	public void fileUploadHandle(FileUploadEvent e) {
		try {
			UploadedFile file = e.getFile();
			this.image = new DefaultStreamedContent(file.getInputstream());
			this.fileName = file.getFileName();
			this.contentType = file.getContentType();
		} catch (Exception ex) {
			FacesMessage exceptionMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), ex.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, exceptionMessage);
		}
	}

	public void fileImageUploadHandle(FileUploadEvent e) {
		try {
			UploadedFile file = e.getFile();
			this.image = new DefaultStreamedContent(file.getInputstream());
			this.fileName = file.getFileName();
			this.contentType = file.getContentType();
			enableUpload = true;
		} catch (Exception ex) {
			FacesMessage exceptionMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), ex.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, exceptionMessage);
			enableUpload = false;
		}
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

	public String getSecA() {
		return secA;
	}

	public void setSecA(String secA) {
		this.secA = secA;
	}

	public StreamedContent getImage() {
		return image;
	}

	public void setImage(StreamedContent image) {
		this.image = image;
	}

	public String getUploadSrc() {
		return uploadSrc;
	}

	public void setUploadSrc(String uploadSrc) {
		this.uploadSrc = uploadSrc;
	}

	public boolean isShow() {
		return show;
	}

	public void setShow(boolean show) {
		this.show = show;
	}

	public boolean isShowDef() {
		return showDef;
	}

	public void setShowDef(boolean showDef) {
		this.showDef = showDef;
	}

	public boolean isEnableUpload() {
		return enableUpload;
	}

	public void setEnableUpload(boolean enableUpload) {
		this.enableUpload = enableUpload;
	}

	public String getSecQ() {
		return secQ;
	}

	public void setSecQ(String secQ) {
		this.secQ = secQ;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

}
