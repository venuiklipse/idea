package za.co.idea.ip.orm.bean;

/**
 * IpClaim entity. @author MyEclipse Persistence Tools
 */

public class IpClaim implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long claimId;
	private IpUser ipUser;
	private IpClaimStatus ipClaimStatus;
	private IpRewards ipRewards;

	// Constructors

	/** default constructor */
	public IpClaim() {
	}

	/** minimal constructor */
	public IpClaim(Long claimId) {
		this.claimId = claimId;
	}

	/** full constructor */
	public IpClaim(Long claimId, IpUser ipUser, IpClaimStatus ipClaimStatus, IpRewards ipRewards) {
		this.claimId = claimId;
		this.ipUser = ipUser;
		this.ipClaimStatus = ipClaimStatus;
		this.ipRewards = ipRewards;
	}

	// Property accessors

	public Long getClaimId() {
		return this.claimId;
	}

	public void setClaimId(Long claimId) {
		this.claimId = claimId;
	}

	public IpUser getIpUser() {
		return this.ipUser;
	}

	public void setIpUser(IpUser ipUser) {
		this.ipUser = ipUser;
	}

	public IpClaimStatus getIpClaimStatus() {
		return this.ipClaimStatus;
	}

	public void setIpClaimStatus(IpClaimStatus ipClaimStatus) {
		this.ipClaimStatus = ipClaimStatus;
	}

	public IpRewards getIpRewards() {
		return this.ipRewards;
	}

	public void setIpRewards(IpRewards ipRewards) {
		this.ipRewards = ipRewards;
	}

}