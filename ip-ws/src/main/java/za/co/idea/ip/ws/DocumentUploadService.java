package za.co.idea.ip.ws;

import java.io.ByteArrayInputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.ContentDisposition;
import org.hibernate.Hibernate;

import za.co.idea.ip.orm.bean.IpBlob;
import za.co.idea.ip.orm.dao.IpBlobDAO;
import za.co.idea.ip.ws.bean.AttachmentMessage;

@Path(value = "/ds")
public class DocumentUploadService {

	private IpBlobDAO ipBlobDAO;

	@POST
	@Path("/doc/upload/{blobId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response doUpload(final Attachment stream, @PathParam("blobId") Long blobId) {
		try {
			IpBlob blob = ipBlobDAO.findById(blobId);
			if (blob != null) {
				blob.setBlobContent(Hibernate.createBlob(stream.getDataHandler().getInputStream()));
				ipBlobDAO.merge(blob);
				return Response.ok().build();
			} else {
				return Response.status(Status.INTERNAL_SERVER_ERROR).build();
			}
		} catch (Exception e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.MULTIPART_FORM_DATA)
	@Path("/doc/download/{blobId}")
	public Attachment doDownload(@PathParam("blobId") Long blobId) {
		try {
			IpBlob blob = ipBlobDAO.findById(blobId);
			if (blob != null) {
				byte[] b = blob.getBlobContent().getBytes(1l, (int) blob.getBlobContent().length());
				ByteArrayInputStream bais = new ByteArrayInputStream(b);
				Attachment attachment = new Attachment(blob.getBlobId().toString(), bais, new ContentDisposition("attachment;filename=" + blob.getBlobName()));
				return attachment;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public IpBlobDAO getIpBlobDAO() {
		return ipBlobDAO;
	}

	public void setIpBlobDAO(IpBlobDAO ipBlobDAO) {
		this.ipBlobDAO = ipBlobDAO;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/doc/create")
	public Response createDocument(AttachmentMessage message) {
		try {
			IpBlob blob = new IpBlob();
			blob.setBlobContentType(message.getBlobContentType());
			blob.setBlobEntityId(message.getBlobEntityId());
			blob.setBlobEntityTblNm(message.getBlobEntityTblNm());
			blob.setBlobId(message.getBlobId());
			blob.setBlobName(message.getBlobName());
			ipBlobDAO.save(blob);
			return Response.ok().build();
		} catch (Exception e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/doc/getId/{entityId}/{entityTblNm}")
	public Long getId(@PathParam("entityId") Long entityId, @PathParam("entityTblNm") String entityTblNm) {
		Long ret = -999l;
		IpBlob blob = ipBlobDAO.getBlobByEntity(entityId, entityTblNm);
		if (blob != null)
			ret = blob.getBlobId();
		return ret;
	}
}
