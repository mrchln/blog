package com.flchy.blog.privilege.config.entity;

import com.flchy.blog.base.entity.BaseManagedEntity;

/**
 * PRIV_MAP_ROLE_PANEL
 * 角色面板资源映射表
 */
public class BaseMapRolePanelEntity extends BaseManagedEntity {
	private static final long serialVersionUID = 3352624858207254018L;
	private int id;
	private int roleId;
	private int panelId;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public int getPanelId() {
		return panelId;
	}

	public void setPanelId(int panelId) {
		this.panelId = panelId;
	}

}
