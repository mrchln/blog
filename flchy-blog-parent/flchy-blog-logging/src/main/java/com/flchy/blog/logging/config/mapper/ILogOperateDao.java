package com.flchy.blog.logging.config.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.flchy.blog.base.datasource.annotation.ConfigRepository;
import com.flchy.blog.logging.config.entity.LogOperate;

@ConfigRepository
public interface ILogOperateDao {
	public List<LogOperate> selectLogOperate(@Param("mainAccount")String mainAccount
			,@Param("isError")Integer isError
			,@Param("oprBeginTimeStart")Date oprBeginTimeStart
			,@Param("oprBeginTimeStop")Date oprBeginTimeStop
			,@Param("oprEndTimeStart")Date oprEndTimeStart
			,@Param("oprEndTimeStop")Date oprEndTimeStop
			);
}
