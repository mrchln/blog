package com.flchy.blog.privilege.config.entity;

import com.flchy.blog.base.entity.BaseManagedEntity;

/**
 * PRIV_MAP_ROLE_VISIT
 * 角色访问权限映射表
 *
 */
public class BaseMapRolePrivEntity extends BaseManagedEntity {
	private static final long serialVersionUID = -4972804182803167338L;
	private int id;
	private int roleId;
	private String privVisitId;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPrivVisitId() {
		return privVisitId;
	}

	public void setPrivVisitId(String privVisitId) {
		this.privVisitId = privVisitId;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

}
