package com.flchy.blog.inlets.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.flchy.blog.common.response.ResponseCommand;
import com.flchy.blog.utils.NewMapUtil;

/**
 * controller全局异常处理
 * 
 * @author flchy
 *
 */
@ControllerAdvice
public class GlobalExceptionHandler {
	private final static Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(BusinessException.class)
	@ResponseBody
	public Object handle(BusinessException e) {
		logger.warn("全局处理异常: " +e.getErrMsg()+"\nmessage:"+ e.getMessage());
		// 业务失败返回
		return new ResponseCommand(e.getErrCode(),new NewMapUtil("message", e.getErrMsg()).get());
	}

	@ExceptionHandler(Exception.class) // 可以直接写@EceptionHandler，IOExeption继承于Exception
	@ResponseBody
	public Object allExceptionHandler(Exception exception) {
		logger.error("-------------------------分割线start-------------------------");
		logger.error("业务异常:" + exception.getMessage());
		logger.error("-------------------------分割线end---------------------------");
		return new ResponseCommand(ResponseCommand.STATUS_ERROR,
				new NewMapUtil("message", "网络异常请稍后再试！！").get());
	}
}
