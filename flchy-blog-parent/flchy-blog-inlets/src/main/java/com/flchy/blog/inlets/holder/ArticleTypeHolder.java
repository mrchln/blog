package com.flchy.blog.inlets.holder;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.flchy.blog.base.holder.SpringContextHolder;
import com.flchy.blog.common.service.ScheduledService;
import com.flchy.blog.inlets.cache.ArticleTypeCache;
import com.flchy.blog.inlets.entity.ArticleType;
import com.flchy.blog.inlets.service.IArticleTypeService;

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
public class ArticleTypeHolder {
	private static final Logger logger = LoggerFactory.getLogger(ArticleTypeHolder.class);
	
	/** CACHEKEY_FIELDNAME:枚举分组 关系配置表缓存 */
	private static final String CACHEKEY_FIELDNAME = "articleType";

	/**
	 * 刷新枚举值缓存数据。<br/> 
	 * 详细描述：调用枚举缓存方法，刷新缓存<br/> 
	 * 使用方式：对外提供调用refreshCache()方法
	 */
	public static void refreshCache() {
		ArticleTypeCache.getInstance().initialize();
	}
	/**
	 * 从缓存中筛选
	 * @param appPlatId
	 * @return
	 */
	public static List<ArticleType> getArticleType(Integer id){
		Element element = (Element) ArticleTypeCache.getInstance().getCacheAllValue(CACHEKEY_FIELDNAME);
		if (element != null && !element.isExpired()) {
			@SuppressWarnings("unchecked")
			List<ArticleType> list= (List<ArticleType>) element.getObjectValue();
			if(id!=null){
				list=list.stream().filter(l->l.getId()==id).collect(Collectors.toList());
			}
			return list;
		}
		logger.info("未查询到缓存,查询数据库！！");
		ArticleType entity=new ArticleType();
		if(id!=null){
			entity.setId(id);
		}
		return SpringContextHolder.getBean("iArticleTypeService", IArticleTypeService.class).selectList(new EntityWrapper<ArticleType>(entity).orderBy("`order`,id", true));
	}
	
	
	public String getName() {
		return "刷新文章类型字典";
	}
	public void schedule() {
		ArticleTypeCache.getInstance().initialize();
	}
	
}
