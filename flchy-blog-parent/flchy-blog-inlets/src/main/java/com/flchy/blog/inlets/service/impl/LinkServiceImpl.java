package com.flchy.blog.inlets.service.impl;

import com.flchy.blog.inlets.mapper.LinkMapper;
import com.flchy.blog.inlets.service.ILinkService;
import com.flchy.blog.pojo.Link;
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
