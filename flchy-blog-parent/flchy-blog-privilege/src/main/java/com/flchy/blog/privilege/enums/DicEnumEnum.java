package com.flchy.blog.privilege.enums;

public enum DicEnumEnum {
	//菜单类型
	MENU(1, "MENU_TYPE"),
	//功能
	RES(2, "RES_TYPE"),
	//菜单加载类型
	LOAD_TARGET(3, "LOAD_TARGET"),
	//元素类型
	ELEMENT_TYPE(4, "ELEMENT_TYPE");

	private Integer code;

	private String enumGroup;

	DicEnumEnum(Integer code, String enumGroup) {
		this.code = code;
		this.enumGroup = enumGroup;
	}

	public Integer getCode() {
		return code;
	}
	
	public void setCode(Integer code) {
		this.code = code;
	}

	public String getEnumGroup() {
		return enumGroup;
	}

	public void setEnumGroup(String enumGroup) {
		this.enumGroup = enumGroup;
	}
	


}
