package com.flchy.blog.privilege.config.service;

import java.util.List;

import com.flchy.blog.base.response.PageHelperResult;
import com.flchy.blog.privilege.config.entity.BaseResElementEntity;

/**
 * 元素权限，用于角色赋权
 * @author nieqs
 *
 */
public interface IBaseResElementService {


	/**
	 * @param baseResElementEntity
	 * @return
	 */
	boolean insert(BaseResElementEntity baseResElementEntity);

	/**
	 * @param baseResElementEntity
	 * @return
	 */
	boolean update(BaseResElementEntity baseResElementEntity);

	/**
	 * @param elementId
	 * @return
	 */
	Object delete(String elementId);


	PageHelperResult selectResElement(int pageSize, int currentPage, BaseResElementEntity baseResElementEntity);

	List<BaseResElementEntity> selectResElement(BaseResElementEntity baseResElementEntity);

	PageHelperResult selectByRoleSelected(int pageSize, int currentPage, Integer roleId, Integer menuId, Integer panelId, String elementName);

	List<BaseResElementEntity> selectByRoleNotSelected(Integer roleId, Integer menuId, Integer panelId, String elementName);

	List<BaseResElementEntity> selectVisitElement(Integer userId, String privVisitId, Integer elementId, Integer panelId, String elementName);

}
