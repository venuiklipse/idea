package za.co.idea.ip.orm.bean;

import java.sql.Timestamp;

/**
 * IpLogin entity. @author MyEclipse Persistence Tools
 */

public class IpLogin implements java.io.Serializable {

	// Fields

	private static final long serialVersionUID = -1321698455449836969L;
	private Long loginId;
	private IpUser ipUser;
	private String loginName;
	private String loginPwd;
	private Timestamp loginLastDt;

	// Constructors

	/** default constructor */
	public IpLogin() {
	}

	/** full constructor */
	public IpLogin(Long loginId, IpUser ipUser, String loginName,
			String loginPwd, Timestamp loginLastDt) {
		this.loginId = loginId;
		this.ipUser = ipUser;
		this.loginName = loginName;
		this.loginPwd = loginPwd;
		this.loginLastDt = loginLastDt;
	}

	// Property accessors

	public Long getLoginId() {
		return this.loginId;
	}

	public void setLoginId(Long loginId) {
		this.loginId = loginId;
	}

	public IpUser getIpUser() {
		return this.ipUser;
	}

	public void setIpUser(IpUser ipUser) {
		this.ipUser = ipUser;
	}

	public String getLoginName() {
		return this.loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getLoginPwd() {
		return this.loginPwd;
	}

	public void setLoginPwd(String loginPwd) {
		this.loginPwd = loginPwd;
	}

	public Timestamp getLoginLastDt() {
		return loginLastDt;
	}

	public void setLoginLastDt(Timestamp loginLastDt) {
		this.loginLastDt = loginLastDt;
	}

}