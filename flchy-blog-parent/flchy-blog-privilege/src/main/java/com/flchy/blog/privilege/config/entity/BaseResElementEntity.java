package com.flchy.blog.privilege.config.entity;

import com.flchy.blog.base.entity.BaseManagedEntity;

/**
 * CONF_INFO_PANEL 元素资源表
 * 
 * @author KingXu
 */
public class BaseResElementEntity extends BaseManagedEntity {
	private static final long serialVersionUID = -8990017951745331660L;
	private Integer elementId;
	private String elementName;
	private Integer elementType;
	private int menuId;
	private Integer panelId;
	private String remark;
	private Integer index;
	private String panelName;

	public String getPanelName() {
		return panelName;
	}

	public void setPanelName(String panelName) {
		this.panelName = panelName;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public Integer getElementId() {
		return elementId;
	}

	public void setElementId(Integer elementId) {
		this.elementId = elementId;
	}

	public String getElementName() {
		return elementName;
	}

	public void setElementName(String elementName) {
		this.elementName = elementName;
	}

	public Integer getElementType() {
		return elementType;
	}

	public void setElementType(Integer elementType) {
		this.elementType = elementType;
	}

	public int getMenuId() {
		return menuId;
	}

	public void setMenuId(int menuId) {
		this.menuId = menuId;
	}

	public Integer getPanelId() {
		return panelId;
	}

	public void setPanelId(Integer panelId) {
		this.panelId = panelId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
