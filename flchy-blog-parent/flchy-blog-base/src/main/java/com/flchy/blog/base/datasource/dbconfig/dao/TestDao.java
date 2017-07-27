package com.flchy.blog.base.datasource.dbconfig.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.flchy.blog.base.datasource.annotation.BaseRepository;
import com.flchy.blog.base.datasource.dbconfig.entity.test;

@BaseRepository
@Mapper
public interface TestDao extends BaseMapper<test> {

	List<test> selectList(Page<test> page);
	
}
