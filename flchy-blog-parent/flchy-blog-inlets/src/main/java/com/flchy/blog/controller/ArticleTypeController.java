package com.flchy.blog.controller;

import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.flchy.blog.utils.NewMapUtil;
import org.springframework.stereotype.Controller;

import com.flchy.blog.entity.Article;
import com.zuobiao.analysis.common.response.ResponseCommand;
import com.zuobiao.analysis.privilege.config.response.VisitsResult;

/**
 * <p>
 * 文章类型 前端控制器
 * </p>
 *
 * @author nieqs
 * @since 2017-08-08
 */
@Path("articleType")
@Controller
@Produces(MediaType.APPLICATION_JSON) 
public class ArticleTypeController {
	@GET
	public Object selectArticleKey(@NotNull int id) {
		return null;
	}

	
}
