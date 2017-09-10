package com.flchy.blog.inlets.controller;

import java.util.List;

import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.flchy.blog.common.response.ResponseCommand;
import com.flchy.blog.inlets.exception.BusinessException;
import com.flchy.blog.inlets.holder.ArticleTypeHolder;
import com.flchy.blog.inlets.service.IArticleTypeService;
import com.flchy.blog.pojo.ArticleType;

/**
 * <p>
 * 文章类型 前端控制器
 * </p>
 *
 * @author nieqs
 * @since 2017-08-08
 */
@RestController
@RequestMapping("articleType")
public class ArticleTypeController {
	@Autowired
	private IArticleTypeService iArticleTypeService;
	
	@PostMapping
	public Object add(ArticleType entity){
		boolean isok=iArticleTypeService.insert(entity);
		if (!isok) {
			throw new BusinessException("Add failed");
		}
		ArticleTypeHolder.refreshCache();
		return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, entity);
	}
	
	@PutMapping
	public Object update(ArticleType entity){
		if(entity.getId()==null){
			throw new BusinessException("ID must preach");
		}
		boolean isok = iArticleTypeService.update(entity, new EntityWrapper<ArticleType>().where("id={0}", entity.getId()));
		if (!isok) {
			throw new BusinessException("Update failed");
		}
		ArticleTypeHolder.refreshCache();
		return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, entity);
	}
	
	@DeleteMapping
	public Object delete(ArticleType entity){
		boolean isok = iArticleTypeService.delete(new EntityWrapper<ArticleType>(entity));
		if (!isok) {
			throw new BusinessException("Delete failed");
		}
		ArticleTypeHolder.refreshCache();
		return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, entity);
	}
		
	@GetMapping
	public Object selectKey(@QueryParam("id") Integer id) {
//		if(id!=null){
//			return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, iArticleTypeService.selectById(id));
//		}
		List<ArticleType> response= ArticleTypeHolder.getArticleType(id);
		return new ResponseCommand(ResponseCommand.STATUS_SUCCESS,response);
	}

	
}
