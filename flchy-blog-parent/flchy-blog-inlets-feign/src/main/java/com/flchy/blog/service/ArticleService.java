package com.flchy.blog.service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.flchy.blog.hystric.ArticleServiceHystric;

@FeignClient(value = "service-blog", fallback = ArticleServiceHystric.class)
public interface ArticleService {
	static String path="/article";
	// @PostMapping(value = "/page")
	// public Object selectArticlePage(Integer current, Integer size);

	 @GetMapping(path+"/deleted")
	 public Object selectArticleDeleted();
	/*	
	 * @PostMapping public Object insert(Article article);
	 */

	@RequestMapping(value=path+"/{id}",method=RequestMethod.GET, consumes = MediaType.ALL_VALUE)
	public Object selectArticleKey(@PathVariable("id") Integer id);
}
