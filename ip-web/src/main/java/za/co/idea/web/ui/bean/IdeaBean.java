package za.co.idea.web.ui.bean;

import java.io.InputStream;
import java.io.Serializable;

import org.apache.commons.io.IOUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

public class IdeaBean implements Serializable {

	private static final long serialVersionUID = -8721231413352705770L;

	private Long ideaId;
	private String ideaTitle;
	private String ideaDesc;
	private ListSelectorBean ideaCat;
	private ListSelectorBean ideaStatus;
	private String ideaBa;
	private String ideaTag;
	private String fileUpload;
	private String contentType;
	private FileDownloadController fileDownloadController;
	private UserBean crtdBy;

	public void fileUploadHandle(FileUploadEvent e) {
		UploadedFile file = e.getFile();
		fileUpload = new String(file.getContents());
		contentType = file.getContentType();
	}

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

	public ListSelectorBean getIdeaCat() {
		return ideaCat;
	}

	public void setIdeaCat(ListSelectorBean ideaCat) {
		this.ideaCat = ideaCat;
	}

	public ListSelectorBean getIdeaStatus() {
		return ideaStatus;
	}

	public void setIdeaStatus(ListSelectorBean ideaStatus) {
		this.ideaStatus = ideaStatus;
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

	public UserBean getCrtdBy() {
		return crtdBy;
	}

	public void setCrtdBy(UserBean crtdBy) {
		this.crtdBy = crtdBy;
	}

	public FileDownloadController getFileDownloadController() {
		return fileDownloadController;
	}

	public void setFileDownloadController(FileDownloadController fileDownloadController) {
		this.fileDownloadController = fileDownloadController;
	}

	protected class FileDownloadController {
		private StreamedContent file;

		public FileDownloadController() {
			InputStream stream = IOUtils.toInputStream(fileUpload);
			file = new DefaultStreamedContent(stream, contentType, "downloaded_optimus.jpg");
		}

		public StreamedContent getFile() {
			return file;
		}
	}
}
