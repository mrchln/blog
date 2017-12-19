package com.flchy.blog.base.enums;

 /**
 * 操作日志类型编码枚举类，用于在需要记录日志的service层的相关方法上标记注解，标识此方法是什么操作。
 * @since 0.0.1-SNAPSHOT
 */
 
public enum OperateCodeEnum {
    UNKNOW("UNKNOW"),
    PUBLIC("PUBLIC"),
    INSERT("INSERT"),
    UPDATE("UPDATE"), 
    DELETE("DELETE"),
    SELECT("SELECT"),
    EXPORT("EXPORT");
    
    private String value;
    
    public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	private OperateCodeEnum(String value) {
		this.value=value;
	}
	
	
	public static OperateCodeEnum getOperateCodeEnum(String oprType){
		  if(oprType==null){
       	   oprType="UNKNOW";
          }else{
       	   oprType=oprType.toUpperCase();
          }
		OperateCodeEnum codeEnum = OperateCodeEnum.valueOf(OperateCodeEnum.class, oprType);  
        switch (codeEnum) {
        case UNKNOW:
			return OperateCodeEnum.UNKNOW;
        case INSERT:
    		return OperateCodeEnum.INSERT;
        case UPDATE:
    		return OperateCodeEnum.UPDATE;
        case SELECT:
    		return OperateCodeEnum.SELECT;
        case EXPORT:
    		return OperateCodeEnum.EXPORT;
		default:
    		return OperateCodeEnum.UNKNOW;
		}
	}
}