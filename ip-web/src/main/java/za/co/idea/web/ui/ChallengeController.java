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

import za.co.idea.ip.ws.bean.ChallengeMessage;
import za.co.idea.ip.ws.bean.MetaDataMessage;
import za.co.idea.ip.ws.bean.ResponseMessage;
import za.co.idea.ip.ws.bean.TagMessage;
import za.co.idea.ip.ws.bean.UserMessage;
import za.co.idea.ip.ws.util.CustomObjectMapper;
import za.co.idea.web.ui.bean.ChallengeBean;
import za.co.idea.web.ui.bean.ListSelectorBean;
import za.co.idea.web.ui.bean.TagBean;
import za.co.idea.web.ui.bean.UserBean;
import za.co.idea.web.util.IdNumberGen;

public class ChallengeController implements Serializable {

	private static final long serialVersionUID = -6939719926402016671L;
	private static final Log LOG = LogFactory.getLog(IdeaController.class);
	private ChallengeBean challengeBean;
	private List<ChallengeBean> viewChallenges;
	private List<UserBean> admUsers;
	private List<ListSelectorBean> challengeCats;
	private List<ListSelectorBean> challengeStatuses;
	private StreamedContent fileContent;
	private TagCloudModel likes;
	private List<TagBean> comments;
	private String commentText;
	private boolean fileAvail;
	private boolean showChallengeComments;
	private boolean showChallengeLikes;
	private String likeCnt;
	private String commentCnt;
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

	public String showCreateChallenge() {
		try {
			admUsers = fetchAllUsers();
			challengeCats = fetchAllChallengeCat();
			challengeStatuses = fetchAllChallengeStatuses();
			challengeBean = new ChallengeBean();
			return "chalcc";
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e, e);
			FacesMessage exceptionMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, exceptionMessage);
			return "";
		}
	}

	public String showViewChallenge() {
		try {
			viewChallenges = fetchAllChallenges();
			challengeCats = fetchAllChallengeCat();
			admUsers = fetchAllUsers();
			challengeStatuses = fetchAllChallengeStatuses();
			return "chalvc";
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e, e);
			FacesMessage exceptionMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, exceptionMessage);
			return "";
		}
	}

	public String showEditChallenge() {
		try {
			challengeCats = fetchAllChallengeCat();
			admUsers = fetchAllUsers();
			challengeStatuses = fetchAllChallengeStatuses();
			if (challengeBean.getBlob() != null && challengeBean.getBlob().length() > 0) {
				fileAvail = false;
				fileContent = new DefaultStreamedContent(new ByteArrayInputStream(challengeBean.getBlob().getBytes()), challengeBean.getContentType(), challengeBean.getFileName());
			} else {
				fileAvail = true;
				fileContent = null;
			}
			return "chalec";
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e, e);
			FacesMessage exceptionMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, exceptionMessage);
			return "";
		}
	}

	public String showSummaryChallenge() {
		likes = fetchAllLikes();
		comments = fetchAllComments();
		likeCnt = "(" + likes.getTags().size() + ")";
		commentCnt = "(" + comments.size() + ")";
		showChallengeComments = false;
		showChallengeLikes = false;
		return "chalsc";
	}

	public String showCreateSolution() {
		return "";
	}

	public String showViewSolution() {
		return "";
	}

	public String showEditSolution() {
		return "";
	}

	public String showSummarySolution() {
		return "";
	}

	public String saveChallenge() {
		try {
			WebClient addIdeaClient = createCustomClient("http://127.0.0.1:38080/ip-ws/ip/cs/challenge/add");
			ChallengeMessage message = new ChallengeMessage();
			message.setBlob(challengeBean.getBlob());
			message.setCatId(challengeBean.getCatId());
			message.setContentType(challengeBean.getContentType());
			message.setCrtdById((Long) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userId"));
			message.setCrtdDt(new Date());
			message.setDesc(challengeBean.getDesc());
			message.setExprDt(challengeBean.getExprDt());
			message.setFileName(challengeBean.getFileName());
			message.setHoverText(challengeBean.getHoverText());
			message.setId(COUNTER.getNextId("ip_challenge"));
			message.setLaunchDt(challengeBean.getLaunchDt());
			message.setStatusId(1);
			message.setTag(challengeBean.getTag());
			message.setTitle(challengeBean.getTitle());
			ResponseMessage response = addIdeaClient.accept(MediaType.APPLICATION_JSON).post(message, ResponseMessage.class);
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

	public String updateChallenge() {
		try {
			WebClient addIdeaClient = createCustomClient("http://127.0.0.1:38080/ip-ws/ip/cs/challenge/modify");
			ChallengeMessage message = new ChallengeMessage();
			message.setBlob(challengeBean.getBlob());
			message.setCatId(challengeBean.getCatId());
			message.setContentType(challengeBean.getContentType());
			message.setCrtdById(challengeBean.getId());
			message.setCrtdDt(challengeBean.getCrtdDt());
			message.setDesc(challengeBean.getDesc());
			message.setExprDt(challengeBean.getExprDt());
			message.setFileName(challengeBean.getFileName());
			message.setHoverText(challengeBean.getHoverText());
			message.setId(challengeBean.getId());
			message.setLaunchDt(challengeBean.getLaunchDt());
			message.setStatusId(challengeBean.getStatusId());
			message.setTag(challengeBean.getTag());
			message.setTitle(challengeBean.getTitle());
			ResponseMessage response = addIdeaClient.accept(MediaType.APPLICATION_JSON).put(message, ResponseMessage.class);
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

	private List<ChallengeBean> fetchAllChallenges() {
		List<ChallengeBean> ret = new ArrayList<ChallengeBean>();
		WebClient fetchChallengeClient = createCustomClient("http://127.0.0.1:38080/ip-ws/ip/cs/challenge/list");
		Collection<? extends ChallengeMessage> challenges = new ArrayList<ChallengeMessage>(fetchChallengeClient.accept(MediaType.APPLICATION_JSON).getCollection(ChallengeMessage.class));
		for (ChallengeMessage challengeMessage : challenges) {
			ChallengeBean bean = new ChallengeBean();
			bean.setBlob(challengeMessage.getBlob());
			bean.setCatId(challengeMessage.getCatId());
			bean.setContentType(challengeMessage.getContentType());
			bean.setCrtdById(challengeMessage.getId());
			bean.setCrtdDt(challengeMessage.getCrtdDt());
			bean.setDesc(challengeMessage.getDesc());
			bean.setExprDt(challengeMessage.getExprDt());
			bean.setFileName(challengeMessage.getFileName());
			bean.setHoverText(challengeMessage.getHoverText());
			bean.setId(challengeMessage.getId());
			bean.setLaunchDt(challengeMessage.getLaunchDt());
			bean.setStatusId(challengeMessage.getStatusId());
			bean.setTag(challengeMessage.getTag());
			bean.setTitle(challengeMessage.getTitle());
			ret.add(bean);
		}
		fetchChallengeClient.close();
		return ret;
	}

	public void fileUploadHandle(FileUploadEvent e) {
		try {
			UploadedFile file = e.getFile();
			this.challengeBean.setBlob(new String(file.getContents()));
			this.challengeBean.setContentType(file.getContentType());
			this.challengeBean.setFileName(file.getFileName());
		} catch (Exception ex) {
			LOG.error(ex, ex);
			FacesMessage exceptionMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), ex.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, exceptionMessage);
		}
	}

	public String showCommentChallenge() {
		comments = fetchAllComments();
		showChallengeComments = true;
		showChallengeLikes = false;
		return "";
	}

	private TagCloudModel fetchAllLikes() {
		TagCloudModel likes = new DefaultTagCloudModel();
		WebClient fetchChallengeLikesClient = createCustomClient("http://127.0.0.1:38080/ip-ws/ip/ts/tag/get/" + challengeBean.getId() + "/2/1");
		Collection<? extends TagMessage> likeList = new ArrayList<TagMessage>(fetchChallengeLikesClient.accept(MediaType.APPLICATION_JSON).getCollection(TagMessage.class));
		for (TagMessage tagMessage : likeList)
			likes.addTag(new DefaultTagCloudItem(tagMessage.getUsrScreenName(), 1));
		fetchChallengeLikesClient.close();
		return likes;
	}

	private List<TagBean> fetchAllComments() {
		WebClient fetchChallengeCommentsClient = createCustomClient("http://127.0.0.1:38080/ip-ws/ip/ts/tag/get/" + challengeBean.getId() + "/2/2");
		Collection<? extends TagMessage> msgs = new ArrayList<TagMessage>(fetchChallengeCommentsClient.accept(MediaType.APPLICATION_JSON).getCollection(TagMessage.class));
		fetchChallengeCommentsClient.close();
		List<TagBean> ret = new ArrayList<TagBean>();
		for (TagMessage msg : msgs) {
			TagBean bean = new TagBean();
			bean.setTagText(msg.getTagText());
			bean.setUsrScreenName(msg.getUsrScreenName());
			ret.add(bean);
		}
		return ret;
	}

	private List<ListSelectorBean> fetchAllChallengeStatuses() {
		List<ListSelectorBean> ret = new ArrayList<ListSelectorBean>();
		WebClient viewChallengeSelectClient = createCustomClient("http://127.0.0.1:38080/ip-ws/ip/cs/challenge/status/list");
		Collection<? extends MetaDataMessage> md = new ArrayList<MetaDataMessage>(viewChallengeSelectClient.accept(MediaType.APPLICATION_JSON).getCollection(MetaDataMessage.class));
		for (MetaDataMessage metaDataMessage : md) {
			ListSelectorBean bean = new ListSelectorBean();
			bean.setId(metaDataMessage.getId());
			bean.setDesc(metaDataMessage.getDesc());
			ret.add(bean);
		}
		viewChallengeSelectClient.close();
		return ret;
	}

	private List<ListSelectorBean> fetchAllChallengeCat() {
		List<ListSelectorBean> ret = new ArrayList<ListSelectorBean>();
		WebClient viewChallengeSelectClient = createCustomClient("http://127.0.0.1:38080/ip-ws/ip/cs/challenge/cat/list");
		Collection<? extends MetaDataMessage> md = new ArrayList<MetaDataMessage>(viewChallengeSelectClient.accept(MediaType.APPLICATION_JSON).getCollection(MetaDataMessage.class));
		for (MetaDataMessage metaDataMessage : md) {
			ListSelectorBean bean = new ListSelectorBean();
			bean.setId(metaDataMessage.getId());
			bean.setDesc(metaDataMessage.getDesc());
			ret.add(bean);
		}
		viewChallengeSelectClient.close();
		return ret;
	}

	public String commentChallenge() {
		WebClient addTagClient = createCustomClient("http://127.0.0.1:38080/ip-ws/ip/ts/tag/add");
		TagMessage message = new TagMessage();
		message.setEntityId(challengeBean.getId());
		message.setTagId(COUNTER.getNextId("ip_tag"));
		message.setTagText(commentText);
		message.setTeId(2);
		message.setTtId(2);
		message.setDuplicate(true);
		message.setUserId((Long) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userId"));
		ResponseMessage response = addTagClient.accept(MediaType.APPLICATION_JSON).post(message, ResponseMessage.class);
		if (response.getStatusCode() != 0)
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error While Saving Comment", "Error While Saving Comment"));
		comments = fetchAllComments();
		commentCnt = "(" + comments.size() + ")";
		commentText = "";
		showChallengeComments = true;
		showChallengeLikes = false;
		addTagClient.close();
		return "";
	}

	public String likeChallenge() {
		WebClient addTagClient = createCustomClient("http://127.0.0.1:38080/ip-ws/ip/ts/tag/add");
		TagMessage message = new TagMessage();
		message.setEntityId(challengeBean.getId());
		message.setTagId(COUNTER.getNextId("ip_tag"));
		message.setTeId(2);
		message.setTtId(1);
		message.setUserId((Long) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userId"));
		message.setDuplicate(false);
		ResponseMessage response = addTagClient.accept(MediaType.APPLICATION_JSON).post(message, ResponseMessage.class);
		if (response.getStatusCode() != 0 && response.getStatusCode() != 2)
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error While Saving Like", "Error While Saving Like"));
		likes = fetchAllLikes();
		likeCnt = "(" + likes.getTags().size() + ")";
		showChallengeComments = false;
		showChallengeLikes = true;
		addTagClient.close();
		return "";
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

	public ChallengeBean getChallengeBean() {
		if (challengeBean == null)
			challengeBean = new ChallengeBean();
		return challengeBean;
	}

	public void setChallengeBean(ChallengeBean challengeBean) {
		this.challengeBean = challengeBean;
	}

	public List<ChallengeBean> getViewChallenges() {
		if (viewChallenges == null)
			viewChallenges = new ArrayList<ChallengeBean>();
		return viewChallenges;
	}

	public void setViewChallenges(List<ChallengeBean> viewChallenges) {
		this.viewChallenges = viewChallenges;
	}

	public List<UserBean> getAdmUsers() {
		if (admUsers == null)
			admUsers = new ArrayList<UserBean>();
		return admUsers;
	}

	public void setAdmUsers(List<UserBean> admUsers) {
		this.admUsers = admUsers;
	}

	public List<ListSelectorBean> getChallengeCats() {
		if (challengeCats == null)
			challengeCats = new ArrayList<ListSelectorBean>();
		return challengeCats;
	}

	public void setChallengeCats(List<ListSelectorBean> challengeCats) {
		this.challengeCats = challengeCats;
	}

	public List<ListSelectorBean> getChallengeStatuses() {
		if (challengeStatuses == null)
			challengeStatuses = new ArrayList<ListSelectorBean>();
		return challengeStatuses;
	}

	public void setChallengeStatuses(List<ListSelectorBean> challengeStatuses) {
		this.challengeStatuses = challengeStatuses;
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

	public String getCommentText() {
		return commentText;
	}

	public void setCommentText(String commentText) {
		this.commentText = commentText;
	}

	public boolean isFileAvail() {
		return fileAvail;
	}

	public void setFileAvail(boolean fileAvail) {
		this.fileAvail = fileAvail;
	}

	public boolean isShowChallengeComments() {
		return showChallengeComments;
	}

	public void setShowChallengeComments(boolean showChallengeComments) {
		this.showChallengeComments = showChallengeComments;
	}

	public boolean isShowChallengeLikes() {
		return showChallengeLikes;
	}

	public void setShowChallengeLikes(boolean showChallengeLikes) {
		this.showChallengeLikes = showChallengeLikes;
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

	public StreamedContent getFileContent() {
		return fileContent;
	}

	public void setFileContent(StreamedContent fileContent) {
		this.fileContent = fileContent;
	}

}
