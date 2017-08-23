package com.zuobiao.analysis.common.fastdfs;

import java.io.File;
import java.io.IOException;

import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.alibaba.fastjson.JSONArray;

/**
 * fastDFS文件服务Java客户端实现,所有执行方法均为静态方法。
 * 
 * @author xq
 *
 */
public class FastDFSClient {

	/**
	 * 客户端
	 */
	private static StorageClient1 storageClient1 = null;

	// 初始化客户端,加载类时候执行片段
	static {
		try {
			Resource resource = new ClassPathResource("conf/resource/client.conf");
			File file = resource.getFile();
			String configFile = file.getAbsolutePath();

			ClientGlobal.init(configFile);
			//
			TrackerClient trackerClient = new TrackerClient(ClientGlobal.g_tracker_group);
			//
			TrackerServer trackerServer = trackerClient.getConnection();
			//
			StorageServer storageServer = trackerClient.getStoreStorage(trackerServer);
			//
			storageClient1 = new StorageClient1(trackerServer, storageServer);
			System.out.println(storageClient1.toString());
			System.out.println("FastDFS Client Init Success!");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("FastDFS Client Init Fail!");
		}
	}

	/***
	 * 文件上传
	 * 
	 * @param fastDSFile
	 * @return
	 * @throws IOException
	 * @throws MyException
	 */
	public static JSONArray upload(FastDSFile fastDSFile) throws IOException, MyException {
		String[] uploadResult = storageClient1.upload_file(fastDSFile.getContent(), fastDSFile.getExt(), null);
		// String arr = JSONArray.toJSONString(uploadResult);
		JSONArray arr = (JSONArray) JSONArray.toJSON(uploadResult);
		return arr;
	}

	/**
	 * 文件下载
	 * 
	 * @param groupName
	 * @param remoteFileName
	 * @return
	 * @throws IOException
	 * @throws MyException
	 */
	public static byte[] download(String groupName, String remoteFileName) throws IOException, MyException {
		return storageClient1.download_file(groupName, remoteFileName);
	}

	/**
	 * 文件删除
	 * 
	 * @param groupName
	 * @param remoteFileName
	 * @throws Exception
	 * @return 返回0成功;非0失败.
	 */
	public static int delete(String groupName, String remoteFileName) throws Exception {
		return storageClient1.delete_file(groupName, remoteFileName);
	}
}