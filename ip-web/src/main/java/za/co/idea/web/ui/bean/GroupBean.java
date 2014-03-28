package za.co.idea.web.ui.bean;

import java.io.Serializable;

public class GroupBean implements Serializable {

	private static final long serialVersionUID = 7914098858832070407L;
	private Long gId;
	private String gName;
	private String geMail;
	private GroupBean selPGrp;
	private UserBean selAdmUser;
	private Boolean isActive;

	public Long getgId() {
		return gId;
	}

	public void setgId(Long gId) {
		this.gId = gId;
	}

	public String getgName() {
		return gName;
	}

	public void setgName(String gName) {
		this.gName = gName;
	}

	public String getGeMail() {
		return geMail;
	}

	public void setGeMail(String geMail) {
		this.geMail = geMail;
	}

	public GroupBean getSelPGrp() {
		return selPGrp;
	}

	public void setSelPGrp(GroupBean selPGrp) {
		this.selPGrp = selPGrp;
	}

	public UserBean getSelAdmUser() {
		return selAdmUser;
	}

	public void setSelAdmUser(UserBean selAdmUser) {
		this.selAdmUser = selAdmUser;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

}
