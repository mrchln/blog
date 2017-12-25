package com.flchy.blog.privilege.config.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.flchy.blog.base.entity.BaseManagedEntity;

/**
 * CONF_INFO_ROLE
 * 角色信息表，存储不同的角色
 * @author KingXu
 */
public class BaseInfoRoleEntity extends BaseManagedEntity {
	private static final long serialVersionUID = -6513965716299180429L;
	private int roleId;
	private String roleName;
	private String roleNameQp;
	private String remark;
	private Integer sortIndex;
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date validBegin;
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date validEnd;

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleNameQp() {
		return roleNameQp;
	}

	public void setRoleNameQp(String roleNameQp) {
		this.roleNameQp = roleNameQp;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getSortIndex() {
		return sortIndex;
	}

	public void setSortIndex(Integer sortIndex) {
		this.sortIndex = sortIndex;
	}

	public Date getValidBegin() {
		return validBegin;
	}

	public void setValidBegin(Date validBegin) {
		this.validBegin = validBegin;
	}

	public Date getValidEnd() {
		return validEnd;
	}

	public void setValidEnd(Date validEnd) {
		this.validEnd = validEnd;
	}

}
