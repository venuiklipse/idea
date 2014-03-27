package za.co.idea.ip.orm.bean;

import java.util.HashSet;
import java.util.Set;

/**
 * IpUser entity. @author MyEclipse Persistence Tools
 */

public class IpUser implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 6242867654257472506L;
	private Long userId;
	private String userFName;
	private String userLName;
	private String userMName;
	private String userIdNum;
	private String userScreenName;
	private String userEmail;
	private String userContact;
	private String userSkills;
	private String userBio;
	private String userFbHandle;
	private String userTwHandle;
	private String userAvatar;
	private String userStatus;
	private Set<IpIdea> ipIdeas = new HashSet<IpIdea>(0);
	private Set<IpTag> ipTags = new HashSet<IpTag>(0);
	private Set<IpGroup> ipGroups = new HashSet<IpGroup>(0);

	// Constructors

	/** default constructor */
	public IpUser() {
	}

	/** minimal constructor */
	public IpUser(Long userId, String userFName, String userLName,
			String userIdNum, String userScreenName, String userEmail,
			String userSkills, String userBio, String userFbHandle,
			String userTwHandle, String userAvatar, String userStatus) {
		this.userId = userId;
		this.userFName = userFName;
		this.userLName = userLName;
		this.userIdNum = userIdNum;
		this.userScreenName = userScreenName;
		this.userEmail = userEmail;
		this.userSkills = userSkills;
		this.userBio = userBio;
		this.userFbHandle = userFbHandle;
		this.userTwHandle = userTwHandle;
		this.userAvatar = userAvatar;
		this.userStatus = userStatus;
	}

	/** full constructor */
	public IpUser(Long userId, String userFName, String userLName,
			String userMName, String userIdNum, String userScreenName,
			String userEmail, String userContact, String userSkills,
			String userBio, String userFbHandle, String userTwHandle,
			String userAvatar, String userStatus, Set<IpIdea> ipIdeas,
			Set<IpTag> ipTags, Set<IpGroup> ipGroups) {
		this.userId = userId;
		this.userFName = userFName;
		this.userLName = userLName;
		this.userMName = userMName;
		this.userIdNum = userIdNum;
		this.userScreenName = userScreenName;
		this.userEmail = userEmail;
		this.userContact = userContact;
		this.userSkills = userSkills;
		this.userBio = userBio;
		this.userFbHandle = userFbHandle;
		this.userTwHandle = userTwHandle;
		this.userAvatar = userAvatar;
		this.userStatus = userStatus;
		this.ipIdeas = ipIdeas;
		this.ipTags = ipTags;
		this.ipGroups = ipGroups;
	}

	// Property accessors

	public Long getUserId() {
		return this.userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserFName() {
		return this.userFName;
	}

	public void setUserFName(String userFName) {
		this.userFName = userFName;
	}

	public String getUserLName() {
		return this.userLName;
	}

	public void setUserLName(String userLName) {
		this.userLName = userLName;
	}

	public String getUserMName() {
		return this.userMName;
	}

	public void setUserMName(String userMName) {
		this.userMName = userMName;
	}

	public String getUserIdNum() {
		return this.userIdNum;
	}

	public void setUserIdNum(String userIdNum) {
		this.userIdNum = userIdNum;
	}

	public String getUserScreenName() {
		return this.userScreenName;
	}

	public void setUserScreenName(String userScreenName) {
		this.userScreenName = userScreenName;
	}

	public String getUserEmail() {
		return this.userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getUserContact() {
		return this.userContact;
	}

	public void setUserContact(String userContact) {
		this.userContact = userContact;
	}

	public String getUserSkills() {
		return this.userSkills;
	}

	public void setUserSkills(String userSkills) {
		this.userSkills = userSkills;
	}

	public String getUserBio() {
		return this.userBio;
	}

	public void setUserBio(String userBio) {
		this.userBio = userBio;
	}

	public String getUserFbHandle() {
		return this.userFbHandle;
	}

	public void setUserFbHandle(String userFbHandle) {
		this.userFbHandle = userFbHandle;
	}

	public String getUserTwHandle() {
		return this.userTwHandle;
	}

	public void setUserTwHandle(String userTwHandle) {
		this.userTwHandle = userTwHandle;
	}

	public String getUserAvatar() {
		return this.userAvatar;
	}

	public void setUserAvatar(String userAvatar) {
		this.userAvatar = userAvatar;
	}

	public String getUserStatus() {
		return this.userStatus;
	}

	public void setUserStatus(String userStatus) {
		this.userStatus = userStatus;
	}

	public Set<IpIdea> getIpIdeas() {
		return this.ipIdeas;
	}

	public void setIpIdeas(Set<IpIdea> ipIdeas) {
		this.ipIdeas = ipIdeas;
	}

	public Set<IpTag> getIpTags() {
		return this.ipTags;
	}

	public void setIpTags(Set<IpTag> ipTags) {
		this.ipTags = ipTags;
	}

	public Set<IpGroup> getIpGroups() {
		return this.ipGroups;
	}

	public void setIpGroups(Set<IpGroup> ipGroups) {
		this.ipGroups = ipGroups;
	}

}