package com.flchy.blog.privilege.config.service;

import java.util.List;

import com.flchy.blog.base.response.PageHelperResult;
import com.flchy.blog.privilege.config.entity.BaseInfoRoleEntity;

/**
 * CONF_INFO_ROLE:  角色数据操作类
 *
 */
public interface IBaseInfoRoleService {

	public List<BaseInfoRoleEntity> selectRolesByRoleIds(Integer[] roleIds);



	/**
	 * 按名称模糊查询或者查询全部角色带分页
	 * @param pageSize
	 * @param currentPage
	 * @param roleName
	 * @return
	 */
	public PageHelperResult selectRoles(int pageSize, int currentPage, BaseInfoRoleEntity infoRoleEntity);



	/**
	 * 添加角色
	 * @param baseInfoRoleEntity
	 * @return
	 */
	boolean insertRole(BaseInfoRoleEntity baseInfoRoleEntity);



	/**
	 * 修改角色
	 * @param infoRoleEntity
	 * @return
	 */
	public boolean updateRole(BaseInfoRoleEntity infoRoleEntity);



	/**
	 * @param roleId
	 * @return
	 */
	Object deleteRole(String roleId);



	BaseInfoRoleEntity getRole(Integer roleId);





}
