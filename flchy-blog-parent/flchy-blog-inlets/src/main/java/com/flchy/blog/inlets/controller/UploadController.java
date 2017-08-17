package com.flchy.blog.inlets.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.commons.io.FileUtils;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.springframework.stereotype.Controller;

@Path("file")
@Consumes(MediaType.MULTIPART_FORM_DATA)
@Produces(MediaType.APPLICATION_JSON)
@Controller
public class UploadController {
    /** 
     * Constants operating with images 
     */  
    private static final String ARTICLE_IMAGES_PATH = "c:/Newsportal/article_images/";  
    private static final String JPG_CONTENT_TYPE = "image/jpeg";  
    private static final String PNG_CONTENT_TYPE = "image/png"; 
	@POST
	@Path("upload")
	public Object insertssss(@FormDataParam(value = "file") InputStream file,
			@FormDataParam(value = "file") FormDataContentDisposition fileDisposition) {
		final String fileName = fileDisposition.getFileName();
		String type = fileDisposition.getType();
		System.out.println(fileName);
		System.out.println(type);
		return null;
	}

	/**
	 * * 第二种方式上传 使用FormDataMultiPart 获取表单数据
	 * 
	 * @param form
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@POST
	@Path("uploadimage2")

	public String uploadimage2(FormDataMultiPart form, @Context HttpServletResponse response)
			throws UnsupportedEncodingException {
		// 获取文件流
		FormDataBodyPart filePart = form.getField("file");
		// 获取表单的其他数据
		FormDataBodyPart usernamePart = form.getField("username");

		// ContentDisposition headerOfFilePart =
		// filePart.getContentDisposition();
		// 把表单内容转换成流
		InputStream fileInputStream = filePart.getValueAs(InputStream.class);

		FormDataContentDisposition formDataContentDisposition = filePart.getFormDataContentDisposition();

		String source = formDataContentDisposition.getFileName();
		String result = new String(source.getBytes("ISO8859-1"), "UTF-8");

		System.out.println("formDataContentDisposition.getFileName()result " + result);

		String filePath = ARTICLE_IMAGES_PATH + result;
		File file = new File(filePath);
		System.out.println("file " + file.getAbsolutePath());
		try {
			// 保存文件
			FileUtils.copyInputStreamToFile(fileInputStream, file);
			// saveFile(fileInputStream, file);
		} catch (IOException ex) {
//			Logger.getLogger(UploadImageResource.class.getName()).log(Level.SEVERE, null, ex);
		}
		System.out.println("" + "images/" + result);

		response.setCharacterEncoding("UTF-8");
		return "images/" + result;
	}

	// @POST
	// @Path("/upload")
	// @Consumes(MediaType.MULTIPART_FORM_DATA)
	// @Produces(MediaType.APPLICATION_JSON)
	// public Object insertssss(@Context RequestContext request) {
	// try {
	// String fileName = saveFile(request);
	// if (!fileName.equals("")) {
	// } else {
	//
	// }
	// } catch (Exception ex) {
	// }
	// return null;
	// }

	// private String saveFile(RequestContext request) {
	// boolean isMultipart = ServletFileUpload.isMultipartContent(request);
	// if (isMultipart) {
	// FileItemFactory factory = new DiskFileItemFactory();
	// ServletFileUpload upload = new ServletFileUpload(factory);
	// List items = upload.parseRequest(request);
	// Iterator iter = items.iterator();
	// while (iter.hasNext()) {
	// FileItem item = (FileItem) iter.next();
	// if (item.isFormField()) {
	// // 接收表单里的参数
	// if (item.getFieldName().equals("json")) {
	// String json = item.getString("UTF-8");
	// System.out.println("接收参数json："+json);
	// }
	// } else {
	// // 存储文件
	// FileUtils.copyInputStreamToFile(item.getInputStream(), new
	// File(request.getRealPath("/")+"upload/"+item.getFieldName()));
	// }
	// }
	//
	// }
	// return null;
	// }
	//
	// private String processFileName(String fileNameInput) {
	// String fileNameOutput = null;
	// fileNameOutput = fileNameInput.substring(fileNameInput.lastIndexOf("\\")
	// + 1, fileNameInput.length());
	// return fileNameOutput;
	// }
}
