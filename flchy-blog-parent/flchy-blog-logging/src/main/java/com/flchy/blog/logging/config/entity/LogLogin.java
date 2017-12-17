package com.flchy.blog.logging.config.entity;

import java.io.Serializable;
import java.util.Date;
/**
 * 存放用户的登录、登出日志，包含嵌入页面的单点登录日志
 * @author nieqs
 *
 */
public class LogLogin implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String logId;
	private String sessionId;
	private String oprUserId;
	private String mainAccount;
	private String serverIp;
	private String clientIp;
	private Date loginTime;
	private String userAgent;
	private String browserType;

	public String getLogId() {
		return logId;
	}

	public void setLogId(String logId) {
		this.logId = logId;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getOprUserId() {
		return oprUserId;
	}

	public void setOprUserId(String oprUserId) {
		this.oprUserId = oprUserId;
	}

	public String getMainAccount() {
		return mainAccount;
	}

	public void setMainAccount(String mainAccount) {
		this.mainAccount = mainAccount;
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

	public Date getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
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

}
