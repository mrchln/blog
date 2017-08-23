package com.flchy.blog.base.datasource.dbconfig.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.flchy.blog.base.datasource.dbconfig.entity.test;

public interface ITestService {

	Integer insertAll(test test);

	Page<test> selectPages(Page<test> page);

}
