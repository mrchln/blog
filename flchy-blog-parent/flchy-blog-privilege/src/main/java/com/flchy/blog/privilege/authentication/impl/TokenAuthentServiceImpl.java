package com.flchy.blog.privilege.authentication.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.flchy.blog.privilege.authentication.ITokenAuthentService;
import com.flchy.blog.privilege.config.bean.BaseMenu;
import com.flchy.blog.privilege.config.bean.BaseUser;
import com.flchy.blog.privilege.support.IResourceService;
import com.flchy.blog.privilege.support.ITokenPrivilegeService;
import com.flchy.blog.utils.convert.BeanConvertUtil;

@Service
public class TokenAuthentServiceImpl implements ITokenAuthentService {
	private static Logger logger = LoggerFactory.getLogger(TokenAuthentServiceImpl.class);
	// 菜单根节点编码
	public static final int MENU_ROOT_ID = -1;
	@Resource
	private IResourceService iResourceService;
	@Resource
	private ITokenPrivilegeService tokenPrivilegeService;

	/**
	 * 获取用户授权的portal菜单
	 */
	@Override
	public Collection<BaseMenu> getAuthPortalMenus(String adoptToken) {
		// 获取portal菜单对象
		BaseMenu portalMenu = iResourceService.getPortalMenu();
		if (null == portalMenu) {
			throw new RuntimeException("获取portal菜单失败!");
		}
		// 获取用户授权访问的菜单编码集合
		if (!tokenPrivilegeService.isSuperAdmin(adoptToken)) {
			Set<Integer> authMenuSet = tokenPrivilegeService.getCurrentUser(adoptToken).getAuthMenuIds();
			if (!CollectionUtils.isEmpty(authMenuSet)) {
				Collection<BaseMenu> result = this.filterAuthMenu(portalMenu.getChildren(), authMenuSet, true);
				return result;
			}
			return null;
		} else {
			// 如果当前登录用户为超级管理员,则返回全量菜单
			return portalMenu.getChildren();
		}
	}

	/**
	 * 根据专题编码,获取该专题下的所有子菜单
	 */
	@Override
	public Collection<BaseMenu> getAuthSubMenus(String adoptToken, int menuId) {
		return getAuthSubMenus(adoptToken, menuId, false);
	}

	/**
	 * 根据父级编码，获取所有子菜单,及父级菜单 hasParentMenu 是否显示父级菜单
	 */
	@Override
	public Collection<BaseMenu> getAuthSubMenus(String adoptToken, int menuId, boolean hasParentMenu) {
		// hasParentMenu NULL -1获取所有菜单
		if ((menuId == MENU_ROOT_ID) && hasParentMenu) {
			return getAuthPortalMenus(adoptToken);
		}
		// 根据专题ID,获取该专题的菜单对象
		BaseMenu subjectMenu = iResourceService.getSubMenu(menuId);
		if (null == subjectMenu) {
			throw new RuntimeException("菜单Id:[" + menuId + "]不存在!");
		}
		// 获取用户授权访问的菜单编码集合
		if (!tokenPrivilegeService.isSuperAdmin(adoptToken)) {
			Set<Integer> authMenuSet = tokenPrivilegeService.getCurrentUser(adoptToken).getAuthMenuIds();
			if (!CollectionUtils.isEmpty(authMenuSet)) {
				if (hasParentMenu) {
					List<BaseMenu> subMenulist = recursionSubBaseMenu(menuId, iResourceService.getAllMenuMap(), menuId);
					if (subMenulist != null && !subMenulist.isEmpty()) {
						Collection<BaseMenu> result = this.filterAuthMenu(subMenulist, authMenuSet, false);
						return result;
					}
				} else {
					Collection<BaseMenu> result = this.filterAuthMenu(subjectMenu.getChildren(), authMenuSet, false);
					return result;
				}
			}
			return null;
		} else {
			// 如果当前登录用户为超级管理员,则返回全量菜单
			if (hasParentMenu) {
				return recursionSubBaseMenu(menuId, iResourceService.getAllMenuMap(), menuId);
			}
			return subjectMenu.getChildren();
		}
	}

	// 过滤授权菜单
	private List<BaseMenu> filterAuthMenu(List<BaseMenu> menuList, Set<Integer> authMenuSet, boolean isPortal) {
		List<BaseMenu> resultList = new ArrayList<BaseMenu>();
		for (BaseMenu baseMenu : menuList) {
			// 如果目录菜单则加入菜单集合,如是菜单项则判断权限后添加
			if (baseMenu.isFolder() || authMenuSet.contains(baseMenu.getMenuId())) {
				BaseMenu menu = BeanConvertUtil.map(baseMenu, BaseMenu.class);
				menu.clearChildren();
				resultList.add(menu);
				List<BaseMenu> children = baseMenu.getChildren();
				if (!CollectionUtils.isEmpty(children)) {
					menu.addChildren(this.filterAuthMenu(children, authMenuSet, isPortal));
				}
			}
		}
		return resultList;
	}

	// 递归菜单
	private List<BaseMenu> recursionSubBaseMenu(int menuId, Map<Integer, BaseMenu> allMenuMap, int subjectId) {
		BaseMenu menuBean = allMenuMap.get(menuId);
		BaseMenu subjuctBean = null;
		if (MENU_ROOT_ID == menuBean.getMenuPid()) {
			BaseMenu menuBeanRoot = new BaseMenu();
			menuBeanRoot = BeanConvertUtil.map(menuBean, BaseMenu.class);
			List<BaseMenu> menuList = menuBeanRoot.getChildren();
			BaseMenu menuItem = null;
			for (BaseMenu item : menuList) {
				if (subjectId == item.getMenuId()) {
					menuItem = item;
				}
			}
			List<BaseMenu> childrenList = new ArrayList<BaseMenu>();
			childrenList.add(menuItem);
			menuBeanRoot.setChildren(childrenList);
			subjuctBean = menuBeanRoot;
		}
		List<BaseMenu> resultlist = null;
		if (menuBean != null) {
			resultlist = new ArrayList<BaseMenu>();
			if (MENU_ROOT_ID == menuBean.getMenuPid()) {
				resultlist.add(subjuctBean);
			} else {
				resultlist.add(menuBean);
			}
			int menuPid = menuBean.getMenuPid();
			if (menuPid != MENU_ROOT_ID) {
				List<BaseMenu> list = recursionSubBaseMenu(menuPid, allMenuMap, subjectId);
				if (list != null) {
					resultlist.addAll(list);
				}
			}
		}
		return resultlist;
	}
	
	/**
	 * 获取令牌的登录用户
	 * @param adoptToken
	 * @return
	 */
	@Override
	public BaseUser getAuthTokenUser(String adoptToken) {
		return tokenPrivilegeService.getCurrentUser(adoptToken);
	}
}
