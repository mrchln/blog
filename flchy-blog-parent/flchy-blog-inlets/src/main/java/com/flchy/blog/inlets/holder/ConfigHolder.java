package com.flchy.blog.inlets.holder;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.flchy.blog.base.holder.SpringContextHolder;
import com.flchy.blog.base.service.ScheduledService;
import com.flchy.blog.inlets.cache.ArticleTypeCache;
import com.flchy.blog.inlets.cache.ConfigCache;
import com.flchy.blog.inlets.service.IArticleTypeService;
import com.flchy.blog.inlets.service.IConfigService;
import com.flchy.blog.pojo.ArticleType;
import com.flchy.blog.pojo.Config;

import net.sf.ehcache.Element;
/**
 * 
 * 
 * @Description:  
 * @author nieqs
 *
 */
// implements ScheduledService 10分钟刷新一次
@Component
public class ConfigHolder {
	private static final Logger logger = LoggerFactory.getLogger(ConfigHolder.class);
	
	/** CACHEKEY_FIELDNAME:枚举分组 关系配置表缓存 */
	private static final String CACHEKEY_FIELDNAME = "config";

	/**
	 * 刷新枚举值缓存数据。<br/> 
	 * 详细描述：调用枚举缓存方法，刷新缓存<br/> 
	 * 使用方式：对外提供调用refreshCache()方法
	 */
	public static void refreshCache() {
		ConfigCache.getInstance().initialize();
	}
	/**
	 * 查询全部
	 * @return
	 */
	public static List<Config> getConfig(){
		Element element = (Element) ConfigCache.getInstance().getCacheAllValue(CACHEKEY_FIELDNAME);
		if (element != null && !element.isExpired()) {
			@SuppressWarnings("unchecked")
			List<Config> list= (List<Config>) element.getObjectValue();
			return list;
		}
		logger.info("未查询到缓存,查询数据库！！");
		return SpringContextHolder.getBean("iConfigService", IConfigService.class).selectList(new EntityWrapper<Config>());
	}
	/**
	 * 从缓存中筛选
	 * @param key
	 * @return
	 */
	public static Config getConfigByKey(String key){
		Element element = (Element) ConfigCache.getInstance().getCacheAllValue(CACHEKEY_FIELDNAME);
		if (element != null && !element.isExpired()) {
			@SuppressWarnings("unchecked")
			List<Config> list= (List<Config>) element.getObjectValue();
			if(key!=null){
				list=list.stream().filter(l->l.getKey().equals(key)).collect(Collectors.toList());
			}
			return list!=null? list.get(0):null;
		}
		logger.info("未查询到缓存,查询数据库！！");
		Config entity=new Config();
		if(key!=null){
			entity.setKey(key);
		}
		return SpringContextHolder.getBean("iConfigService", IConfigService.class).selectList(new EntityWrapper<Config>(entity)).get(0);
	}
	
	public static String getConfig(String key){
		Config configByKey = getConfigByKey(key);
		return configByKey.getConfig();
	}
	
	public String getName() {
		return "刷新文章类型字典";
	}
	public void schedule() {
		ConfigCache.getInstance().initialize();
	}
	
}
