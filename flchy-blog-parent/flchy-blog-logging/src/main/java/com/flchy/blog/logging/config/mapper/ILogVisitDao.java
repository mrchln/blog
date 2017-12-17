package com.flchy.blog.logging.config.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.flchy.blog.base.datasource.annotation.ConfigRepository;
import com.flchy.blog.logging.config.entity.LogVisit;

@ConfigRepository
public interface ILogVisitDao {
	
	public List<LogVisit> selectLogVisit(@Param("mainAccount")String mainAccount
			,@Param("isError")Integer isError
			,@Param("visitBeginTimeStart")Date visitBeginTimeStart
			,@Param("visitBeginTimeStop")Date visitBeginTimeStop
			,@Param("visitEndTimeStart")Date visitEndTimeStart
			,@Param("visitEndTimeStop")Date visitEndTimeStop
			);

}
