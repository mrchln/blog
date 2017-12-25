package com.flchy.blog.base.dbconfig.service.impl;

import org.springframework.stereotype.Service;

import com.flchy.blog.base.dbconfig.holder.EnumDicHolder;
import com.flchy.blog.base.service.ScheduledService;

/**
 * 定时刷新枚举配重 
 * @ClassName: EnumDicScheduledService.java 
 * @Description:  
 * @author nieqs
 * @date 2017-07-14 
 *
 */
@Service
public class EnumDicScheduledService implements ScheduledService {

	@Override
	public String getName() {
		return "定时刷新枚举配重";
	}

	@Override
	public void schedule() {
		EnumDicHolder.refreshCache();
	}

}
