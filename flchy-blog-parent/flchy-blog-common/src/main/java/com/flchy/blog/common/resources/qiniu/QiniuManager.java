package com.flchy.blog.common.resources.qiniu;

import java.io.UnsupportedEncodingException;

import com.flchy.blog.common.holder.PropertiesHolder;
import com.flchy.blog.common.resources.ScheduledResources;
import com.flchy.blog.common.resources.bean.UploadResponse;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;

public class QiniuManager implements ScheduledResources {
	private static Zone zone;
	private static String accessKey;
	private static String secretKey;
	private static String bucket;
	static {
		String ZoneIndex = PropertiesHolder.getProperty("qiniu.Zone");
		switch (Integer.valueOf(ZoneIndex)) {
		case 0:
			zone = Zone.zone0();
			break;
		case 1:
			zone = Zone.zone1();
			break;
		case 2:
			zone = Zone.zone2();
			break;
		case 3:
			zone = Zone.zoneNa0();
			break;
		default:
			zone = Zone.zone2();
			break;
		}
		accessKey = PropertiesHolder.getProperty("qiniu.accessKey");
		secretKey = PropertiesHolder.getProperty("qiniu.secretKey");
		bucket = PropertiesHolder.getProperty("qiniu.bucket");
	}

	/**
	 * 本地上传
	 */
	@Override
	public UploadResponse uploadLocal(String localFilePath, String fileName) {
		// 构造一个带指定Zone对象的配置类
		Configuration cfg = new Configuration(zone);
		// ...其他参数参考类注释
		UploadManager uploadManager = new UploadManager(cfg);
		// 默认不指定key的情况下，以文件内容的hash值作为文件名
		String key = fileName;
		// ...生成上传凭证，然后准备上传
		Auth auth = Auth.create(accessKey, secretKey);
		String upToken = auth.uploadToken(bucket);
		try {
			Response response = uploadManager.put(localFilePath, key, upToken);
			// 解析上传成功的结果
			DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
			return new UploadResponse(putRet);
		} catch (QiniuException ex) {
			Response r = ex.response;
			System.err.println(r.toString());
			try {
				System.err.println(r.bodyString());
			} catch (QiniuException ex2) {
			}
		}
		return null;
	}
	
	
	
	
	public UploadResponse uploadByte(byte[] uploadBytes, String fileName) {
		//构造一个带指定Zone对象的配置类
		Configuration cfg = new Configuration(zone);
		//...其他参数参考类注释
		UploadManager uploadManager = new UploadManager(cfg);
		//默认不指定key的情况下，以文件内容的hash值作为文件名
		String key = fileName;
		//...生成上传凭证，然后准备上传
		Auth auth = Auth.create(accessKey, secretKey);
		String upToken = auth.uploadToken(bucket);
		try {
		    Response response = uploadManager.put(uploadBytes, key, upToken);
		    //解析上传成功的结果
		    DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
		   return new UploadResponse(putRet);
		} catch (QiniuException ex) {
		    Response r = ex.response;
		    System.err.println(r.toString());
		    try {
		        System.err.println(r.bodyString());
		    } catch (QiniuException ex2) {
		        //ignore
		    }
		}
		return null;
	}

}
