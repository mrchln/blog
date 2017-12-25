package com.flchy.blog.privilege.config.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flchy.blog.privilege.config.dao.IBaseInfoDimAuthDao;
import com.flchy.blog.privilege.config.dao.IBaseResElementDao;
import com.flchy.blog.privilege.config.dao.IBaseResPanelDao;
import com.flchy.blog.privilege.config.entity.BaseInfoDimAuthEntity;
import com.flchy.blog.privilege.config.service.IBaseResInfoService;

@Service
public class BaseResInfoServiceImpl implements IBaseResInfoService {
	private static Logger logger = LoggerFactory.getLogger(BaseResInfoServiceImpl.class);
	@Autowired
	private IBaseInfoDimAuthDao baseInfoDimAuthDao;
	@Autowired
	private IBaseResElementDao baseResElementDao;
	@Autowired
	private IBaseResPanelDao baseResPanelDao;

	@Override
	public List<BaseInfoDimAuthEntity> selectDimAuthInfoByDimAuthIds(Integer[] dimAuthIdArray) {
		return baseInfoDimAuthDao.selectDimAuthInfoByDimAuthIds(dimAuthIdArray);
	}

}
