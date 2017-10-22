package com.flchy.blog.inlets.enums;

/**
 * 1：正常，-1：删除 2,草稿
 * 
 * @author nieqs
 *
 */
public enum StatusEnum {
	/**
	 * 枚举
	 */
	NORMAL(1, "正常"), DELETE(-1, "已删除"), DRAFT(2, "草稿");

	private int code;
	private String value;

	StatusEnum(int code, String value) {
		this.code = code;
		this.value = value;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
