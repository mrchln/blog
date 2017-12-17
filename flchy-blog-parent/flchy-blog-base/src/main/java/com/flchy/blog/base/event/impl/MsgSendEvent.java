package com.flchy.blog.base.event.impl;

import java.util.Date;

import com.flchy.blog.base.event.AbstractWebVisitEvent;

/**
 * @Title:短信、彩信、邮件推送日志
 */
public class MsgSendEvent extends AbstractWebVisitEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2834193767891016661L;

	public MsgSendEvent(Object source) {
		super(source);
	}

	private String msgId;// 消息id

	private Integer msgType;// 来源于枚举表：1 ：短信 2：彩信 3： 邮件

	private String sender;// 短信、彩信为服务接入码，邮件为发送人地址

	private String receiver;// 接收人

	private String msgTitle;// 标题

	private String msgCont;// 短信、邮件有内容

	private String filePath;// 彩信、邮件有附件

	private Date sendBeginTime;// 开始发送时间

	private Date sendEndTime;// 结束发送时间

	private Integer sendTtatus;// 来源于枚举表

	/**
	 * @param source
	 * @param msgId
	 * @param msgType
	 * @param sender
	 * @param receiver
	 * @param msgTitle
	 * @param msgCont
	 * @param filePath
	 * @param sendBeginTime
	 * @param sendEndTime
	 * @param sendTtatus
	 */
	public MsgSendEvent(Object source, String msgId, Integer msgType, String sender, String receiver, String msgTitle,
			String msgCont, String filePath, Date sendBeginTime, Date sendEndTime, Integer sendTtatus) {
		super(source);
		this.msgId = msgId;
		this.msgType = msgType;
		this.sender = sender;
		this.receiver = receiver;
		this.msgTitle = msgTitle;
		this.msgCont = msgCont;
		this.filePath = filePath;
		this.sendBeginTime = sendBeginTime;
		this.sendEndTime = sendEndTime;
		this.sendTtatus = sendTtatus;
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

	public Integer getSendTtatus() {
		return sendTtatus;
	}

	public void setSendTtatus(Integer sendTtatus) {
		this.sendTtatus = sendTtatus;
	}

}
