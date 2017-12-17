package com.flchy.blog.base.event.impl;



import java.util.Date;

import com.flchy.blog.base.event.AbstractWebVisitEvent;

/**
 * @Title:资源访问事件,如:(菜单点击、报表点击等)
 */
public class VisitEvent extends AbstractWebVisitEvent {
	private static final long serialVersionUID = -5137266049278359826L;

	public VisitEvent(Object source) {
		super(source);
	}
	
	public VisitEvent(Object source,  String sessionId, String resId, String objId, String oprUserId, String mainAccount, Integer isError,
			String errMsg, String serverIp, String clientIp, String userAgent, String browserType, Date visitBeginTime,
			Date visitEndTime) {
		super(source);
		this.sessionId = sessionId;
		this.resId = resId;
		this.objId = objId;
		this.oprUserId = oprUserId;
		this.mainAccount = mainAccount;
		this.isError = isError;
		this.errMsg = errMsg;
		this.serverIp = serverIp;
		this.clientIp = clientIp;
		this.userAgent = userAgent;
		this.browserType = browserType;
		this.visitBeginTime = visitBeginTime;
		this.visitEndTime = visitEndTime;
	}


	private String sessionId;// 资源编码
	private String resId;// 资源编码
	
	private String objId;//如：内容分类个体分析专题，记录新闻类编码
	
	private String oprUserId;//操作人
	
	private String mainAccount;//4a集成时,为4a主账号名；嵌入客服系统使用时,为客服人员账号
	
	private Integer  isError;//是否访问报错
	
	private String   errMsg;//错误信息
	
	private String   serverIp;//服务端ip
	
	private String   clientIp;//客户端ip
	
	private String   userAgent;//客户端ua信息
	
	private String   browserType;//来源于

	private Date visitBeginTime;

	private Date visitEndTime;

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

	public Integer isError() {
		return isError;
	}

	public void setError(Integer isError) {
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

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}


}
