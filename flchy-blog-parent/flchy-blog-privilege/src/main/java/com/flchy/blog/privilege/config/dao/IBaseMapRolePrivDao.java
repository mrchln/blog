package com.flchy.blog.privilege.config.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

import com.flchy.blog.base.datasource.annotation.ConfigRepository;
import com.flchy.blog.privilege.config.bean.RoleVisitBean;
import com.flchy.blog.privilege.config.entity.BaseMapRolePrivEntity;

@ConfigRepository
public interface IBaseMapRolePrivDao {

	public List<BaseMapRolePrivEntity> selectConfPrivByRoleIds(@Param("roleIdArray") Integer[] roleIdArray);
	
	public boolean delete(List<Integer> asList);
	//按id查询角色和菜单Id
	public List<RoleVisitBean> selectByIdMenuRole(@Param("idArray")List<Integer> asList);
	
	@Insert("INSERT INTO `priv_map_role_visit` ( `role_id`, `priv_visit_id`, `status`, `create_user`, `create_time`) VALUES ( #{roleId}, #{privVisitId}, #{status}, #{createUser}, #{createTime})")
	public boolean insert(BaseMapRolePrivEntity baseMapRolePrivEntity);
	
	
	public boolean insertCodeBatch(List<BaseMapRolePrivEntity> list);
	
}
