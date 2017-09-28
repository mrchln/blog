package com.flchy.blog.privilege.config.emun;

/**
 * URL地址状态
 * 
 * @author 1st
 *
 */
public enum UrlPathType {
	ALL(-1, "所有人都可以执行"), ONE(1, "正常");

	private int code;
	private String values;

	UrlPathType(int code, String values) {
		this.code = code;
		this.values = values;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getValues() {
		return values;
	}

	public void setValues(String values) {
		this.values = values;
	}

}
