/**
 * 
 */
package com.flchy.blog.privilege.enums;

/**
 * 拼装格式枚举
 * @author nieqs
 */
public enum FormatEnum {
	///访问权限id 自动拼装 
	VISIT("M:");
	
	private String code;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	FormatEnum(String code) {
		this.code = code;
	}
	

}
