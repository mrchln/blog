package com.flchy.blog.service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

public interface ArticleService {

	@GetMapping("/article")
	public Object selectArticleKey( Integer id);
}
