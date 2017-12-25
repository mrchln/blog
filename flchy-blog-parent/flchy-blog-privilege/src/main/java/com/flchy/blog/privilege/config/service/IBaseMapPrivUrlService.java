package com.flchy.blog.privilege.config.service;

import java.util.List;

import com.flchy.blog.privilege.config.entity.BaseMapPrivUrlEntity;

/**
 * 访问权限与URL映射关系表，为每个访问权限对应的Url地址,需要配置，不配置则无法访问
 *
 */
public interface IBaseMapPrivUrlService {

	public List<BaseMapPrivUrlEntity> selectPrivUrlMapByPrivVisitIds(String[] privVisitIdArray);

	List<BaseMapPrivUrlEntity> selectPrivUrlMapBySelected(String privVisitId);

	Object delete(String id);

	boolean insert(BaseMapPrivUrlEntity baseMapPrivUrlEntity);

	boolean insertCodeBatch(Integer[] urlId, String privVisitId, String userName);
}
