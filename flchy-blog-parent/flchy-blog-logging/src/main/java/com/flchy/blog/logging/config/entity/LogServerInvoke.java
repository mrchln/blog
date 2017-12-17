package com.flchy.blog.logging.config.entity;

import java.io.Serializable;
import java.util.Date;
/**
 * 记录功能操作日志，如：新增，修改，删除操作，导出，导入操作，清单下载操作等操作日志，主要是便于审计
 * @author nieqs
 *
 */
public class LogServerInvoke implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String logId;
	private Integer interfaceId;
	private String invokeCont;
	private Integer isFail;
	private String errMsg;
	private Date invokeBeginTime;
	private Date invokeEndTime;
	private String oprUserId;
	private String mainAccount;
	private String serverIp;
	public String getLogId() {
		return logId;
	}
	public void setLogId(String logId) {
		this.logId = logId;
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
