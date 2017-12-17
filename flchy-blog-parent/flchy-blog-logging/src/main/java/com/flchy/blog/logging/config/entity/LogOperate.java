package com.flchy.blog.logging.config.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 记录功能操作日志，如：新增，修改，删除操作，导出，导入操作，清单下载操作等操作日志，主要是便于审计
 * 
 * @author nieqs
 *
 */
public class LogOperate implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String logId;
	private String sessionId;
	private String resId;
	private String oprType;
	private String oprObj;
	private String oprCont;
	private String oprUserId;
	private String mainAccount;
	private Date oprBeginTime;
	private Date oprEndTime;
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

	public String getOprType() {
		return oprType;
	}

	public void setOprType(String oprType) {
		this.oprType = oprType;
	}

	public String getOprObj() {
		return oprObj;
	}

	public void setOprObj(String oprObj) {
		this.oprObj = oprObj;
	}

	public String getOprCont() {
		return oprCont;
	}

	public void setOprCont(String oprCont) {
		this.oprCont = oprCont;
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

	public Date getOprBeginTime() {
		return oprBeginTime;
	}

	public void setOprBeginTime(Date oprBeginTime) {
		this.oprBeginTime = oprBeginTime;
	}

	public Date getOprEndTime() {
		return oprEndTime;
	}

	public void setOprEndTime(Date oprEndTime) {
		this.oprEndTime = oprEndTime;
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
