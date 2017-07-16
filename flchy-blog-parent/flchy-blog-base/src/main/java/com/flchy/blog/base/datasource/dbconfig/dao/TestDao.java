package com.flchy.blog.base.datasource.dbconfig.dao;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.flchy.blog.base.datasource.annotation.BaseRepository;
import com.flchy.blog.base.datasource.dbconfig.entity.test;

@BaseRepository
@Mapper
public interface TestDao extends BaseMapper<test> {
	
}
