package com.flchy.blog.privilege.config.service;

import java.util.List;

import com.flchy.blog.privilege.config.entity.BaseInfoDimAuthEntity;

/**
 * 资源数据操作类; 含（面板资源、元素资源、数据资源）
 */
public interface IBaseResInfoService {

	public List<BaseInfoDimAuthEntity>  selectDimAuthInfoByDimAuthIds(Integer[] dimAuthIdArray);
}
