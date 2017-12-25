package com.flchy.blog.privilege.config.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

import com.flchy.blog.base.datasource.annotation.ConfigRepository;
import com.flchy.blog.privilege.config.entity.BaseMapRoleUserEntity;

@ConfigRepository
public interface IBaseMapRoleUserDao {

	public List<BaseMapRoleUserEntity> selectRolesByUserId(@Param("userId") int userId);
	
	public boolean delete(List<Integer> asList);
	
	@Insert("INSERT INTO `priv_map_user_role` ( `user_id`, `role_id`, `status`, `create_user`, `create_time`) VALUES (#{userId}, #{roleId}, #{status}, #{createUser}, #{createTime})")
	public boolean insert(BaseMapRoleUserEntity baseMapRoleUserEntity);
	
	public boolean insertCodeBatch(List<BaseMapRoleUserEntity> list);
}
