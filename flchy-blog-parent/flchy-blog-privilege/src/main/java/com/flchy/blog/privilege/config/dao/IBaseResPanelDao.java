package com.flchy.blog.privilege.config.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

import com.flchy.blog.base.datasource.annotation.ConfigRepository;
import com.flchy.blog.privilege.config.entity.BaseResPanelEntity;

/**
 * 面板权限，用于角色赋权
 * 
 * @author nieqs
 *
 */
@ConfigRepository
public interface IBaseResPanelDao {

	public List<BaseResPanelEntity> selectResPanel(BaseResPanelEntity baseResPanelEntity);

	/**
	 * @param baseResPanelEntity
	 * @return
	 */
	public boolean update(BaseResPanelEntity baseResPanelEntity);

	@Insert("INSERT INTO `conf_info_panel` ( `panel_name`, `menu_id`, `remark`, `status`, `create_user`, `create_time`) VALUES ( #{panelName}, #{menuId}, #{remark}, #{status}, #{createUser}, #{createTime})")
	public boolean insert(BaseResPanelEntity baseResPanelEntity);

	/**
	 * @param asList
	 * @return
	 */
	public boolean delete(List<Integer> asList);
	//按菜单ID删除面板   逻辑删除
	public boolean deleteByMenuId(List<Integer> menuIds);

	public List<BaseResPanelEntity> selectVisitPanel(@Param("userId") Integer userId, @Param("privVisitId") String privVisitId, @Param("panelId") Integer panelId, @Param("panelName") String panelName);
	
	public List<BaseResPanelEntity> selectByAllVisitPanel( @Param("privVisitId") String privVisitId, @Param("panelId") Integer panelId,@Param("menuId") Integer menuId, @Param("panelName") String panelName);

	public List<Map<String, Object>> selectByRoleSelected(@Param("roleId") Integer roleId, @Param("menuId") Integer menuId, @Param("panelName") String panelName);

	public List<BaseResPanelEntity> selectByRoleNotSelected(@Param("roleId") Integer roleId, @Param("menuId") Integer menuId, @Param("panelName") String panelName);
}
