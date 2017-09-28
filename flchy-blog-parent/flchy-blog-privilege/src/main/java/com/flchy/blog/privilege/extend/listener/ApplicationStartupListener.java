package com.flchy.blog.privilege.extend.listener;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.AbstractHandlerMethodMapping;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

import com.flchy.blog.base.holder.SpringContextHolder;
import com.flchy.blog.pojo.ConfUrl;
import com.flchy.blog.privilege.config.service.IConfUrlService;
import com.flchy.blog.utils.StringUtil;
/**
 * 自动更新url地址到数据库
 * @author 1st
 *
 */
public class ApplicationStartupListener implements ApplicationListener<ContextRefreshedEvent> {

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		ApplicationContext applicationContext = event.getApplicationContext();
		@SuppressWarnings("unchecked")
		AbstractHandlerMethodMapping<RequestMappingInfo> objHandlerMethodMapping = (AbstractHandlerMethodMapping<RequestMappingInfo>) applicationContext
				.getBean("requestMappingHandlerMapping");
		Map<RequestMappingInfo, HandlerMethod> mapRet = objHandlerMethodMapping.getHandlerMethods();
		if (null != mapRet && !mapRet.isEmpty()) {
			Set<PatternsRequestCondition> requestConditionSet = new HashSet<>();
			for (RequestMappingInfo requestMapping : mapRet.keySet()) {
				requestConditionSet.add(requestMapping.getPatternsCondition());
			}
			IConfUrlService confUrlService = SpringContextHolder.getBean("iConfUrlService", IConfUrlService.class);
			if (null != confUrlService) {
				boolean isUpdate=false;
				List<ConfUrl> confurlAll = confUrlService.getConfurlAll();
				for (PatternsRequestCondition requestCondition : requestConditionSet) {
					Set<String> patternUrls = requestCondition.getPatterns();
					if (!StringUtil.isNullOrEmpty(patternUrls)) {
						for (String urlPath : patternUrls) {
							List<ConfUrl> collect = confurlAll.stream().filter(l->l.getUrlPath().equals(urlPath)).collect(Collectors.toList());
							if(collect==null||collect.isEmpty()||collect.size()<1){
								isUpdate=true;
								confUrlService.saveConfUrlByUrlIds(urlPath);
							}
						}
					}
				}
				if(isUpdate){
					confUrlService.replaceConfurlAll();
				}
			}
		}

	}

}
