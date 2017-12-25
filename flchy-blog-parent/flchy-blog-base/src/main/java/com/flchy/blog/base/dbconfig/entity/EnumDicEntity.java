package com.flchy.blog.base.dbconfig.entity;

import com.flchy.blog.base.entity.BaseManagedEntity;

/**
 * 枚举字典表的实体层。
 */
public class EnumDicEntity extends BaseManagedEntity {
	private static final long serialVersionUID = 561091098001061515L;
	private Integer id;
	private String enumCode;
	private String enumGroup;
	private String enumValue;
	private String enumTxt;
	private String remark;
	/** sortIndex:排序索引 */
	private Integer sortIndex;
	/** isCache:是否缓存 */
	private Integer isCache;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * 获取枚举的枚举编码。
	 * 
	 * @return 以String形式返回枚举编码。
	 */
	public String getEnumCode() {
		return enumCode;
	}

	/**
	 * 添加主键枚举编码到枚举对象中 。
	 * 
	 * @param enumCode
	 *            枚举编码，为String类型字符串。
	 */
	public void setEnumCode(String enumCode) {
		this.enumCode = enumCode;
	}

	/**
	 * 获取枚举的枚举分组。
	 * 
	 * @return 以String形式返回枚举分组。
	 */
	public String getEnumGroup() {
		return enumGroup;
	}

	/**
	 * 添加枚举分组到枚举对象中 。
	 * 
	 * @param enumGroup
	 *            枚举分组，为String类型字符串。
	 */
	public void setEnumGroup(String enumGroup) {
		this.enumGroup = enumGroup;
	}

	/**
	 * 获取枚举的枚举值。
	 * 
	 * @return 以String形式返回枚举值。
	 */
	public String getEnumValue() {
		return enumValue;
	}

	/**
	 * 添加枚举值到枚举对象中 。
	 * 
	 * @param enumValue
	 *            枚举值，为String类型字符串。
	 */
	public void setEnumValue(String enumValue) {
		this.enumValue = enumValue;
	}

	/**
	 * 获取枚举的枚举中文。
	 * 
	 * @return 以String形式返回枚举中文。
	 */
	public String getEnumTxt() {
		return enumTxt;
	}

	/**
	 * 添加枚举中文到枚举对象中 。
	 * 
	 * @param enumTxt
	 *            枚举中文，为String类型字符串。
	 */
	public void setEnumTxt(String enumTxt) {
		this.enumTxt = enumTxt;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * 获取枚举的枚举索引。
	 * 
	 * @return 以String形式返回枚举索引。
	 */
	public Integer getSortIndex() {
		return sortIndex;
	}

	/**
	 * 添加枚举索引到枚举对象中 。
	 * 
	 * @param sortIndex
	 *            枚举索引，为String类型字符串。
	 */
	public void setSortIndex(Integer sortIndex) {
		this.sortIndex = sortIndex;
	}

	/**
	 * 获取枚举是否缓存值。
	 * 
	 * @return 以String形式返回是否缓存值。
	 */
	public Integer getIsCache() {
		return isCache;
	}

	/**
	 * 添加枚举缓存到枚举对象中 。
	 * 
	 * @param isCache
	 *            是否缓存，为String类型字符串。
	 */
	public void setIsCache(Integer isCache) {
		this.isCache = isCache;
	}
}