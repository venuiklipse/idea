package za.co.idea.web.ui;

import java.io.Serializable;

import javax.faces.event.ValueChangeEvent;

import org.primefaces.event.CaptureEvent;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.CroppedImage;
import org.primefaces.model.StreamedContent;

public class ImageController implements Serializable {

	/**
	 * Image upload properties
	 */
	private static final long serialVersionUID = -7238289945335966417L;
	private StreamedContent image;
	private CroppedImage croppedImage;
	private String uploadSrc;
	private boolean webCamEnabled;
	private boolean uploadEnabled;
	private boolean show;

	public String cropImage() {
		return "";
	}

	public void captureImage(CaptureEvent event) {

	}

	public void uploadImageFile(FileUploadEvent event) {

	}

	public void changeImgSrc(ValueChangeEvent event) {
		System.out.println(event.getSource() + " :: " + event.getNewValue());
		if (uploadSrc.equalsIgnoreCase("srcWebCam")) {
			webCamEnabled = true;
			uploadEnabled = false;
		} else if (uploadSrc.equalsIgnoreCase("srcUpload")) {
			uploadEnabled = true;
			webCamEnabled = false;
		} else {
			uploadEnabled = false;
			webCamEnabled = false;
		}
	}

	public StreamedContent getImage() {
		return image;
	}

	public void setImage(StreamedContent image) {
		this.image = image;
	}

	public CroppedImage getCroppedImage() {
		return croppedImage;
	}

	public void setCroppedImage(CroppedImage croppedImage) {
		this.croppedImage = croppedImage;
	}

	public String getUploadSrc() {
		return uploadSrc;
	}

	public void setUploadSrc(String uploadSrc) {
		this.uploadSrc = uploadSrc;
	}

	public boolean isWebCamEnabled() {
		return webCamEnabled;
	}

	public void setWebCamEnabled(boolean webCamEnabled) {
		this.webCamEnabled = webCamEnabled;
	}

	public boolean isUploadEnabled() {
		return uploadEnabled;
	}

	public void setUploadEnabled(boolean uploadEnabled) {
		this.uploadEnabled = uploadEnabled;
	}

	public boolean isShow() {
		return show;
	}

	public void setShow(boolean show) {
		this.show = show;
	}

}
