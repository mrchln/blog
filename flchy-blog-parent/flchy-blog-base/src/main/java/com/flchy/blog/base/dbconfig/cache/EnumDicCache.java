package com.flchy.blog.base.dbconfig.cache;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.flchy.blog.base.dbconfig.bean.EnumDicBean;
import com.flchy.blog.base.dbconfig.entity.EnumDicEntity;
import com.flchy.blog.base.dbconfig.service.IEnumDicService;
import com.flchy.blog.base.holder.AbstractCacheHolder;
import com.flchy.blog.base.holder.SpringContextHolder;
import com.flchy.blog.base.initialize.IWebInitializable;
import com.flchy.blog.utils.helper.ObjectClassHelper;

@Component
public class EnumDicCache extends AbstractCacheHolder {
	private static final Logger logger = LoggerFactory.getLogger(EnumDicCache.class);
    private static final String cacheKey = "com.zuobiao.analysis.base.enumdic";
    /** 缓存区域中的element的key */
    private static final String cacheNodeKey = "enumDicInfo";
    
    /** isCacheKey:枚举模块isCache为1时缓存里有值 */
    private static final String isCacheKey = "1";

    /** statusKey:枚举模块status为1是有效 */
    private static final String statusKey = "1";
    
    private static EnumDicCache enumDicCache = new EnumDicCache();

    public static EnumDicCache getInstance() {
        if (enumDicCache == null) {
            enumDicCache = new EnumDicCache();
        }
        return enumDicCache;
    }
    
    private IEnumDicService getIEnumDicService() {
        return SpringContextHolder.getBean("iEnumDicService", IEnumDicService.class);
    }
    
	/**
     * 缓存启动加载设置缓存对象数据。<br/>
     * 详细描述：初始化缓存属性信息。<br/>
     * 使用方式：当服务加载启动时初始化缓存属性值信息。
     */
    @Override
    public void initialize() {
        // 加载枚举字典表信息到缓存中
        this.saveOrUpdateCacheValue(cacheNodeKey, this.queryAllEnumsInfoList());
    }

    /**
     * 缓存加载日志输出。<br/>
     * 详细描述：加载缓存的时候输出枚举的日志信息。<br/>
     * 使用方式：当容器启动时会自动调用 printInitLog()方法。
     */
    @Override
    public String cacheDesc() {
        return "枚举字典";
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
        return cacheKey;
    }

    /**
     * 查询数据供缓存不存在时候调用数据库。<br/>
     * 详细描述：当缓存不存在时，数据信息直接在数据库中查询。<br/>
     * 使用方式： 在对外提供调用接口holder中，调用getDataBaseValue(cacheNodeKey,propertyName, propertyValue)方法。
     * 
     * @param cacheNodeKey 缓存区域中的element的key。
     * @param propertyName 枚举字段。
     * @param propertyValue 枚举值。
     * @return 从数据库中查询出来的枚举值。
     */
    @Override
    protected Object getDataBaseValue(String cacheNodeKey, String propertyName, String propertyValue) {
    	  // 设置对象的属性值
        EnumDicBean enumDicBean = new EnumDicBean();
        ObjectClassHelper.setFieldValue(enumDicBean, propertyName, propertyValue);
        ObjectClassHelper.setFieldValue(enumDicBean, "isCache", isCacheKey);
        ObjectClassHelper.setFieldValue(enumDicBean, "status", statusKey);
        return this.getIEnumDicService().selectEnumsListByObj(enumDicBean);
    }

    /**
     * 刷新缓存调用该方法。<br/>
     * 详细描述：当刷新缓存，调用该方法。<br/>
     * 使用方式：当需要刷新缓存的时候调用saveOrUpdateCacheValue(cacheNodeKey,obj)。
     * 
     * @param cacheNodeKey 缓存区域中的element的key。
     * @param obj 要保存到缓存中的枚举值。
     */
    @Override
    protected void saveOrUpdateCacheValue(String cacheNodeKey, Object obj) {
        super.addOrUpdateCacheValue(cacheNodeKey, obj);
    }

    /**
     * 清除缓存调用该方法。<br/>
     * 详细描述：当清除缓存，调用该方法。<br/>
     * 使用方式：当需要刷新缓存的时候调用clearCacheValue(cacheNodeKey)。
     * 
     * @param cacheNodeKey 缓存区域中的element的key。
     */
    @Override
    protected void clearCacheValue(String cacheNodeKey) {
        super.removeCacheElement(cacheNodeKey);
    }

    /**
     * 根据条件查询枚举字典表中的信息(全量)。<br/>
     * 详细描述：设置缓存为有效的，设置数据是有效数据后查询信息。<br/>
     * 使用方式： 查询枚举字典表全部信息时调用给queryAllEnumsInfoList()方法。
     * 
     * @return 枚举字典实体数据集合。
     */
    protected List<EnumDicEntity> queryAllEnumsInfoList() {
        // 设置对象的属性值
        EnumDicBean enumDicBean = new EnumDicBean();
        ObjectClassHelper.setFieldValue(enumDicBean, "isCache", isCacheKey);
        ObjectClassHelper.setFieldValue(enumDicBean, "status", statusKey);
        return this.getIEnumDicService().selectEnumsListByObj(enumDicBean);
    }

    @Override
    public Class<? extends IWebInitializable> setInitDepend() {
        return IWebInitializable.class;
    }
}
