package za.co.idea.ip.ws.bean;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement(name="functionMessage")
public class FunctionMessage {
	private Long funcId;
	private String funcName;
	private List<UserMessage> userList;
	private List<GroupMessage> groupList;
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

	public List<UserMessage> getUserList() {
		if (userList == null)
			userList = new ArrayList<UserMessage>();
		return userList;
	}

	public void setUserList(List<UserMessage> userList) {
		this.userList = userList;
	}

	public List<GroupMessage> getGroupList() {
		if (groupList == null)
			groupList = new ArrayList<GroupMessage>();
		return groupList;
	}

	public void setGroupList(List<GroupMessage> groupList) {
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
