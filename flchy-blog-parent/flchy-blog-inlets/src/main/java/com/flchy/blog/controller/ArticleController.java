package com.flchy.blog.controller;

import static org.hamcrest.CoreMatchers.nullValue;

import java.util.Map;

import javax.validation.constraints.NotNull;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.flchy.blog.utils.NewMapUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.flchy.blog.entity.Article;
import com.flchy.blog.service.IArticleService;
import com.zuobiao.analysis.common.response.ResponseCommand;
import com.zuobiao.analysis.privilege.config.response.VisitsResult;

/**
 * <p>
 * 文章表 前端控制器
 * </p>
 *
 * @author nieqs
 * @since 2017-08-08
 */
@Path("article")
@Controller
@Produces(MediaType.APPLICATION_JSON)  
public class ArticleController {
	@Autowired
	private IArticleService iArticleService;

	/**
	 * 添加
	 * 
	 * @param article
	 * @return
	 */
	@POST
	public Object insert(Article article) {
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
	@PUT
	public Object update(@NotNull Article article) {
		boolean isok = iArticleService.update(article, new EntityWrapper<Article>().where("id={0}", article.getId()));
		if (!isok) {
			return new ResponseCommand(ResponseCommand.STATUS_ERROR,
					new VisitsResult(new NewMapUtil("message", "Update failed").get()));
		}
		return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, article);
	}

	@DELETE
	public Object delete(Article article) {
		boolean isok = iArticleService.delete(new EntityWrapper<Article>(article));
		if (!isok) {
			return new ResponseCommand(ResponseCommand.STATUS_ERROR,
					new VisitsResult(new NewMapUtil("message", "Delete failed").get()));
		}
		return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, article);
	}

	@POST
	@Path("page")
//	@Consumes(MediaType.APPLICATION_JSON)  
	public Object selectArticlePage(@NotNull  Map<String, Object> map) {
		Page<Article> page=new Page<>(Integer.valueOf(map.get("current").toString()),Integer.valueOf(map.get("size").toString()));
		iArticleService.selectPage(page);
		return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, page);
//		return null;
	}

	@GET
	public Object selectArticleKey(@NotNull @QueryParam("id") Integer id) {
		
		Article article = iArticleService.selectById(id);
		if (article == null) {
			return new ResponseCommand(ResponseCommand.STATUS_ERROR,
					new VisitsResult(new NewMapUtil("message", "isNull").get()));
		}
		return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, article);
	}

}
