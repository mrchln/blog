package com.flchy.blog.inlets.service.impl;

import com.flchy.blog.inlets.mapper.ErrorLogMapper;
import com.flchy.blog.inlets.service.IErrorLogService;
import com.flchy.blog.pojo.ErrorLog;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 错误日志表 服务实现类
 * </p>
 *
 * @author nieqs
 * @since 2017-08-28
 */
@Service
public class ErrorLogServiceImpl extends ServiceImpl<ErrorLogMapper, ErrorLog> implements IErrorLogService {
	@Autowired
	private ErrorLogMapper errorLogMapper;

	/**
	 * 添加日志
	 */
	@Override
	public void insertException(Exception exception) {
		ErrorLog log=new ErrorLog();
		log.setCause(exception.getClass().toString());
		log.setClasss(exception.getCause().toString());
		log.setCreateTime(new Date());
		log.setMessage(exception.getMessage());
		errorLogMapper.insert(log);
	}

}
