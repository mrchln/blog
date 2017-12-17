package com.flchy.blog.base.event;



import java.util.Date;

import org.joda.time.DateTime;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;

import com.flchy.blog.base.enums.MsgTypeEnum;
import com.flchy.blog.base.enums.OperateCodeEnum;
import com.flchy.blog.base.enums.StatusEnum;
import com.flchy.blog.base.event.impl.InvokeEvent;
import com.flchy.blog.base.event.impl.LoginEvent;
import com.flchy.blog.base.event.impl.MsgSendEvent;
import com.flchy.blog.base.event.impl.UsageEvent;
import com.flchy.blog.base.event.impl.VisitEvent;
import com.flchy.blog.base.holder.SpringContextHolder;

/**
 * @Title:Web事件发布 对应LoggingEventListener(ApplicationListener),由LoggingEventListener继续处理
 *                例如发布日志：LoggingEventPublish.getInstance().operateEvent(OperateCodeEnum.LOOK,"跳转到内页查看",
 *                false, null, destObj, resCode, startTime, new Date());
 * @author xucong
 */
@Component
public class LoggingEventPublish implements ApplicationEventPublisherAware {

	public static LoggingEventPublish getInstance() {
		return SpringContextHolder.getBean(LoggingEventPublish.class);
	}

	private ApplicationEventPublisher eventPublisher;

	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		this.eventPublisher = applicationEventPublisher;
	}

	/**
	 * 登录日志
	 * 
	 * @param userId
	 * @param mainUserId
	 * @param serverIp
	 * @param clientIp
	 * @param userAgent
	 * @param browserType
	 * @param loginTime
	 */
	public void loginEvent(String userId, String sessionId,String mainUserId, String serverIp, String clientIp, String userAgent, String browserType, Date loginTime) {
		LoginEvent loginEvent = new LoginEvent(this, userId,sessionId, mainUserId, serverIp, clientIp, userAgent, browserType, DateTime.now().toDate());
		this.eventPublisher.publishEvent(loginEvent);
	}

	/**
	 * 访问日志
	 * 
	 * @param menuId
	 * @param visitBeginTime
	 * @param visitEndTime
	 * @param isSuccess
	 * @param errorMsg
	 */
	public void visitEvent( String sessionId,String resId, String objId, String oprUserId, String mainAccount, Integer isError, String errMsg, String serverIp, String clientIp, String userAgent, String browserType, Date visitBeginTime, Date visitEndTime) {
		VisitEvent visitResEvent = new VisitEvent(this, sessionId,resId, objId, oprUserId, mainAccount, isError, errMsg, serverIp, clientIp, userAgent, browserType, visitBeginTime, visitEndTime);
		this.eventPublisher.publishEvent(visitResEvent);
	}

	/**
	 * 操作日志
	 * 
	 * @param operateCodeEnum
	 * @param content
	 * @param isError
	 * @param ErrorMsg
	 * @param destObj
	 * @param menuId
	 * @param beginTime
	 * @param endTime
	 */
	public void usageEvent( String sessionId, String resId, OperateCodeEnum operateCodeEnum,String oprObj, String oprCont, String oprUserId, String mainAccount, Date oprBeginTime, Date oprEndTime, Integer isError, String errMsg, String serverIp, String clientIp, String userAgent, String browserType) {
		UsageEvent event = new UsageEvent(this, sessionId, resId, operateCodeEnum.getValue(), oprObj, oprCont, oprUserId, mainAccount, oprBeginTime, oprEndTime, isError, errMsg, serverIp, clientIp, userAgent, browserType);
		this.eventPublisher.publishEvent(event);
	}

	/**
	 * 接口调用日志
	 * 
	 * @param interCode
	 * @param invokeCont
	 * @param isFail
	 * @param errMsg
	 * @param invokeBeginTime
	 * @param invokeEndTime
	 */
	public void invokeEvent(Integer interfaceId, String invokeCont, StatusEnum isFail, String errMsg, Date invokeBeginTime, Date invokeEndTime, String oprUserId, String mainAccount, String serverIp) {
		InvokeEvent event=new InvokeEvent(this, interfaceId, invokeCont, isFail.getValue(), errMsg, invokeBeginTime, invokeEndTime, oprUserId, mainAccount, serverIp);
		this.eventPublisher.publishEvent(event);
	}
	
	/**
	 * 短信、彩信、邮件推送日志
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
	public void msgSendEvent(String msgId, MsgTypeEnum msgType, String sender, String receiver, String msgTitle, String msgCont, String filePath, Date sendBeginTime, Date sendEndTime, StatusEnum sendTtatus){
		MsgSendEvent event=new MsgSendEvent(this, msgId, msgType.getValue(), sender, receiver, msgTitle, msgCont, filePath, sendBeginTime, sendEndTime, sendTtatus.getValue());
		this.eventPublisher.publishEvent(event);
	}

}
