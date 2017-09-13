package com.flchy.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.flchy.blog.service.ArticleService;

@RestController
@RequestMapping
public class ArticleController {
	@Autowired
	private ArticleService articleService;


//	@GetMapping("deleted")
//	public Object selectArticleDeleted() {
//		return articleService.selectArticleDeleted();
//	}

	/**
	 * 添加
	 * 
	 * @param article
	 * @return
	 */
//	@PostMapping
//	public Object insert(@ModelAttribute Article article) {
//		return articleService.insert(article);
//	}

	@GetMapping("/article")
	public Object selectArticleKey(Integer id) {
		return articleService.selectArticleKey(id);
	}
}
