package com.flchy.blog.privilege.config.service;

import java.util.List;

import com.flchy.blog.base.response.PageHelperResult;
import com.flchy.blog.privilege.config.entity.BaseResPanelEntity;

/**
 * @author nieqs
 *
 */
public interface IBaseResPanelService {


	/**
	 * @param baseResPanelEntity
	 * @return
	 */
	boolean update(BaseResPanelEntity baseResPanelEntity);

	/**
	 * @param baseResPanelEntity
	 * @return
	 */
	boolean insert(BaseResPanelEntity baseResPanelEntity);

	/**
	 * @param panelId
	 * @return
	 */
	Object delete(String panelId);

	List<BaseResPanelEntity> selectVisitPanel(Integer userId, String privVisitId, Integer panelId, String panelName);

	PageHelperResult selectResPanel(int pageSize, int currentPage, BaseResPanelEntity baseResPanelEntity);

	List<BaseResPanelEntity> selectResPanel(BaseResPanelEntity baseResPanelEntity);

	PageHelperResult selectByRoleSelected(int pageSize, int currentPage, Integer roleId, Integer menuId, String panelName);


	List<BaseResPanelEntity> selectByRoleNotSelected(Integer roleId, Integer menuId, String panelName);

	boolean deleteResPanels(List<Integer> panelId);

}
