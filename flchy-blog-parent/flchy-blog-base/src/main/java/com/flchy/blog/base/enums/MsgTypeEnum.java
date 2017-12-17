package com.flchy.blog.base.enums;

 /**
 * 来源于枚举表：1 ：短信  2：彩信  3： 邮件
 * @since 0.0.1-SNAPSHOT
 */
 
public enum MsgTypeEnum {
	//短信
	SMS(1),
	//彩信
	MMS(2),
	//邮件
	MAIL(3);
    
    private Integer value;
    
    public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	private MsgTypeEnum(Integer value) {
		this.value=value;
	}
	
}