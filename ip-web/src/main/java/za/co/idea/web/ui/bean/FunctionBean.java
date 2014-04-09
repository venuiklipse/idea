package za.co.idea.web.ui.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FunctionBean implements Serializable {

	private static final long serialVersionUID = -5754104006986083662L;
	private Long funcId;
	private String funcName;
	private List<UserBean> userList;
	private List<GroupBean> groupList;
	private List<Long> userIdList;
	private List<Long> groupIdList;

	public Long getFuncId() {
		return funcId;
	}

	public void setFuncId(Long funcId) {
		this.funcId = funcId;
	}

	public String getFuncName() {
		return funcName;
	}

	public void setFuncName(String funcName) {
		this.funcName = funcName;
	}

	public List<UserBean> getUserList() {
		if (userList == null)
			userList = new ArrayList<UserBean>();
		return userList;
	}

	public void setUserList(List<UserBean> userList) {
		this.userList = userList;
	}

	public List<GroupBean> getGroupList() {
		if (groupList == null)
			groupList = new ArrayList<GroupBean>();
		return groupList;
	}

	public void setGroupList(List<GroupBean> groupList) {
		this.groupList = groupList;
	}

	public List<Long> getUserIdList() {
		return userIdList;
	}

	public void setUserIdList(List<Long> userIdList) {
		this.userIdList = userIdList;
	}

	public List<Long> getGroupIdList() {
		return groupIdList;
	}

	public void setGroupIdList(List<Long> groupIdList) {
		this.groupIdList = groupIdList;
	}

}
