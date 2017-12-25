package com.flchy.blog.privilege.config.service;

import java.util.List;

import com.flchy.blog.privilege.config.entity.BaseMapRoleElementEntity;

public interface IBaseMapRoleElementService {

	List<BaseMapRoleElementEntity> selectRoleElementBySelected(Integer roleId, Integer menuId, Integer panelId);

	boolean insert(BaseMapRoleElementEntity baseMapRoleElementEntity);

	Object delete(String id);

	boolean insertCodeBatch(Integer[] elementId, Integer roleId, String userName);


}
