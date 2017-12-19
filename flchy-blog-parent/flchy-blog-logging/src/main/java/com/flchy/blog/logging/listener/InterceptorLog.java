package com.flchy.blog.logging.listener;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.flchy.blog.base.annotation.Log;
import com.flchy.blog.base.enums.OperateCodeEnum;
import com.flchy.blog.base.event.LoggingEventPublish;
import com.flchy.blog.logging.bean.ErrorMsgBean;
import com.flchy.blog.utils.ip.InternetProtocol;
import com.flchy.blog.utils.ip.copy.IpUtils;

import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;

/**
 * aop记录日志
 * 
 * @author nieqs
 */
@Aspect
@Component
public class InterceptorLog {
//	private static Logger logger = LoggerFactory.getLogger(InterceptorLog.class);
	private ThreadLocal<Long> startTime = new ThreadLocal<Long>();
	private static String serverIp;
	static {
		serverIp = IpUtils.getIPAddress();
	}

	/**
	 * 定义拦截规则
	 */
	@Pointcut("@annotation(com.flchy.blog.base.annotation.Log)")
	protected void pointcuts() {
	}

	/**
	 * 执行前 记录 HTTP 请求详细
	 * 
	 * @param joinPoint
	 *            joinPoint
	 */
	@Before("pointcuts()")
	public void logBefore(JoinPoint joinPoint) {
		// 开始计时
		startTime.set(System.currentTimeMillis());
	}

	/**
	 * 执行后 请求结束, 记录返回内容
	 * 
	 * @param result
	 *            响应内容
	 */
	@AfterReturning(pointcut = "pointcuts()", returning = "result")
	public void logAfterReturning(JoinPoint joinPoint, Object result) {
		Long start = startTime.get();
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		long endTime = System.currentTimeMillis() - start;
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod(); // 获取被拦截的方法
		String methodName = method.getName(); // 获取被拦截的方法名
		String adoptToken = request.getParameter("token");
		String ua = request.getHeader("User-Agent");
		// 转成UserAgent对象
		UserAgent userAgent = UserAgent.parseUserAgentString(ua);
		// 获取浏览器信息
		Browser browser = userAgent.getBrowser();
		// 浏览器名称
		String browserName = browser.getName();
		Log annotation = method.getAnnotation(Log.class);
		String value = annotation.value();
		OperateCodeEnum type = annotation.type();
		String remoteAddr = InternetProtocol.getRemoteAddr(request);
		if (type.equals(OperateCodeEnum.PUBLIC)) {
			Map<String, Object> cookieMap = new HashMap<>();
			Cookie[] cookies = request.getCookies();
			if (null != cookies) {
				for (Cookie cookie : cookies) {
					cookieMap.put(cookie.getName(), cookie);
				}
			}
			// 获取系统信息
			OperatingSystem os = userAgent.getOperatingSystem();
			// 系统名称
			String system = os.getName();
			LoggingEventPublish.getInstance().publicLogEvent(value, JSONArray.toJSONString(joinPoint.getArgs()),
					JSON.toJSONString(cookieMap), new Date(start), DateTime.now().toDate(), -1, null, serverIp,
					remoteAddr, ua, browserName, system);
		} else {
			if (type.equals(OperateCodeEnum.SELECT)) {
				LoggingEventPublish.getInstance().visitEvent(adoptToken, methodName, value, "1", "admin", -1, null,
						serverIp, remoteAddr, ua, browserName, new Date(start), DateTime.now().toDate());
			} else {
				ErrorMsgBean bean = new ErrorMsgBean();
				bean.setMethodName(methodName);
				bean.setParameters(JSONArray.toJSONString(joinPoint.getArgs()));
				bean.setTime(endTime + "");
				// bean.setResult(result.toString());
				LoggingEventPublish.getInstance().usageEvent(adoptToken, methodName, type, value,
						JSON.toJSONString(bean), "userId", "admin", new Date(start), DateTime.now().toDate(), -1, null,
						serverIp, remoteAddr, ua, browserName);
			}
		}

	}

	/**
	 * 异常通知 用于拦截记录异常日志
	 * 
	 * @param joinPoint
	 * @param e
	 */
	@AfterThrowing(pointcut = "pointcuts()", throwing = "e")
	public void doAfterThrowing(JoinPoint joinPoint, Throwable e) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		Long start = startTime.get();
		long endTime = System.currentTimeMillis() - start;
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod(); // 获取被拦截的方法
		String methodName = method.getName(); // 获取被拦截的方法名
		String adoptToken = request.getParameter("token");
		String ua = request.getHeader("User-Agent");
		// 转成UserAgent对象
		UserAgent userAgent = UserAgent.parseUserAgentString(ua);
		// 获取浏览器信息
		Browser browser = userAgent.getBrowser();
		// 浏览器名称
		String browserName = browser.getName();
		Log annotation = method.getAnnotation(Log.class);
		String value = annotation.value();
		OperateCodeEnum type = annotation.type();
		String remoteAddr = InternetProtocol.getRemoteAddr(request);
		ErrorMsgBean bean = new ErrorMsgBean();
		bean.setErrorCode(e.getClass().getName());
		bean.setMessage(e.getMessage());
		bean.setMethodName(methodName);
		bean.setParameters(JSONArray.toJSONString(joinPoint.getArgs()));
		bean.setTime(endTime + "");
		if (type.equals(OperateCodeEnum.PUBLIC)) {
			Map<String, Object> cookieMap = new HashMap<>();
			Cookie[] cookies = request.getCookies();
			if (null != cookies) {
				for (Cookie cookie : cookies) {
					cookieMap.put(cookie.getName(), cookie);
				}
			}
			// 获取系统信息
			OperatingSystem os = userAgent.getOperatingSystem();
			// 系统名称
			String system = os.getName();
			LoggingEventPublish.getInstance().publicLogEvent(value, JSONArray.toJSONString(joinPoint.getArgs()),
					JSON.toJSONString(cookieMap), new Date(start), DateTime.now().toDate(), 1, e.getMessage(), serverIp,
					remoteAddr, ua, browserName, system);
		} else {
			if (type.equals(OperateCodeEnum.SELECT)) {
				LoggingEventPublish.getInstance().visitEvent(adoptToken, methodName, value, "1", "admin", 1,
						JSON.toJSONString(bean), serverIp, remoteAddr, ua, browserName, new Date(start),
						DateTime.now().toDate());
			} else {
				LoggingEventPublish.getInstance().usageEvent(adoptToken, methodName, type, value,
						JSON.toJSONString(bean), "userId", "admin", new Date(start), DateTime.now().toDate(), 1,
						e.getMessage(), serverIp, remoteAddr, ua, browserName);
			}
		}
	}

	/**
	 * 拦截器具体实现
	 * 
	 * @param pjp
	 * @return JsonResult（被拦截方法的执行结果，或需要登录的错误提示。）
	 *//*
		 * @Around("execution(* com.flchy.blog..*(..))  and @annotation(com.flchy.blog.logging.annotation.Visit)"
		 * ) //指定拦截器规则；也可以直接把"execution(* com.xjj………)"写进这里 public Object
		 * Interceptor(ProceedingJoinPoint pjp){ Date
		 * beginDate=DateTime.now().toDate(); long beginTime =
		 * System.currentTimeMillis(); MethodSignature signature =
		 * (MethodSignature) pjp.getSignature(); Method method =
		 * signature.getMethod(); //获取被拦截的方法 String methodName =
		 * method.getName(); //获取被拦截的方法名 logger.info("请求开始，方法：{}", methodName);
		 * Object result = null; RequestAttributes ra =
		 * RequestContextHolder.getRequestAttributes(); ServletRequestAttributes
		 * sra = (ServletRequestAttributes) ra; HttpServletRequest request =
		 * sra.getRequest();
		 * 
		 * String adoptToken = request.getParameter("token"); String remoteAddr
		 * = InternetProtocol.getRemoteAddr(request); String ua =
		 * request.getHeader("User-Agent"); //转成UserAgent对象 UserAgent userAgent
		 * = UserAgent.parseUserAgentString(ua); //获取浏览器信息 Browser browser =
		 * userAgent.getBrowser(); //浏览器名称 String browserName =
		 * browser.getName(); boolean isError=false; String errMsg=null; try {
		 * if(result == null){ // 一切正常的情况下，继续执行被拦截的方法 result = pjp.proceed(); }
		 * } catch (Throwable e) { isError=true; errMsg=e.getMessage();
		 * logger.info("exception: ", e); //异常 } long costMs =
		 * System.currentTimeMillis() - beginTime; Visit annotation =
		 * method.getAnnotation(Visit.class); String value = annotation.value();
		 * logger.info("{}请求结束，耗时：{}ms", methodName, costMs);
		 * LoggingEventPublish.getInstance().visitEvent(adoptToken, "res",
		 * value, "1", "admin", isError?1:-1, errMsg, serverIp, remoteAddr, ua,
		 * browserName, beginDate, DateTime.now().toDate()); return result; }
		 */

}
