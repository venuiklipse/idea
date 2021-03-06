package za.co.idea.ip.jaxws.document;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * This class was generated by Apache CXF 2.7.10 2014-05-31T23:19:46.187+02:00
 * Generated source version: 2.7.10
 * 
 */
@WebService(targetNamespace = "http://za.co.idea/document/", name = "documentPort")
@XmlSeeAlso({ ObjectFactory.class })
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface DocumentPort {

	@WebMethod(action = "http://za.co.idea/document/uploadDocument")
	@WebResult(name = "uploadDocumentRsMsg", targetNamespace = "http://za.co.idea/document/", partName = "param")
	public UploadDocumentRs uploadDocument(@WebParam(partName = "param", name = "uploadDocumentRqMsg", targetNamespace = "http://za.co.idea/document/") UploadDocumentRq param);

	@WebMethod(action = "http://za.co.idea/document/downloadDocument")
	@WebResult(name = "downloadDocumentRsMsg", targetNamespace = "http://za.co.idea/document/", partName = "param")
	public DownloadDocumentRs downloadDocument(@WebParam(partName = "param", name = "downloadDocumentRqMsg", targetNamespace = "http://za.co.idea/document/") DownloadDocumentRq param);

	@WebMethod(action = "http://za.co.idea/document/listDocument")
	@WebResult(name = "listDocumentRsMsg", targetNamespace = "http://za.co.idea/document/", partName = "param")
	public ListDocumentRs listDocument(@WebParam(partName = "param", name = "listDocumentRqMsg", targetNamespace = "http://za.co.idea/document/") ListDocumentRq param);
}
