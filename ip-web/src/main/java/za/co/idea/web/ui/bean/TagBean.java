package za.co.idea.web.ui.bean;

import java.io.Serializable;

public class TagBean implements Serializable {
	private static final long serialVersionUID = 3679231894527770044L;
	private String usrScreenName;
	private String userFullName;
	private Integer teId;
	private Integer ttId;
	private Integer tagText;
	private Long entityId;

	public String getUsrScreenName() {
		return usrScreenName;
	}

	public void setUsrScreenName(String usrScreenName) {
		this.usrScreenName = usrScreenName;
	}

	public String getUserFullName() {
		return userFullName;
	}

	public void setUserFullName(String userFullName) {
		this.userFullName = userFullName;
	}

	public Integer getTeId() {
		return teId;
	}

	public void setTeId(Integer teId) {
		this.teId = teId;
	}

	public Integer getTtId() {
		return ttId;
	}

	public void setTtId(Integer ttId) {
		this.ttId = ttId;
	}

	public Integer getTagText() {
		return tagText;
	}

	public void setTagText(Integer tagText) {
		this.tagText = tagText;
	}

	public Long getEntityId() {
		return entityId;
	}

	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}
}
