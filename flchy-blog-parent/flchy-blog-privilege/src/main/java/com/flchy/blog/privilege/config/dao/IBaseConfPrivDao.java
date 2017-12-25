package com.flchy.blog.privilege.config.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

import com.flchy.blog.base.datasource.annotation.ConfigRepository;
import com.flchy.blog.privilege.config.entity.BaseConfPrivEntity;

@ConfigRepository
public interface IBaseConfPrivDao {
     
	public List<BaseConfPrivEntity> selectConfPrivByPrivVisitIds(@Param("privVisitIdArray") String[] privVisitIdArray);
	
	public List<BaseConfPrivEntity> selectAllPrivVisitUrl();
	
	@Insert("INSERT INTO `priv_conf_visit` (`priv_visit_id`, `menu_id`,  `status`, `create_user`, `create_time`) VALUES (#{privVisitId}, #{menuId}, #{status}, #{createUser}, #{createTime})")
	public boolean insert(BaseConfPrivEntity baseConfPrivEntity);
	
	public boolean delete(List<Integer> menuIdList);
}
