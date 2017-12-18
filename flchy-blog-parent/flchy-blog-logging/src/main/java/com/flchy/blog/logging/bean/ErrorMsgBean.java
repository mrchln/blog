package com.flchy.blog.logging.bean;

import java.io.Serializable;

public class ErrorMsgBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5114414130275605095L;
	/**
	 * 异常代码
	 */
	private String errorCode;
	/**
	 * 异常信息
	 */
	private String message;
	/**
	 * 异常方法名
	 */
	private String methodName;
	/**
	 * 执行时间
	 */
	private String time;
	/**
	 * 请求参数
	 */
	private String parameters;
	/**
	 * 返回参数
	 */
	private String result;
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getParameters() {
		return parameters;
	}
	public void setParameters(String parameters) {
		this.parameters = parameters;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	

}
