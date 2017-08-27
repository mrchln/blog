package com.flchy.blog.inlets.controller;

import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.flchy.blog.common.response.ResponseCommand;
import org.flchy.blog.common.response.VisitsResult;
import org.flchy.blog.utils.NewMapUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.flchy.blog.inlets.entity.ArticleType;
import com.flchy.blog.inlets.service.IArticleTypeService;

/**
 * <p>
 * 文章类型 前端控制器
 * </p>
 *
 * @author nieqs
 * @since 2017-08-08
 */
//@Path("articleType")
//@Controller
//@Produces(MediaType.APPLICATION_JSON) 
@RestController(value="/articleType")
public class ArticleTypeController {
	@Autowired
	private IArticleTypeService iArticleTypeService;
	
	@PostMapping
	public Object add(ArticleType entity){
		boolean isok=iArticleTypeService.insert(entity);
		if (!isok) {
			return new ResponseCommand(ResponseCommand.STATUS_ERROR,
					new VisitsResult(new NewMapUtil("message", "Add failed").get()));
		}
		return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, entity);
	}
	
	@PutMapping
	public Object update(ArticleType entity){
		if(entity.getId()==null){
			return new ResponseCommand(ResponseCommand.STATUS_ERROR,
					new VisitsResult(new NewMapUtil("message", "ID must preach").get()));
		}
		boolean isok = iArticleTypeService.update(entity, new EntityWrapper<ArticleType>().where("id={0}", entity.getId()));
		if (!isok) {
			return new ResponseCommand(ResponseCommand.STATUS_ERROR,
					new VisitsResult(new NewMapUtil("message", "Update failed").get()));
		}
		return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, entity);
	}
	
	@DeleteMapping
	public Object delete(ArticleType entity){
		boolean isok = iArticleTypeService.delete(new EntityWrapper<ArticleType>(entity));
		if (!isok) {
			return new ResponseCommand(ResponseCommand.STATUS_ERROR,
					new VisitsResult(new NewMapUtil("message", "Delete failed").get()));
		}
		return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, entity);
	}
		
	@GetMapping
	public Object selectKey(@QueryParam("id")Integer id) {
		if(id!=null){
			return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, iArticleTypeService.selectById(id));
		}
		List<ArticleType> response= iArticleTypeService.selectList(new EntityWrapper<ArticleType>());
		return new ResponseCommand(ResponseCommand.STATUS_SUCCESS,response);
	}

	
}
