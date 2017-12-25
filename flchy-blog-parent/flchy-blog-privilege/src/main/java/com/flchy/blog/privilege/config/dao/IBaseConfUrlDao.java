package com.flchy.blog.privilege.config.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.flchy.blog.base.datasource.annotation.ConfigRepository;
import com.flchy.blog.privilege.config.entity.BaseConfUrlEntity;

@ConfigRepository
public interface IBaseConfUrlDao {

	public List<BaseConfUrlEntity> selectConfUrlByUrlIds(@Param("urlIdArray") Integer[] urlIdArray);

	public List<BaseConfUrlEntity> selectConfUrlByUrlPath(@Param("urlPath") String urlPath, @Param("method") String method);

	public void saveConfUrlByUrlIds(Map<String, Object> paramMap);

	public List<BaseConfUrlEntity> select(BaseConfUrlEntity baseConfUrlEntity);

	public boolean delete(List<Integer> urlIdList);
	
	public List<Map<String, Object>> selectUrlSelected(@Param("privVisitId") String privVisitId, @Param("urlPath") String urlPath);
	
	public List<BaseConfUrlEntity> selectUrlNotSelected(@Param("privVisitId") String privVisitId, @Param("urlPath")  String urlPath);
}
