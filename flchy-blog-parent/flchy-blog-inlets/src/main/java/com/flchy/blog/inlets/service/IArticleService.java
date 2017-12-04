package com.flchy.blog.inlets.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.flchy.blog.pojo.Article;

/**
 * <p>
 * 文章表 服务类
 * </p>
 *
 * @author nieqs
 * @since 2017-08-08
 */
public interface IArticleService extends IService<Article> {


	public Page<Article> selectArticlePage(Page<Article> page, Article article);
	
}
