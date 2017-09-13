package com.flchy.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.flchy.blog.service.ArticleServiceClient;

@RestController
public class ArticleController {
	@Autowired
	private ArticleServiceClient articleService;

	@GetMapping("/article")
	public Object selectArticleKey(Integer id) {
		return articleService.selectArticleKey(id);
	}
}
