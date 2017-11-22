package com.flchy.blog.inlets.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.flchy.blog.base.datasource.annotation.BaseRepository;
import com.flchy.blog.pojo.Config;

/**
 * <p>
  * 配置信息表 Mapper 接口
 * </p>
 *
 * @author nieqs
 * @since 2017-11-22
 */
@BaseRepository
public interface ConfigMapper extends BaseMapper<Config> {

}