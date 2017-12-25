package com.flchy.blog.privilege.config.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flchy.blog.privilege.config.dao.IBaseConfPrivDao;
import com.flchy.blog.privilege.config.entity.BaseConfPrivEntity;
import com.flchy.blog.privilege.config.service.IBaseConfPrivService;

@Service
public class BaseConfPrivServiceImpl implements IBaseConfPrivService {
	private static Logger logger = LoggerFactory.getLogger(BaseConfPrivServiceImpl.class);
	@Autowired
	private IBaseConfPrivDao baseConfPrivDao;

	@Override
	public List<BaseConfPrivEntity> selectConfPrivByPrivVisitIds(String[] privVisitIdArray) {
		return this.baseConfPrivDao.selectConfPrivByPrivVisitIds(privVisitIdArray);
	}

	@Override
	public List<BaseConfPrivEntity> selectAllPrivVisitUrl() {
		return this.baseConfPrivDao.selectAllPrivVisitUrl();
	}
	
	@Override
	public boolean insert(BaseConfPrivEntity baseConfPrivEntity) {
		boolean insert = baseConfPrivDao.insert(baseConfPrivEntity);
		System.out.println("添加访问权限表:"+insert);
		return insert;
	}
	
	@Override
	public boolean delete(List<Integer> menuIdList){
		return baseConfPrivDao.delete(menuIdList);
	}
}
