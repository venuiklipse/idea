package za.co.idea.ip.orm.bean;

import java.util.HashSet;
import java.util.Set;

/**
 * IpChallenge entity. @author MyEclipse Persistence Tools
 */

@SuppressWarnings("rawtypes")
public class IpChallenge implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long chalId;
	private IpChallengeStatus ipChallengeStatus;
	private IpUser ipUser;
	private Set ipSolutions = new HashSet(0);

	// Constructors

	/** default constructor */
	public IpChallenge() {
	}

	/** minimal constructor */
	public IpChallenge(Long chalId) {
		this.chalId = chalId;
	}

	/** full constructor */
	public IpChallenge(Long chalId, IpChallengeStatus ipChallengeStatus, IpUser ipUser, Set ipSolutions) {
		this.chalId = chalId;
		this.ipChallengeStatus = ipChallengeStatus;
		this.ipUser = ipUser;
		this.ipSolutions = ipSolutions;
	}

	// Property accessors

	public Long getChalId() {
		return this.chalId;
	}

	public void setChalId(Long chalId) {
		this.chalId = chalId;
	}

	public IpChallengeStatus getIpChallengeStatus() {
		return this.ipChallengeStatus;
	}

	public void setIpChallengeStatus(IpChallengeStatus ipChallengeStatus) {
		this.ipChallengeStatus = ipChallengeStatus;
	}

	public IpUser getIpUser() {
		return this.ipUser;
	}

	public void setIpUser(IpUser ipUser) {
		this.ipUser = ipUser;
	}

	public Set getIpSolutions() {
		return this.ipSolutions;
	}

	public void setIpSolutions(Set ipSolutions) {
		this.ipSolutions = ipSolutions;
	}

}