package com.flchy.blog.privilege.config.entity;

import com.flchy.blog.base.entity.BaseManagedEntity;

/**
 * PRIV_MAP_ROLE_ELEMENT
 * 角色和面板资源映射表
 */
public class BaseMapRoleElementEntity extends BaseManagedEntity {
	private static final long serialVersionUID = 3352624858207254018L;
	private int id;
	private int roleId;
	private int elementId;
	
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
	public int getElementId() {
		return elementId;
	}
	public void setElementId(int elementId) {
		this.elementId = elementId;
	}



}
