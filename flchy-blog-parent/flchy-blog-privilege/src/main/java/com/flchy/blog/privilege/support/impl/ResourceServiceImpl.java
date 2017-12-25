package com.flchy.blog.privilege.support.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.flchy.blog.privilege.config.bean.BaseMenu;
import com.flchy.blog.privilege.config.cache.MenuResourceCache;
import com.flchy.blog.privilege.config.entity.BaseInfoMenuEntity;
import com.flchy.blog.privilege.config.service.IBaseInfoMenuService;
import com.flchy.blog.privilege.support.IResourceService;

/**
 * 供认证和授权使用,使不直接操作资源相关的表，对外提供资源的服务
 * @author Administrator
 */
@Service("iResourceService")
public class ResourceServiceImpl implements IResourceService {
	private static final Logger logger = LoggerFactory.getLogger(ResourceServiceImpl.class);
	public static final int MENU_ROOT_ID = -1;
	@Autowired
	private IBaseInfoMenuService baseInfoMenuService;

	@Override
	public Map<String, Object> getResourceProperties(int menuId) {
		Map<String, Object> map = new HashMap<>();
		BaseInfoMenuEntity menuResEntity = baseInfoMenuService.selectMenuInfoById(menuId);
		map.put("resName", menuResEntity.getMenuName());
		map.put("url", menuResEntity.getUrlPath());
		return map;
	}

	/**
	 * 从缓存中获取所有菜单Map,key为菜单编码,value为菜单对象<br/>
	 */
	public Map<Integer, BaseMenu> getAllMenuMap() {
		return MenuResourceCache.getInstance().getAllMenuFromCache();
	}

	/**
	 * 从缓存中获取Portal级菜单对象<br/>
	 */
	public BaseMenu getPortalMenu() {
		Map<Integer, BaseMenu> menuMap = this.getAllMenuMap();
		if (menuMap != null) {
			return menuMap.get(MENU_ROOT_ID);
		}
		return null;
	}

	/**
	 * 根据主题编码,从缓存中获取主题级菜单对象<br/>
	 */
	public BaseMenu getSubMenu(int menuId) {
		Map<Integer, BaseMenu> menuMap = this.getAllMenuMap();
		if (menuMap != null) {
			return menuMap.get(menuId);
		}
		return null;
	}

	/**
	 * 根据菜单id获取菜单名称全路径
	 **/
	public String getFullPathByMenuId(int menuId) {
		String[] parentMenus = getAllParentByMenuId(menuId);
		if (!ArrayUtils.isEmpty(parentMenus)) {
			return StringUtils.join(parentMenus, ">");
		}
		return null;
	}

	/**
	 * 根据菜单id获取所有父菜单名称
	 **/
	public String[] getAllParentByMenuId(int menuId) {
		Map<Integer, BaseMenu> menuMap = this.getAllMenuMap();
		List<String> resultlist = getPathByMenuId(menuMap, menuId);
		if (!CollectionUtils.isEmpty(resultlist)) {
			Collections.reverse(resultlist);
			return resultlist.toArray(new String[resultlist.size()]);
		}
		return null;
	}

	private List<String> getPathByMenuId(Map<Integer, BaseMenu> menuMap, Integer menuId) {
		BaseMenu menuBean = menuMap.get(menuId);
		List<String> resultlist = null;
		if (menuBean != null) {
			resultlist = new ArrayList<String>();
			resultlist.add(menuBean.getMenuName());
		}
		Integer menuPid = menuBean.getMenuPid();
		if (menuPid != MENU_ROOT_ID) {
			List<String> list = getPathByMenuId(menuMap, menuPid);
			if (list != null) {
				resultlist.addAll(list);
			}
		}
		return resultlist;
	}
}
