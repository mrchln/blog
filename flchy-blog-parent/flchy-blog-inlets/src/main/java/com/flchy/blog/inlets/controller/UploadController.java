package com.flchy.blog.inlets.controller;

import java.io.InputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.springframework.stereotype.Controller;

@Path("file")
@Controller
public class UploadController {
	@POST
	@Path("/upload")
	@Consumes(MediaType.MULTIPART_FORM_DATA)  // 消费注解必须是这个类型
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Object insertssss(@FormDataParam("file") InputStream file,
			@FormDataParam("file") FormDataContentDisposition fileDisposition) {
		final String fileName = fileDisposition.getFileName();
		String type = fileDisposition.getType();
		System.out.println(fileName);
		System.out.println( type);
		return null;
	}
}
