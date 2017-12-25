package com.flchy.blog.base.event.impl;

import java.util.Date;

import com.flchy.blog.base.event.AbstractWebVisitEvent;

/**
 * @Title:登录注销事件
 */
public class LoginEvent extends AbstractWebVisitEvent {
	private static final long serialVersionUID = 5009199522943324769L;
	private String userId;
	private String sessionId;
	private String mainUserId;
	private String serverIp;
	private String clientIp;
	private String userAgent;
	private String browserType;
	private Date loginTime;
	private Integer isSuccess;
	private String parameter;
	

	

	public LoginEvent(Object source) {
		super(source);
	}

	public LoginEvent(Object source, String userId, String sessionId, String mainUserId, String serverIp,
			String clientIp, String userAgent, String browserType, Date loginTime,Integer isSuccess,String parameter) {
		super(source);
		this.userId = userId;
		this.sessionId = sessionId;
		this.mainUserId = mainUserId;
		this.serverIp = serverIp;
		this.clientIp = clientIp;
		this.userAgent = userAgent;
		this.browserType = browserType;
		this.loginTime = loginTime;
		this.isSuccess=isSuccess;
		this.parameter=parameter;
	}
	
	
	public String getParameter() {
		return parameter;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	public Integer getIsSuccess() {
		return isSuccess;
	}

	public void setIsSuccess(Integer isSuccess) {
		this.isSuccess = isSuccess;
	}
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getMainUserId() {
		return mainUserId;
	}

	public void setMainUserId(String mainUserId) {
		this.mainUserId = mainUserId;
	}

	public String getServerIp() {
		return serverIp;
	}

	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}

	public String getClientIp() {
		return clientIp;
	}

	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public String getBrowserType() {
		return browserType;
	}

	public void setBrowserType(String browserType) {
		this.browserType = browserType;
	}

	public Date getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

}
