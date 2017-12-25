package com.flchy.blog.privilege.config.entity;

import com.flchy.blog.base.entity.BaseManagedEntity;

/**
 * PRIV_MAP_USER_ROLE
 * 用户与角色映射关系存储表
 */
public class BaseMapRoleUserEntity extends BaseManagedEntity {
	private static final long serialVersionUID = 5076727462996477048L;
	private int id;
	private Integer userId;
	private int roleId;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

}
