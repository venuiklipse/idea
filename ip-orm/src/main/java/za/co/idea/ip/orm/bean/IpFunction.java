package za.co.idea.ip.orm.bean;

import java.util.HashSet;
import java.util.Set;

/**
 * IpFunction entity. @author MyEclipse Persistence Tools
 */

@SuppressWarnings("rawtypes")
public class IpFunction implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -2202065199665268149L;
	private Long funcId;
	private String funcName;
	private Set ipFunctionConfigs = new HashSet(0);

	// Constructors

	/** default constructor */
	public IpFunction() {
	}

	/** minimal constructor */
	public IpFunction(Long funcId, String funcName) {
		this.funcId = funcId;
		this.funcName = funcName;
	}

	/** full constructor */
	public IpFunction(Long funcId, String funcName, Set ipFunctionConfigs) {
		this.funcId = funcId;
		this.funcName = funcName;
		this.ipFunctionConfigs = ipFunctionConfigs;
	}

	// Property accessors

	public Long getFuncId() {
		return this.funcId;
	}

	public void setFuncId(Long funcId) {
		this.funcId = funcId;
	}

	public String getFuncName() {
		return this.funcName;
	}

	public void setFuncName(String funcName) {
		this.funcName = funcName;
	}

	public Set getIpFunctionConfigs() {
		return this.ipFunctionConfigs;
	}

	public void setIpFunctionConfigs(Set ipFunctionConfigs) {
		this.ipFunctionConfigs = ipFunctionConfigs;
	}

}