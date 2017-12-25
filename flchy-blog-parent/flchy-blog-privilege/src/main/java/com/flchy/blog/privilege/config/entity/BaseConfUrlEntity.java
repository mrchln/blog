package com.flchy.blog.privilege.config.entity;

import com.flchy.blog.base.entity.BaseManagedEntity;

/**
 * PRIV_CONF_URL
 * 每次启动自动扫描，添加当前表，若不存在的，则标记为失效，增量维护，不删除
 */
public class BaseConfUrlEntity extends BaseManagedEntity {
	private static final long serialVersionUID = 578044075307311202L;

	private int urlId;

	private String urlPath;
	
	private String method;
	//前端用
	private Integer index;
	
	

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public int getUrlId() {
		return urlId;
	}

	public void setUrlId(int urlId) {
		this.urlId = urlId;
	}

	public String getUrlPath() {
		return urlPath;
	}

	public void setUrlPath(String urlPath) {
		this.urlPath = urlPath;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

}
