package com.flchy.blog.privilege.config.cache;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.flchy.blog.base.dbconfig.cache.EnumDicCache;
import com.flchy.blog.base.holder.AbstractCacheHolder;
import com.flchy.blog.base.holder.SpringContextHolder;
import com.flchy.blog.base.initialize.IWebInitializable;
import com.flchy.blog.privilege.config.bean.BaseMenu;
import com.flchy.blog.privilege.config.entity.BaseInfoMenuEntity;
import com.flchy.blog.privilege.config.service.IBaseInfoMenuService;
import com.flchy.blog.privilege.support.impl.ResourceServiceImpl;
import com.flchy.blog.utils.convert.BeanConvertUtil;

import net.sf.ehcache.Element;

/**
 * 权限管理-菜单资源缓存类。用于存取菜单资源信息 注：如果类没有泛型参数的话可以连标注一起删除
 *
 * @since 0.0.1-SNAPSHOT
 */
@Component
public class MenuResourceCache extends AbstractCacheHolder {
	private static final Logger logger = LoggerFactory.getLogger(MenuResourceCache.class);
	private static final String cacheKey = "com.zuobiao.analysis.privilege.menu";
	private static final String protalMenuKey = "protalMenu";
	
	@Autowired
	private IBaseInfoMenuService baseInfoMenuService;
	private static MenuResourceCache menuResCache = new MenuResourceCache();
	public static MenuResourceCache getInstance() {
		if (menuResCache == null) {
			menuResCache = new MenuResourceCache();
		}
		return menuResCache;
	}

	protected IBaseInfoMenuService getMenuResService() {
		if (this.baseInfoMenuService == null) {
			this.baseInfoMenuService = SpringContextHolder.getBean("iBaseInfoMenuService", IBaseInfoMenuService.class);
		}
		return this.baseInfoMenuService;
	}

	@Override
	public void initialize() {
		// protal系统菜单资源信息
		this.saveOrUpdateCacheValue(protalMenuKey, this.getAllMenu());
	}

	@Override
	public String cacheDesc() {
		return "菜单资源";
	}

	/**
	 * 配置菜单资源缓存区key值
	 */
	@Override
	protected String setCacheKey() {
		return cacheKey;
	}

	@Override
	protected Object getDataBaseValue(String cacheNodeKey, String propertyName, String propertyValue) {
		return null;
	}

	@Override
	protected void saveOrUpdateCacheValue(String cacheNodeKey, Object obj) {
		super.addOrUpdateCacheValue(cacheNodeKey, obj);
	}

	@Override
	protected void clearCacheValue(String cacheNodeKey) {
		super.removeCacheElement(cacheNodeKey);
	}

	/**
	 * 调用业务层方法获取Protal系统级菜单信息
	 * @return 主题菜单List
	 */
	@SuppressWarnings("unchecked")
	public Map<Integer, BaseMenu> getAllMenu() {
		List<BaseInfoMenuEntity> menuList = this.getMenuResService().selectAllMenuList();
		if (CollectionUtils.isEmpty(menuList)) {
			return null;
		}

		Map<Integer, BaseMenu> menuMap = new CaseInsensitiveMap(menuList.size());
		for (BaseInfoMenuEntity menuResEntity : menuList) {
			BaseMenu menuBean = BeanConvertUtil.map(menuResEntity, BaseMenu.class);
			menuBean.setLabel(menuBean.getMenuName());
			menuMap.put(menuResEntity.getMenuId(), menuBean);
		}
		BaseMenu rootMenuBean = new BaseMenu(ResourceServiceImpl.MENU_ROOT_ID);
		for (BaseMenu menuBean : menuMap.values()) {
			Integer pid = menuBean.getMenuPid();
			if (null != pid && pid.equals(rootMenuBean.getMenuId())) {
				rootMenuBean.addChild(menuBean);
			} else {
				BaseMenu parentMenuBean = menuMap.get(pid);
				if (null != parentMenuBean) {
					parentMenuBean.addChild(menuBean);
				}
			}
		}
		menuMap.put(-1, rootMenuBean);
		// 按sort_index进行菜单排序
		rootMenuBean.sort();
		return menuMap;

	}

	@SuppressWarnings("unchecked")
	public Map<Integer, BaseMenu> getAllMenuFromCache() {
		Element element = (Element) this.getCacheAllValue(protalMenuKey);
		if (element != null && !element.isExpired()) {
			return (Map<Integer, BaseMenu>) element.getObjectValue();
		} else {
			return this.getAllMenu();
		}

	}
	
	  @Override
	    public Class<? extends IWebInitializable> setInitDepend() {
	        return EnumDicCache.class;
	    }
}
