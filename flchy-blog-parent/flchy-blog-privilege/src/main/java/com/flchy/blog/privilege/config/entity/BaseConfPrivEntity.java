package com.flchy.blog.privilege.config.entity;

import com.flchy.blog.base.entity.BaseManagedEntity;

/**
 * PRIV_CONF_VISIT
 * 访问权限表，一个菜单对应一个访问权限，新建一个菜单记录，则自动创建一个访问权限记录
 */
public class BaseConfPrivEntity extends BaseManagedEntity {
	private static final long serialVersionUID = -499334490850136228L;

	private String privVisitId;

	private int menuId;

	public String getPrivVisitId() {
		return privVisitId;
	}

	public void setPrivVisitId(String privVisitId) {
		this.privVisitId = privVisitId;
	}

	public int getMenuId() {
		return menuId;
	}

	public void setMenuId(int menuId) {
		this.menuId = menuId;
	}

}
