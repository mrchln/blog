package com.flchy.blog.service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.flchy.blog.hystric.ArticleServiceHystric;
import com.flchy.blog.pojo.Article;

@FeignClient(value = "service-blog", fallback = ArticleServiceHystric.class)
public interface ArticleService {
	// @PostMapping(value = "/page")
	// public Object selectArticlePage(Integer current, Integer size);

	// @GetMapping("deleted")
	// public Object selectArticleDeleted();
	/*
	 * @PostMapping public Object insert(Article article);
	 */

	@GetMapping
	public Object selectArticleKey(@RequestParam Integer id);
}
