package com.flchy.blog.inlets.mapper;

import com.flchy.blog.base.datasource.annotation.BaseRepository;
import com.flchy.blog.pojo.ErrorLog;
import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * <p>
  * 错误日志表 Mapper 接口
 * </p>
 *
 * @author nieqs
 * @since 2017-08-28
 */
@BaseRepository
public interface ErrorLogMapper extends BaseMapper<ErrorLog> {

}