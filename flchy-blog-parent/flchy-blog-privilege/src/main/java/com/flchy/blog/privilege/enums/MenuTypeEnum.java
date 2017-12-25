package com.flchy.blog.privilege.enums;

import com.flchy.blog.base.dbconfig.holder.EnumDicHolder;

public enum MenuTypeEnum {
	MENU("MENU_ITEM", "菜单"), FOLDER("MENU_FOLDER", "目录");

	private String code;

	private String desc;

	MenuTypeEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public int getId() {
		return Integer.valueOf(EnumDicHolder.getEnumsValueByCode("MENU_TYPE", this.code));
	}
}
