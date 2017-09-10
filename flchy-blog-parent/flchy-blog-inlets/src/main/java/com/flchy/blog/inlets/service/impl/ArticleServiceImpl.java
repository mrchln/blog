package com.flchy.blog.inlets.service.impl;

import com.flchy.blog.inlets.mapper.ArticleMapper;
import com.flchy.blog.inlets.service.IArticleService;
import com.flchy.blog.pojo.Article;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 文章表 服务实现类
 * </p>
 *
 * @author nieqs
 * @since 2017-08-08
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements IArticleService {
	
	
}
