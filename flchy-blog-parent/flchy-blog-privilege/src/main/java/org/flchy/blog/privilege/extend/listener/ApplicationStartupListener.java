package org.flchy.blog.privilege.extend.listener;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

public class ApplicationStartupListener implements ApplicationListener<ContextRefreshedEvent> {

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
//		ApplicationContext applicationContext = event.getApplicationContext();
//		AbstractHandlerMethodMapping<RequestMappingInfo> objHandlerMethodMapping = (AbstractHandlerMethodMapping<RequestMappingInfo>) applicationContext.getBean("requestMappingHandlerMapping");
//		Map<RequestMappingInfo, HandlerMethod> mapRet = objHandlerMethodMapping.getHandlerMethods();
//		if (null != mapRet && !mapRet.isEmpty()) {
//			Set<PatternsRequestCondition> requestConditionSet = new HashSet<>();
//             for(RequestMappingInfo requestMapping: mapRet.keySet()){
//            	 requestConditionSet.add(requestMapping.getPatternsCondition());
//             }
//             IBaseConfUrlService baseConfUrlService =  SpringContextHolder.getBean("iBaseConfUrlService", IBaseConfUrlService.class);
//             if(null!=baseConfUrlService){
//            	 for(PatternsRequestCondition requestCondition: requestConditionSet){
//            		 Set<String> patternUrls = requestCondition.getPatterns();
//            		 if(!StringUtil.isNullOrEmpty(patternUrls)){
//            			 for(String urlPath :patternUrls){
//            				 baseConfUrlService.saveConfUrlByUrlIds(urlPath);
//            			 }
//            		 }
//            	 }
//             }
             
//		}
		
	}

}
