package com.flchy.blog.logging.config.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.flchy.blog.base.datasource.annotation.ConfigRepository;
import com.flchy.blog.logging.config.entity.LogMsgSend;

@ConfigRepository
public interface ILogMsgSendDao {
	
	public List<LogMsgSend> selectLogMsgSend(@Param("receiver")String receiver
			,@Param("msgTitle")String msgTitle
			,@Param("sendBeginTimeStart")Date sendBeginTimeStart
			,@Param("sendBeginTimeStop")Date sendBeginTimeStop
			,@Param("sendEndTimeStart")Date sendEndTimeStart
			,@Param("sendEndTimeStop")Date sendEndTimeStop
			);

}
