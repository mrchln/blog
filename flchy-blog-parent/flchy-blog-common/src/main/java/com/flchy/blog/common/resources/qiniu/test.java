package com.flchy.blog.common.resources.qiniu;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;

public class test {
	public static void main(String[] args) throws UnsupportedEncodingException {
		new test().privateDownloadUrl();
//		new test().uploadManager();
	}
	
	/**
	 * 上传本地文件
	 */
	public void uploadManager() {
		// 构造一个带指定Zone对象的配置类
		Configuration cfg = new Configuration(Zone.zone2());
		// ...其他参数参考类注释
		UploadManager uploadManager = new UploadManager(cfg);
		// ...生成上传凭证，然后准备上传
		String accessKey = "XB7Lbps-JI_79C5sIBSSjhgl-YXo56SnT2aWrr-C";
		String secretKey = "W6L0q2cGwktvst_KWAopYq3egW1YZpMHSh-5s1BA";
		String bucket = "myfile";
		// 如果是Windows情况下，格式是 D:\\qiniu\\test.png
		String localFilePath = "F:\\JavaGit.zip";
		// 默认不指定key的情况下，以文件内容的hash值作为文件名
		String key = "JavaGit.zip";
		Auth auth = Auth.create(accessKey, secretKey);
		String upToken = auth.uploadToken(bucket);
		try {
			Response response = uploadManager.put(localFilePath, key, upToken);
			// 解析上传成功的结果
			DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
			System.out.println(putRet.key);
			System.out.println(putRet.hash);
		} catch (QiniuException ex) {
			Response r = ex.response;
			System.err.println(r.toString());
			try {
				System.err.println(r.bodyString());
			} catch (QiniuException ex2) {
				// ignore
			}
		}
	}
	
	
	
	public void privateDownloadUrl() throws UnsupportedEncodingException {
		String fileName = "JavaGit.zip";
		String domainOfBucket = "http://qiniufile.flchy.cn";
		String encodedFileName = URLEncoder.encode(fileName, "utf-8");
		String publicUrl = String.format("%s/%s", domainOfBucket, encodedFileName);
		String accessKey = "XB7Lbps-JI_79C5sIBSSjhgl-YXo56SnT2aWrr-C";
		String secretKey = "W6L0q2cGwktvst_KWAopYq3egW1YZpMHSh-5s1BA";
		Auth auth = Auth.create(accessKey, secretKey);
		long expireInSeconds = 3600;//1小时，可以自定义链接过期时间
		String finalUrl = auth.privateDownloadUrl(publicUrl, expireInSeconds);
		System.out.println(finalUrl);
	}
}
