package com.flchy.blog.privilege.config.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

import com.flchy.blog.base.datasource.annotation.ConfigRepository;
import com.flchy.blog.privilege.config.entity.BaseMapRoleElementEntity;

@ConfigRepository
public interface IBaseMapRoleElementDao {

	public List<BaseMapRoleElementEntity> selectRoleElementByRoleIds(@Param("roleIdArray") Integer[] roleIdArray);
	
	//按面板id和角色id查询     角色面板权限
	public List<BaseMapRoleElementEntity> selectRoleElementByPanelId(@Param("panelId")List<Integer> panelId, @Param("roleId")Integer roleId);
	
	/**
	 * 查询已选
	 * @param roleId
	 * @param menuId
	 * @return
	 */
	public List<BaseMapRoleElementEntity> selectRoleElementBySelected(@Param("roleId") Integer roleId,@Param("menuId") Integer menuId,@Param("panelId") Integer panelId);
	
	public List<BaseMapRoleElementEntity> selectRoleElementByRoleIdMenuIds(@Param("roleId") Integer roleId,@Param("menuIds") List<Integer> menuIds);
	
	
	public boolean delete(List<Integer> asList);

	
	@Insert("INSERT INTO `priv_map_role_element` ( `role_id`, `element_id`, `status`, `create_user`, `create_time`) VALUES ( #{roleId}, #{elementId}, #{status}, #{createUser}, #{createTime})")
	public boolean insert(BaseMapRoleElementEntity baseMapRoleElementEntity);
	
	public boolean insertCodeBatch(List<BaseMapRoleElementEntity> list);
}
