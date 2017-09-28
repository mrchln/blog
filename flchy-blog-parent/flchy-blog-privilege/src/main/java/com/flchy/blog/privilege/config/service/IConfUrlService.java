package com.flchy.blog.privilege.config.service;

import com.flchy.blog.pojo.ConfUrl;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author nieqs
 * @since 2017-09-26
 */
public interface IConfUrlService extends IService<ConfUrl> {

	ConfUrl saveConfUrlByUrlIds(String urlPath);

	List<ConfUrl> getConfurlAll();

	List<ConfUrl> replaceConfurlAll();

	List<ConfUrl> getTypeConfUrl(Integer type);
	
}
