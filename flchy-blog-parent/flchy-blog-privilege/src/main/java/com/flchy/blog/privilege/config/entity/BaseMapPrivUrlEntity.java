package com.flchy.blog.privilege.config.entity;

import com.flchy.blog.base.entity.BaseManagedEntity;
/**
 * PRIV_MAP_VISIT_URL
 * 访问权限与URL映射关系表，为每个菜单下所有Url,需要配置，不配置则无法访问
 */
public class BaseMapPrivUrlEntity extends BaseManagedEntity {
	private static final long serialVersionUID = 3352624858207254018L;
	private int id;
	private String privVisitId;
	private int urlId;

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

	public int getUrlId() {
		return urlId;
	}

	public void setUrlId(int urlId) {
		this.urlId = urlId;
	}

}
