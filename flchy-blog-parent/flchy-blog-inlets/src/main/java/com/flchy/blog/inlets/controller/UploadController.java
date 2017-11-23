package com.flchy.blog.inlets.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;

import org.apache.commons.io.FileUtils;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.alibaba.fastjson.JSONArray;
import com.flchy.blog.base.response.ResponseCommand;
import com.flchy.blog.common.fastdfs.FastDFSClient;
import com.flchy.blog.common.fastdfs.FastDSFile;
/**
 * 
 * @author 1st
 *
 */
@RestController
@RequestMapping("file")
public class UploadController {
	/**
	 * Constants operating with images
	 */
	private static final String ARTICLE_IMAGES_PATH = "D:/Newsportal/article_images/";
	private static final String JPG_CONTENT_TYPE = "image/jpeg";
	private static final String PNG_CONTENT_TYPE = "image/png";
	@Autowired
	private FastDFSClient fastDFSClient;
	/*
	 * 采用spring提供的上传文件的方法
	 */
	@RequestMapping("upload")
	public Object springUpload(HttpServletRequest request) throws Exception {
		List<String> urlList = new ArrayList<>();
		// 将当前上下文初始化给 CommonsMutipartResolver （多部分解析器）
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		// 检查form中是否有enctype="multipart/form-data"
		if (multipartResolver.isMultipart(request)) {
			// 将request变成多部分request
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
			// 获取multiRequest 中所有的文件名
			Iterator<?> iter = multiRequest.getFileNames();

			while (iter.hasNext()) {
				// 一次遍历所有文件
				MultipartFile file = multiRequest.getFile(iter.next().toString());
				if (file != null) {
					final String fileName = file.getOriginalFilename();
					FastDSFile fastDSFile = new FastDSFile();
					System.out.println(file.getContentType());
					// fastDSFile.setContent(input2byte(file.getInputStream()));
					fastDSFile.setContent(file.getBytes());
					fastDSFile.setExt(fileName.substring(fileName.lastIndexOf(".")+1));
					JSONArray rs = fastDFSClient.upload(fastDSFile);
					String url = rs.get(0) + "/" + rs.get(1);
					urlList.add(url);
				}

			}

		} else {
			throw new Exception("未找到文件");
		}
		return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, urlList);
	}

	public static final byte[] input2byte(InputStream inStream) throws IOException {
		ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
		byte[] buff = new byte[100];
		int rc = 0;
		while ((rc = inStream.read(buff, 0, 100)) > 0) {
			swapStream.write(buff, 0, rc);
		}
		byte[] in2b = swapStream.toByteArray();
		return in2b;
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
	@PostMapping(value = "/uploadimage2")
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
			// Logger.getLogger(UploadImageResource.class.getName()).log(Level.SEVERE,
			// null, ex);
		}
		System.out.println("" + "images/" + result);

		response.setCharacterEncoding("UTF-8");
		return "images/" + result;
	}

}
