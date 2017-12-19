package com.flchy.blog.logging.listener;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.flchy.blog.base.event.AbstractWebVisitEvent;
import com.flchy.blog.base.event.impl.InvokeEvent;
import com.flchy.blog.base.event.impl.LoginEvent;
import com.flchy.blog.base.event.impl.MsgSendEvent;
import com.flchy.blog.base.event.impl.PublicLogEvent;
import com.flchy.blog.base.event.impl.UsageEvent;
import com.flchy.blog.base.event.impl.VisitEvent;
import com.flchy.blog.base.holder.SpringContextHolder;
import com.flchy.blog.logging.AbstractLog;
import com.flchy.blog.utils.IDGenerator;
import com.flchy.blog.utils.NewMapUtil;


/**
 * @Title:Web访问监听,用于web访问事件发生时,记录日志，对应loggingEventPublish (ApplicationEventPublisherAware) 日志注册发布 author
 */
@Component
public class LoggingEventListener implements ApplicationListener<AbstractWebVisitEvent> {

	/**
	 * 日志
	 */
    @Override
	public void onApplicationEvent(AbstractWebVisitEvent event) {
    	AbstractLog bean=null;
		// 登录、注销事件
		if (event instanceof LoginEvent) {
			LoginEvent loginEvent = (LoginEvent) event;
			bean= SpringContextHolder.getBean("loginLog", AbstractLog.class);
			bean.start(new NewMapUtil().set("log_id",IDGenerator.getId())
					.set("session_id",loginEvent.getSessionId())
					.set("opr_user_id",loginEvent.getUserId())
					.set("main_account",loginEvent.getMainUserId())
					.set("server_ip",loginEvent.getServerIp())
					.set("client_ip",loginEvent.getClientIp())
					.set("login_time",loginEvent.getLoginTime())
					.set("user_agent",loginEvent.getUserAgent())
					.set("browser_type",loginEvent.getBrowserType())
					.get());
		}
		// 菜单点击事件
		if (event instanceof VisitEvent) {
			VisitEvent visitEvent = (VisitEvent) event;
			bean= SpringContextHolder.getBean("visitLog", AbstractLog.class);
			bean.start(new NewMapUtil().set("log_id",IDGenerator.getId())
					.set("session_id",visitEvent.getSessionId())
					.set("res_id",visitEvent.getResId())
					.set("obj_id",visitEvent.getObjId())
					.set("opr_user_id",visitEvent.getOprUserId())
					.set("main_account",visitEvent.getMainAccount())
					.set("visit_begin_time",visitEvent.getVisitBeginTime())
					.set("visit_end_time",visitEvent.getVisitEndTime())
					.set("is_error",visitEvent.isError())
					.set("err_msg",visitEvent.getErrMsg())
					.set("server_ip",visitEvent.getServerIp())
					.set("client_ip",visitEvent.getClientIp())
					.set("user_agent",visitEvent.getUserAgent())
					.set("browser_type",visitEvent.getBrowserType())
					.get());
		
		}
		// 功能操作事件
		if (event instanceof UsageEvent) {
			UsageEvent usageEvent = (UsageEvent) event;
			bean= SpringContextHolder.getBean("operateLog", AbstractLog.class);
			bean.start(new NewMapUtil().set("log_id",IDGenerator.getId())
					.set("session_id",usageEvent.getSessionId())
					.set("res_id",usageEvent.getResId())
					.set("opr_type",usageEvent.getOprType())
					.set("opr_obj",usageEvent.getOprObj())
					.set("opr_cont",usageEvent.getOprCont())
					.set("opr_user_id",usageEvent.getOprUserId())
					.set("main_account",usageEvent.getMainAccount())
					.set("opr_begin_time",usageEvent.getOprBeginTime())
					.set("opr_end_time",usageEvent.getOprEndTime())
					.set("is_error",usageEvent.isError())
					.set("err_msg",usageEvent.getErrMsg())
					.set("server_ip",usageEvent.getServerIp())
					.set("client_ip",usageEvent.getClientIp())
					.set("user_agent",usageEvent.getUserAgent())
					.set("browser_type",usageEvent.getBrowserType())
					.get());
			
		}
		// 与外部系统服务接口调用事件
		if (event instanceof InvokeEvent) {
			InvokeEvent invokeEvent = (InvokeEvent) event;
			bean= SpringContextHolder.getBean("serverInvokeLog", AbstractLog.class);
			bean.start(new NewMapUtil().set("log_id",IDGenerator.getId())
					.set("interface_id",invokeEvent.getInterfaceId())
					.set("invoke_cont",invokeEvent.getInvokeCont())
					.set("is_fail",invokeEvent.getIsFail())
					.set("err_msg",invokeEvent.getErrMsg())
					.set("invoke_begin_time",invokeEvent.getInvokeBeginTime())
					.set("invoke_end_time",invokeEvent.getInvokeEndTime())
					.set("opr_user_id",invokeEvent.getOprUserId())
					.set("main_account",invokeEvent.getMainAccount())
					.set("server_ip",invokeEvent.getServerIp())
					.get());
		}
		// 短信、彩信、邮件推送日志事件
		if (event instanceof MsgSendEvent) {
			MsgSendEvent sendEvent = (MsgSendEvent) event;
			bean= SpringContextHolder.getBean("msgSendLog", AbstractLog.class);
			bean.start(new NewMapUtil().set("log_id",IDGenerator.getId())
					.set("msg_id",sendEvent.getMsgId())
					.set("msg_type",sendEvent.getMsgType())
					.set("sender",sendEvent.getSender())
					.set("receiver",sendEvent.getReceiver())
					.set("msg_title",sendEvent.getMsgTitle())
					.set("msg_cont",sendEvent.getMsgCont())
					.set("file_path",sendEvent.getFilePath())
					.set("send_begin_time",sendEvent.getSendBeginTime())
					.set("send_end_time",sendEvent.getSendEndTime())
					.set("send_status",sendEvent.getSendTtatus())
					.get());
		}
		if(event instanceof PublicLogEvent){
			PublicLogEvent publicLogEvent=(PublicLogEvent) event;
			bean= SpringContextHolder.getBean("publicLog", AbstractLog.class);
			bean.start(new NewMapUtil().set("log_id",IDGenerator.getId())
					.set("opr_obj",publicLogEvent.getOprObj())
					.set("opr_cont",publicLogEvent.getOprCont())
					.set("account",publicLogEvent.getAccount())
					.set("begin_time",publicLogEvent.getBeginTime())
					.set("end_time",publicLogEvent.getEndTime())
					.set("is_error",publicLogEvent.getIsError())
					.set("err_msg",publicLogEvent.getErrMsg())
					.set("server_ip",publicLogEvent.getServerip())
					.set("client_ip",publicLogEvent.getClientIp())
					.set("user_agent",publicLogEvent.getUserAgent())
					.set("browser_type",publicLogEvent.getBrowserType())
					.set("system",publicLogEvent.getSystem())
					.get());
		}
	}
}
