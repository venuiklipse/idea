package za.co.idea.web.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.ws.rs.core.MediaType;

import org.apache.cxf.jaxrs.client.WebClient;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;

import za.co.idea.ip.ws.bean.MetaDataMessage;
import za.co.idea.ip.ws.bean.ResponseMessage;
import za.co.idea.ip.ws.util.CustomObjectMapper;
import za.co.idea.web.ui.bean.MetaDataBean;
import za.co.idea.web.util.IdNumberGen;

public class MetaDataController {

	private HashMap<String, String> metaList;
	private boolean showAddPanel;
	private boolean showModPanel;
	private boolean showAddBtn;
	private List<MetaDataBean> beans;
	private MetaDataBean bean;
	private String table;
	private static final IdNumberGen COUNTER = new IdNumberGen();

	private WebClient createCustomClient(String url) {
		WebClient client = WebClient.create(url, Collections.singletonList(new JacksonJsonProvider(new CustomObjectMapper())));
		client.header("Content-Type", "application/json");
		client.header("Accept", "application/json");
		return client;
	}

	public String showMetadataMaintain() {
		this.showAddPanel = false;
		this.showModPanel = false;
		this.showAddBtn = false;
		this.table = "";
		this.beans = null;
		return "mdata";
	}

	public String showMetaData() {
		this.showAddPanel = false;
		this.showModPanel = true;
		return "mdata";
	}

	public String showMetaDataAdd() {
		bean = new MetaDataBean();
		bean.setId(COUNTER.getNextId(table).intValue());
		this.showAddBtn = false;
		this.showAddPanel = true;
		this.showModPanel = false;
		return "mdata";
	}

	public String updateMetaData() {
		this.showAddPanel = false;
		this.showModPanel = false;
		this.showAddBtn = true;
		WebClient mDataClient = createCustomClient("http://127.0.0.1:38080/ip-ws/ip/ms/modify");
		MetaDataMessage message = new MetaDataMessage();
		message.setDesc(bean.getDesc());
		message.setId(bean.getId());
		message.setTable(table);
		ResponseMessage response = mDataClient.accept(MediaType.APPLICATION_JSON).put(message, ResponseMessage.class);
		if (response.getStatusCode() == 0) {
			beans = fetchAllMetadata();
			return "mdata";
		} else {
			FacesMessage exceptionMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, response.getStatusDesc(), response.getStatusDesc());
			FacesContext.getCurrentInstance().addMessage(null, exceptionMessage);
			return "";
		}
	}

	public String addMetaData() {
		this.showAddPanel = false;
		this.showModPanel = false;
		this.showAddBtn = true;
		WebClient mDataClient = createCustomClient("http://127.0.0.1:38080/ip-ws/ip/ms/add");
		MetaDataMessage message = new MetaDataMessage();
		message.setDesc(bean.getDesc());
		message.setId(bean.getId());
		message.setTable(table);
		ResponseMessage response = mDataClient.accept(MediaType.APPLICATION_JSON).post(message, ResponseMessage.class);
		if (response.getStatusCode() == 0) {
			beans = fetchAllMetadata();
			return "mdata";
		} else {
			FacesMessage exceptionMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, response.getStatusDesc(), response.getStatusDesc());
			FacesContext.getCurrentInstance().addMessage(null, exceptionMessage);
			return "";
		}
	}

	public void changeMetaData() {
		if (table.equalsIgnoreCase("")) {
			beans = new ArrayList<MetaDataBean>();
			this.showAddPanel = false;
			this.showModPanel = false;
			this.showAddBtn = false;
		} else {
			beans = fetchAllMetadata();
			this.showAddPanel = false;
			this.showModPanel = false;
			this.showAddBtn = true;
		}
	}

	private List<MetaDataBean> fetchAllMetadata() {
		List<MetaDataBean> ret = new ArrayList<MetaDataBean>();
		WebClient mDataClient = createCustomClient("http://127.0.0.1:38080/ip-ws/ip/ms/list/" + table);
		Collection<? extends MetaDataMessage> messages = new ArrayList<MetaDataMessage>(mDataClient.accept(MediaType.APPLICATION_JSON).getCollection(MetaDataMessage.class));
		for (MetaDataMessage message : messages) {
			MetaDataBean bean = new MetaDataBean();
			bean.setDesc(message.getDesc());
			bean.setId(message.getId());
			bean.setTable(message.getTable());
			ret.add(bean);
		}
		return ret;
	}

	public HashMap<String, String> getMetaList() {
		if (metaList == null) {
			metaList = new HashMap<String, String>();
			metaList.put("Challenge Category", "ip_challenge_cat");
			metaList.put("Idea Category", "ip_idea_cat");
			metaList.put("Rewards Category", "ip_rewards_cat");
			metaList.put("Solution Category", "ip_solution_cat");
		}
		return metaList;
	}

	public void setMetaList(HashMap<String, String> metaList) {
		this.metaList = metaList;
	}

	public boolean isShowAddPanel() {
		return showAddPanel;
	}

	public void setShowAddPanel(boolean showAddPanel) {
		this.showAddPanel = showAddPanel;
	}

	public boolean isShowModPanel() {
		return showModPanel;
	}

	public void setShowModPanel(boolean showModPanel) {
		this.showModPanel = showModPanel;
	}

	public boolean isShowAddBtn() {
		return showAddBtn;
	}

	public void setShowAddBtn(boolean showAddBtn) {
		this.showAddBtn = showAddBtn;
	}

	public List<MetaDataBean> getBeans() {
		if (beans == null)
			beans = new ArrayList<MetaDataBean>();
		return beans;
	}

	public void setBeans(List<MetaDataBean> beans) {
		this.beans = beans;
	}

	public MetaDataBean getBean() {
		if (bean == null)
			bean = new MetaDataBean();
		return bean;
	}

	public void setBean(MetaDataBean bean) {
		this.bean = bean;
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

}
