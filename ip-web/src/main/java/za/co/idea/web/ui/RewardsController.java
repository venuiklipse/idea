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

import za.co.idea.ip.jaxws.document.Document;
import za.co.idea.ip.jaxws.document.DocumentService;
import za.co.idea.ip.jaxws.document.DownloadDocumentRq;
import za.co.idea.ip.jaxws.document.DownloadDocumentRs;
import za.co.idea.ip.jaxws.document.UploadDocumentRq;
import za.co.idea.ip.jaxws.document.UploadDocumentRs;
import za.co.idea.ip.ws.bean.MetaDataMessage;
import za.co.idea.ip.ws.bean.ResponseMessage;
import za.co.idea.ip.ws.bean.RewardsMessage;
import za.co.idea.ip.ws.bean.TagMessage;
import za.co.idea.ip.ws.bean.UserMessage;
import za.co.idea.ip.ws.util.CustomObjectMapper;
import za.co.idea.web.ui.bean.ListSelectorBean;
import za.co.idea.web.ui.bean.RewardsBean;
import za.co.idea.web.ui.bean.TagBean;
import za.co.idea.web.ui.bean.UserBean;
import za.co.idea.web.util.IdNumberGen;

public class RewardsController implements Serializable {

	private static final Log LOG = LogFactory.getLog(RewardsController.class);
	private static final long serialVersionUID = 1830096234056863422L;

	private RewardsBean rewardsBean;
	private StreamedContent fileContent;
	private List<RewardsBean> viewRewardsBeans;
	private List<ListSelectorBean> rewardsCat;
	private List<ListSelectorBean> rewardsStatus;
	private List<UserBean> admUsers;
	private List<TagBean> rewardsWishlist;
	private String rewardsWishlistCnt;
	private boolean fileAvail;
	private boolean showRewardsWishlist;
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

	public String showCreateReward() {
		try {
			admUsers = fetchAllUsers();
			rewardsCat = fetchAllRewardsCat();
			rewardsStatus = fetchAllRewardsStatuses();
			rewardsBean = new RewardsBean();
			return "rwcr";
		} catch (Exception e) {
			e.printStackTrace();
			FacesMessage exceptionMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, exceptionMessage);
			return "";
		}
	}

	public String showViewReward() {
		try {
			admUsers = fetchAllUsers();
			rewardsCat = fetchAllRewardsCat();
			rewardsStatus = fetchAllRewardsStatuses();
			viewRewardsBeans = fetchAllRewards();
			return "rwvr";
		} catch (Exception e) {
			e.printStackTrace();
			FacesMessage exceptionMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, exceptionMessage);
			return "";
		}
	}

	public String showViewRewardByUser() {
		try {
			admUsers = fetchAllUsers();
			rewardsCat = fetchAllRewardsCat();
			rewardsStatus = fetchAllRewardsStatuses();
			rewardsBean = new RewardsBean();
			viewRewardsBeans = fetchAllRewardsByUser();
			return "rwur";
		} catch (Exception e) {
			e.printStackTrace();
			FacesMessage exceptionMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, exceptionMessage);
			return "";
		}
	}

	public String showEditReward() {
		try {
			admUsers = fetchAllUsers();
			rewardsCat = fetchAllRewardsCat();
			rewardsStatus = fetchNextRewardsStatuses();
			try {
				DocumentService service = new DocumentService();
				DownloadDocumentRq rq = new DownloadDocumentRq();
				rq.setEntityId(rewardsBean.getRwId().toString());
				rq.setEntityTableName("ip_rewards");
				DownloadDocumentRs rs = service.getDocumentSOAP().downloadDocument(rq);
				if (Integer.parseInt(rs.getResponse().getRespCode()) == 0) {
					fileAvail = true;
					rewardsBean.setRwFileName(rs.getFileName());
					rewardsBean.setRwFileType(rs.getContentType());
					fileContent = new DefaultStreamedContent(new ByteArrayInputStream(rs.getFileContent()));
				} else {
					fileAvail = false;
					fileContent = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
				FacesMessage exceptionMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage());
				FacesContext.getCurrentInstance().addMessage(null, exceptionMessage);
				fileAvail = false;
				fileContent = null;
			}
			return "rwer";
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e, e);
			FacesMessage exceptionMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, exceptionMessage);
			return "";
		}
	}

	public String showPointProfile() {
		return "rwpp";
	}

	public String saveRewards() {
		try {
			WebClient addRewardsClient = createCustomClient("http://127.0.0.1:8080/ip-ws/ip/rs/rewards/add");
			RewardsMessage message = new RewardsMessage();
			message.setrCatId(rewardsBean.getrCatId());
			message.setRwCrtdDt(new Date());
			message.setRwDesc(rewardsBean.getRwDesc());
			message.setRwId(COUNTER.getNextId("IpRewards"));
			message.setrStatusId(1);
			message.setRwTag(rewardsBean.getRwTag());
			message.setRwTitle(rewardsBean.getRwTitle());
			message.setRwValue(rewardsBean.getRwValue());
			message.setRwHoverText(rewardsBean.getRwHoverText());
			message.setRwStockCodeNum(rewardsBean.getRwStockCodeNum());
			message.setRwLaunchDt(rewardsBean.getRwLaunchDt());
			message.setRwExpiryDt(rewardsBean.getRwExpiryDt());
			ResponseMessage response = addRewardsClient.accept(MediaType.APPLICATION_JSON).post(message, ResponseMessage.class);
			addRewardsClient.close();
			if (response.getStatusCode() == 0) {
				try {
					Document document = new Document();
					document.setContentType(rewardsBean.getRwFileType());
					document.setEntityId(message.getRwId().toString());
					document.setEntityTableName("ip_rewards");
					document.setFileContent(rewardsBean.getRwBlob().getBytes());
					document.setFileName(rewardsBean.getRwFileName());
					DocumentService service = new DocumentService();
					UploadDocumentRq rq = new UploadDocumentRq();
					rq.setDocument(document);
					UploadDocumentRs rs = service.getDocumentSOAP().uploadDocument(rq);
					if (Integer.valueOf(rs.getResponse().getRespCode()) != 0) {
						FacesMessage exceptionMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Unable to upload attachment. Please update later", rs.getResponse().getRespDesc());
						FacesContext.getCurrentInstance().addMessage(null, exceptionMessage);
					}
				} catch (Exception e) {
					e.printStackTrace();
					FacesMessage exceptionMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Unable to upload attachment. Please update later", response.getStatusDesc());
					FacesContext.getCurrentInstance().addMessage(null, exceptionMessage);
				}
				return showViewReward();
			} else {
				FacesMessage exceptionMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, response.getStatusDesc(), response.getStatusDesc());
				FacesContext.getCurrentInstance().addMessage(null, exceptionMessage);
				return "";
			}
		} catch (Exception e) {
			e.printStackTrace();
			FacesMessage exceptionMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, exceptionMessage);
			return "";
		}
	}

	public String updateRewards() {
		try {
			WebClient updateRewardsClient = createCustomClient("http://127.0.0.1:8080/ip-ws/ip/rs/rewards/modify");
			RewardsMessage message = new RewardsMessage();
			message.setrCatId(rewardsBean.getrCatId());
			message.setRwCrtdDt(new Date());
			message.setRwDesc(rewardsBean.getRwDesc());
			message.setRwId(rewardsBean.getRwId());
			message.setrStatusId(rewardsBean.getrStatusId());
			message.setRwTag(rewardsBean.getRwTag());
			message.setRwTitle(rewardsBean.getRwTitle());
			message.setRwValue(rewardsBean.getRwValue());
			message.setRwHoverText(rewardsBean.getRwHoverText());
			message.setRwStockCodeNum(rewardsBean.getRwStockCodeNum());
			message.setRwLaunchDt(rewardsBean.getRwLaunchDt());
			message.setRwExpiryDt(rewardsBean.getRwExpiryDt());
			ResponseMessage response = updateRewardsClient.accept(MediaType.APPLICATION_JSON).put(message, ResponseMessage.class);
			updateRewardsClient.close();
			if (response.getStatusCode() == 0) {
				try {
					Document document = new Document();
					document.setContentType(rewardsBean.getRwFileType());
					document.setEntityId(message.getRwId().toString());
					document.setEntityTableName("ip_rewards");
					document.setFileContent(rewardsBean.getRwBlob().getBytes());
					document.setFileName(rewardsBean.getRwFileName());
					DocumentService service = new DocumentService();
					UploadDocumentRq rq = new UploadDocumentRq();
					rq.setDocument(document);
					UploadDocumentRs rs = service.getDocumentSOAP().uploadDocument(rq);
					if (Integer.valueOf(rs.getResponse().getRespCode()) != 0) {
						FacesMessage exceptionMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Unable to upload attachment. Please update later", rs.getResponse().getRespDesc());
						FacesContext.getCurrentInstance().addMessage(null, exceptionMessage);
					}
				} catch (Exception e) {
					e.printStackTrace();
					FacesMessage exceptionMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Unable to upload attachment. Please update later", response.getStatusDesc());
					FacesContext.getCurrentInstance().addMessage(null, exceptionMessage);
				}
				return showViewReward();
			} else {
				FacesMessage exceptionMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, response.getStatusDesc(), response.getStatusDesc());
				FacesContext.getCurrentInstance().addMessage(null, exceptionMessage);
				return "";
			}
		} catch (Exception e) {
			e.printStackTrace();
			FacesMessage exceptionMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, exceptionMessage);
			return "";
		}
	}

	private List<UserBean> fetchAllUsers() {
		List<UserBean> ret = new ArrayList<UserBean>();
		WebClient viewUsersClient = createCustomClient("http://127.0.0.1:8080/ip-ws/ip/as/user/list");
		Collection<? extends UserMessage> users = new ArrayList<UserMessage>(viewUsersClient.accept(MediaType.APPLICATION_JSON).getCollection(UserMessage.class));
		viewUsersClient.close();
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
			bean.setIsActive(true);
			bean.setuId(userMessage.getuId());
			ret.add(bean);
		}
		return ret;
	}

	public String addToWishlist() {
		WebClient addTagClient = createCustomClient("http://127.0.0.1:8080/ip-ws/ip/ts/tag/add");
		TagMessage message = new TagMessage();
		message.setEntityId(rewardsBean.getRwId());
		message.setTagId(COUNTER.getNextId("IpTag"));
		message.setTeId(4);
		message.setTtId(4);
		message.setUserId((Long) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userId"));
		message.setDuplicate(false);
		ResponseMessage response = addTagClient.accept(MediaType.APPLICATION_JSON).post(message, ResponseMessage.class);
		addTagClient.close();
		if (response.getStatusCode() != 0 && response.getStatusCode() != 2)
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error While Saving Like", "Error While Saving Like"));
		return "";
	}

	private List<RewardsBean> fetchAllRewards() {
		List<RewardsBean> ret = new ArrayList<RewardsBean>();
		WebClient viewRewardsClient = createCustomClient("http://127.0.0.1:8080/ip-ws/ip/rs/rewards/list");
		Collection<? extends RewardsMessage> rewards = new ArrayList<RewardsMessage>(viewRewardsClient.accept(MediaType.APPLICATION_JSON).getCollection(RewardsMessage.class));
		viewRewardsClient.close();
		DocumentService service = new DocumentService();
		for (RewardsMessage message : rewards) {
			RewardsBean bean = new RewardsBean();
			bean.setrCatId(message.getrCatId());
			bean.setrStatusId(message.getrStatusId());
			bean.setRwCrtdDt(message.getRwCrtdDt());
			bean.setRwDesc(message.getRwDesc());
			bean.setRwExpiryDt(message.getRwExpiryDt());
			bean.setRwHoverText(message.getRwHoverText());
			bean.setRwId(message.getRwId());
			bean.setRwLaunchDt(message.getRwLaunchDt());
			bean.setRwStockCodeNum(message.getRwStockCodeNum());
			bean.setRwTag(message.getRwTag());
			bean.setRwTitle(message.getRwTitle());
			bean.setRwValue(message.getRwValue());
			try {
				DownloadDocumentRq rq = new DownloadDocumentRq();
				rq.setEntityId(message.getRwId().toString());
				rq.setEntityTableName("ip_rewards");
				DownloadDocumentRs rs = service.getDocumentSOAP().downloadDocument(rq);
				if (Integer.parseInt(rs.getResponse().getRespCode()) == 0) {
					bean.setRwFileName(rs.getFileName());
					bean.setRwFileType(rs.getContentType());
					bean.setRwBlob(new String(rs.getFileContent()));
				}
			} catch (Exception e) {
				e.printStackTrace();
				FacesMessage exceptionMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Cannot Fetch Image For Id :: " + message.getRwId(), e.getMessage());
				FacesContext.getCurrentInstance().addMessage(null, exceptionMessage);
			}
			ret.add(bean);
		}
		return ret;
	}

	private List<RewardsBean> fetchAllRewardsByUser() {
		List<RewardsBean> ret = new ArrayList<RewardsBean>();
		WebClient viewRewardsClient = createCustomClient("http://127.0.0.1:8080/ip-ws/ip/rs/rewards/list/" + ((Long) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userId")).longValue());
		Collection<? extends RewardsMessage> rewards = new ArrayList<RewardsMessage>(viewRewardsClient.accept(MediaType.APPLICATION_JSON).getCollection(RewardsMessage.class));
		viewRewardsClient.close();
		DocumentService service = new DocumentService();
		for (RewardsMessage message : rewards) {
			RewardsBean bean = new RewardsBean();
			bean.setrCatId(message.getrCatId());
			bean.setrStatusId(message.getrStatusId());
			bean.setRwCrtdDt(message.getRwCrtdDt());
			bean.setRwDesc(message.getRwDesc());
			bean.setRwExpiryDt(message.getRwExpiryDt());
			bean.setRwHoverText(message.getRwHoverText());
			bean.setRwId(message.getRwId());
			bean.setRwLaunchDt(message.getRwLaunchDt());
			bean.setRwStockCodeNum(message.getRwStockCodeNum());
			bean.setRwTag(message.getRwTag());
			bean.setRwTitle(message.getRwTitle());
			bean.setRwValue(message.getRwValue());
			try {
				DownloadDocumentRq rq = new DownloadDocumentRq();
				rq.setEntityId(message.getRwId().toString());
				rq.setEntityTableName("ip_rewards");
				DownloadDocumentRs rs = service.getDocumentSOAP().downloadDocument(rq);
				if (Integer.parseInt(rs.getResponse().getRespCode()) == 0) {
					bean.setRwFileName(rs.getFileName());
					bean.setRwFileType(rs.getContentType());
					bean.setRwBlob(new String(rs.getFileContent()));
				}
			} catch (Exception e) {
				e.printStackTrace();
				FacesMessage exceptionMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Cannot Fetch Image For Id :: " + message.getRwId(), e.getMessage());
				FacesContext.getCurrentInstance().addMessage(null, exceptionMessage);
			}
			ret.add(bean);
		}
		return ret;
	}

	private List<ListSelectorBean> fetchAllRewardsCat() {
		List<ListSelectorBean> ret = new ArrayList<ListSelectorBean>();
		WebClient viewRewardsSelectClient = createCustomClient("http://127.0.0.1:8080/ip-ws/ip/rs/rewards/cat/list");
		Collection<? extends MetaDataMessage> md = new ArrayList<MetaDataMessage>(viewRewardsSelectClient.accept(MediaType.APPLICATION_JSON).getCollection(MetaDataMessage.class));
		viewRewardsSelectClient.close();
		for (MetaDataMessage metaDataMessage : md) {
			ListSelectorBean bean = new ListSelectorBean();
			bean.setId(metaDataMessage.getId());
			bean.setDesc(metaDataMessage.getDesc());
			ret.add(bean);
		}
		return ret;
	}

	private List<ListSelectorBean> fetchAllRewardsStatuses() {
		List<ListSelectorBean> ret = new ArrayList<ListSelectorBean>();
		WebClient viewRewardsSelectClient = createCustomClient("http://127.0.0.1:8080/ip-ws/ip/rs/rewards/status/list");
		Collection<? extends MetaDataMessage> md = new ArrayList<MetaDataMessage>(viewRewardsSelectClient.accept(MediaType.APPLICATION_JSON).getCollection(MetaDataMessage.class));
		viewRewardsSelectClient.close();
		for (MetaDataMessage metaDataMessage : md) {
			ListSelectorBean bean = new ListSelectorBean();
			bean.setId(metaDataMessage.getId());
			bean.setDesc(metaDataMessage.getDesc());
			ret.add(bean);
		}
		return ret;
	}

	private List<ListSelectorBean> fetchNextRewardsStatuses() {
		List<ListSelectorBean> ret = new ArrayList<ListSelectorBean>();
		WebClient viewRewardsSelectClient = createCustomClient("http://127.0.0.1:8080/ip-ws/ip/rs/rewards/status/list/" + rewardsBean.getrStatusId());
		Collection<? extends MetaDataMessage> md = new ArrayList<MetaDataMessage>(viewRewardsSelectClient.accept(MediaType.APPLICATION_JSON).getCollection(MetaDataMessage.class));
		viewRewardsSelectClient.close();
		for (MetaDataMessage metaDataMessage : md) {
			ListSelectorBean bean = new ListSelectorBean();
			bean.setId(metaDataMessage.getId());
			bean.setDesc(metaDataMessage.getDesc());
			ret.add(bean);
		}
		return ret;
	}

	public void rwFileUploadHandle(FileUploadEvent fue) {
		try {
			UploadedFile file = fue.getFile();
			this.rewardsBean.setRwBlob(new String(file.getContents()));
			this.rewardsBean.setRwFileType(file.getContentType());
			this.rewardsBean.setRwFileName(file.getFileName());
		} catch (Exception e) {
			e.printStackTrace();
			FacesMessage exceptionMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, exceptionMessage);
		}
	}

	public RewardsBean getRewardsBean() {
		return rewardsBean;
	}

	public void setRewardsBean(RewardsBean rewardsBean) {
		this.rewardsBean = rewardsBean;
	}

	public StreamedContent getFileContent() {
		return fileContent;
	}

	public void setFileContent(StreamedContent fileContent) {
		this.fileContent = fileContent;
	}

	public List<RewardsBean> getViewRewardsBeans() {
		return viewRewardsBeans;
	}

	public void setViewRewardsBeans(List<RewardsBean> viewRewardsBeans) {
		this.viewRewardsBeans = viewRewardsBeans;
	}

	public List<ListSelectorBean> getRewardsCat() {
		return rewardsCat;
	}

	public void setRewardsCat(List<ListSelectorBean> rewardsCat) {
		this.rewardsCat = rewardsCat;
	}

	public List<ListSelectorBean> getRewardsStatus() {
		return rewardsStatus;
	}

	public void setRewardsStatus(List<ListSelectorBean> rewardsStatus) {
		this.rewardsStatus = rewardsStatus;
	}

	public List<UserBean> getAdmUsers() {
		return admUsers;
	}

	public void setAdmUsers(List<UserBean> admUsers) {
		this.admUsers = admUsers;
	}

	public List<TagBean> getRewardsWishlist() {
		return rewardsWishlist;
	}

	public void setRewardsWishlist(List<TagBean> rewardsWishlist) {
		this.rewardsWishlist = rewardsWishlist;
	}

	public String getRewardsWishlistCnt() {
		return rewardsWishlistCnt;
	}

	public void setRewardsWishlistCnt(String rewardsWishlistCnt) {
		this.rewardsWishlistCnt = rewardsWishlistCnt;
	}

	public boolean isFileAvail() {
		return fileAvail;
	}

	public void setFileAvail(boolean fileAvail) {
		this.fileAvail = fileAvail;
	}

	public boolean isShowRewardsWishlist() {
		return showRewardsWishlist;
	}

	public void setShowRewardsWishlist(boolean showRewardsWishlist) {
		this.showRewardsWishlist = showRewardsWishlist;
	}

}
