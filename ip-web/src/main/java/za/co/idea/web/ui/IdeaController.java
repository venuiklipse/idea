package za.co.idea.web.ui;

import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.ws.rs.core.MediaType;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.jaxrs.client.WebClient;
import org.codehaus.jackson.jaxrs.JacksonJaxbJsonProvider;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import org.primefaces.model.tagcloud.DefaultTagCloudItem;
import org.primefaces.model.tagcloud.DefaultTagCloudModel;
import org.primefaces.model.tagcloud.TagCloudModel;

import za.co.idea.ip.ws.bean.IdeaMessage;
import za.co.idea.ip.ws.bean.MetaDataMessage;
import za.co.idea.ip.ws.bean.ResponseMessage;
import za.co.idea.ip.ws.bean.TagMessage;
import za.co.idea.ip.ws.bean.UserMessage;
import za.co.idea.ip.ws.util.CustomObjectMapper;
import za.co.idea.web.ui.bean.IdeaBean;
import za.co.idea.web.ui.bean.ListSelectorBean;
import za.co.idea.web.ui.bean.TagBean;
import za.co.idea.web.ui.bean.UserBean;
import za.co.idea.web.util.IdNumberGen;

public class IdeaController implements Serializable {

	private static final long serialVersionUID = -2647562847121760969L;
	private static final Log LOG = LogFactory.getLog(IdeaController.class);
	private IdeaBean ideaBean;
	private List<UserBean> admUsers;
	private List<ListSelectorBean> ideaCats;
	private List<ListSelectorBean> ideaStatuses;
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
	private static final IdNumberGen COUNTER = new IdNumberGen();

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private WebClient createCustomClient(String url) {
		List providers = new ArrayList();
		providers.add(new JacksonJaxbJsonProvider(new CustomObjectMapper(), JacksonJaxbJsonProvider.DEFAULT_ANNOTATIONS));
		WebClient client = WebClient.create(url, providers);
		client.header("Content-Type", "application/json");
		client.header("Accept", "application/json");
		return client;
	}

	public String showViewIdeas() {
		try {
			viewIdeas = fetchAllIdeas();
			ideaCats = fetchAllIdeaCat();
			admUsers = fetchAllUsers();
			ideaStatuses = fetchAllIdeaStatuses();
			return "ideavi";
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e, e);
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
			e.printStackTrace();
			LOG.error(e, e);
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
		WebClient addTagClient = createCustomClient("http://127.0.0.1:38080/ip-ws/ip/ts/tag/add");
		TagMessage message = new TagMessage();
		message.setEntityId(ideaBean.getIdeaId());
		message.setTagId(COUNTER.getNextId("ip_tag"));
		message.setTeId(1);
		message.setTtId(1);
		message.setUserId((Long) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userId"));
		message.setDuplicate(false);
		ResponseMessage response = addTagClient.accept(MediaType.APPLICATION_JSON).post(message, ResponseMessage.class);
		if (response.getStatusCode() != 0 && response.getStatusCode() != 2)
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
		WebClient addTagClient = createCustomClient("http://127.0.0.1:38080/ip-ws/ip/ts/tag/add");
		TagMessage message = new TagMessage();
		message.setEntityId(ideaBean.getIdeaId());
		message.setTagId(COUNTER.getNextId("ip_tag"));
		message.setTagText(commentText);
		message.setTeId(1);
		message.setTtId(2);
		message.setDuplicate(true);
		message.setUserId((Long) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userId"));
		ResponseMessage response = addTagClient.accept(MediaType.APPLICATION_JSON).post(message, ResponseMessage.class);
		if (response.getStatusCode() != 0)
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
		WebClient addTagClient = createCustomClient("http://127.0.0.1:38080/ip-ws/ip/ts/tag/add");
		TagMessage message = new TagMessage();
		message.setEntityId(ideaBean.getIdeaId());
		message.setTagId(COUNTER.getNextId("ip_tag"));
		message.setTagText(buildOnText);
		message.setTeId(1);
		message.setTtId(3);
		message.setDuplicate(true);
		message.setUserId((Long) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userId"));
		ResponseMessage response = addTagClient.accept(MediaType.APPLICATION_JSON).post(message, ResponseMessage.class);
		if (response.getStatusCode() != 0)
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
			e.printStackTrace();
			LOG.error(e, e);
			FacesMessage exceptionMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, exceptionMessage);
			return "";
		}
	}

	public String saveIdea() {
		try {
			WebClient addIdeaClient = createCustomClient("http://127.0.0.1:38080/ip-ws/ip/is/idea/add");
			IdeaMessage ideaMessage = new IdeaMessage();
			ideaMessage.setContentType(ideaBean.getContentType());
			ideaMessage.setCrtdById((Long) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userId"));
			ideaMessage.setCrtdDate(new Date());
			if (ideaBean.getFileUpload() != null && ideaBean.getFileUpload().length() > 0)
				ideaMessage.setFileUpload(new String(Base64.encodeBase64URLSafe(ideaBean.getFileUpload().getBytes())));
			else
				ideaMessage.setFileUpload(null);
			ideaMessage.setIdeaBa(ideaBean.getIdeaBa());
			ideaMessage.setIdeaDesc(ideaBean.getIdeaDesc());
			ideaMessage.setIdeaTag(ideaBean.getIdeaTag());
			ideaMessage.setIdeaId(COUNTER.getNextId("ip_idea"));
			ideaMessage.setIdeaTitle(ideaBean.getIdeaTitle());
			ideaMessage.setSelCatId(ideaBean.getSelCatId());
			ideaMessage.setFileName(ideaBean.getFileName());
			ideaMessage.setSetStatusId(1l);
			ResponseMessage response = addIdeaClient.accept(MediaType.APPLICATION_JSON).post(ideaMessage, ResponseMessage.class);
			if (response.getStatusCode() == 0) {
				addIdeaClient.close();
				return "home";
			} else {
				addIdeaClient.close();
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

	public String updateIdea() {
		try {
			WebClient updateIdeaClient = createCustomClient("http://127.0.0.1:38080/ip-ws/ip/is/idea/modify");
			IdeaMessage ideaMessage = new IdeaMessage();
			ideaMessage.setContentType(ideaBean.getContentType());
			ideaMessage.setCrtdById(ideaBean.getCrtdById());
			ideaMessage.setCrtdDate(new Date());
			if (ideaBean.getFileUpload() != null && ideaBean.getFileUpload().length() > 0)
				ideaMessage.setFileUpload(new String(Base64.encodeBase64URLSafe(ideaBean.getFileUpload().getBytes())));
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
			ResponseMessage response = updateIdeaClient.accept(MediaType.APPLICATION_JSON).put(ideaMessage, ResponseMessage.class);
			if (response.getStatusCode() == 0) {
				updateIdeaClient.close();
				return "home";
			} else {
				updateIdeaClient.close();
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

	private TagCloudModel fetchAllLikes() {
		TagCloudModel likes = new DefaultTagCloudModel();
		WebClient fetchIdeaLikesClient = createCustomClient("http://127.0.0.1:38080/ip-ws/ip/ts/tag/get/" + ideaBean.getIdeaId() + "/1/1");
		Collection<? extends TagMessage> likeList = new ArrayList<TagMessage>(fetchIdeaLikesClient.accept(MediaType.APPLICATION_JSON).getCollection(TagMessage.class));
		for (TagMessage tagMessage : likeList)
			likes.addTag(new DefaultTagCloudItem(tagMessage.getUsrScreenName(), 1));
		fetchIdeaLikesClient.close();
		return likes;
	}

	private List<TagBean> fetchAllComments() {
		WebClient fetchIdeaCommentsClient = createCustomClient("http://127.0.0.1:38080/ip-ws/ip/ts/tag/get/" + ideaBean.getIdeaId() + "/1/2");
		Collection<? extends TagMessage> msgs = new ArrayList<TagMessage>(fetchIdeaCommentsClient.accept(MediaType.APPLICATION_JSON).getCollection(TagMessage.class));
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
		WebClient fetchIdeaBuildOnsClient = createCustomClient("http://127.0.0.1:38080/ip-ws/ip/ts/tag/get/" + ideaBean.getIdeaId() + "/1/3");
		Collection<? extends TagMessage> msgs = new ArrayList<TagMessage>(fetchIdeaBuildOnsClient.accept(MediaType.APPLICATION_JSON).getCollection(TagMessage.class));
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
		WebClient fetchIdeaClient = createCustomClient("http://127.0.0.1:38080/ip-ws/ip/is/idea/list");
		Collection<? extends IdeaMessage> ideas = new ArrayList<IdeaMessage>(fetchIdeaClient.accept(MediaType.APPLICATION_JSON).getCollection(IdeaMessage.class));
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
		WebClient viewIdeaSelectClient = createCustomClient("http://127.0.0.1:38080/ip-ws/ip/is/idea/status/list");
		Collection<? extends MetaDataMessage> md = new ArrayList<MetaDataMessage>(viewIdeaSelectClient.accept(MediaType.APPLICATION_JSON).getCollection(MetaDataMessage.class));
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
		WebClient viewIdeaSelectClient = createCustomClient("http://127.0.0.1:38080/ip-ws/ip/is/idea/cat/list");
		Collection<? extends MetaDataMessage> md = new ArrayList<MetaDataMessage>(viewIdeaSelectClient.accept(MediaType.APPLICATION_JSON).getCollection(MetaDataMessage.class));
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
			LOG.error(ex, ex);
			FacesMessage exceptionMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), ex.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, exceptionMessage);
		}
	}

	private List<UserBean> fetchAllUsers() {
		List<UserBean> ret = new ArrayList<UserBean>();
		WebClient viewUsersClient = createCustomClient("http://127.0.0.1:38080/ip-ws/ip/as/user/list");
		Collection<? extends UserMessage> users = new ArrayList<UserMessage>(viewUsersClient.accept(MediaType.APPLICATION_JSON).getCollection(UserMessage.class));
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
