package za.co.idea.web.ui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.client.WebClient;

import za.co.idea.web.ui.bean.GroupBean;
import za.co.idea.web.ui.bean.UserBean;

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

	public String saveUser() {
		za.co.idea.ip.ws.bean.UserBean bean = new za.co.idea.ip.ws.bean.UserBean();
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
		bean.setuId(userBean.getuId());
		WebClient client = WebClient.create("");
		Response response = client.path("http://127.0.0.1:8080/ip-ws/").accept("as/user/add").post(bean);
		if (response.getStatus() == Response.Status.OK.getStatusCode())
			return "home";
		else {
			return "";
		}
	}

	public String updateUser() {
		return "home";
	}

	public String saveGroup() {
		return "home";
	}

	public String updateGroup() {
		return "home";
	}

	public UserBean getUserBean() {
		return userBean;
	}

	public void setUserBean(UserBean userBean) {
		this.userBean = userBean;
	}

	public GroupBean getGroupBean() {
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
