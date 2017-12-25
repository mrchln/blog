package com.flchy.blog.privilege.config.bean;

import java.io.Serializable;
/**
 *	可访问路径实体
 * @author nieqs
 *
 */
public class ConfUrlBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7880133754738539452L;
	/**
	 * url路径
	 */
	private String urlPath;
	/**
	 * 请求Method
	 */
	private String method;

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
