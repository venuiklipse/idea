package za.co.idea.ip.orm.bean;

/**
 * IpFunction entity. @author MyEclipse Persistence Tools
 */

public class IpFunction implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 4188336242416759204L;
	private Integer funcId;
	private IpUser ipUser;
	private IpGroup ipGroup;
	private String funcName;

	// Constructors

	/** default constructor */
	public IpFunction() {
	}

	/** minimal constructor */
	public IpFunction(Integer funcId, String funcName) {
		this.funcId = funcId;
		this.funcName = funcName;
	}

	/** full constructor */
	public IpFunction(Integer funcId, IpUser ipUser, IpGroup ipGroup, String funcName) {
		this.funcId = funcId;
		this.ipUser = ipUser;
		this.ipGroup = ipGroup;
		this.funcName = funcName;
	}

	// Property accessors

	public Integer getFuncId() {
		return this.funcId;
	}

	public void setFuncId(Integer funcId) {
		this.funcId = funcId;
	}

	public IpUser getIpUser() {
		return this.ipUser;
	}

	public void setIpUser(IpUser ipUser) {
		this.ipUser = ipUser;
	}

	public IpGroup getIpGroup() {
		return this.ipGroup;
	}

	public void setIpGroup(IpGroup ipGroup) {
		this.ipGroup = ipGroup;
	}

	public String getFuncName() {
		return this.funcName;
	}

	public void setFuncName(String funcName) {
		this.funcName = funcName;
	}

}