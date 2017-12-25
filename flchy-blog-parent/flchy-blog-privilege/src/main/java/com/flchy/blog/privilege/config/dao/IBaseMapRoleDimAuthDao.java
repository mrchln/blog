package com.flchy.blog.privilege.config.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

import com.flchy.blog.base.datasource.annotation.ConfigRepository;
import com.flchy.blog.privilege.config.entity.BaseMapRoleDimAuthEntity;

@ConfigRepository
public interface IBaseMapRoleDimAuthDao {

	public List<BaseMapRoleDimAuthEntity> selectRoleDimAuthByRoleIds(@Param("roleIdArray") Integer[] roleIdArray);

	public boolean delete(List<Integer> asList);

	@Insert("INSERT INTO `priv_map_role_dataauth` ( `role_id`, `dimauth_id`, `status`, `create_user`, `create_time`) VALUES ( #{roleId}, #{dimauthId}, #{status}, #{createUser}, #{createTime})")
	public boolean insert(BaseMapRoleDimAuthEntity baseMapRoleDimAuthEntity);
	
	public boolean insertCodeBatch(List<BaseMapRoleDimAuthEntity> list);
}
