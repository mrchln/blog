package com.flchy.blog.privilege.config.service;

import java.util.List;

import com.flchy.blog.base.response.PageHelperResult;
import com.flchy.blog.privilege.config.entity.BaseInfoDimAuthEntity;

/**
 * 用户数据权限，直接存储用户对应的数据权限
 * @author nieqs
 *
 */
public interface IBaseInfoDimAuthService {

	/**
	 * @param baseInfoDimAuthEntity
	 * @return
	 */
	boolean insert(BaseInfoDimAuthEntity baseInfoDimAuthEntity);

	/**
	 * @param baseInfoDimAuthEntity
	 * @return
	 */
	boolean update(BaseInfoDimAuthEntity baseInfoDimAuthEntity);

	/**
	 * @param id
	 * @return
	 */
	Object delete(String id);

	PageHelperResult selectDimAuth(int pageSize, int currentPage, BaseInfoDimAuthEntity baseInfoDimAuthEntity);

	PageHelperResult selectByUser(int pageSize, int currentPage, Integer userId, String remark);

	PageHelperResult selectByRole(int pageSize, int currentPage, Integer roleId, String remark);

	List<BaseInfoDimAuthEntity> selectByRoleNotSelected(Integer roleId, String remark);


}
