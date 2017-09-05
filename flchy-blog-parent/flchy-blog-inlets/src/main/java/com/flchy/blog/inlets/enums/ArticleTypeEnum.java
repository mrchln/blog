package com.flchy.blog.inlets.enums;

public enum ArticleTypeEnum {
	ARTICLE(1, "文章类型"), URL(2, "地址");
	private int code;
	private String value;

	ArticleTypeEnum(int code, String value) {
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
