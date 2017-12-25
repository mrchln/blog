package com.flchy.blog.privilege.config.bean;

import java.io.Serializable;

public class RulesBean implements Serializable {

	private static final long serialVersionUID = 1562297810802743651L;

	private String uriPath;

	private String permission;

	public String getUriPath() {
		return uriPath;
	}

	public void setUriPath(String uriPath) {
		this.uriPath = uriPath;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

}
