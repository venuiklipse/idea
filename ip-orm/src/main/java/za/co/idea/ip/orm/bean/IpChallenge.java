package za.co.idea.ip.orm.bean;

/**
 * IpChallenge entity. @author MyEclipse Persistence Tools
 */

public class IpChallenge implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 2990277896845949077L;
	private Long chalId;

	// Constructors

	/** default constructor */
	public IpChallenge() {
	}

	/** full constructor */
	public IpChallenge(Long chalId) {
		this.chalId = chalId;
	}

	// Property accessors

	public Long getChalId() {
		return this.chalId;
	}

	public void setChalId(Long chalId) {
		this.chalId = chalId;
	}

}