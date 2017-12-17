package com.flchy.blog.logging.config.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.flchy.blog.base.datasource.annotation.ConfigRepository;
import com.flchy.blog.logging.config.entity.LogServerInvoke;

@ConfigRepository
public interface ILogServerInvokeDao {
	public List<LogServerInvoke> selectLogServerInvoke(@Param("invokeCont")String invokeCont
			,@Param("isFail")Integer isFail
			,@Param("mainAccount")String mainAccount
			,@Param("invokeBeginTimeStart")Date invokeBeginTimeStart
			,@Param("invokeBeginTimeStop")Date invokeBeginTimeStop
			,@Param("invokeEndTimeStart")Date invokeEndTimeStart
			,@Param("invokeEndTimeStop")Date invokeEndTimeStop
			);
}
