package com.flchy.blog.privilege.config.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.flchy.blog.base.datasource.annotation.ConfigRepository;
import com.flchy.blog.privilege.config.entity.BaseInfoRoleEntity;

@ConfigRepository
public interface IBaseInfoRoleDao {

	public List<BaseInfoRoleEntity> selectRolesByRoleIds(@Param("roleIdArray") Integer[] roleIdArray);
	
	
	public List<BaseInfoRoleEntity> selectRoles(BaseInfoRoleEntity infoRoleEntity);


	/**
	 *添加角色
	 * @param baseInfoRoleEntity
	 * @return
	 */
	@Insert("INSERT INTO `conf_info_role` (`role_name`, `role_name_qp`, `remark`, `sort_index`, `valid_begin_date`, `valid_end_date`, `status`, `create_user`, `create_time`) VALUES  ( #{roleName}, #{roleNameQp}, #{remark}, #{sortIndex}, #{validBegin}, #{validEnd}, #{status}, #{createUser}, #{createTime})")
	public Integer insertRole(BaseInfoRoleEntity baseInfoRoleEntity);


	/**
	 * 修改角色
	 * @param infoRoleEntity
	 * @return
	 */
	public boolean updateRole(BaseInfoRoleEntity infoRoleEntity);


	/**
	 * 删除角色
	 * @param asList
	 * @return
	 */
	public boolean deleteRole(List<Integer> asList);
	
	@Select("SELECT `pk_id` 'roleId',`role_name` 'roleName',`role_name_qp` 'roleNameQp',`remark` 'remark',`sort_index` 'sortIndex',`valid_begin_date` 'validBegin',`valid_end_date` 'validEnd', `status`,`create_user` 'createUser', `create_time` 'createTime',`update_user` 'updateUser',`update_time` 'updateTime' FROM conf_info_role where pk_id=#{roleId}")
	public BaseInfoRoleEntity getRole(Integer roleId);


	
	
}
