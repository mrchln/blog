package com.flchy.blog.privilege.config.service;

import java.util.List;

import com.flchy.blog.privilege.config.entity.BaseMapRoleUserEntity;

/**
 * 用户和角色映射类
 *
 */
public interface IBaseMapUserRoleService {

	public List<BaseMapRoleUserEntity> selectRolesByUserId(int userId);

	boolean insert(BaseMapRoleUserEntity baseMapRoleUserEntity);

	Object delete(String id);

	boolean insertCodeBatch(Integer[] userId, Integer roleId, String userName);

	boolean updateCodeBatch(Integer userId, Integer[] roleId, String userName);
}
