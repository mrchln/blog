package com.flchy.blog.privilege.extend.listener;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.AbstractHandlerMethodMapping;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

import com.flchy.blog.base.holder.SpringContextHolder;
import com.flchy.blog.privilege.config.entity.BaseConfUrlEntity;
import com.flchy.blog.privilege.config.service.IBaseConfUrlService;

/*
 * 
 * 启动扫描MappingRequest地址，入库
 */
public class ApplicationStartupListener implements ApplicationListener<ContextRefreshedEvent> {
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		ApplicationContext applicationContext = event.getApplicationContext();
		AbstractHandlerMethodMapping<RequestMappingInfo> objHandlerMethodMapping = (AbstractHandlerMethodMapping<RequestMappingInfo>) applicationContext.getBean("requestMappingHandlerMapping");
		Map<RequestMappingInfo, HandlerMethod> mapRet = objHandlerMethodMapping.getHandlerMethods();
		if (null != mapRet && !mapRet.isEmpty()) {
			Set<BaseConfUrlEntity> confUrlEntities=new HashSet<>();
             for(RequestMappingInfo requestMapping: mapRet.keySet()){
            	RequestMethodsRequestCondition methodsCondition = requestMapping.getMethodsCondition();
            	Set<RequestMethod> methods = methodsCondition.getMethods();
          		BaseConfUrlEntity confUrlEntity=new BaseConfUrlEntity();
            	for (RequestMethod requestMethod : methods) {
            		confUrlEntity.setMethod(requestMethod.name());
				}
            	Set<String> patterns = requestMapping.getPatternsCondition().getPatterns();
            	for (String string : patterns) {
            		confUrlEntity.setUrlPath(string);
				}
        		confUrlEntities.add(confUrlEntity);
             }
             IBaseConfUrlService baseConfUrlService =  SpringContextHolder.getBean("iBaseConfUrlService", IBaseConfUrlService.class);
             if(null!=baseConfUrlService){
            	 for (BaseConfUrlEntity baseConfUrlEntity : confUrlEntities) {
            		 baseConfUrlService.saveConfUrlByUrlIds(baseConfUrlEntity.getUrlPath(),baseConfUrlEntity.getMethod());
				}
             }
		}
	}
}