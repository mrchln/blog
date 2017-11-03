package com.flchy.blog.inlets.mapper;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.flchy.blog.base.datasource.annotation.BaseRepository;
import com.flchy.blog.pojo.Comment;

/**
 * <p>
 * 评论表 Mapper 接口
 * </p>
 *
 * @author nieqs
 * @since 2017-11-02
 */
@BaseRepository
public interface CommentMapper extends BaseMapper<Comment> {

	public List<Comment> selectWebComment(List<Map<String, Object>> parameter);
}