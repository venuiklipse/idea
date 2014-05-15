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

import za.co.idea.ip.ws.bean.ChallengeMessage;
import za.co.idea.ip.ws.bean.MetaDataMessage;
import za.co.idea.ip.ws.bean.ResponseMessage;
import za.co.idea.ip.ws.bean.SolutionMessage;
import za.co.idea.ip.ws.bean.TagMessage;
import za.co.idea.ip.ws.bean.UserMessage;
import za.co.idea.ip.ws.util.CustomObjectMapper;
import za.co.idea.web.ui.bean.ChallengeBean;
import za.co.idea.web.ui.bean.ListSelectorBean;
import za.co.idea.web.ui.bean.SolutionBean;
import za.co.idea.web.ui.bean.TagBean;
import za.co.idea.web.ui.bean.UserBean;
import za.co.idea.web.util.IdNumberGen;

public class ChallengeController implements Serializable {

	private static final long serialVersionUID = -6939719926402016671L;
	private static final Log LOG = LogFactory.getLog(ChallengeController.class);
	private ChallengeBean challengeBean;
	private SolutionBean solutionBean;
	private List<ChallengeBean> viewChallenges;
	private List<SolutionBean> viewSolutions;
	private List<UserBean> admUsers;
	private List<ListSelectorBean> challengeCats;
	private List<ListSelectorBean> challengeStatuses;
	private List<ListSelectorBean> solutionCats;
	private List<ListSelectorBean> solutionStatuses;
	private StreamedContent fileContent;
	private TagCloudModel chalLikes;
	private List<TagBean> chalComments;
	private TagCloudModel solLikes;
	private List<TagBean> solComments;
	private String chalLikeCnt;
	private String chalCommentCnt;
	private String solLikeCnt;
	private String solCommentCnt;
	private String commentText;
	private boolean fileAvail;
	private boolean showChallengeComments;
	private boolean showChallengeLikes;
	private boolean showSolutionComments;
	private boolean showSolutionLikes;
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

	public String showViewChallengeByUser() {
		try {
			viewChallenges = fetchAllChallengesByUser();
			challengeCats = fetchAllChallengeCat();
			admUsers = fetchAllUsers();
			challengeStatuses = fetchAllChallengeStatuses();
			return "chaluc";
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
		chalLikes = fetchAllChalLikes();
		chalComments = fetchAllChalComments();
		chalLikeCnt = "(" + chalLikes.getTags().size() + ")";
		chalCommentCnt = "(" + chalComments.size() + ")";
		showChallengeComments = false;
		showChallengeLikes = false;
		return "chalsc";
	}

	public String saveChallenge() {
		try {
			WebClient addChallengeClient = createCustomClient("http://127.0.0.1:38080/ip-ws/ip/cs/challenge/add");
			ChallengeMessage message = new ChallengeMessage();
			message.setBlob(new String(Base64.encodeBase64URLSafe(challengeBean.getBlob().getBytes())));
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
			ResponseMessage response = addChallengeClient.accept(MediaType.APPLICATION_JSON).post(message, ResponseMessage.class);
			if (response.getStatusCode() == 0) {
				addChallengeClient.close();
				return "home";
			} else {
				addChallengeClient.close();
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
			WebClient updateChallengeClient = createCustomClient("http://127.0.0.1:38080/ip-ws/ip/cs/challenge/modify");
			ChallengeMessage message = new ChallengeMessage();
			message.setBlob(new String(Base64.encodeBase64URLSafe(challengeBean.getBlob().getBytes())));
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
			ResponseMessage response = updateChallengeClient.accept(MediaType.APPLICATION_JSON).put(message, ResponseMessage.class);
			if (response.getStatusCode() == 0) {
				updateChallengeClient.close();
				return "home";
			} else {
				updateChallengeClient.close();
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
		chalComments = fetchAllChalComments();
		chalCommentCnt = "(" + chalComments.size() + ")";
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
		chalLikes = fetchAllChalLikes();
		chalLikeCnt = "(" + chalLikes.getTags().size() + ")";
		showChallengeComments = false;
		showChallengeLikes = true;
		addTagClient.close();
		return "";
	}

	public String showCommentChallenge() {
		chalComments = fetchAllChalComments();
		showChallengeComments = true;
		showChallengeLikes = false;
		return "";
	}

	public String showCreateSolution() {
		try {
			admUsers = fetchAllUsers();
			viewChallenges = fetchAllChallenges();
			solutionCats = fetchAllSolutionCat();
			solutionStatuses = fetchAllSolutionStatuses();
			solutionBean = new SolutionBean();
			return "solcs";
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e, e);
			FacesMessage exceptionMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, exceptionMessage);
			return "";
		}
	}

	public String showViewSolution() {
		try {
			admUsers = fetchAllUsers();
			viewChallenges = fetchAllChallenges();
			solutionCats = fetchAllSolutionCat();
			solutionStatuses = fetchAllSolutionStatuses();
			solutionBean = new SolutionBean();
			viewSolutions = fetchAllSolutions();
			return "solvs";
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e, e);
			FacesMessage exceptionMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, exceptionMessage);
			return "";
		}
	}

	public String showViewSolutionByUser() {
		try {
			admUsers = fetchAllUsers();
			viewChallenges = fetchAllChallenges();
			solutionCats = fetchAllSolutionCat();
			solutionStatuses = fetchAllSolutionStatuses();
			solutionBean = new SolutionBean();
			viewSolutions = fetchAllSolutionsByUser();
			return "solus";
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e, e);
			FacesMessage exceptionMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, exceptionMessage);
			return "";
		}
	}

	public String showEditSolution() {
		try {
			admUsers = fetchAllUsers();
			viewChallenges = fetchAllChallenges();
			solutionCats = fetchAllSolutionCat();
			solutionStatuses = fetchAllSolutionStatuses();
			if (solutionBean.getBlob() != null && solutionBean.getBlob().length() > 0) {
				fileAvail = false;
				fileContent = new DefaultStreamedContent(new ByteArrayInputStream(solutionBean.getBlob().getBytes()), solutionBean.getContentType(), solutionBean.getFileName());
			} else {
				fileAvail = true;
				fileContent = null;
			}
			return "soles";
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e, e);
			FacesMessage exceptionMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, exceptionMessage);
			return "";
		}
	}

	public String showSummarySolution() {
		solLikes = fetchAllSolLikes();
		solComments = fetchAllSolComments();
		solLikeCnt = "(" + solLikes.getTags().size() + ")";
		solCommentCnt = "(" + solComments.size() + ")";
		showSolutionComments = false;
		showSolutionLikes = false;
		return "solss";
	}

	public String saveSolution() {
		try {
			WebClient addSolutionClient = createCustomClient("http://127.0.0.1:38080/ip-ws/ip/ss/solution/add");
			SolutionMessage message = new SolutionMessage();
			message.setBlob(new String(Base64.encodeBase64URLSafe(solutionBean.getBlob().getBytes())));
			message.setFileName(solutionBean.getFileName());
			message.setContentType(solutionBean.getContentType());
			message.setCatId(solutionBean.getCatId());
			message.setChalId(solutionBean.getChalId());
			message.setCrtdById((Long) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userId"));
			message.setCrtdDt(new Date());
			message.setDesc(solutionBean.getDesc());
			message.setId(COUNTER.getNextId("ip_solution"));
			message.setStatusId(1);
			message.setTags(solutionBean.getTags());
			message.setTitle(solutionBean.getTitle());
			ResponseMessage response = addSolutionClient.accept(MediaType.APPLICATION_JSON).post(message, ResponseMessage.class);
			if (response.getStatusCode() == 0) {
				addSolutionClient.close();
				return "home";
			} else {
				addSolutionClient.close();
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

	public String updateSolution() {
		WebClient updateSolutionClient = createCustomClient("http://127.0.0.1:38080/ip-ws/ip/ss/solution/modify");
		SolutionMessage message = new SolutionMessage();
		message.setBlob(new String(Base64.encodeBase64URLSafe(solutionBean.getBlob().getBytes())));
		message.setFileName(solutionBean.getFileName());
		message.setContentType(solutionBean.getContentType());
		message.setCatId(solutionBean.getCatId());
		message.setChalId(solutionBean.getChalId());
		message.setCrtdById((Long) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userId"));
		message.setCrtdDt(new Date());
		message.setDesc(solutionBean.getDesc());
		message.setId(solutionBean.getId());
		message.setStatusId(1);
		message.setTags(solutionBean.getTags());
		message.setTitle(solutionBean.getTitle());
		ResponseMessage response = updateSolutionClient.accept(MediaType.APPLICATION_JSON).put(message, ResponseMessage.class);
		if (response.getStatusCode() == 0) {
			updateSolutionClient.close();
			return "home";
		} else {
			updateSolutionClient.close();
			return "";
		}
	}

	public String likeSolution() {
		WebClient addTagClient = createCustomClient("http://127.0.0.1:38080/ip-ws/ip/ts/tag/add");
		TagMessage message = new TagMessage();
		message.setEntityId(solutionBean.getId());
		message.setTagId(COUNTER.getNextId("ip_tag"));
		message.setTeId(3);
		message.setTtId(1);
		message.setUserId((Long) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userId"));
		message.setDuplicate(false);
		ResponseMessage response = addTagClient.accept(MediaType.APPLICATION_JSON).post(message, ResponseMessage.class);
		if (response.getStatusCode() != 0 && response.getStatusCode() != 2)
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error While Saving Like", "Error While Saving Like"));
		solLikes = fetchAllSolLikes();
		solLikeCnt = "(" + solLikes.getTags().size() + ")";
		showSolutionComments = false;
		showSolutionLikes = true;
		addTagClient.close();
		return "";
	}

	public String commentSolution() {
		WebClient addTagClient = createCustomClient("http://127.0.0.1:38080/ip-ws/ip/ts/tag/add");
		TagMessage message = new TagMessage();
		message.setEntityId(solutionBean.getId());
		message.setTagId(COUNTER.getNextId("ip_tag"));
		message.setTagText(commentText);
		message.setTeId(3);
		message.setTtId(2);
		message.setDuplicate(true);
		message.setUserId((Long) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userId"));
		ResponseMessage response = addTagClient.accept(MediaType.APPLICATION_JSON).post(message, ResponseMessage.class);
		if (response.getStatusCode() != 0)
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error While Saving Comment", "Error While Saving Comment"));
		solComments = fetchAllSolComments();
		solCommentCnt = "(" + solComments.size() + ")";
		commentText = "";
		showSolutionComments = true;
		showSolutionLikes = false;
		addTagClient.close();
		return "";
	}

	public String showCommentSolution() {
		solComments = fetchAllSolComments();
		showSolutionComments = true;
		showSolutionLikes = false;
		return "";
	}

	private List<ChallengeBean> fetchAllChallenges() {
		List<ChallengeBean> ret = new ArrayList<ChallengeBean>();
		WebClient fetchChallengeClient = createCustomClient("http://127.0.0.1:38080/ip-ws/ip/cs/challenge/list");
		Collection<? extends ChallengeMessage> challenges = new ArrayList<ChallengeMessage>(fetchChallengeClient.accept(MediaType.APPLICATION_JSON).getCollection(ChallengeMessage.class));
		for (ChallengeMessage challengeMessage : challenges) {
			ChallengeBean bean = new ChallengeBean();
			bean.setBlob(new String(Base64.decodeBase64(challengeMessage.getBlob())));
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

	private List<ChallengeBean> fetchAllChallengesByUser() {
		List<ChallengeBean> ret = new ArrayList<ChallengeBean>();
		WebClient fetchChallengeClient = createCustomClient("http://127.0.0.1:38080/ip-ws/ip/cs/challenge/list/" + ((Long) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userId")).longValue());
		Collection<? extends ChallengeMessage> challenges = new ArrayList<ChallengeMessage>(fetchChallengeClient.accept(MediaType.APPLICATION_JSON).getCollection(ChallengeMessage.class));
		for (ChallengeMessage challengeMessage : challenges) {
			ChallengeBean bean = new ChallengeBean();
			bean.setBlob(new String(Base64.decodeBase64(challengeMessage.getBlob())));
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

	private List<SolutionBean> fetchAllSolutions() {
		List<SolutionBean> ret = new ArrayList<SolutionBean>();
		WebClient fetchSolutionClient = createCustomClient("http://127.0.0.1:38080/ip-ws/ip/ss/solution/list");
		Collection<? extends SolutionMessage> solutions = new ArrayList<SolutionMessage>(fetchSolutionClient.accept(MediaType.APPLICATION_JSON).getCollection(SolutionMessage.class));
		for (SolutionMessage solutionMessage : solutions) {
			SolutionBean bean = new SolutionBean();
			bean.setChalId(solutionMessage.getChalId());
			bean.setBlob(new String(Base64.decodeBase64(solutionMessage.getBlob())));
			bean.setCatId(solutionMessage.getCatId());
			bean.setContentType(solutionMessage.getContentType());
			bean.setCrtdById(solutionMessage.getId());
			bean.setCrtdDt(solutionMessage.getCrtdDt());
			bean.setDesc(solutionMessage.getDesc());
			bean.setFileName(solutionMessage.getFileName());
			bean.setId(solutionMessage.getId());
			bean.setStatusId(solutionMessage.getStatusId());
			bean.setTags(solutionMessage.getTags());
			bean.setTitle(solutionMessage.getTitle());
			ret.add(bean);
		}
		fetchSolutionClient.close();
		return ret;
	}

	private List<SolutionBean> fetchAllSolutionsByUser() {
		List<SolutionBean> ret = new ArrayList<SolutionBean>();
		WebClient fetchSolutionClient = createCustomClient("http://127.0.0.1:38080/ip-ws/ip/ss/solution/list/" + ((Long) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userId")).longValue());
		Collection<? extends SolutionMessage> solutions = new ArrayList<SolutionMessage>(fetchSolutionClient.accept(MediaType.APPLICATION_JSON).getCollection(SolutionMessage.class));
		for (SolutionMessage solutionMessage : solutions) {
			SolutionBean bean = new SolutionBean();
			bean.setChalId(solutionMessage.getChalId());
			bean.setBlob(new String(Base64.decodeBase64(solutionMessage.getBlob())));
			bean.setCatId(solutionMessage.getCatId());
			bean.setContentType(solutionMessage.getContentType());
			bean.setCrtdById(solutionMessage.getId());
			bean.setCrtdDt(solutionMessage.getCrtdDt());
			bean.setDesc(solutionMessage.getDesc());
			bean.setFileName(solutionMessage.getFileName());
			bean.setId(solutionMessage.getId());
			bean.setStatusId(solutionMessage.getStatusId());
			bean.setTags(solutionMessage.getTags());
			bean.setTitle(solutionMessage.getTitle());
			ret.add(bean);
		}
		fetchSolutionClient.close();
		return ret;
	}

	public void chalFileUploadHandle(FileUploadEvent e) {
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

	public void solFileUploadHandle(FileUploadEvent e) {
		try {
			UploadedFile file = e.getFile();
			this.solutionBean.setBlob(new String(file.getContents()));
			this.solutionBean.setContentType(file.getContentType());
			this.solutionBean.setFileName(file.getFileName());
		} catch (Exception ex) {
			LOG.error(ex, ex);
			FacesMessage exceptionMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), ex.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, exceptionMessage);
		}
	}

	private TagCloudModel fetchAllChalLikes() {
		TagCloudModel likes = new DefaultTagCloudModel();
		WebClient fetchChallengeLikesClient = createCustomClient("http://127.0.0.1:38080/ip-ws/ip/ts/tag/get/" + challengeBean.getId() + "/2/1");
		Collection<? extends TagMessage> likeList = new ArrayList<TagMessage>(fetchChallengeLikesClient.accept(MediaType.APPLICATION_JSON).getCollection(TagMessage.class));
		for (TagMessage tagMessage : likeList)
			likes.addTag(new DefaultTagCloudItem(tagMessage.getUsrScreenName(), 1));
		fetchChallengeLikesClient.close();
		return likes;
	}

	private List<TagBean> fetchAllChalComments() {
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

	private TagCloudModel fetchAllSolLikes() {
		TagCloudModel likes = new DefaultTagCloudModel();
		WebClient fetchSolutionLikesClient = createCustomClient("http://127.0.0.1:38080/ip-ws/ip/ts/tag/get/" + solutionBean.getId() + "/3/1");
		Collection<? extends TagMessage> likeList = new ArrayList<TagMessage>(fetchSolutionLikesClient.accept(MediaType.APPLICATION_JSON).getCollection(TagMessage.class));
		for (TagMessage tagMessage : likeList)
			likes.addTag(new DefaultTagCloudItem(tagMessage.getUsrScreenName(), 1));
		fetchSolutionLikesClient.close();
		return likes;
	}

	private List<TagBean> fetchAllSolComments() {
		WebClient fetchSolutionCommentsClient = createCustomClient("http://127.0.0.1:38080/ip-ws/ip/ts/tag/get/" + solutionBean.getId() + "/3/2");
		Collection<? extends TagMessage> msgs = new ArrayList<TagMessage>(fetchSolutionCommentsClient.accept(MediaType.APPLICATION_JSON).getCollection(TagMessage.class));
		fetchSolutionCommentsClient.close();
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

	private List<ListSelectorBean> fetchAllSolutionStatuses() {
		List<ListSelectorBean> ret = new ArrayList<ListSelectorBean>();
		WebClient viewSolutionSelectClient = createCustomClient("http://127.0.0.1:38080/ip-ws/ip/ss/solution/status/list");
		Collection<? extends MetaDataMessage> md = new ArrayList<MetaDataMessage>(viewSolutionSelectClient.accept(MediaType.APPLICATION_JSON).getCollection(MetaDataMessage.class));
		for (MetaDataMessage metaDataMessage : md) {
			ListSelectorBean bean = new ListSelectorBean();
			bean.setId(metaDataMessage.getId());
			bean.setDesc(metaDataMessage.getDesc());
			ret.add(bean);
		}
		viewSolutionSelectClient.close();
		return ret;
	}

	private List<ListSelectorBean> fetchAllSolutionCat() {
		List<ListSelectorBean> ret = new ArrayList<ListSelectorBean>();
		WebClient viewSolutionSelectClient = createCustomClient("http://127.0.0.1:38080/ip-ws/ip/ss/solution/cat/list");
		Collection<? extends MetaDataMessage> md = new ArrayList<MetaDataMessage>(viewSolutionSelectClient.accept(MediaType.APPLICATION_JSON).getCollection(MetaDataMessage.class));
		for (MetaDataMessage metaDataMessage : md) {
			ListSelectorBean bean = new ListSelectorBean();
			bean.setId(metaDataMessage.getId());
			bean.setDesc(metaDataMessage.getDesc());
			ret.add(bean);
		}
		viewSolutionSelectClient.close();
		return ret;
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

	public StreamedContent getFileContent() {
		return fileContent;
	}

	public void setFileContent(StreamedContent fileContent) {
		this.fileContent = fileContent;
	}

	public TagCloudModel getChalLikes() {
		return chalLikes;
	}

	public void setChalLikes(TagCloudModel chalLikes) {
		this.chalLikes = chalLikes;
	}

	public List<TagBean> getChalComments() {
		if (chalComments == null)
			chalComments = new ArrayList<TagBean>();
		return chalComments;
	}

	public void setChalComments(List<TagBean> chalComments) {
		this.chalComments = chalComments;
	}

	public TagCloudModel getSolLikes() {
		return solLikes;
	}

	public void setSolLikes(TagCloudModel solLikes) {
		this.solLikes = solLikes;
	}

	public List<TagBean> getSolComments() {
		if (solComments == null)
			solComments = new ArrayList<TagBean>();
		return solComments;
	}

	public void setSolComments(List<TagBean> solComments) {
		this.solComments = solComments;
	}

	public String getChalLikeCnt() {
		return chalLikeCnt;
	}

	public void setChalLikeCnt(String chalLikeCnt) {
		this.chalLikeCnt = chalLikeCnt;
	}

	public String getChalCommentCnt() {
		return chalCommentCnt;
	}

	public void setChalCommentCnt(String chalCommentCnt) {
		this.chalCommentCnt = chalCommentCnt;
	}

	public String getSolLikeCnt() {
		return solLikeCnt;
	}

	public void setSolLikeCnt(String solLikeCnt) {
		this.solLikeCnt = solLikeCnt;
	}

	public String getSolCommentCnt() {
		return solCommentCnt;
	}

	public void setSolCommentCnt(String solCommentCnt) {
		this.solCommentCnt = solCommentCnt;
	}

	public SolutionBean getSolutionBean() {
		if (solutionBean == null)
			solutionBean = new SolutionBean();
		return solutionBean;
	}

	public void setSolutionBean(SolutionBean solutionBean) {
		this.solutionBean = solutionBean;
	}

	public List<SolutionBean> getViewSolutions() {
		if (viewSolutions == null)
			viewSolutions = new ArrayList<SolutionBean>();
		return viewSolutions;
	}

	public void setViewSolutions(List<SolutionBean> viewSolutions) {
		this.viewSolutions = viewSolutions;
	}

	public List<ListSelectorBean> getSolutionCats() {
		if (solutionCats == null)
			solutionCats = new ArrayList<ListSelectorBean>();
		return solutionCats;
	}

	public void setSolutionCats(List<ListSelectorBean> solutionCats) {
		this.solutionCats = solutionCats;
	}

	public List<ListSelectorBean> getSolutionStatuses() {
		if (solutionStatuses == null)
			solutionStatuses = new ArrayList<ListSelectorBean>();
		return solutionStatuses;
	}

	public void setSolutionStatuses(List<ListSelectorBean> solutionStatuses) {
		this.solutionStatuses = solutionStatuses;
	}

	public boolean isShowSolutionComments() {
		return showSolutionComments;
	}

	public void setShowSolutionComments(boolean showSolutionComments) {
		this.showSolutionComments = showSolutionComments;
	}

	public boolean isShowSolutionLikes() {
		return showSolutionLikes;
	}

	public void setShowSolutionLikes(boolean showSolutionLikes) {
		this.showSolutionLikes = showSolutionLikes;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public static Log getLog() {
		return LOG;
	}

	public static IdNumberGen getCounter() {
		return COUNTER;
	}

}
