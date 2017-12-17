package com.flchy.blog.logging;

import java.util.Map;


/**
 * 日志添加
 * @author nieqs
 */
public interface AbstractLog {
	
	public void init();


	public void stop();

	void start(Map<String, Object> map);
}
