package com.flchy.blog.privilege.config.bean;

import java.io.Serializable;

public class BaseDim implements Serializable {
	private static final long serialVersionUID = 2171955888677615060L;
	private int id;
	private String jsonInfo;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getJsonInfo() {
		return jsonInfo;
	}

	public void setJsonInfo(String jsonInfo) {
		this.jsonInfo = jsonInfo;
	}

}
