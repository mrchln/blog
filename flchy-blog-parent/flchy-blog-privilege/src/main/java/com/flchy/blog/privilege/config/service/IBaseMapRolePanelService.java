package com.flchy.blog.privilege.config.service;

import java.util.List;

import com.flchy.blog.privilege.config.entity.BaseMapRolePanelEntity;
/**
 * 角色面板资源映射表
 * @author nieqs
 */
public interface IBaseMapRolePanelService {

	List<BaseMapRolePanelEntity> selectRolePanelByRoleIds(Integer[] roleIds);

	List<BaseMapRolePanelEntity> selectRolePanelBySelected(Integer roleId, Integer menuId);

	boolean insert(BaseMapRolePanelEntity baseMapRolePanelEntity);

	Object delete(String id);

	boolean updateRolePanel(Integer roleId, Integer menuId, Integer[] panelId, String userName);

	boolean insertCodeBatch(Integer[] panelId, Integer roleId, String userName);

}
