package com.flchy.blog.base.event.impl;

import java.util.Date;

import com.flchy.blog.base.event.AbstractWebVisitEvent;

/**
 * @Title:功能操作事件,如（导出、上传、下载、增删改等）
 */
public class UsageEvent extends AbstractWebVisitEvent {
	private static final long serialVersionUID = -7372309216314198520L;

	public UsageEvent(Object source) {
		super(source);
	}

	private String logId;// 日志id
	private String sessionId;// session编码
	private String resId;// 资源编码
	private String oprType;// 增、删、改;导出;清单下载;登录失败;上传;……
	private String oprObj;// 操作对象
	private String oprCont;// 存放操作详细内容、sql语句等信息
	private String oprUserId;// 操作人
	private String mainAccount;// 4a集成时,为4a主账号名；嵌入客服系统使用时,为客服人员账号
	private Date oprBeginTime;// 开始操作时间
	private Date oprEndTime;// 结束操作时间
	private Integer isError;// 是否错误
	private String errMsg;// 错误信息
	private String serverIp;
	private String clientIp;
	private String userAgent;
	private String browserType;

	// public UsageEvent(Object source, String resId, boolean isError, String
	// operateErrorMsg, Date operateBeginTime, Date operateEndTime,
	// OperateCodeEnum operateType, String operateObjId, String operateCont) {
	// super(source);
	// this.resId = resId;
	// this.isError = isError;
	// this.operateErrorMsg = operateErrorMsg;
	// this.operateBeginTime = operateBeginTime;
	// this.operateEndTime = operateEndTime;
	// this.operateType = operateType;
	// this.operateObjId = operateObjId;
	// this.operateCont = operateCont;
	// }

	public UsageEvent(Object source, String sessionId, String resId, String oprType, String oprObj, String oprCont,
			String oprUserId, String mainAccount, Date oprBeginTime, Date oprEndTime, Integer isError, String errMsg,
			String serverIp, String clientIp, String userAgent, String browserType) {
		super(source);
		this.sessionId = sessionId;
		this.resId = resId;
		this.oprType = oprType;
		this.oprObj = oprObj;
		this.oprCont = oprCont;
		this.oprUserId = oprUserId;
		this.mainAccount = mainAccount;
		this.oprBeginTime = oprBeginTime;
		this.oprEndTime = oprEndTime;
		this.isError = isError;
		this.errMsg = errMsg;
		this.serverIp = serverIp;
		this.clientIp = clientIp;
		this.userAgent = userAgent;
		this.browserType = browserType;
	}

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

}
