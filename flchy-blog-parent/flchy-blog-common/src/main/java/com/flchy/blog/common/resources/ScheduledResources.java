package com.flchy.blog.common.resources;

import com.flchy.blog.common.resources.bean.UploadResponse;

public interface ScheduledResources {

	/**
	 * 本地文件上床
	 * @param localFilePath  文件位置
	 * @param fileName   文件名称
	 * @return
	 */
	public UploadResponse uploadLocal(String localFilePath,String fileName);
}
