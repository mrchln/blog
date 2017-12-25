package com.flchy.blog.privilege.config.service;

import java.util.List;

import com.flchy.blog.privilege.config.entity.BaseMapRoleDimAuthEntity;
import com.flchy.blog.privilege.config.entity.BaseMapRoleElementEntity;
import com.flchy.blog.privilege.config.entity.BaseMapRolePanelEntity;

public interface IBaseMapResRoleService {

	public List<BaseMapRolePanelEntity> selectRolePanelByRoleIds(Integer[] roleIdArray) ;
	
	public List<BaseMapRoleElementEntity> selectRoleElementByRoleIds(Integer[] roleIdArray);
	
	public List<BaseMapRoleDimAuthEntity> selectRoleDimAuthByRoleIds(Integer[] roleIdArray) ;
}
