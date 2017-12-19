package com.flchy.blog.base.event.impl;

import java.util.Date;

import com.flchy.blog.base.event.AbstractWebVisitEvent;

public class PublicLogEvent extends AbstractWebVisitEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5575470907190273415L;

	public PublicLogEvent(Object source) {
		super(source);
		// TODO Auto-generated constructor stub
	}

	private String oprObj;
	private String oprCont;
	private String account;
	private Date beginTime;
	private Date endTime;
	private Integer isError;
	private String errMsg;
	private String serverip;
	private String clientIp;
	private String userAgent;
	private String browserType;
	private String system;

	public PublicLogEvent(Object source, String oprObj, String oprCont, String account, Date beginTime, Date endTime,
			Integer isError, String errMsg, String serverip, String clientIp, String userAgent, String browserType,
			String system) {
		super(source);
		this.oprObj = oprObj;
		this.oprCont = oprCont;
		this.account = account;
		this.beginTime = beginTime;
		this.endTime = endTime;
		this.isError = isError;
		this.errMsg = errMsg;
		this.serverip = serverip;
		this.clientIp = clientIp;
		this.userAgent = userAgent;
		this.browserType = browserType;
		this.system = system;
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

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
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

	public String getServerip() {
		return serverip;
	}

	public void setServerip(String serverip) {
		this.serverip = serverip;
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

	public String getSystem() {
		return system;
	}

	public void setSystem(String system) {
		this.system = system;
	}

}
