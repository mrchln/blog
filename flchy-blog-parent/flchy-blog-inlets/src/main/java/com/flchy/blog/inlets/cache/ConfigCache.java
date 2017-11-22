package com.flchy.blog.inlets.cache;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.flchy.blog.base.holder.AbstractCacheHolder;
import com.flchy.blog.base.holder.SpringContextHolder;
import com.flchy.blog.inlets.service.IConfigService;
import com.flchy.blog.pojo.Config;
/**
 * 
 * @author 测试
 *
 */
@Component
public class ConfigCache extends AbstractCacheHolder {
	private static final Logger LOGGER = LoggerFactory.getLogger(ConfigCache.class);
	private static final String CACHEKEY = "com.flchy.blog.inlets.config";
	private static ConfigCache configCache = new ConfigCache();
	private static final String CACHEKEY_FIELDNAME = "config";

	public static ConfigCache getInstance() {
		if (configCache == null) {
			configCache = new ConfigCache();
		}
		return configCache;
	}

	/**
	 * 缓存启动加载设置缓存对象数据。<br/>
	 * 详细描述：初始化缓存属性信息。<br/>
	 * 使用方式：当服务加载启动时初始化缓存属性值信息。
	 */
	@Override
	public void initialize() {
		// 加载枚举字典表信息到缓存中
		this.saveOrUpdateCacheValue(CACHEKEY_FIELDNAME, this.queryAllList());
	}

	/**
	 * 查询枚举字典表中的信息(全量)。<br/>
	 * 详细描述：设置缓存为有效的，设置数据是有效数据后查询信息。<br/>
	 * 使用方式： 查询枚举字典表全部信息时调用给queryAllList()方法。
	 * 
	 * @return 枚举字典实体数据集合。
	 */
	protected List<Config> queryAllList() {
		return this.getIArticleTypeService().selectList(new EntityWrapper<Config>());
	}

	private IConfigService getIArticleTypeService() {
		return SpringContextHolder.getBean("iConfigService", IConfigService.class);
	}

	/**
	 * 设置缓存的内存划分区域KEY。<br/>
	 * 详细描述：设置缓存的内存划分区域KEY。<br/>
	 * 使用方式：当容器启动时会自动调用 printInitLog()方法。
	 * 
	 * @return cacheKey
	 */
	@Override
	protected String setCacheKey() {
		// TODO Auto-generated method stub
		return CACHEKEY;
	}

	/**
	 * 调用此方法查询数据时，缓存是有效的，但是当缓存中没有数据，则调用数据库查询枚举信息。<br/>
	 * 使用方式：在对外提供调用接口holder中，调用getDataBaseValue方法。
	 * 
	 * @param groupField
	 *            枚举字段。
	 * @param groupId
	 *            枚举字段id值。
	 * @return 从数据库中查询出来的枚举值。
	 */
	@Override
	protected Object getDataBaseValue(String groupPropertyName, String groupPropertyValue) {
		return this.getIArticleTypeService().selectList(new EntityWrapper<Config>());
	}

	@Override
	protected Object getDataBaseValue(String groupPropertyName, String propertyName, String propertyValue) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 刷新缓存调用该方法。<br/>
	 * 详细描述：当刷新缓存，调用该方法。<br/>
	 * 使用方式：当需要刷新缓存的时候调用saveOrUpdateCacheValue(groupField,obj)。
	 * 
	 * @param groupField
	 *            缓存中保存枚举数据的key值。
	 * @param obj
	 *            要保存到缓存中的枚举值。
	 */
	@Override
	protected void saveOrUpdateCacheValue(String groupPropertyName, Object obj) {
		// TODO Auto-generated method stub
		super.addOrUpdateCacheValue(groupPropertyName, obj);
	}

	/**
	 * 清除缓存调用该方法。<br/>
	 * 详细描述：当清除缓存，调用该方法。<br/>
	 * 使用方式：当需要刷新缓存的时候调用clearCacheValue(groupField)。
	 * 
	 * @param groupField
	 *            缓存中保存枚举的key值。
	 */
	@Override
	protected void clearCacheValue(String groupPropertyName) {
		super.removeCacheElement(groupPropertyName);
	}

	/**
	 * 缓存加载日志输出。<br/>
	 * 详细描述：加载缓存的时候输出枚举的日志信息。<br/>
	 * 使用方式：当容器启动时会自动调用 printInitLog()方法。
	 */
	@Override
	public String cacheDesc() {
		return "配置信息字典";
	}

}
