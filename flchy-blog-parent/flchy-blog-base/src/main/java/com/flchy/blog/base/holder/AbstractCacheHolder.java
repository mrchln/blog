package com.flchy.blog.base.holder;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import com.flchy.blog.base.enums.BaseExceptionEnums;
import com.flchy.blog.base.exception.BaseRuntimeException;
import com.flchy.blog.base.initialize.IWebInitializable;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

/**
 * AbstractCacheHolder是一个抽象类，实现了IWebInitializable接口， 
 * 该抽象类提供了操作缓存的常用方法，同时还包装了一层需要继承者实现这些方法供外部调用， 
 * 无需关注内部实现。
 * 
 * @since 0.0.1-SNAPSHOT
 */
@DependsOn({"springContextHolder"})
@Service
public abstract class AbstractCacheHolder implements IWebInitializable {
    private static final Logger logger = LoggerFactory.getLogger(AbstractCacheHolder.class);

    private CacheManager getCacheManager() {
        return SpringContextHolder.getBean("ehCacheManagerFactory");
    }

    /**
     * 根据element的key获取对应的value。<br/>
     * 详细描述：根据propertyName获取缓存中存放的数据，在通过propertyName定位到该数据集合中的字段，在找到符合propertyValue值的数据。<br/>
     * 使用方式：该类被继承后直接使用super.getCacheValue或者用this方式都可以调用。
     * 
     * @param cacheNodeKey 缓存区域中的element的key。
     * @param propertyName 集合数据中的字段名称。
     * @param propertyValue 集合数据中该字段的具体值。
     * @return 参数对应的查询到的具体值。
     */
	public Object getCacheValue(String cacheNodeKey, String propertyName, String propertyValue) {
		Cache cache = this.getCacheManager().getCache(this.setCacheKey());
		if (null != cache) {
			Element element = cache.get(cacheNodeKey);
			if (null != element) {
				return getGroupListById(element.getObjectValue(), propertyName, propertyValue);
			} else {
				return getDataBaseValue(cacheNodeKey, propertyName, propertyValue);
			}
		} else {
			logger.error(this.setCacheKey() + "缓存区域不存在！");
			throw new BaseRuntimeException(BaseExceptionEnums.NO_EXIST_CACHE).setParams(new Object[] { this.setCacheKey() });
		}
	}

    /**
     * 获取缓存区域中key为propertyName的数据。<br/>
     * 详细描述：根据传入propertyName的值，从缓存中读取key为propertyName的数据。<br/>
     * 使用方式：该类被继承后直接使用super.getCacheAllValue或者用this方式都可以调用。
     * 
     * @param cacheNodeKey 缓存区域中的element的key。
     * @return 缓存区域中propertyName的数据。
     */
	public Object getCacheAllValue(String cacheNodeKey) {
		Cache cache = this.getCacheManager().getCache(this.setCacheKey());
		if (null != cache) {
			return cache.get(cacheNodeKey);
		} else {
			logger.error(this.setCacheKey() + "缓存区域不存在！");
			throw new BaseRuntimeException(BaseExceptionEnums.NO_EXIST_CACHE).setParams(new Object[] { this.setCacheKey() });
		}
	}

    /**
     * 解析obj，获取和propertyValue值相匹配的数据。<br/>
     * 详细描述：解析obj数据对象中，propertyName字段并且值为propertyValue的数据集合。<br/>
     * 使用方式：该类被继承后直接使用super.getGroupListById或者用this方式都可以调用。
     * 
     * @param propertyName 数据中的key值。
     * @param propertyValue 该数据key值对应的具体值。
     * @param obj 需要被解析的数据对象，可以是list、map。
     */
    @SuppressWarnings("unchecked")
	public List<Object> getGroupListById(Object obj, String propertyName, String propertyValue) {
		List<Object> groupList = new ArrayList<Object>();
		if (obj instanceof List) {
			List<Object> list = (List<Object>) obj;
			for (Object object : list) {
				if (object instanceof Map) {
					Map<Object, Object> map = (Map<Object, Object>) object;
					if (propertyValue.equals(String.valueOf(map.get(propertyName)))) {
						groupList.add(map);
					}
				} else {
					try {
						Field[] fieldArray = object.getClass().getDeclaredFields();
						Field.setAccessible(fieldArray, true);
						for (int i = 0; i < fieldArray.length; i++) {
							Field field = fieldArray[i];
							if (field.getName().equals(propertyName)) {
								if (propertyValue.equals(field.get(object))) {
									groupList.add(object);
								}
							}
						}
					} catch (SecurityException e) {
						logger.error("AbstractCacheHolder class (getGroupListById method) SecurityException：", e);
					} catch (IllegalArgumentException e) {
						logger.error("AbstractCacheHolder class (getGroupListById method) IllegalArgumentException：", e);
					} catch (IllegalAccessException e) {
						logger.error("AbstractCacheHolder class (getGroupListById method) IllegalAccessException：", e);
					}
				}
			}
		}
		return groupList;
	}

    /**
     * 把传入的obj数据对象保存或更新到缓存key为propertyName的区域。<br/>
     * 详细描述：把数据对象obj缓存到key值propertyName的区域，该方法会先删除该区域的数据，在进行添加。<br/>
     * 使用方式：该类被继承后直接使用super.addOrUpdateCacheValue或者用this方式都可以调用。
     * 
     * @param cacheNodeKey 缓存区域中对应的key值。
     * @param obj 要保存或更新的缓存数据。
     */
	public void addOrUpdateCacheValue(String cacheNodeKey, Object obj) {
		Cache cache = this.getCacheManager().getCache(this.setCacheKey());
		if (null != cache) {
			Element element = new Element(cacheNodeKey, obj);
			cache.remove(cacheNodeKey);
			cache.put(element);
		} else {
			logger.error(this.setCacheKey() + "缓存区域不存在！");
			throw new BaseRuntimeException(BaseExceptionEnums.NO_EXIST_CACHE).setParams(new Object[] { this.setCacheKey() });
		}
	}

    /**
     * 根据propertyName清除该缓存区域。<br/>
     * 详细描述：根据传入的propertyName值，清除缓存中对应该值的区域。<br/>
     * 使用方式：该类被继承后直接使用super.removeCacheElement或者用this方式都可以调用。
     * 
     * @param cacheNodeKey 缓存区域中对应的key值。
     */
	public void removeCacheElement(String cacheNodeKey) {
		Cache cache = this.getCacheManager().getCache(this.setCacheKey());
		if (null != cache) {
			cache.remove(cacheNodeKey);
		} else {
			logger.error(this.setCacheKey() + "缓存区域不存在！");
			throw new BaseRuntimeException(BaseExceptionEnums.NO_EXIST_CACHE).setParams(new Object[] { this.setCacheKey() });
		}
	}

    /**
     * 清除缓存中的所有区域。<br/>
     * 详细描述：清空所有的缓存区域。<br/>
     * 使用方式：该类被继承后直接使用super.clearCacheAll或者用this方式都可以调用。
     */
    public void clearCacheAll() {
        this.getCacheManager().clearAll();
    }

    /**
     * 供全局使用的缓存区域的key值。<br/>
     * 详细描述：子类继承该抽象类后，需要实现该方法，子类中全局使用的缓存区域key值作为该方法返回值供全局使用。<br/>
     * 使用方式：该类被继承后直接使用super.setCacheKey或者用this方式都可以调用。
     * 
     * @return 缓存区域的key值。
     */
    protected abstract String setCacheKey();

    /**
     * 根据参数到数据库中查询值为propertyValue的数据。<br/>
     * 详细描述：缓存区中不存在该数据，则进行回调到数据库中查询值为propertyValue的数据。<br/>
     * 使用方式：该方法主要是为了实现回调，首先到缓存中查询，如果不存在会自动调用该方法到数据库中查询。
     * 
     * @param cacheNodeKey 缓存区域的key值。
     * @param propertyName 数据集合中的字段名称。
     * @param propertyValue 数据集合中该字段名称的具体值。
     * @return 数据库中查询的结果集。
     */
    protected abstract Object getDataBaseValue(String cacheNodeKey, String propertyName, String propertyValue);

    /**
     * 该方法抽象了saveOrUpdateCacheValue，供外层进行调用。<br/>
     * 详细描述：子类实现该方法，实质是调用父类中的saveOrUpdateCacheValue方法，只是在其方法之外包了一层供外部使用。<br/>
     * 使用方式：子类实现该方法。
     * 
     * @param cacheNodeKey 缓存区域中对应的key值。
     * @param obj 数据对象。
     */
    protected abstract void saveOrUpdateCacheValue(String cacheNodeKey, Object obj);

    /**
     * 该方法抽象了removeCacheElement，供外层进行调用。<br/>
     * 详细描述：子类实现该方法，实质是调用父类中的removeCacheElement方法，只是在其方法之外包了一层供外部使用。<br/>
     * 使用方式：子类实现该方法。
     * 
     * @param cacheNodeKey 缓存区域中对应的key值。
     */
    protected abstract void clearCacheValue(String cacheNodeKey);

    /**
     * 设置初始化时类的依赖关系。<br/>
     * 详细描述：该方法是IWebInitializable接口中的方法，需要子类重写来实现类加载的依赖。<br/>
     * 使用方式：该方法需要被子类重写。
     * 
     * @return 需要依赖的class对象。
     */
    public Class<? extends IWebInitializable> setInitDepend() {
        return null;
    }

    @Override
    public void destroy() {
        Cache cache = this.getCacheManager().getCache(this.setCacheKey());
        cache.removeAll();
    }

    public abstract String cacheDesc();

    @Override
    public String destroyLog() {
        return "清理缓存数据：[" + this.cacheDesc() + "]";
    }

    @Override
    public String initLog() {
        Cache cache = this.getCacheManager().getCache(this.setCacheKey());
        int cacheObjCount = cache.getSize();
        return "加载缓存数据:[" + this.cacheDesc() + "]  缓存对象个数:[" + cacheObjCount + "]";
    }
}