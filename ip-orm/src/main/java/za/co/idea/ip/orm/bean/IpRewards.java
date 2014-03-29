package za.co.idea.ip.orm.bean;

import java.util.HashSet;
import java.util.Set;

/**
 * IpRewards entity. @author MyEclipse Persistence Tools
 */

@SuppressWarnings("rawtypes")
public class IpRewards implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer rwId;
	private IpRewardsStatus ipRewardsStatus;
	private Set ipClaims = new HashSet(0);

	// Constructors

	/** default constructor */
	public IpRewards() {
	}

	/** minimal constructor */
	public IpRewards(Integer rwId) {
		this.rwId = rwId;
	}

	/** full constructor */
	public IpRewards(Integer rwId, IpRewardsStatus ipRewardsStatus, Set ipClaims) {
		this.rwId = rwId;
		this.ipRewardsStatus = ipRewardsStatus;
		this.ipClaims = ipClaims;
	}

	// Property accessors

	public Integer getRwId() {
		return this.rwId;
	}

	public void setRwId(Integer rwId) {
		this.rwId = rwId;
	}

	public IpRewardsStatus getIpRewardsStatus() {
		return this.ipRewardsStatus;
	}

	public void setIpRewardsStatus(IpRewardsStatus ipRewardsStatus) {
		this.ipRewardsStatus = ipRewardsStatus;
	}

	public Set getIpClaims() {
		return this.ipClaims;
	}

	public void setIpClaims(Set ipClaims) {
		this.ipClaims = ipClaims;
	}

}