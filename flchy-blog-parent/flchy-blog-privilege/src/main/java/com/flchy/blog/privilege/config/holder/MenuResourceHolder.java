package com.flchy.blog.privilege.config.holder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.flchy.blog.base.service.ScheduledService;
import com.flchy.blog.privilege.config.cache.MenuResourceCache;

public class MenuResourceHolder implements ScheduledService {
	private static final Logger logger = LoggerFactory.getLogger(MenuResourceHolder.class);

	/**
	 * 系统参数配置值缓存数据。<br/>
	 * 详细描述：调用枚举缓存方法，刷新缓存<br/>
	 * 使用方式：对外提供调用refreshCache()方法
	 */
	public static void refreshCache() {
		MenuResourceCache.getInstance().initialize();
	}

	@Override
	public String getName() {
		return "菜单缓存";
	}

	@Override
	public void schedule() {
		refreshCache();
	}
}
