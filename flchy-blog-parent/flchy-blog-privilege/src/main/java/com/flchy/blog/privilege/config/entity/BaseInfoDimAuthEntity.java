package com.flchy.blog.privilege.config.entity;

import com.flchy.blog.base.entity.BaseManagedEntity;

/**
 * CONF_INFO_DATAAUTH 数据权限配置表，主要对 维度值控制，例如查询时候选项值 控制
 * 
 */
public class BaseInfoDimAuthEntity extends BaseManagedEntity {
	private static final long serialVersionUID = -8990017951745331660L;
	private Integer id;
	private String jsonInfo;
	private String remark;

	private Integer index;

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getJsonInfo() {
		return jsonInfo;
	}

	public void setJsonInfo(String jsonInfo) {
		this.jsonInfo = jsonInfo;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
