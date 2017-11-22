package com.flchy.blog.inlets.service;

import com.flchy.blog.base.response.ResultPage;
import com.flchy.blog.pojo.Comment;
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

	Comment saveComment(Comment comment);


	ResultPage selectWebComment(Integer articleId, Integer current, Integer size, String nickName);
	
}
