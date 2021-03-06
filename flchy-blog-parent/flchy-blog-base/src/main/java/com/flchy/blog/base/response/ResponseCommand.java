package com.flchy.blog.base.response;

import java.io.Serializable;

/**
 * @author XuCong
 * @param <V>
 *
 */
public class ResponseCommand implements Serializable {
	private static final long serialVersionUID = 6700648664405181335L;
	public final static int STATUS_SUCCESS = 0;//成功
	public final static int STATUS_ERROR = 1;//请求错误
	public final static int STATUS_LOGIN_ERROR = 2;//登录过期
	
	public final static int STATUS_AUTHORITY_ERROR = 3; //没有权限
	public final static int STATUS_TOKEN_ERROR = 4;//登录过期

	private int status;
	private Object result;

	public ResponseCommand(int status, Object result) {
		this.status = status;
		this.result = result;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

}
