package com.flchy.blog.dao;

import com.flchy.blog.base.datasource.annotation.BaseRepository;
import com.flchy.blog.entity.Article;
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

}