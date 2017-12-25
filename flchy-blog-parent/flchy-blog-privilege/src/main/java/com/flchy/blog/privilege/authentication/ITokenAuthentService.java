package com.flchy.blog.privilege.authentication;

import java.util.Collection;

import com.flchy.blog.privilege.config.bean.BaseMenu;
import com.flchy.blog.privilege.config.bean.BaseUser;

public interface ITokenAuthentService {
	// 获取用户授权的portal菜单
	public Collection<BaseMenu> getAuthPortalMenus(String adoptToken);

	// 根据菜单Id,获取该下的所有子菜单
	public Collection<BaseMenu> getAuthSubMenus(String adoptToken, int menuId, boolean hasParentMenu);

	// 根据菜单Id,获取该下的所有子菜单,不含当前级别
	public Collection<BaseMenu> getAuthSubMenus(String adoptToken, int menuId);

	// 获取令牌的登录用户
	public BaseUser getAuthTokenUser(String adoptToken);
}
