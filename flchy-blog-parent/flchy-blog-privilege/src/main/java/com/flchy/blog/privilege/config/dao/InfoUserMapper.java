package com.flchy.blog.privilege.config.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.flchy.blog.base.datasource.annotation.BaseRepository;
import com.flchy.blog.privilege.config.entity.InfoUser;

/**
 * <p>
  * 用户表 Mapper 接口
 * </p>
 *
 * @author nieqs
 * @since 2017-08-02
 */
@BaseRepository
public interface InfoUserMapper extends BaseMapper<InfoUser> {

}