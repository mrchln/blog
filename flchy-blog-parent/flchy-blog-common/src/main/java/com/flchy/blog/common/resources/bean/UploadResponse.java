package com.flchy.blog.common.resources.bean;

import com.qiniu.storage.model.DefaultPutRet;

public class UploadResponse {
	public String hash;
	public String key;

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public UploadResponse(DefaultPutRet putRet) {
		super();
		this.hash = putRet.hash;
		this.key = putRet.key;
	}

	
}
