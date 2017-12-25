package com.flchy.blog.privilege.config.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

import com.flchy.blog.base.datasource.annotation.ConfigRepository;
import com.flchy.blog.privilege.config.entity.BaseResElementEntity;

@ConfigRepository
public interface IBaseResElementDao {

	public List<BaseResElementEntity> selectResElement(BaseResElementEntity baseResElementEntity);

	/**
	 * @param baseResPanelEntity
	 * @return
	 */
	public boolean update(BaseResElementEntity baseResElementEntity);

	@Insert("INSERT INTO `conf_info_element` (`element_name`, `element_type`, `menu_id`, `panel_id`, `remark`, `status`, `create_user`, `create_time`) VALUES ( #{elementName}, #{elementType}, #{menuId}, #{panelId}, #{remark}, #{status}, #{createUser}, #{createTime})")
	public boolean insert(BaseResElementEntity baseResElementEntity);

	/**
	 * ��idɾ��   �߼�ɾ��
	 * @param asList
	 * @return
	 */
	public boolean delete(List<Integer> asList);
	//�����idɾ��  �߼�ɾ��
	public boolean deleteByPanelId(List<Integer> asList);
	//���˵�idɾ��  �߼�ɾ��
	public boolean deleteByMenuId(List<Integer> menuIds);

	public List<BaseResElementEntity> selectVisitElement(@Param("userId") Integer userId, @Param("privVisitId") String privVisitId, @Param("elementId") Integer elementId, @Param("panelId") Integer panelId, @Param("elementName") String elementName);

	public List<Map<String, Object>> selectByRoleSelected(@Param("roleId") Integer roleId, @Param("menuId") Integer menuId, @Param("panelId") Integer panelId, @Param("elementName") String elementName);

	public List<BaseResElementEntity> selectByRoleNotSelected(@Param("roleId") Integer roleId, @Param("menuId") Integer menuId, @Param("panelId") Integer panelId, @Param("elementName") String elementName);
	
	public List<BaseResElementEntity> selectByAdminRole(@Param("panelId") Integer panelId, @Param("privVisitId") String privVisitId, @Param("elementId") Integer elementId,@Param("elementName") String elementName);
}
