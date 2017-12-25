package com.flchy.blog.privilege.enums;

import com.flchy.blog.base.dbconfig.holder.EnumDicHolder;

public enum ResTypeEnum {
    M("RES_MENU", "菜单"), O("RES_OTHER", "其他");

    private String code;

    private String desc;

    ResTypeEnum(String code, String desc) {
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
       return Integer.valueOf(EnumDicHolder.getEnumsValueByCode("RES_TYPE", this.code));
    }

}
