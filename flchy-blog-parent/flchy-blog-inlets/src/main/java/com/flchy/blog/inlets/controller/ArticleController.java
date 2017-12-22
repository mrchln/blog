package com.flchy.blog.inlets.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.flchy.blog.base.annotation.Log;
import com.flchy.blog.base.enums.OperateCodeEnum;
import com.flchy.blog.base.exception.BusinessException;
import com.flchy.blog.base.response.ResponseCommand;
import com.flchy.blog.base.response.ResultPage;
import com.flchy.blog.inlets.enums.StatusEnum;
import com.flchy.blog.inlets.service.IArticleService;
import com.flchy.blog.pojo.Article;

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
	 * @param article
	 * @return
	 */
	@PostMapping
	@Log(value="添加文章",type=OperateCodeEnum.INSERT)
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
	@PutMapping
	@Log(value="修改文章",type=OperateCodeEnum.UPDATE)
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
	@Log(value="删除文章",type=OperateCodeEnum.DELETE)
	public Object delete(Article article) {
		Article selectById = article.selectById();
		if(selectById.getStatus()==StatusEnum.DELETE.getCode()){
			return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, selectById.deleteById());
		}
		Article ar = new Article();
		ar.setStatus(StatusEnum.DELETE.getCode());
		boolean isok = iArticleService.update(ar, new EntityWrapper<Article>().where("id={0}", article.getId()));
		if (!isok) {
			throw new BusinessException("Delete failed");
		}
		return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, article);
	}

	@PostMapping(value = "/page")
	@Log(value="查询文章分页",type=OperateCodeEnum.SELECT)
	public Object selectArticlePage(@RequestParam(value = "current", required = true) Integer current,
			@RequestParam(value = "size", required = true) Integer size, Article article) {
		Page<Article> page = new Page<>(Integer.valueOf(current), Integer.valueOf(size));
		iArticleService.selectPage(page, new EntityWrapper<Article>(article).where("status!={0}", StatusEnum.DELETE.getCode()));
		return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, new ResultPage(page));
	}

	@GetMapping(value="/{id}")
	@Log(value="通过ID查询文章",type=OperateCodeEnum.SELECT)
	public Object selectArticleKey(@PathVariable Integer id,@RequestParam(value = "token", required = false) String token) {
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
	@RequestMapping(value="deleted",method=RequestMethod.POST)
	@Log(value="查询已删除文章",type=OperateCodeEnum.SELECT)
	public Object selectArticleDeleted(@RequestParam(value = "current", required = true) Integer current,
			@RequestParam(value = "size", required = true) Integer size) {
		Page<Article> page = new Page<>(Integer.valueOf(current), Integer.valueOf(size));
		iArticleService.selectPage(page, new EntityWrapper<Article>().where("status={0}", StatusEnum.DELETE.getCode()));
		return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, new ResultPage(page));
	}

}
