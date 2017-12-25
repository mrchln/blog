package com.flchy.blog.privilege.config.service;

import java.util.List;

import com.flchy.blog.privilege.config.entity.BaseMapRolePrivEntity;

/**
 * 角色访问权限映射表
 *
 */
public interface IBaseMapPrivRoleService {

	public List<BaseMapRolePrivEntity> selectConfPrivByRoleIds(Integer[] roleIds);

	Object delete(String id);

	boolean insert(BaseMapRolePrivEntity baseMapRolePrivEntity);

	boolean insertCodeBatch(String[] privVisitId, Integer roleId, String userName);
}
