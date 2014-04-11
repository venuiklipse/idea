package za.co.idea.ip.orm.bean;

import java.sql.Blob;
import java.util.Date;

/**
 * IpSolution entity. @author MyEclipse Persistence Tools
 */

public class IpSolution implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -3744868475222553664L;
	private Long solId;
	private IpSolutionStatus ipSolutionStatus;
	private IpSolutionCat ipSolutionCat;
	private IpUser ipUser;
	private IpChallenge ipChallenge;
	private String solTitle;
	private String solDesc;
	private String solTags;
	private Blob solBlob;
	private Date solCrtdDt;
	private String solFileName;
	private String solFileType;

	// Constructors

	/** default constructor */
	public IpSolution() {
	}

	/** minimal constructor */
	public IpSolution(Long solId, IpSolutionStatus ipSolutionStatus, IpUser ipUser, IpChallenge ipChallenge, String solTitle, String solDesc, Date solCrtdDt, String solFileName, String solFileType) {
		this.solId = solId;
		this.ipSolutionStatus = ipSolutionStatus;
		this.ipUser = ipUser;
		this.ipChallenge = ipChallenge;
		this.solTitle = solTitle;
		this.solDesc = solDesc;
		this.solCrtdDt = solCrtdDt;
		this.solFileName = solFileName;
		this.solFileType = solFileType;
	}

	/** full constructor */
	public IpSolution(Long solId, IpSolutionStatus ipSolutionStatus, IpSolutionCat ipSolutionCat, IpUser ipUser, IpChallenge ipChallenge, String solTitle, String solDesc, String solTags, Blob solBlob, Date solCrtdDt, String solFileName, String solFileType) {
		this.solId = solId;
		this.ipSolutionStatus = ipSolutionStatus;
		this.ipSolutionCat = ipSolutionCat;
		this.ipUser = ipUser;
		this.ipChallenge = ipChallenge;
		this.solTitle = solTitle;
		this.solDesc = solDesc;
		this.solTags = solTags;
		this.solBlob = solBlob;
		this.solCrtdDt = solCrtdDt;
		this.solFileName = solFileName;
		this.solFileType = solFileType;
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

	public IpSolutionCat getIpSolutionCat() {
		return this.ipSolutionCat;
	}

	public void setIpSolutionCat(IpSolutionCat ipSolutionCat) {
		this.ipSolutionCat = ipSolutionCat;
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

	public String getSolTitle() {
		return this.solTitle;
	}

	public void setSolTitle(String solTitle) {
		this.solTitle = solTitle;
	}

	public String getSolDesc() {
		return this.solDesc;
	}

	public void setSolDesc(String solDesc) {
		this.solDesc = solDesc;
	}

	public String getSolTags() {
		return this.solTags;
	}

	public void setSolTags(String solTags) {
		this.solTags = solTags;
	}

	public Blob getSolBlob() {
		return this.solBlob;
	}

	public void setSolBlob(Blob solBlob) {
		this.solBlob = solBlob;
	}

	public Date getSolCrtdDt() {
		return this.solCrtdDt;
	}

	public void setSolCrtdDt(Date solCrtdDt) {
		this.solCrtdDt = solCrtdDt;
	}

	public String getSolFileName() {
		return solFileName;
	}

	public void setSolFileName(String solFileName) {
		this.solFileName = solFileName;
	}

	public String getSolFileType() {
		return solFileType;
	}

	public void setSolFileType(String solFileType) {
		this.solFileType = solFileType;
	}

}