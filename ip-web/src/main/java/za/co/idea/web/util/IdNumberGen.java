package za.co.idea.web.util;

import java.util.Collections;

import javax.ws.rs.core.MediaType;

import org.apache.cxf.jaxrs.client.WebClient;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;

import za.co.idea.ip.ws.util.CustomObjectMapper;

public class IdNumberGen {

	public Long getNextId(String table) {
		WebClient client = WebClient.create("http://127.0.0.1:8080/ip-ws/ip/as/gen/" + table, Collections.singletonList(new JacksonJsonProvider(new CustomObjectMapper())));
		client.header("Content-Type", "application/json");
		client.header("Accept", "application/json");
		return client.accept(MediaType.APPLICATION_JSON).get(Long.class);
	}
}
