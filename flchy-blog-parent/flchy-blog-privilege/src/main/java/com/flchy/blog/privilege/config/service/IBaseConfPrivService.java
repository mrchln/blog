package com.flchy.blog.privilege.config.service;

import java.util.List;

import com.flchy.blog.privilege.config.entity.BaseConfPrivEntity;

/**
 * 访问权限表，一个菜单对应一个访问权限，新建一个菜单记录，则自动创建一个访问权限记录
 *
 */
public interface IBaseConfPrivService {

	public List<BaseConfPrivEntity> selectConfPrivByPrivVisitIds(String[] privVisitIdArray);
	
	public  List<BaseConfPrivEntity> selectAllPrivVisitUrl();

	/**
	 * @param baseConfPrivEntity
	 * @return
	 */
	boolean insert(BaseConfPrivEntity baseConfPrivEntity);

	/**
	 * 批量删除权限表
	 * @param menuIdList 菜单ID
	 * @return
	 */
	boolean delete(List<Integer> menuIdList);
}
