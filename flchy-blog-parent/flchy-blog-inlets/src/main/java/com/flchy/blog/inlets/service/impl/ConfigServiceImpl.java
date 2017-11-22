package com.flchy.blog.inlets.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.flchy.blog.inlets.mapper.ConfigMapper;
import com.flchy.blog.inlets.service.IConfigService;
import com.flchy.blog.pojo.Config;

/**
 * <p>
 * 配置信息表 服务实现类
 * </p>
 *
 * @author nieqs
 * @since 2017-11-22
 */
@Service
public class ConfigServiceImpl extends ServiceImpl<ConfigMapper, Config> implements IConfigService {
	
}
