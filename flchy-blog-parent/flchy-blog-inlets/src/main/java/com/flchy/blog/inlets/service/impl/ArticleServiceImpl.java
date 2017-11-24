package com.flchy.blog.inlets.service.impl;

import com.flchy.blog.inlets.mapper.ArticleMapper;
import com.flchy.blog.inlets.service.IArticleService;
import com.flchy.blog.pojo.Article;
import com.flchy.blog.pojo.Comment;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
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
	
	@Autowired
	private ArticleMapper articleMapper;
	
	
	@Override
	public Page<Article> selectArticlePage(Page<Article> page,Article article){
		 page.setRecords( articleMapper.selectPage(article));
		 return page;
	}
	
	
}
