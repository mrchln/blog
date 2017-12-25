package com.flchy.blog.privilege.config.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

import com.flchy.blog.base.datasource.annotation.ConfigRepository;
import com.flchy.blog.privilege.config.entity.BaseMapRolePanelEntity;

@ConfigRepository
public interface IBaseMapRolePanelDao {
	
	public List<BaseMapRolePanelEntity> selectRolePanelByRoleIds(@Param("roleIdArray") Integer[] roleIdArray) ;
	
	/**
	 * 查询已选
	 * @param roleId
	 * @param menuId
	 * @return
	 */
	public List<BaseMapRolePanelEntity> selectRolePanelBySelected(@Param("roleId") Integer roleId,@Param("menuId") Integer menuId);
	
	
	public boolean delete(List<Integer> asList);
	//按角色Id和 多个菜单Id查询角色面板权限
	public List<BaseMapRolePanelEntity> selectRolePanelByRoleIdMenuIds(@Param("roleId")Integer roleId,@Param("menuIds")List<Integer> menuIds);
	/**
	 * 按id查询
	 * @param ids
	 * @return
	 */
	public List<BaseMapRolePanelEntity> selectRolePanelById(@Param("roleIdArray") List<Integer> ids);
	
	@Insert("INSERT INTO `priv_map_role_panel` ( `role_id`, `panel_id`, `status`, `create_user`, `create_time`) VALUES ( #{roleId}, #{panelId}, #{status}, #{createUser}, #{createTime})")
	public boolean insert(BaseMapRolePanelEntity baseMapRolePanelEntity);
	
	public boolean insertCodeBatch(List<BaseMapRolePanelEntity> list);
}
