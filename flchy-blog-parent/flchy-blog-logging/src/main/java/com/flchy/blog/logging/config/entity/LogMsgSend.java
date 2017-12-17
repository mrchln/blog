package com.flchy.blog.logging.config.entity;

import java.io.Serializable;
import java.util.Date;
/**
 * 短信、彩信、邮件推送日志
 * @author nieqs
 *
 */
public class LogMsgSend implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String logId;
	private String msgId;
	private Integer msgType;
	private String sender;
	private String receiver;
	private String msgTitle;
	private String msgCont;
	private String filePath;
	private Date sendBeginTime;
	private Date sendEndTime;
	private Integer sendStatus;

	public String getLogId() {
		return logId;
	}

	public void setLogId(String logId) {
		this.logId = logId;
	}

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public Integer getMsgType() {
		return msgType;
	}

	public void setMsgType(Integer msgType) {
		this.msgType = msgType;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getMsgTitle() {
		return msgTitle;
	}

	public void setMsgTitle(String msgTitle) {
		this.msgTitle = msgTitle;
	}

	public String getMsgCont() {
		return msgCont;
	}

	public void setMsgCont(String msgCont) {
		this.msgCont = msgCont;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public Date getSendBeginTime() {
		return sendBeginTime;
	}

	public void setSendBeginTime(Date sendBeginTime) {
		this.sendBeginTime = sendBeginTime;
	}

	public Date getSendEndTime() {
		return sendEndTime;
	}

	public void setSendEndTime(Date sendEndTime) {
		this.sendEndTime = sendEndTime;
	}

	public Integer getSendStatus() {
		return sendStatus;
	}

	public void setSendStatus(Integer sendStatus) {
		this.sendStatus = sendStatus;
	}

}
