package com.flchy.blog.privilege.config.entity;

import com.flchy.blog.base.entity.BaseManagedEntity;

/**
 * CONF_INFO_MENU
 * 系统菜单资源配置数据存储表，存储系统的菜单等数据
 */
public class BaseInfoMenuEntity extends BaseManagedEntity {
	private static final long serialVersionUID = -6406458410898250529L;
	private Integer menuId;
	private Integer menuPid;// 不支持一个菜单配置在多个目录下，如有需求需创建多个重复菜单
	private String menuName;
	private String alias;//菜单别名
	private String menuNameQp;
	private String remark;
	private Integer menuLevel;
	private Integer menuType;
	private String urlPath;
	private String menuCls;// 菜单图标样式
	private Integer loadTarget;// 菜单加载类型
	private Integer sortIndex;
	private Integer isShare;// 1: 是 2：否
	private String fullPath;// 菜单面包屑全路径
	
	private String privVisitId;
	
	
	
	public String getPrivVisitId() {
		return privVisitId;
	}

	public void setPrivVisitId(String privVisitId) {
		this.privVisitId = privVisitId;
	}

	public Integer getMenuId() {
		return menuId;
	}

	public void setMenuId(Integer menuId) {
		this.menuId = menuId;
	}

	public Integer getMenuPid() {
		return menuPid;
	}

	public void setMenuPid(Integer menuPid) {
		this.menuPid = menuPid;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public String getMenuNameQp() {
		return menuNameQp;
	}

	public void setMenuNameQp(String menuNameQp) {
		this.menuNameQp = menuNameQp;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getMenuLevel() {
		return menuLevel;
	}

	public void setMenuLevel(Integer menuLevel) {
		this.menuLevel = menuLevel;
	}

	public String getUrlPath() {
		return urlPath;
	}

	public void setUrlPath(String urlPath) {
		this.urlPath = urlPath;
	}

	public String getMenuCls() {
		return menuCls;
	}

	public void setMenuCls(String menuCls) {
		this.menuCls = menuCls;
	}

	public Integer getLoadTarget() {
		return loadTarget;
	}

	public void setLoadTarget(Integer loadTarget) {
		this.loadTarget = loadTarget;
	}

	public Integer getSortIndex() {
		return sortIndex;
	}

	public void setSortIndex(Integer sortIndex) {
		this.sortIndex = sortIndex;
	}

	public Integer getIsShare() {
		return isShare;
	}

	public void setIsShare(Integer isShare) {
		this.isShare = isShare;
	}

	public String getFullPath() {
		return fullPath;
	}

	public void setFullPath(String fullPath) {
		this.fullPath = fullPath;
	}

	public Integer getMenuType() {
		return menuType;
	}

	public void setMenuType(Integer menuType) {
		this.menuType = menuType;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}
	
	
}
