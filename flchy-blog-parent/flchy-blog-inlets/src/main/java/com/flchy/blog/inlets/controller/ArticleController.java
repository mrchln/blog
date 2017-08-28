package com.flchy.blog.inlets.controller;

import java.util.Date;
import java.util.Map;

import javax.validation.constraints.NotNull;
import javax.ws.rs.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.flchy.blog.common.response.ResponseCommand;
import com.flchy.blog.inlets.entity.Article;
import com.flchy.blog.inlets.exception.BusinessException;
import com.flchy.blog.inlets.response.ResultPage;
import com.flchy.blog.inlets.service.IArticleService;
import com.flchy.blog.utils.convert.MapConvertUtil;

/**
 * <p>
 * 文章表 前端控制器
 * </p>
 *
 * @author nieqs
 * @since 2017-08-08
 */
@RestController
@RequestMapping("article")
public class ArticleController {
	@Autowired
	private IArticleService iArticleService;

	/**
	 * 添加
	 * 
	 * @param article
	 * @return
	 */
	@PostMapping
	public Object insert(@ModelAttribute Article article) {
		if (article == null) {
			throw new BusinessException("添加为空！");
		}
		if (article.getTitle() == null) {
			throw new BusinessException("标题不能为空！");
		}
		article.setCreateTime(new Date());
		boolean isok = iArticleService.insert(article);
		if (!isok) {
			throw new BusinessException("Add failed");
		}
		return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, article);
	}

	/**
	 * 修改
	 * 
	 * @param article
	 * @return
	 */
	@PostMapping(value = "/")
	public Object update(@NotNull Article article) {
		if (article.getId() == null) {
			throw new BusinessException("ID must preach");
		}
		boolean isok = iArticleService.update(article, new EntityWrapper<Article>().where("id={0}", article.getId()));
		if (!isok) {
			throw new BusinessException("Update failed");
		}
		return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, article);
	}

	@DeleteMapping
	public Object delete(Article article) {
		Article ar = new Article();
		ar.setStatus(-1);
		boolean isok = iArticleService.update(ar, new EntityWrapper<Article>().where("id={0}", article.getId()));
		if (!isok) {
			throw new BusinessException("Delete failed");
		}
		return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, article);
	}

	@PostMapping(value = "/page")
	public Object selectArticlePage(Map<String, Object> map) {
		System.out.println(map.containsKey("current"));
		System.out.println(map.get("current"));
		if (!map.containsKey("current") || map.get("current").toString().isEmpty()) {
			throw new BusinessException("current must preach");
		}
		if (!map.containsKey("size") || map.get("size").toString().isEmpty()) {
			throw new BusinessException("size must preach");
		}
		Article article = MapConvertUtil.toBean(Article.class, map);
		Page<Article> page = new Page<>(Integer.valueOf(map.get("current").toString()),
				Integer.valueOf(map.get("size").toString()));
		iArticleService.selectPage(page, new EntityWrapper<Article>(article));
		return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, new ResultPage(page));
	}

	@GetMapping()
	public Object selectArticleKey(@PathParam("id") Integer id) {

		Article article = iArticleService.selectById(id);
		if (article == null) {
			throw new BusinessException("isNull");
		}
		return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, article);
	}

}
