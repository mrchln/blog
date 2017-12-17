package com.flchy.blog.inlets.listener;

import java.lang.reflect.Method;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.flchy.blog.base.enums.OperateCodeEnum;
import com.flchy.blog.base.event.LoggingEventPublish;
import com.flchy.blog.base.response.ResponseCommand;
import com.flchy.blog.utils.ip.InternetProtocol;
import com.flchy.blog.utils.ip.copy.IpUtils;

import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.UserAgent;
/**
 * aop记录日志
 * @author nieqs
 */
@Aspect
@Component
public class ControllerInterceptorLog {
	private static Logger logger = LoggerFactory.getLogger(ControllerInterceptorLog.class);
	private static String  serverIp;
	static {
		serverIp=IpUtils.getIPAddress();
	}
    /** 
     * 拦截器具体实现 
     * @param pjp 
     * @return JsonResult（被拦截方法的执行结果，或需要登录的错误提示。） 
     */  
    @Around("execution(* com.flchy.blog..*(..))  and @annotation(com.flchy.blog.logging.annotation.Visit)") //指定拦截器规则；也可以直接把"execution(* com.xjj………)"写进这里  
    public Object Interceptor(ProceedingJoinPoint pjp){  
    	Date beginDate=DateTime.now().toDate();
        long beginTime = System.currentTimeMillis();  
        MethodSignature signature = (MethodSignature) pjp.getSignature();  
        Method method = signature.getMethod(); //获取被拦截的方法  
        String methodName = method.getName(); //获取被拦截的方法名  
        logger.info("请求开始，方法：{}", methodName);  

        Object result = null;  
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();
        
        String adoptToken =   request.getParameter("token");
    	String remoteAddr = InternetProtocol.getRemoteAddr(request);
    	String ua = request.getHeader("User-Agent");
		//转成UserAgent对象
		UserAgent userAgent = UserAgent.parseUserAgentString(ua); 
		//获取浏览器信息
		Browser browser = userAgent.getBrowser();  
		//浏览器名称
		String browserName = browser.getName();
		 boolean isError=false;
	        String errMsg=null;
	        try {  
	            if(result == null){  
	                // 一切正常的情况下，继续执行被拦截的方法  
	                result = pjp.proceed();  
	            }  
	        } catch (Throwable e) {  
	        	isError=true;
	        	errMsg=e.getMessage();
	            logger.info("exception: ", e);  
	            //异常
	        }  
	        
	        
	        if(result instanceof ResponseCommand){  
	            long costMs = System.currentTimeMillis() - beginTime;  
	            logger.info("{}请求结束，耗时：{}ms", methodName, costMs);  
	            	 LoggingEventPublish.getInstance().visitEvent(adoptToken, "res", "obj", "1", "admin", isError?1:-1, errMsg, serverIp, remoteAddr, ua, browserName, beginDate, DateTime.now().toDate());
	            
	        }
        return  result;
    }
	
}
