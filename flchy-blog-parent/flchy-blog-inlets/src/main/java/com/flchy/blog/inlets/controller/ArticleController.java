package com.flchy.blog.inlets.controller;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.flchy.blog.common.response.ResponseCommand;
import com.flchy.blog.inlets.enums.StatusEnum;
import com.flchy.blog.inlets.exception.BusinessException;
import com.flchy.blog.inlets.response.ResultPage;
import com.flchy.blog.inlets.service.IArticleService;
import com.flchy.blog.pojo.Article;
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
	public Object update(Article article) {
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
		ar.setStatus(StatusEnum.DELETE.getCode());
		boolean isok = iArticleService.update(ar, new EntityWrapper<Article>().where("id={0}", article.getId()));
		if (!isok) {
			throw new BusinessException("Delete failed");
		}
		return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, article);
	}

	@PostMapping(value = "/page")
	public Object selectArticlePage(@RequestParam(value = "current", required = true) Integer current,
			@RequestParam(value = "size", required = true) Integer size, Article article) {
		Page<Article> page = new Page<>(Integer.valueOf(current), Integer.valueOf(size));
		iArticleService.selectPage(page, new EntityWrapper<Article>(article).where("status!={0}", StatusEnum.DELETE.getCode()));
		return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, new ResultPage(page));
	}

	@GetMapping
	public Object selectArticleKey(@RequestParam Integer id) {

		Article article = iArticleService.selectById(id);
		if (article == null) {
			throw new BusinessException("isNull");
		}
		return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, article);
	}

	/**
	 * 已删除数据
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value="deleted",method=RequestMethod.GET)
	public Object selectArticleDeleted() {

		List<Article> article = iArticleService
				.selectList(new EntityWrapper<Article>().where("status={0}", StatusEnum.DELETE.getCode()));
		if (article == null) {
			throw new BusinessException("isNull");
		}
		return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, article);
	}

}
