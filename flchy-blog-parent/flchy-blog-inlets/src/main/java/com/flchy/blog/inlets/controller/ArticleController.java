package com.flchy.blog.inlets.controller;

import java.util.Date;
import java.util.Map;

import javax.validation.constraints.NotNull;
import javax.ws.rs.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.flchy.blog.common.response.ResponseCommand;
import com.flchy.blog.common.response.VisitsResult;
import com.flchy.blog.inlets.entity.Article;
import com.flchy.blog.inlets.response.ResultPage;
import com.flchy.blog.inlets.service.IArticleService;
import com.flchy.blog.utils.NewMapUtil;
import com.flchy.blog.utils.convert.MapConvertUtil;

/**
 * <p>
 * 文章表 前端控制器
 * </p>
 *
 * @author nieqs
 * @since 2017-08-08
 */
//@Path("article")
//@Controller
//@Produces(MediaType.APPLICATION_JSON)
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
//	@Consumes(MediaType.APPLICATION_JSON)
//	@Produces(MediaType.APPLICATION_JSON)
//	@POST
	@PostMapping
	public Object insert(Article article) {
		article.setCreateTime(new Date());
		boolean isok = iArticleService.insert(article);
		if (!isok) {
			return new ResponseCommand(ResponseCommand.STATUS_ERROR,
					new VisitsResult(new NewMapUtil("message", "Add failed").get()));
		}
		return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, article);
	}

	/**
	 * 修改
	 * 
	 * @param article
	 * @return
	 */
//	@PUT
	@PostMapping(value="/")
	public Object update(@NotNull Article article) {
		if (article.getId() == null) {
			return new ResponseCommand(ResponseCommand.STATUS_ERROR,
					new VisitsResult(new NewMapUtil("message", "ID must preach").get()));
		}
		boolean isok = iArticleService.update(article, new EntityWrapper<Article>().where("id={0}", article.getId()));
		if (!isok) {
			return new ResponseCommand(ResponseCommand.STATUS_ERROR,
					new VisitsResult(new NewMapUtil("message", "Update failed").get()));
		}
		return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, article);
	}

//	@DELETE
	@DeleteMapping
	public Object delete(Article article) {
		Article ar = new Article();
		ar.setStatus(-1);
		boolean isok = iArticleService.update(ar, new EntityWrapper<Article>().where("id={0}", article.getId()));
		if (!isok) {
			return new ResponseCommand(ResponseCommand.STATUS_ERROR,
					new VisitsResult(new NewMapUtil("message", "Delete failed").get()));
		}
		return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, article);
	}

//	@POST
//	@Path("page")
	// @Consumes(MediaType.APPLICATION_JSON)
	@PostMapping(value="/page")
	public Object selectArticlePage(Map<String, Object> map) {
		System.out.println(map.containsKey("current"));
		System.out.println(map.get("current"));
		if (!map.containsKey("current") || map.get("current").toString().isEmpty()) {
			return new ResponseCommand(ResponseCommand.STATUS_ERROR,
					new VisitsResult(new NewMapUtil("message", "current must preach").get()));
		}
		if (!map.containsKey("size") || map.get("size").toString().isEmpty()) {
			return new ResponseCommand(ResponseCommand.STATUS_ERROR,
					new VisitsResult(new NewMapUtil("message", "size must preach").get()));
		}
		Article article = MapConvertUtil.toBean(Article.class, map);
		Page<Article> page = new Page<>(Integer.valueOf(map.get("current").toString()),
				Integer.valueOf(map.get("size").toString()));
		iArticleService.selectPage(page, new EntityWrapper<Article>(article));
		return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, new ResultPage(page));
	}

//	@GET
//	@Path("fff")
	@GetMapping()
	public Object selectArticleKey(@PathParam("id") Integer id) {

		Article article = iArticleService.selectById(id);
		if (article == null) {
			return new ResponseCommand(ResponseCommand.STATUS_ERROR,
					new VisitsResult(new NewMapUtil("message", "isNull").get()));
		}
		return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, article);
	}

}
