package com.flchy.blog.base.datasource.dbconfig.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.flchy.blog.base.datasource.dbconfig.dao.TestDao;
import com.flchy.blog.base.datasource.dbconfig.entity.test;
import com.flchy.blog.base.datasource.dbconfig.service.ITestService;
@Service
public class TestServiceImpl extends ServiceImpl<TestDao, test> implements ITestService {
	@Autowired
	private TestDao testDao;
	
	@Override
	public Integer insertAll(test test){
	return 	baseMapper.insert(test);
	}

}
