package com.flchy.blog.base.event.impl;

import java.util.Date;

import com.flchy.blog.base.event.AbstractWebVisitEvent;

/**
 * @Title:与外部系统服务接口调用事件,如:webservice、RMI、HttpClient等
 */

public class InvokeEvent extends AbstractWebVisitEvent {
	private static final long serialVersionUID = -6779226084902143975L;

	public InvokeEvent(Object source) {
		super(source);
	}

	private Integer interfaceId;// 接口id
	private String invokeCont;// 输入参数、完整url
	private Integer isFail;// 1：是，-1：否
	private String errMsg;// 1：是，-1：否
	private Date invokeBeginTime;// 调用开始时间
	private Date invokeEndTime;// 调用结束时间
	private String oprUserId;// 操作人id
	private String mainAccount;// 4a集成时,为4a主账号名；嵌入客服系统使用时,为客服人员账号
	private String serverIp;// 服务器id

	public InvokeEvent(Object source, Integer interfaceId, String invokeCont, Integer isFail, String errMsg,
			Date invokeBeginTime, Date invokeEndTime, String oprUserId, String mainAccount, String serverIp) {
		super(source);
		this.interfaceId = interfaceId;
		this.invokeCont = invokeCont;
		this.isFail = isFail;
		this.errMsg = errMsg;
		this.invokeBeginTime = invokeBeginTime;
		this.invokeEndTime = invokeEndTime;
		this.oprUserId = oprUserId;
		this.mainAccount = mainAccount;
		this.serverIp = serverIp;
	}

	public Integer getInterfaceId() {
		return interfaceId;
	}

	public void setInterfaceId(Integer interfaceId) {
		this.interfaceId = interfaceId;
	}

	public String getInvokeCont() {
		return invokeCont;
	}

	public void setInvokeCont(String invokeCont) {
		this.invokeCont = invokeCont;
	}

	public Integer getIsFail() {
		return isFail;
	}

	public void setIsFail(Integer isFail) {
		this.isFail = isFail;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public Date getInvokeBeginTime() {
		return invokeBeginTime;
	}

	public void setInvokeBeginTime(Date invokeBeginTime) {
		this.invokeBeginTime = invokeBeginTime;
	}

	public Date getInvokeEndTime() {
		return invokeEndTime;
	}

	public void setInvokeEndTime(Date invokeEndTime) {
		this.invokeEndTime = invokeEndTime;
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
}
