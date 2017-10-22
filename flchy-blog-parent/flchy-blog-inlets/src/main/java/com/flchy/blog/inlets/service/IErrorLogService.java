package com.flchy.blog.inlets.service;

import com.baomidou.mybatisplus.service.IService;
import com.flchy.blog.pojo.ErrorLog;

/**
 * <p>
 * 错误日志表 服务类
 * </p>
 *
 * @author nieqs
 * @since 2017-08-28
 */
public interface IErrorLogService extends IService<ErrorLog> {

	/**
	 * 登录验证
	 * @param exception 异常信息
	 */
	void insertException(Exception exception);
	
}
