package com.flchy.blog.controller;

import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.stereotype.Controller;

/**
 * <p>
 * 友情链接 前端控制器
 * </p>
 *
 * @author nieqs
 * @since 2017-08-08
 */
@Path("link")
@Controller
@Produces(MediaType.APPLICATION_JSON) 
public class LinkController {
	@GET
	public Object selectArticleKey(@NotNull int id) {
		return null;
	}
}
