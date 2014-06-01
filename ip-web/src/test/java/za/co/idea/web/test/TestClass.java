package za.co.idea.web.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;

import za.co.idea.ip.jaxws.document.Document;
import za.co.idea.ip.jaxws.document.DocumentPort;
import za.co.idea.ip.jaxws.document.DocumentService;
import za.co.idea.ip.jaxws.document.DownloadDocumentRq;
import za.co.idea.ip.jaxws.document.DownloadDocumentRs;
import za.co.idea.ip.jaxws.document.UploadDocumentRq;

public class TestClass {

	public static void main(String[] args) throws Exception {
	}

	public static void testUpload() throws FileNotFoundException, IOException {
		Document document = new Document();
		document.setContentType("image/jpeg");
		document.setEntityId("0");
		document.setEntityTableName("ip_user");
		File file = new File("/home/main/Documents/sample.jpg");
		System.out.println(file.length());
		document.setFileContent(IOUtils.toByteArray(new FileInputStream(file)));
		document.setFileName("profile.jpeg");
		DocumentService service = new DocumentService();
		DocumentPort port = service.getDocumentSOAP();
		UploadDocumentRq rq = new UploadDocumentRq();
		rq.setDocument(document);
		port.uploadDocument(rq);
	}

	public static void testDownload() throws Exception {
		DownloadDocumentRq rq = new DownloadDocumentRq();
		rq.setEntityId("0");
		rq.setEntityTableName("ip_user");
		DocumentService service = new DocumentService();
		DocumentPort port = service.getDocumentSOAP();
		DownloadDocumentRs rs = port.downloadDocument(rq);
		if (rs.getFileContent() == null || rs.getFileContent().length == 0)
			throw new Exception("Profile Image Not Avavilable");
		else {
			File file = new File(System.getProperty("user.home") + File.separator + rs.getFileName());
			if (!file.exists())
				file.createNewFile();
			FileOutputStream out = new FileOutputStream(file);
			out.write(rs.getFileContent());
			out.flush();
			out.close();
		}
	}

}
