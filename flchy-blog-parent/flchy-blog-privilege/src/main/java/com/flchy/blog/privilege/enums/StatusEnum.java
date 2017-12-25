package com.flchy.blog.privilege.enums;
/**
 * 状态枚举
 * @author nieqs
 *
 */
public enum StatusEnum {
	///正常
	NORMAL(1), 
	///删除
	DEL(-1)
    ;

	StatusEnum(Integer values) {
        this.values = values;
    }

    private Integer values;

	public Integer getValues() {
		return values;
	}

	public void setValues(Integer values) {
		this.values = values;
	}


}
