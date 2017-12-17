package com.flchy.blog.base.enums;

 /**
 * 状态
 * @since 0.0.1-SNAPSHOT
 */
 
public enum StatusEnum {
	//成功
	SUCCESS(1),
	//失败
	ERROR(2);
    
    private Integer value;
    
    public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	private StatusEnum(Integer value) {
		this.value=value;
	}
	
}