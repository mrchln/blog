package com.flchy.blog.privilege.config.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

import com.flchy.blog.base.datasource.annotation.ConfigRepository;
import com.flchy.blog.privilege.config.entity.BaseMapPrivUrlEntity;

@ConfigRepository
public interface IBaseMapPrivUrlDao {

	public List<BaseMapPrivUrlEntity> selectPrivUrlMapByPrivVisitIds(@Param("privVisitIdArray") String[] privVisitIdArray);
	
	public boolean delete(List<Integer> asList);

	@Insert("INSERT INTO `priv_map_visit_url` ( `priv_visit_id`, `url_id`, `status`, `create_user`, `create_time`) VALUES ( #{privVisitId}, #{urlId}, #{status}, #{createUser}, #{createTime})")
	public boolean insert(BaseMapPrivUrlEntity baseMapPrivUrlEntity);
	
	public boolean insertCodeBatch(List<BaseMapPrivUrlEntity> list);
	
}
