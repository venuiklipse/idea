package za.co.idea.web.ui;

import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.codec.binary.Base64;
import org.apache.cxf.jaxrs.client.WebClient;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.codehaus.jackson.map.ObjectMapper;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import org.primefaces.model.tagcloud.DefaultTagCloudItem;
import org.primefaces.model.tagcloud.DefaultTagCloudModel;
import org.primefaces.model.tagcloud.TagCloudModel;

import za.co.idea.ip.ws.bean.IdeaMessage;
import za.co.idea.ip.ws.bean.MetaDataMessage;
import za.co.idea.ip.ws.bean.TagMessage;
import za.co.idea.ip.ws.bean.UserMessage;
import za.co.idea.web.ui.bean.IdeaBean;
import za.co.idea.web.ui.bean.ListSelectorBean;
import za.co.idea.web.ui.bean.TagBean;
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
	private TagCloudModel likes;
	private List<TagBean> comments;
	private List<TagBean> buildOns;
	private StreamedContent fileContent;
	private String commentText;
	private String buildOnText;
	private boolean fileAvail;
	private boolean showIdeaComments;
	private boolean showIdeaLikes;
	private boolean showIdeaBuildOns;
	private String likeCnt;
	private String commentCnt;
	private String buildOnCnt;

	public String showViewIdeas() {
		try {
			viewIdeas = fetchAllIdeas();
			ideaCats = fetchAllIdeaCat();
			admUsers = fetchAllUsers();
			ideaStatuses = fetchAllIdeaStatuses();
			return "ideavi";
		} catch (Exception e) {
			FacesMessage exceptionMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, exceptionMessage);
			return "";
		}
	}

	public String showEditIdea() {
		try {
			ideaCats = fetchAllIdeaCat();
			admUsers = fetchAllUsers();
			ideaStatuses = fetchAllIdeaStatuses();
			if (ideaBean.getFileUpload() != null && ideaBean.getFileUpload().length() > 0) {
				fileAvail = false;
				fileContent = new DefaultStreamedContent(new ByteArrayInputStream(ideaBean.getFileUpload().getBytes()), ideaBean.getContentType(), ideaBean.getFileName());
			} else {
				fileAvail = true;
				fileContent = null;
			}
			return "ideaei";
		} catch (Exception e) {
			FacesMessage exceptionMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, exceptionMessage);
			return "";
		}
	}

	public String showSummaryIdea() {
		likes = fetchAllLikes();
		comments = fetchAllComments();
		buildOns = fetchAllBuildOns();
		likeCnt = "(" + likes.getTags().size() + ")";
		commentCnt = "(" + comments.size() + ")";
		buildOnCnt = "(" + buildOns.size() + ")";
		showIdeaComments = false;
		showIdeaLikes = false;
		showIdeaBuildOns = false;
		return "ideasi";
	}

	public String showCommentIdea() {
		comments = fetchAllComments();
		showIdeaComments = true;
		showIdeaLikes = false;
		showIdeaBuildOns = false;
		return "";
	}

	public String showBuildOnIdea() {
		buildOns = fetchAllBuildOns();
		showIdeaComments = false;
		showIdeaLikes = false;
		showIdeaBuildOns = true;
		return "";
	}

	public String likeIdea() {
		List providers = new ArrayList();
		providers.add(new JacksonJsonProvider(new ObjectMapper()));
		WebClient addTagClient = WebClient.create("http://127.0.0.1:8080/ip-ws/ip/ts/tag/add", providers);
		addTagClient.header("Content-Type", "application/json");
		addTagClient.header("Accept", "application/json");
		TagMessage message = new TagMessage();
		message.setEntityId(ideaBean.getIdeaId());
		message.setTagId(System.currentTimeMillis());
		message.setTeId(1);
		message.setTtId(1);
		message.setUserId((Long) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userId"));
		message.setDuplicate(false);
		Response response = addTagClient.accept(MediaType.APPLICATION_JSON).post(message);
		if (response.getStatus() != Response.Status.OK.getStatusCode() && response.getStatus() != Response.Status.EXPECTATION_FAILED.getStatusCode())
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error While Saving Like", "Error While Saving Like"));
		likes = fetchAllLikes();
		likeCnt = "(" + likes.getTags().size() + ")";
		showIdeaComments = false;
		showIdeaLikes = true;
		showIdeaBuildOns = false;
		addTagClient.close();
		return "";
	}

	public String commentIdea() {
		List providers = new ArrayList();
		providers.add(new JacksonJsonProvider(new ObjectMapper()));
		WebClient addTagClient = WebClient.create("http://127.0.0.1:8080/ip-ws/ip/ts/tag/add", providers);
		addTagClient.header("Content-Type", "application/json");
		addTagClient.header("Accept", "application/json");
		TagMessage message = new TagMessage();
		message.setEntityId(ideaBean.getIdeaId());
		message.setTagId(System.currentTimeMillis());
		message.setTagText(commentText);
		message.setTeId(1);
		message.setTtId(2);
		message.setDuplicate(true);
		message.setUserId((Long) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userId"));
		Response response = addTagClient.accept(MediaType.APPLICATION_JSON).post(message);
		if (response.getStatus() != Response.Status.OK.getStatusCode())
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error While Saving Comment", "Error While Saving Comment"));
		comments = fetchAllComments();
		commentCnt = "(" + comments.size() + ")";
		commentText = "";
		showIdeaComments = true;
		showIdeaLikes = false;
		showIdeaBuildOns = false;
		addTagClient.close();
		return "";
	}

	public String buildOnIdea() {
		List providers = new ArrayList();
		providers.add(new JacksonJsonProvider(new ObjectMapper()));
		WebClient addTagClient = WebClient.create("http://127.0.0.1:8080/ip-ws/ip/ts/tag/add", providers);
		addTagClient.header("Content-Type", "application/json");
		addTagClient.header("Accept", "application/json");
		TagMessage message = new TagMessage();
		message.setEntityId(ideaBean.getIdeaId());
		message.setTagId(System.currentTimeMillis());
		message.setTagText(buildOnText);
		message.setTeId(1);
		message.setTtId(3);
		message.setDuplicate(true);
		message.setUserId((Long) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userId"));
		Response response = addTagClient.accept(MediaType.APPLICATION_JSON).post(message);
		if (response.getStatus() != Response.Status.OK.getStatusCode())
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error While Saving Build-On", "Error While Saving Build-On"));
		buildOns = fetchAllBuildOns();
		buildOnCnt = "(" + buildOns.size() + ")";
		buildOnText = "";
		showIdeaComments = false;
		showIdeaLikes = false;
		showIdeaBuildOns = true;
		addTagClient.close();
		return "";
	}

	public String showCreateIdea() {
		try {
			ideaCats = fetchAllIdeaCat();
			admUsers = fetchAllUsers();
			ideaStatuses = fetchAllIdeaStatuses();
			ideaBean = new IdeaBean();
			return "ideaci";
		} catch (Exception e) {
			FacesMessage exceptionMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, exceptionMessage);
			return "";
		}
	}

	public String saveIdea() {
		try {
			List providers = new ArrayList();
			providers.add(new JacksonJsonProvider(new ObjectMapper()));
			WebClient addIdeaClient = WebClient.create("http://127.0.0.1:8080/ip-ws/ip/is/idea/add", providers);
			addIdeaClient.header("Content-Type", "application/json");
			addIdeaClient.header("Accept", "application/json");
			IdeaMessage ideaMessage = new IdeaMessage();
			ideaMessage.setContentType(ideaBean.getContentType());
			ideaMessage.setCrtdById((Long) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userId"));
			ideaMessage.setCrtdDate(new Date());
			if (ideaBean.getFileUpload() != null && ideaBean.getFileUpload().length() > 0)
				ideaMessage.setFileUpload(Base64.encodeBase64String(ideaBean.getFileUpload().getBytes()));
			else
				ideaMessage.setFileUpload(null);
			ideaMessage.setIdeaBa(ideaBean.getIdeaBa());
			ideaMessage.setIdeaDesc(ideaBean.getIdeaDesc());
			ideaMessage.setIdeaTag(ideaBean.getIdeaTag());
			ideaMessage.setIdeaId(System.currentTimeMillis());
			ideaMessage.setIdeaTitle(ideaBean.getIdeaTitle());
			ideaMessage.setSelCatId(ideaBean.getSelCatId());
			ideaMessage.setFileName(ideaBean.getFileName());
			ideaMessage.setSetStatusId(1l);
			Response response = addIdeaClient.accept(MediaType.APPLICATION_JSON).post(ideaMessage);
			if (response.getStatus() == Response.Status.OK.getStatusCode()) {
				addIdeaClient.close();
				return "home";
			} else {
				addIdeaClient.close();
				return "";
			}
		} catch (Exception e) {
			FacesMessage exceptionMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, exceptionMessage);
			return "";
		}
	}

	public String updateIdea() {
		try {
			List providers = new ArrayList();
			providers.add(new JacksonJsonProvider(new ObjectMapper()));
			WebClient updateIdeaClient = WebClient.create("http://127.0.0.1:8080/ip-ws/ip/is/idea/modify", providers);
			updateIdeaClient.header("Content-Type", "application/json");
			updateIdeaClient.header("Accept", "application/json");
			IdeaMessage ideaMessage = new IdeaMessage();
			ideaMessage.setContentType(ideaBean.getContentType());
			ideaMessage.setCrtdById(ideaBean.getCrtdById());
			ideaMessage.setCrtdDate(new Date());
			if (ideaBean.getFileUpload() != null && ideaBean.getFileUpload().length() > 0)
				ideaMessage.setFileUpload(Base64.encodeBase64String(ideaBean.getFileUpload().getBytes()));
			else
				ideaMessage.setFileUpload(null);
			ideaMessage.setIdeaBa(ideaBean.getIdeaBa());
			ideaMessage.setIdeaDesc(ideaBean.getIdeaDesc());
			ideaMessage.setIdeaTag(ideaBean.getIdeaTag());
			ideaMessage.setIdeaId(ideaBean.getIdeaId());
			ideaMessage.setIdeaTitle(ideaBean.getIdeaTitle());
			ideaMessage.setSelCatId(ideaBean.getSelCatId());
			ideaMessage.setSetStatusId(ideaBean.getSetStatusId());
			ideaMessage.setFileName(ideaBean.getFileName());
			Response response = updateIdeaClient.accept(MediaType.APPLICATION_JSON).put(ideaMessage);
			if (response.getStatus() == Response.Status.OK.getStatusCode()) {
				updateIdeaClient.close();
				return "home";
			} else {
				updateIdeaClient.close();
				return "";
			}
		} catch (Exception e) {
			FacesMessage exceptionMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, exceptionMessage);
			return "";
		}
	}

	private TagCloudModel fetchAllLikes() {
		TagCloudModel likes = new DefaultTagCloudModel();
		List providers = new ArrayList();
		providers.add(new JacksonJsonProvider(new ObjectMapper()));
		WebClient fetchIdeaLikesClient = WebClient.create("http://127.0.0.1:8080/ip-ws/ip/ts/tag/get/" + ideaBean.getIdeaId() + "/1/1", providers);
		fetchIdeaLikesClient.header("Content-Type", "application/json");
		fetchIdeaLikesClient.header("Accept", "application/json");
		List<TagMessage> likeList = new ArrayList<TagMessage>(fetchIdeaLikesClient.accept(MediaType.APPLICATION_JSON).getCollection(TagMessage.class));
		for (TagMessage tagMessage : likeList)
			likes.addTag(new DefaultTagCloudItem(tagMessage.getUsrScreenName(), 1));
		fetchIdeaLikesClient.close();
		return likes;
	}

	private List<TagBean> fetchAllComments() {
		List providers = new ArrayList();
		providers.add(new JacksonJsonProvider(new ObjectMapper()));
		WebClient fetchIdeaCommentsClient = WebClient.create("http://127.0.0.1:8080/ip-ws/ip/ts/tag/get/" + ideaBean.getIdeaId() + "/1/2", providers);
		fetchIdeaCommentsClient.header("Content-Type", "application/json");
		fetchIdeaCommentsClient.header("Accept", "application/json");
		ArrayList<TagMessage> msgs = new ArrayList<TagMessage>(fetchIdeaCommentsClient.accept(MediaType.APPLICATION_JSON).getCollection(TagMessage.class));
		fetchIdeaCommentsClient.close();
		List<TagBean> ret = new ArrayList<TagBean>();
		for (TagMessage msg : msgs) {
			TagBean bean = new TagBean();
			bean.setTagText(msg.getTagText());
			bean.setUsrScreenName(msg.getUsrScreenName());
			ret.add(bean);
		}
		return ret;
	}

	private List<TagBean> fetchAllBuildOns() {
		List providers = new ArrayList();
		providers.add(new JacksonJsonProvider(new ObjectMapper()));
		WebClient fetchIdeaBuildOnsClient = WebClient.create("http://127.0.0.1:8080/ip-ws/ip/ts/tag/get/" + ideaBean.getIdeaId() + "/1/3", providers);
		fetchIdeaBuildOnsClient.header("Content-Type", "application/json");
		fetchIdeaBuildOnsClient.header("Accept", "application/json");
		ArrayList<TagMessage> msgs = new ArrayList<TagMessage>(fetchIdeaBuildOnsClient.accept(MediaType.APPLICATION_JSON).getCollection(TagMessage.class));
		fetchIdeaBuildOnsClient.close();
		List<TagBean> ret = new ArrayList<TagBean>();
		for (TagMessage msg : msgs) {
			TagBean bean = new TagBean();
			bean.setTagText(msg.getTagText());
			bean.setUsrScreenName(msg.getUsrScreenName());
			ret.add(bean);
		}
		return ret;
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
			if (ideaMessage.getFileUpload() != null && ideaMessage.getFileUpload().length() > 0)
				bean.setFileUpload(new String(Base64.decodeBase64(ideaMessage.getFileUpload().getBytes())));
			else
				bean.setFileUpload(null);
			bean.setIdeaBa(ideaMessage.getIdeaBa());
			bean.setIdeaDesc(ideaMessage.getIdeaDesc());
			bean.setIdeaTag(ideaMessage.getIdeaTag());
			bean.setIdeaId(ideaMessage.getIdeaId());
			bean.setIdeaTitle(ideaMessage.getIdeaTitle());
			bean.setSelCatId(ideaMessage.getSelCatId());
			bean.setSetStatusId(ideaMessage.getSetStatusId());
			bean.setFileName(ideaMessage.getFileName());
			ret.add(bean);
		}
		fetchIdeaClient.close();
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
		viewIdeaSelectClient.close();
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
		viewIdeaSelectClient.close();
		return ret;
	}

	public void fileUploadHandle(FileUploadEvent e) {
		try {
			UploadedFile file = e.getFile();
			this.ideaBean.setFileUpload(new String(file.getContents()));
			this.ideaBean.setContentType(file.getContentType());
			this.ideaBean.setFileName(file.getFileName());
		} catch (Exception ex) {
			FacesMessage exceptionMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), ex.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, exceptionMessage);
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
		viewUsersClient.close();
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

	public TagCloudModel getLikes() {
		return likes;
	}

	public void setLikes(TagCloudModel likes) {
		this.likes = likes;
	}

	public List<TagBean> getComments() {
		if (comments == null)
			comments = new ArrayList<TagBean>();
		return comments;
	}

	public void setComments(List<TagBean> comments) {
		this.comments = comments;
	}

	public List<TagBean> getBuildOns() {
		if (buildOns == null)
			buildOns = new ArrayList<TagBean>();
		return buildOns;
	}

	public void setBuildOns(List<TagBean> buildOns) {
		this.buildOns = buildOns;
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

	public StreamedContent getFileContent() {
		return fileContent;
	}

	public void setFileContent(StreamedContent fileContent) {
		this.fileContent = fileContent;
	}

	public boolean isFileAvail() {
		return fileAvail;
	}

	public void setFileAvail(boolean fileAvail) {
		this.fileAvail = fileAvail;
	}

	public boolean isShowIdeaComments() {
		return showIdeaComments;
	}

	public void setShowIdeaComments(boolean showIdeaComments) {
		this.showIdeaComments = showIdeaComments;
	}

	public boolean isShowIdeaLikes() {
		return showIdeaLikes;
	}

	public void setShowIdeaLikes(boolean showIdeaLikes) {
		this.showIdeaLikes = showIdeaLikes;
	}

	public boolean isShowIdeaBuildOns() {
		return showIdeaBuildOns;
	}

	public void setShowIdeaBuildOns(boolean showIdeaBuildOns) {
		this.showIdeaBuildOns = showIdeaBuildOns;
	}

	public String getCommentText() {
		return commentText;
	}

	public void setCommentText(String commentText) {
		this.commentText = commentText;
	}

	public String getBuildOnText() {
		return buildOnText;
	}

	public void setBuildOnText(String buildOnText) {
		this.buildOnText = buildOnText;
	}

	public String getLikeCnt() {
		return likeCnt;
	}

	public void setLikeCnt(String likeCnt) {
		this.likeCnt = likeCnt;
	}

	public String getCommentCnt() {
		return commentCnt;
	}

	public void setCommentCnt(String commentCnt) {
		this.commentCnt = commentCnt;
	}

	public String getBuildOnCnt() {
		return buildOnCnt;
	}

	public void setBuildOnCnt(String buildOnCnt) {
		this.buildOnCnt = buildOnCnt;
	}

}
