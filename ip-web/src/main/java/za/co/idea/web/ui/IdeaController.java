package za.co.idea.web.ui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.client.WebClient;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.codehaus.jackson.map.ObjectMapper;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import za.co.idea.ip.ws.bean.IdeaMessage;
import za.co.idea.ip.ws.bean.MetaDataMessage;
import za.co.idea.ip.ws.bean.UserMessage;
import za.co.idea.web.ui.bean.IdeaBean;
import za.co.idea.web.ui.bean.ListSelectorBean;
import za.co.idea.web.ui.bean.UserBean;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class IdeaController implements Serializable {

	private static final long serialVersionUID = -2647562847121760969L;
	/**
	 * Create and Edit Beans
	 */
	private IdeaBean ideaBean;
	private List<UserBean> admUsers;
	private List<ListSelectorBean> ideaCats;
	private List<ListSelectorBean> ideaStatuses;
	/**
	 * View Beans
	 */
	private List<IdeaBean> viewIdeas;

	public String showViewIdeas() {
		viewIdeas = fetchAllIdeas();
		ideaCats = fetchAllIdeaCat();
		admUsers = fetchAllUsers();
		ideaStatuses = fetchAllIdeaStatuses();
		return "ideavi";
	}

	public String showEditIdea() {
		ideaCats = fetchAllIdeaCat();
		admUsers = fetchAllUsers();
		ideaStatuses = fetchAllIdeaStatuses();
		return "ideaei";
	}

	public String showCreateIdea() {
		ideaCats = fetchAllIdeaCat();
		admUsers = fetchAllUsers();
		ideaStatuses = fetchAllIdeaStatuses();
		ideaBean = new IdeaBean();
		return "ideaci";
	}

	public String saveIdea() {
		List providers = new ArrayList();
		providers.add(new JacksonJsonProvider(new ObjectMapper()));
		WebClient addIdeaClient = WebClient.create("http://127.0.0.1:8080/ip-ws/ip/is/idea/add", providers);
		addIdeaClient.header("Content-Type", "application/json");
		addIdeaClient.header("Accept", "application/json");
		IdeaMessage ideaMessage = new IdeaMessage();
		ideaMessage.setContentType(ideaBean.getContentType());
		ideaMessage.setCrtdById(ideaBean.getCrtdById());
		ideaMessage.setCrtdDate(new Date());
		ideaMessage.setFileUpload(ideaBean.getFileUpload());
		ideaMessage.setIdeaBa(ideaBean.getIdeaBa());
		ideaMessage.setIdeaDesc(ideaBean.getIdeaDesc());
		ideaMessage.setIdeaTag(ideaBean.getIdeaTag());
		ideaMessage.setIdeaId(System.currentTimeMillis());
		ideaMessage.setIdeaTitle(ideaBean.getIdeaTitle());
		ideaMessage.setSelCatId(ideaBean.getSelCatId());
		ideaMessage.setSetStatusId(1l);
		Response response = addIdeaClient.accept(MediaType.APPLICATION_JSON).post(ideaMessage);
		if (response.getStatus() == Response.Status.OK.getStatusCode())
			return "home";
		else {
			return "";
		}
	}

	public String updateIdea() {
		List providers = new ArrayList();
		providers.add(new JacksonJsonProvider(new ObjectMapper()));
		WebClient updateIdeaClient = WebClient.create("http://127.0.0.1:8080/ip-ws/ip/is/idea/modify", providers);
		updateIdeaClient.header("Content-Type", "application/json");
		updateIdeaClient.header("Accept", "application/json");
		IdeaMessage ideaMessage = new IdeaMessage();
		ideaMessage.setContentType(ideaBean.getContentType());
		ideaMessage.setCrtdById(ideaBean.getCrtdById());
		ideaMessage.setCrtdDate(new Date());
		ideaMessage.setFileUpload(ideaBean.getFileUpload());
		ideaMessage.setIdeaBa(ideaBean.getIdeaBa());
		ideaMessage.setIdeaDesc(ideaBean.getIdeaDesc());
		ideaMessage.setIdeaTag(ideaBean.getIdeaTag());
		ideaMessage.setIdeaId(ideaBean.getIdeaId());
		ideaMessage.setIdeaTitle(ideaBean.getIdeaTitle());
		ideaMessage.setSelCatId(ideaBean.getSelCatId());
		ideaMessage.setSetStatusId(ideaBean.getSetStatusId());
		Response response = updateIdeaClient.accept(MediaType.APPLICATION_JSON).put(ideaMessage);
		if (response.getStatus() == Response.Status.OK.getStatusCode())
			return "home";
		else {
			return "";
		}
	}

	private List<IdeaBean> fetchAllIdeas() {
		List<IdeaBean> ret = new ArrayList<IdeaBean>();
		List providers = new ArrayList();
		providers.add(new JacksonJsonProvider(new ObjectMapper()));
		WebClient fetchIdeaClient = WebClient.create("http://127.0.0.1:8080/ip-ws/ip/is/idea/list", providers);
		fetchIdeaClient.header("Content-Type", "application/json");
		fetchIdeaClient.header("Accept", "application/json");
		List<IdeaMessage> ideas = new ArrayList<IdeaMessage>(fetchIdeaClient.accept(MediaType.APPLICATION_JSON).getCollection(IdeaMessage.class));
		for (IdeaMessage ideaMessage : ideas) {
			IdeaBean bean = new IdeaBean();
			bean.setContentType(ideaMessage.getContentType());
			bean.setCrtdById(ideaMessage.getCrtdById());
			bean.setCrtdDate(ideaMessage.getCrtdDate());
			bean.setFileUpload(ideaMessage.getFileUpload());
			bean.setIdeaBa(ideaMessage.getIdeaBa());
			bean.setIdeaDesc(ideaMessage.getIdeaDesc());
			bean.setIdeaTag(ideaMessage.getIdeaTag());
			bean.setIdeaId(ideaMessage.getIdeaId());
			bean.setIdeaTitle(ideaMessage.getIdeaTitle());
			bean.setSelCatId(ideaMessage.getSelCatId());
			bean.setSetStatusId(ideaMessage.getSetStatusId());
			ret.add(bean);
		}
		return ret;
	}

	private List<ListSelectorBean> fetchAllIdeaStatuses() {
		List<ListSelectorBean> ret = new ArrayList<ListSelectorBean>();
		List providers = new ArrayList();
		providers.add(new JacksonJsonProvider(new ObjectMapper()));
		WebClient viewIdeaSelectClient = WebClient.create("http://127.0.0.1:8080/ip-ws/ip/is/idea/status/list", providers);
		viewIdeaSelectClient.header("Content-Type", "application/json");
		viewIdeaSelectClient.header("Accept", "application/json");
		List<MetaDataMessage> md = new ArrayList<MetaDataMessage>(viewIdeaSelectClient.accept(MediaType.APPLICATION_JSON).getCollection(MetaDataMessage.class));
		for (MetaDataMessage metaDataMessage : md) {
			ListSelectorBean bean = new ListSelectorBean();
			bean.setId(metaDataMessage.getId());
			bean.setDesc(metaDataMessage.getDesc());
			ret.add(bean);
		}
		return ret;
	}

	private List<ListSelectorBean> fetchAllIdeaCat() {
		List<ListSelectorBean> ret = new ArrayList<ListSelectorBean>();
		List providers = new ArrayList();
		providers.add(new JacksonJsonProvider(new ObjectMapper()));
		WebClient viewIdeaSelectClient = WebClient.create("http://127.0.0.1:8080/ip-ws/ip/is/idea/cat/list", providers);
		viewIdeaSelectClient.header("Content-Type", "application/json");
		viewIdeaSelectClient.header("Accept", "application/json");
		List<MetaDataMessage> md = new ArrayList<MetaDataMessage>(viewIdeaSelectClient.accept(MediaType.APPLICATION_JSON).getCollection(MetaDataMessage.class));
		for (MetaDataMessage metaDataMessage : md) {
			ListSelectorBean bean = new ListSelectorBean();
			bean.setId(metaDataMessage.getId());
			bean.setDesc(metaDataMessage.getDesc());
			ret.add(bean);
		}
		return ret;
	}

	public void fileUploadHandle(FileUploadEvent e) {
		UploadedFile file = e.getFile();
		this.ideaBean.setFileUpload(new String(file.getContents()));
		this.ideaBean.setContentType(file.getContentType());
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

	public IdeaBean getIdeaBean() {
		if (ideaBean == null)
			ideaBean = new IdeaBean();
		return ideaBean;
	}

	public void setIdeaBean(IdeaBean ideaBean) {
		this.ideaBean = ideaBean;
	}

	public List<IdeaBean> getViewIdeas() {
		if (viewIdeas == null)
			viewIdeas = new ArrayList<IdeaBean>();
		return viewIdeas;
	}

	public void setViewIdeas(List<IdeaBean> viewIdeas) {
		this.viewIdeas = viewIdeas;
	}

	public List<UserBean> getAdmUsers() {
		if (admUsers == null)
			admUsers = new ArrayList<UserBean>();
		return admUsers;
	}

	public void setAdmUsers(List<UserBean> admUsers) {
		this.admUsers = admUsers;
	}

	public List<ListSelectorBean> getIdeaCats() {
		if (ideaCats == null)
			ideaCats = new ArrayList<ListSelectorBean>();
		return ideaCats;
	}

	public void setIdeaCats(List<ListSelectorBean> ideaCats) {
		this.ideaCats = ideaCats;
	}

	public List<ListSelectorBean> getIdeaStatuses() {
		if (ideaStatuses == null)
			ideaStatuses = new ArrayList<ListSelectorBean>();
		return ideaStatuses;
	}

	public void setIdeaStatuses(List<ListSelectorBean> ideaStatuses) {
		this.ideaStatuses = ideaStatuses;
	}

}
