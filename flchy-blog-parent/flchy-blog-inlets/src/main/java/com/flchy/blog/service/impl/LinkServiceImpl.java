package com.flchy.blog.service.impl;

import com.flchy.blog.entity.Link;
import com.flchy.blog.dao.LinkMapper;
import com.flchy.blog.service.ILinkService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 友情链接 服务实现类
 * </p>
 *
 * @author nieqs
 * @since 2017-08-08
 */
@Service
public class LinkServiceImpl extends ServiceImpl<LinkMapper, Link> implements ILinkService {
	
}
