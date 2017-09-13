package com.flchy.blog.controller;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.flchy.blog.service.ArticleService;

@RestController
public class ArticleController implements ArticleService {

	@Override
	public Object selectArticleKey(@RequestParam Integer id) {
		return "sdflkjsdlkfj";
	}
}
