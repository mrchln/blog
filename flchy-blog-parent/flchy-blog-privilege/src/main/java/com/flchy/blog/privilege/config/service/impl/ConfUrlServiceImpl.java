package com.flchy.blog.privilege.config.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.flchy.blog.base.exception.BusinessException;
import com.flchy.blog.inlets.mapper.ConfUrlMapper;
import com.flchy.blog.plugin.redis.RedisBusines;
import com.flchy.blog.pojo.ConfUrl;
import com.flchy.blog.privilege.config.controller.TokenPortalController;
import com.flchy.blog.privilege.config.service.IConfUrlService;
import com.flchy.blog.utils.convert.BeanConvertUtil;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author nieqs
 * @since 2017-09-26 
 */
@Service
public class ConfUrlServiceImpl extends ServiceImpl<ConfUrlMapper, ConfUrl> implements IConfUrlService {
	private static final Logger logger = LoggerFactory.getLogger(ConfUrlServiceImpl.class);
	@Autowired
	private RedisBusines redisBusines;

	private StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
	private String getConfurlAll = stackTraceElement.getClassName() + ".getConfurlAll";

	@Override
	public ConfUrl saveConfUrlByUrlIds(String urlPath) {
		getConfurlAll();
		List<ConfUrl> confUrlList = super.selectList(new EntityWrapper<ConfUrl>().where("url_path={0}", urlPath));
		if (null == confUrlList || confUrlList.isEmpty()) {
			ConfUrl confUrl = new ConfUrl();
			confUrl.setUrlPath(urlPath);
			confUrl.setCreateTime(new Date());
			confUrl.setCreateUser("system");
			confUrl.setStatus(1);
			confUrl.setType(1);
			boolean insert = confUrl.insert();
			if(!insert){
				logger.error("添加URL地址失败");
			}
			return confUrl;
		} else {
			return confUrlList.get(0);
		}
	}

	@Override
	public List<ConfUrl> getConfurlAll() {
		// String rediskey=
		// stackTraceElement.getClassName()+"."+stackTraceElement.getMethodName();
		List<ConfUrl> confUrls = new ArrayList<>();
		try {
			String confurlAll = redisBusines.get(getConfurlAll);
			if (confurlAll == null) {
				confUrls = super.selectList(new EntityWrapper<>());
				redisBusines.set(getConfurlAll, JSON.toJSONString(confUrls));
			} else {
				confUrls = JSONArray.parseArray(confurlAll, ConfUrl.class);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			confUrls = super.selectList(new EntityWrapper<>());
		}
		return confUrls;
	}

	/**
	 * 更新缓存
	 * 
	 * @return
	 */
	@Override
	public List<ConfUrl> replaceConfurlAll() {
		try {
			redisBusines.del(getConfurlAll);
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return getConfurlAll();
	}
	/**
	 * 获取指定类型的数据
	 */
	@Override
	public List<ConfUrl> getTypeConfUrl(Integer type){
		List<ConfUrl> list=this.getConfurlAll().stream().filter(l->l.getType()==type).collect(Collectors.toList());
		return list;
	}
}
