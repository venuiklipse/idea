package za.co.idea.ip.ws.bean;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ideaMessage")
public class IdeaMessage {

	private Long ideaId;
	private String ideaTitle;
	private String ideaDesc;
	private Long selCatId;
	private Long setStatusId;
	private String ideaBa;
	private String ideaTag;
	private String fileUpload;
	private String contentType;
	private Long crtdById;
	private Date crtdDate;
	private String fileName;

	public Long getIdeaId() {
		return ideaId;
	}

	public void setIdeaId(Long ideaId) {
		this.ideaId = ideaId;
	}

	public String getIdeaTitle() {
		return ideaTitle;
	}

	public void setIdeaTitle(String ideaTitle) {
		this.ideaTitle = ideaTitle;
	}

	public String getIdeaDesc() {
		return ideaDesc;
	}

	public void setIdeaDesc(String ideaDesc) {
		this.ideaDesc = ideaDesc;
	}

	public Long getSelCatId() {
		return selCatId;
	}

	public void setSelCatId(Long selCatId) {
		this.selCatId = selCatId;
	}

	public Long getSetStatusId() {
		return setStatusId;
	}

	public void setSetStatusId(Long setStatusId) {
		this.setStatusId = setStatusId;
	}

	public String getIdeaBa() {
		return ideaBa;
	}

	public void setIdeaBa(String ideaBa) {
		this.ideaBa = ideaBa;
	}

	public String getIdeaTag() {
		return ideaTag;
	}

	public void setIdeaTag(String ideaTag) {
		this.ideaTag = ideaTag;
	}

	public String getFileUpload() {
		return fileUpload;
	}

	public void setFileUpload(String fileUpload) {
		this.fileUpload = fileUpload;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public Long getCrtdById() {
		return crtdById;
	}

	public void setCrtdById(Long crtdById) {
		this.crtdById = crtdById;
	}

	public Date getCrtdDate() {
		return crtdDate;
	}

	public void setCrtdDate(Date crtdDate) {
		this.crtdDate = crtdDate;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

}
