package com.flchy.blog.privilege.config.service;

import java.util.List;
import java.util.Map;

import com.flchy.blog.privilege.config.bean.BaseMenu;
import com.flchy.blog.privilege.config.entity.BaseInfoMenuEntity;

/**
 * CONF_INFO_MENU： 菜单数据操作类
 * @author KingXu
 *
 */
public interface IBaseInfoMenuService {

	public BaseInfoMenuEntity selectMenuInfoById(int menuId);
	
	public List<BaseInfoMenuEntity> selectAllMenuList();

	/**
	 * 添加菜单
	 * @param baseInfoMenuEntity
	 * @return
	 */
	boolean insertMenu(BaseInfoMenuEntity baseInfoMenuEntity);

	/**
	 * @param baseInfoMenuEntity
	 * @return
	 */
	boolean update(BaseInfoMenuEntity baseInfoMenuEntity);

	/**
	 * @param menuId
	 * @return
	 */
	Object delete(String menuId);

	BaseMenu selectMenuByUserId(Integer userId);

	List<Map<String, Object>> selectMenuByRoleId(Integer roleId);

	BaseMenu selectByRoleIdAllMenu(Integer roleId);
}
