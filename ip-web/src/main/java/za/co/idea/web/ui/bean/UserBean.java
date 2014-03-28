package za.co.idea.web.ui.bean;

import java.io.Serializable;

public class UserBean implements Serializable {

	private static final long serialVersionUID = -6230544866147907794L;
	private Long uId;
	private String fName;
	private String mName;
	private String lName;
	private Long idNum;
	private String eMail;
	private String contact;
	private String skills;
	private String bio;
	private String fbHandle;
	private String twHandle;
	private String scName;
	private String pwd;
	private String cPw;
	private Boolean isActive;

	public Long getuId() {
		return uId;
	}

	public void setuId(Long uId) {
		this.uId = uId;
	}

	public String getfName() {
		return fName;
	}

	public void setfName(String fName) {
		this.fName = fName;
	}

	public String getmName() {
		return mName;
	}

	public void setmName(String mName) {
		this.mName = mName;
	}

	public String getlName() {
		return lName;
	}

	public void setlName(String lName) {
		this.lName = lName;
	}

	public Long getIdNum() {
		return idNum;
	}

	public void setIdNum(Long idNum) {
		this.idNum = idNum;
	}

	public String geteMail() {
		return eMail;
	}

	public void seteMail(String eMail) {
		this.eMail = eMail;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getSkills() {
		return skills;
	}

	public void setSkills(String skills) {
		this.skills = skills;
	}

	public String getBio() {
		return bio;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}

	public String getFbHandle() {
		return fbHandle;
	}

	public void setFbHandle(String fbHandle) {
		this.fbHandle = fbHandle;
	}

	public String getTwHandle() {
		return twHandle;
	}

	public void setTwHandle(String twHandle) {
		this.twHandle = twHandle;
	}

	public String getScName() {
		return scName;
	}

	public void setScName(String scName) {
		this.scName = scName;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getcPw() {
		return cPw;
	}

	public void setcPw(String cPw) {
		this.cPw = cPw;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

}