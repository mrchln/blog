package com.flchy.blog.privilege.config.bean;

import java.io.Serializable;

/**
 * 角色权限踩点bean 用于级联删除菜单使用
 * 
 * @author nieqs
 *
 */
public class RoleVisitBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7659492797385129041L;

	private Integer menuId;
	private Integer roleId;

	public Integer getMenuId() {
		return menuId;
	}

	public void setMenuId(Integer menuId) {
		this.menuId = menuId;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

}
