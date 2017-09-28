package com.flchy.blog.base.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.flchy.blog.base.response.ResponseCommand;

/**
 * 业务异常
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "业务异常，伪装为错误请求")
public class BusinessException extends BaseRuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6812066610496581818L;
	private Integer errCode;
	private String errMsg;

	public Integer getErrCode() {
		return errCode;
	}

	public void setErrCode(Integer errCode) {
		this.errCode = errCode;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public BusinessException(Integer errCode, String errMsg) {
		this.errCode = errCode;
		this.errMsg = errMsg;
	}
	public BusinessException(String errMsg) {
		this.errCode = ResponseCommand.STATUS_ERROR;
		this.errMsg = errMsg;
	}
}
