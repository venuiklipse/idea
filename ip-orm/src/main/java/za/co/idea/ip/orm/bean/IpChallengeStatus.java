package za.co.idea.ip.orm.bean;

/**
 * IpChallengeStatus entity. @author MyEclipse Persistence Tools
 */

public class IpChallengeStatus implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -1143589684334274074L;
	private Integer csId;
	private String csDesc;

	// Constructors

	/** default constructor */
	public IpChallengeStatus() {
	}

	/** full constructor */
	public IpChallengeStatus(Integer csId, String csDesc) {
		this.csId = csId;
		this.csDesc = csDesc;
	}

	// Property accessors

	public Integer getCsId() {
		return this.csId;
	}

	public void setCsId(Integer csId) {
		this.csId = csId;
	}

	public String getCsDesc() {
		return this.csDesc;
	}

	public void setCsDesc(String csDesc) {
		this.csDesc = csDesc;
	}

}