package za.co.idea.ip.orm.bean;

/**
 * IpPoints entity. @author MyEclipse Persistence Tools
 */

public class IpPoints implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 6456518924772050061L;
	private Long pointId;
	private IpAllocation ipAllocation;
	private IpUser ipUser;

	// Constructors

	/** default constructor */
	public IpPoints() {
	}

	/** minimal constructor */
	public IpPoints(Long pointId) {
		this.pointId = pointId;
	}

	/** full constructor */
	public IpPoints(Long pointId, IpAllocation ipAllocation, IpUser ipUser) {
		this.pointId = pointId;
		this.ipAllocation = ipAllocation;
		this.ipUser = ipUser;
	}

	// Property accessors

	public Long getPointId() {
		return this.pointId;
	}

	public void setPointId(Long pointId) {
		this.pointId = pointId;
	}

	public IpAllocation getIpAllocation() {
		return this.ipAllocation;
	}

	public void setIpAllocation(IpAllocation ipAllocation) {
		this.ipAllocation = ipAllocation;
	}

	public IpUser getIpUser() {
		return this.ipUser;
	}

	public void setIpUser(IpUser ipUser) {
		this.ipUser = ipUser;
	}

}