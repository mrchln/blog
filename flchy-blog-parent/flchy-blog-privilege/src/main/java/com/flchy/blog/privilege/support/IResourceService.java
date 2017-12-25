package com.flchy.blog.privilege.support;

import java.util.Map;

import com.flchy.blog.privilege.config.bean.BaseMenu;

public interface IResourceService {
	
    Map<Integer, BaseMenu> getAllMenuMap();

    BaseMenu getPortalMenu();

    BaseMenu getSubMenu(int menuId);

    String getFullPathByMenuId(int menuId);

    String[] getAllParentByMenuId(int menuId);

    Map<String, Object> getResourceProperties(int menuId);
}
