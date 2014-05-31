package za.co.idea.web.ui;

import java.io.Serializable;

import org.primefaces.model.StreamedContent;

public class ImageController implements Serializable {

	/**
	 * Image upload properties
	 */
	private static final long serialVersionUID = -7238289945335966417L;
	private StreamedContent image;
	private String uploadSrc;
	private boolean show;

	public StreamedContent getImage() {
		return image;
	}

	public void setImage(StreamedContent image) {
		this.image = image;
	}

	public String getUploadSrc() {
		return uploadSrc;
	}

	public void setUploadSrc(String uploadSrc) {
		this.uploadSrc = uploadSrc;
	}

	public boolean isShow() {
		return show;
	}

	public void setShow(boolean show) {
		this.show = show;
	}

}
