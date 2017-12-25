package com.flchy.blog.inlets.service;

import com.flchy.blog.base.response.PageHelperResult;
import com.flchy.blog.pojo.Comment;

import java.util.Map;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 * 评论表 服务类
 * </p>
 *
 * @author nieqs
 * @since 2017-11-02
 */
public interface ICommentService extends IService<Comment> {



	PageHelperResult selectWebComment(Integer articleId, Integer current, Integer size, String nickName);


	Page<Map<String, Object>> selectCommentPage(Page<Map<String, Object>> page, Comment comment);


	Comment saveComment(Comment comment, Boolean isDraft);


	Comment saveComment(Comment comment);
	
}
