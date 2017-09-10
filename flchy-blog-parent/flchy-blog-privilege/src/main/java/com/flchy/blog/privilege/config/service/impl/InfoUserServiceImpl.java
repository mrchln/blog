package com.flchy.blog.privilege.config.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.flchy.blog.pojo.InfoUser;
import com.flchy.blog.privilege.config.service.IInfoUserService;
import com.flchy.blog.privilege.mapper.InfoUserMapper;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author nieqs
 * @since 2017-08-02
 */
@Service
public class InfoUserServiceImpl extends ServiceImpl<InfoUserMapper, InfoUser> implements IInfoUserService {
	
	
}
