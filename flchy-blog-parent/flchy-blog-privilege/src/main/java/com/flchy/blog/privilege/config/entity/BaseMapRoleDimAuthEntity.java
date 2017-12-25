package com.flchy.blog.privilege.config.entity;

import com.flchy.blog.base.entity.BaseManagedEntity;

/**
 * PRIV_MAP_ROLE_DATAAUTH
 * 角色和数据权限映射表
 */
public class BaseMapRoleDimAuthEntity extends BaseManagedEntity {
	private static final long serialVersionUID = 3352624858207254018L;
	private int id;
	private int roleId;
	private int dimAuthId;

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

	public int getDimAuthId() {
		return dimAuthId;
	}

	public void setDimAuthId(int dimAuthId) {
		this.dimAuthId = dimAuthId;
	}
}
