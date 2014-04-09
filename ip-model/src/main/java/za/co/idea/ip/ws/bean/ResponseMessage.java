package za.co.idea.ip.ws.bean;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "responseMessage")
public class ResponseMessage {
	private Integer statusCode;
	private String statusDesc;

	public ResponseMessage() {
	}

	public ResponseMessage(Integer statusCode, String statusDesc) {
		this.statusCode = statusCode;
		this.statusDesc = statusDesc;
	}

	public Integer getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}

	public String getStatusDesc() {
		return statusDesc;
	}

	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}

	public static ResponseMessage createSuccess() {
		return new ResponseMessage(0, "Success");
	}

	public static ResponseMessage createException(Exception e) {
		return new ResponseMessage(1, e.getMessage());
	}

	public static ResponseMessage createMessage(String message) {
		return new ResponseMessage(2, message);
	}

}
