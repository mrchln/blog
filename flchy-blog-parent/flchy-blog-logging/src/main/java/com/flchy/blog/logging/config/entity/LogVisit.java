package com.flchy.blog.logging.config.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 系统之间接口调用的日志记录，记录接口信息，时间信息，结果信息等
 * 
 * @author nieqs
 *
 */
public class LogVisit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String logId;
	private String sessionId;
	private String resId;
	private String objId;
	private String oprUserId;
	private String mainAccount;
	private Date visitBeginTime;
	private Date visitEndTime;
	private Integer isError;
	private String errMsg;
	private String serverIp;
	private String clientIp;
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
	public String getResId() {
		return resId;
	}
	public void setResId(String resId) {
		this.resId = resId;
	}
	public String getObjId() {
		return objId;
	}
	public void setObjId(String objId) {
		this.objId = objId;
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
	public Date getVisitBeginTime() {
		return visitBeginTime;
	}
	public void setVisitBeginTime(Date visitBeginTime) {
		this.visitBeginTime = visitBeginTime;
	}
	public Date getVisitEndTime() {
		return visitEndTime;
	}
	public void setVisitEndTime(Date visitEndTime) {
		this.visitEndTime = visitEndTime;
	}
	public Integer getIsError() {
		return isError;
	}
	public void setIsError(Integer isError) {
		this.isError = isError;
	}
	public String getErrMsg() {
		return errMsg;
	}
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
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
	
	

}
