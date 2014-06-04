package za.co.idea.web.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import javax.xml.ws.soap.MTOMFeature;

import za.co.idea.ip.jaxws.document.Document;
import za.co.idea.ip.jaxws.document.DocumentPort;
import za.co.idea.ip.jaxws.document.DocumentService;
import za.co.idea.ip.jaxws.document.DownloadDocumentRq;
import za.co.idea.ip.jaxws.document.DownloadDocumentRs;
import za.co.idea.ip.jaxws.document.UploadDocumentRq;

public class TestClass {

	public static void main(String[] args) throws Exception {
		testUpload();
		testDownload();
	}

	public static void testUpload() throws FileNotFoundException, IOException {
		Document document = new Document();
		document.setContentType("image/png");
		document.setEntityId("2");
		document.setEntityTableName("ip_user");
		File file = new File("/home/main/Documents/sample.png");
		FileInputStream stream = new FileInputStream(file);
		InputStreamReader r = new InputStreamReader(stream);
		System.out.println(r.getEncoding());
		byte[] b = new byte[1024];
		String s = new String();
		while (stream.read(b) > 0)
			s += new String(b);
		stream.close();
		document.setFileContent(s.getBytes());
		document.setFileName("sample.png");
		DocumentService service = new DocumentService();
		DocumentPort port = service.getDocumentSOAP(new MTOMFeature());
		UploadDocumentRq rq = new UploadDocumentRq();
		rq.setDocument(document);
		port.uploadDocument(rq);
	}

	public static void testDownload() throws Exception {
		DownloadDocumentRq rq = new DownloadDocumentRq();
		rq.setEntityId("2");
		rq.setEntityTableName("ip_user");
		DocumentService service = new DocumentService();
		DocumentPort port = service.getDocumentSOAP(new MTOMFeature());
		DownloadDocumentRs rs = port.downloadDocument(rq);
		if (rs.getFileContent() == null)
			throw new Exception("Profile Image Not Avavilable");
		else {
			File file = new File(System.getProperty("user.home") + File.separator + rs.getFileName());
			if (file.exists())
				file.delete();
			file.createNewFile();
			FileOutputStream out = new FileOutputStream(file);
			OutputStreamWriter r = new OutputStreamWriter(out);
			System.out.println(r.getEncoding());
			out.write(rs.getFileContent());
			out.flush();
			out.close();
		}
	}
}
