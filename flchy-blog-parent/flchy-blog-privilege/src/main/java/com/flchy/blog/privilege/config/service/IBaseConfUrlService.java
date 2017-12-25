package com.flchy.blog.privilege.config.service;

import java.util.List;

import com.flchy.blog.base.response.PageHelperResult;
import com.flchy.blog.privilege.config.entity.BaseConfUrlEntity;

/**
 * 访问Url路径： 每次启动自动扫描，添加当前表，若不存在的，则标记为失效，增量维护，不删除
 * @author KingXu
 *
 */
public interface IBaseConfUrlService {

	public List<BaseConfUrlEntity> selectConfUrlByUrlIds(Integer[] urlIdArray );
	

	public BaseConfUrlEntity saveConfUrlByUrlIds(String urlPath, String method);




	/**
	 * @param pageSize
	 * @param currentPage
	 * @param baseConfUrlEntity
	 * @return
	 */
	PageHelperResult selectPage(int pageSize, int currentPage, BaseConfUrlEntity baseConfUrlEntity);


	/**
	 * @param urlId
	 * @return
	 */
	Object deleteUrl(String urlId);






	PageHelperResult selectUrlSelected(String privVisitId, String urlPath, int pageSize, int currentPage);


	List<BaseConfUrlEntity> selectUrlNotSelected(String privVisitId, String urlPath);
}
