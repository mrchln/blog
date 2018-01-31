package com.flchy.blog.common.crawler.htmlunit.entity;

import java.io.Serializable;
import java.util.Set;

public class Types implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3809535828496163570L;

	private String typeName;
	private Set<String> list;

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public Set<String> getList() {
		return list;
	}

	public void setList(Set<String> list) {
		this.list = list;
	}

}
