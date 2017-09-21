package com.flchy.blog.hystric;

import org.springframework.stereotype.Component;

import com.flchy.blog.pojo.Article;
import com.flchy.blog.service.ArticleService;

/**
 * 断路器
 * 
 * @author 24845
 *
 */
@Component
public class ArticleServiceHystric implements ArticleService {

	@Override
	public Object selectArticleKey(Integer id) {
		// TODO Auto-generated method stub
		return "查询错误";
	}


	@Override
	public Object selectArticleDeleted() {
		return "errr:selectArticleDeleted";
	}


//	@Override
//	public Object selectArticleKey(Integer id) {
//		return "查询错误";
//	}


//	@Override
//	public Object insert(Article article) {
//		// TODO Auto-generated method stub
//		return null;
//	}

}
