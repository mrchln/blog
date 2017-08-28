package com.flchy.blog.inlets.service;

import com.flchy.blog.inlets.entity.ErrorLog;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 * 错误日志表 服务类
 * </p>
 *
 * @author nieqs
 * @since 2017-08-28
 */
public interface IErrorLogService extends IService<ErrorLog> {

	void insertException(Exception exception);
	
}