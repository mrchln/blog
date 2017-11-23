package com.flchy.blog.inlets.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.flchy.blog.inlets.enums.Keys;
import com.flchy.blog.inlets.holder.ConfigHolder;
import com.flchy.blog.inlets.mapper.ErrorLogMapper;
import com.flchy.blog.inlets.service.IErrorLogService;
import com.flchy.blog.pojo.ErrorLog;
import com.flchy.blog.utils.HttpRequestor;
import com.flchy.blog.utils.NewMapUtil;

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
		String cause=exception.getCause()==null?null:exception.getCause().toString();
		String clazz=exception.getClass()==null?null:exception.getClass().toString();
		log.setCause(cause);
		log.setClasss(clazz);
		log.setCreateTime(new Date());
		log.setMessage(exception.getMessage());
		try {
			String content="错误原因:"+cause+" <br>错误详情:"+exception.getMessage();
			new HttpRequestor().doPost(ConfigHolder.getConfig(Keys.MAIL_HTTP_ADDRESS.getKey()), new NewMapUtil()
					.set("to", ConfigHolder.getConfig(Keys.ADMIN_MAIL.getKey()))
					.set("title", "接口错误:"+clazz)
					.set("content",content )
					.get(), null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		errorLogMapper.insert(log);
	}

}
