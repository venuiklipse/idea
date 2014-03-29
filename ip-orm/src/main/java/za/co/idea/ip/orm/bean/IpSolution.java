package za.co.idea.ip.orm.bean;

/**
 * IpSolution entity. @author MyEclipse Persistence Tools
 */

public class IpSolution implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long solId;
	private IpSolutionStatus ipSolutionStatus;
	private IpUser ipUser;
	private IpChallenge ipChallenge;

	// Constructors

	/** default constructor */
	public IpSolution() {
	}

	/** minimal constructor */
	public IpSolution(Long solId) {
		this.solId = solId;
	}

	/** full constructor */
	public IpSolution(Long solId, IpSolutionStatus ipSolutionStatus, IpUser ipUser, IpChallenge ipChallenge) {
		this.solId = solId;
		this.ipSolutionStatus = ipSolutionStatus;
		this.ipUser = ipUser;
		this.ipChallenge = ipChallenge;
	}

	// Property accessors

	public Long getSolId() {
		return this.solId;
	}

	public void setSolId(Long solId) {
		this.solId = solId;
	}

	public IpSolutionStatus getIpSolutionStatus() {
		return this.ipSolutionStatus;
	}

	public void setIpSolutionStatus(IpSolutionStatus ipSolutionStatus) {
		this.ipSolutionStatus = ipSolutionStatus;
	}

	public IpUser getIpUser() {
		return this.ipUser;
	}

	public void setIpUser(IpUser ipUser) {
		this.ipUser = ipUser;
	}

	public IpChallenge getIpChallenge() {
		return this.ipChallenge;
	}

	public void setIpChallenge(IpChallenge ipChallenge) {
		this.ipChallenge = ipChallenge;
	}

}