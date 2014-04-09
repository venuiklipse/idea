package za.co.idea.ip.orm.bean;

/**
 * IpFunctionConfig entity. @author MyEclipse Persistence Tools
 */

public class IpFunctionConfig implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -5433234680552306935L;
	private Long fcId;
	private IpGroup ipGroup;
	private IpFunction ipFunction;
	private IpUser ipUser;

	// Constructors

	/** default constructor */
	public IpFunctionConfig() {
	}

	/** minimal constructor */
	public IpFunctionConfig(Long fcId) {
		this.fcId = fcId;
	}

	/** full constructor */
	public IpFunctionConfig(Long fcId, IpGroup ipGroup, IpFunction ipFunction, IpUser ipUser) {
		this.fcId = fcId;
		this.ipGroup = ipGroup;
		this.ipFunction = ipFunction;
		this.ipUser = ipUser;
	}

	// Property accessors

	public Long getFcId() {
		return this.fcId;
	}

	public void setFcId(Long fcId) {
		this.fcId = fcId;
	}

	public IpGroup getIpGroup() {
		return this.ipGroup;
	}

	public void setIpGroup(IpGroup ipGroup) {
		this.ipGroup = ipGroup;
	}

	public IpFunction getIpFunction() {
		return this.ipFunction;
	}

	public void setIpFunction(IpFunction ipFunction) {
		this.ipFunction = ipFunction;
	}

	public IpUser getIpUser() {
		return this.ipUser;
	}

	public void setIpUser(IpUser ipUser) {
		this.ipUser = ipUser;
	}

}