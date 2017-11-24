package com.flchy.blog.inlets.mapper;

import com.flchy.blog.base.datasource.annotation.BaseRepository;
import com.flchy.blog.pojo.Article;

import java.util.List;

import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * <p>
 * 文章表 Mapper 接口
 * </p>
 *
 * @author nieqs
 * @since 2017-08-08
 */
@BaseRepository
public interface ArticleMapper extends BaseMapper<Article> {

	public List<Article> selectPage(Article article);

}