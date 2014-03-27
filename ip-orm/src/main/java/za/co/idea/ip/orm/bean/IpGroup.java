package za.co.idea.ip.orm.bean;

import java.util.HashSet;
import java.util.Set;

/**
 * IpGroup entity. @author MyEclipse Persistence Tools
 */

public class IpGroup implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1226956195083283696L;
	private Long groupId;
	private IpUser ipUser;
	private IpGroup ipGroup;
	private String groupName;
	private String groupStatus;
	private String groupEmail;
	private Set<IpGroup> ipGroups = new HashSet<IpGroup>(0);

	// Constructors

	/** default constructor */
	public IpGroup() {
	}

	/** minimal constructor */
	public IpGroup(Long groupId, String groupName) {
		this.groupId = groupId;
		this.groupName = groupName;
	}

	/** full constructor */
	public IpGroup(Long groupId, IpUser ipUser, IpGroup ipGroup,
			String groupName, String groupStatus, String groupEmail,
			Set<IpGroup> ipGroups) {
		this.groupId = groupId;
		this.ipUser = ipUser;
		this.ipGroup = ipGroup;
		this.groupName = groupName;
		this.groupStatus = groupStatus;
		this.groupEmail = groupEmail;
		this.ipGroups = ipGroups;
	}

	// Property accessors

	public Long getGroupId() {
		return this.groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
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

	public String getGroupName() {
		return this.groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getGroupStatus() {
		return this.groupStatus;
	}

	public void setGroupStatus(String groupStatus) {
		this.groupStatus = groupStatus;
	}

	public String getGroupEmail() {
		return this.groupEmail;
	}

	public void setGroupEmail(String groupEmail) {
		this.groupEmail = groupEmail;
	}

	public Set<IpGroup> getIpGroups() {
		return this.ipGroups;
	}

	public void setIpGroups(Set<IpGroup> ipGroups) {
		this.ipGroups = ipGroups;
	}

}