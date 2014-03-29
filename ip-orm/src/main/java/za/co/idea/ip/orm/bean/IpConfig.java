package za.co.idea.ip.orm.bean;

import java.sql.Timestamp;

/**
 * IpConfig entity. @author MyEclipse Persistence Tools
 */

public class IpConfig implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer configId;
	private String configKey;
	private String configValue;
	private String configEnv;
	private Timestamp createdDate;
	private String createdBy;

	// Constructors

	/** default constructor */
	public IpConfig() {
	}

	/** full constructor */
	public IpConfig(String configKey, String configValue, String configEnv, Timestamp createdDate, String createdBy) {
		this.configKey = configKey;
		this.configValue = configValue;
		this.configEnv = configEnv;
		this.createdDate = createdDate;
		this.createdBy = createdBy;
	}

	// Property accessors

	public Integer getConfigId() {
		return this.configId;
	}

	public void setConfigId(Integer configId) {
		this.configId = configId;
	}

	public String getConfigKey() {
		return this.configKey;
	}

	public void setConfigKey(String configKey) {
		this.configKey = configKey;
	}

	public String getConfigValue() {
		return this.configValue;
	}

	public void setConfigValue(String configValue) {
		this.configValue = configValue;
	}

	public String getConfigEnv() {
		return this.configEnv;
	}

	public void setConfigEnv(String configEnv) {
		this.configEnv = configEnv;
	}

	public Timestamp getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

}