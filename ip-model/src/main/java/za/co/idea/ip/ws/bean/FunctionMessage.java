package za.co.idea.ip.ws.bean;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "functionMessage")
public class FunctionMessage {
	private Long funcId;
	private String funcName;
	private Long[] userIdList;
	private Long[] groupIdList;

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

	public Long[] getUserIdList() {
		if (userIdList == null)
			userIdList = new Long[] {};
		return userIdList;
	}

	public void setUserIdList(Long[] userIdList) {
		this.userIdList = userIdList;
	}

	public Long[] getGroupIdList() {
		if (groupIdList == null)
			groupIdList = new Long[] {};
		return groupIdList;
	}

	public void setGroupIdList(Long[] groupIdList) {
		this.groupIdList = groupIdList;
	}

}
