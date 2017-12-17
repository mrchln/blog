package com.flchy.blog.logging.config.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.flchy.blog.base.datasource.annotation.ConfigRepository;
import com.flchy.blog.logging.config.entity.LogLogin;

@ConfigRepository
public interface ILogLoginDao {
	
	public List<LogLogin> selectLogLogin(@Param("startDate")Date startDate,@Param("stopDate")Date stopDate,@Param("mainAccount")String mainAccount,@Param("browserType")String browserType);

}
