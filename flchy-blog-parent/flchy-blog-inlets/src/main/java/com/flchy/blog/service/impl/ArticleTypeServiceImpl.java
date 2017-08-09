package com.flchy.blog.service.impl;

import com.flchy.blog.entity.ArticleType;
import com.flchy.blog.dao.ArticleTypeMapper;
import com.flchy.blog.service.IArticleTypeService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 文章类型 服务实现类
 * </p>
 *
 * @author nieqs
 * @since 2017-08-08
 */
@Service
public class ArticleTypeServiceImpl extends ServiceImpl<ArticleTypeMapper, ArticleType> implements IArticleTypeService {
	
}
