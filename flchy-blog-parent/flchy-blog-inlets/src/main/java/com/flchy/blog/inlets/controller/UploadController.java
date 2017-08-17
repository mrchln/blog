package com.flchy.blog.inlets.controller;

import java.io.InputStream;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.springframework.stereotype.Controller;

@Path("file")
@Controller
public class UploadController {
	@POST
	@Path("/upload")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Object insertssss(@FormDataParam(value = "file") InputStream file,
			@FormDataParam(value = "files") FormDataContentDisposition fileDisposition) {
		final String fileName = fileDisposition.getFileName();
		String type = fileDisposition.getType();
		System.out.println(fileName);
		System.out.println(type);
		return null;
	}
	
	
}
