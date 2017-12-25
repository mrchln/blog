package com.flchy.blog.privilege.config.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flchy.blog.privilege.config.dao.IBaseMapRoleDimAuthDao;
import com.flchy.blog.privilege.config.dao.IBaseMapRoleElementDao;
import com.flchy.blog.privilege.config.dao.IBaseMapRolePanelDao;
import com.flchy.blog.privilege.config.entity.BaseMapRoleDimAuthEntity;
import com.flchy.blog.privilege.config.entity.BaseMapRoleElementEntity;
import com.flchy.blog.privilege.config.entity.BaseMapRolePanelEntity;
import com.flchy.blog.privilege.config.service.IBaseMapResRoleService;
@Service
public class BaseMapRoleResServiceImpl implements IBaseMapResRoleService {
	private static Logger logger = LoggerFactory.getLogger(BaseMapRoleResServiceImpl.class);
	@Autowired
	private  IBaseMapRoleDimAuthDao baseMapRoleDimAuthDao;
	
	@Autowired
	private  IBaseMapRoleElementDao baseMapRoleElementDao;
	
	@Autowired
	private  IBaseMapRolePanelDao baseMapRolePanelDao;
	
	@Override
	public List<BaseMapRoleElementEntity> selectRoleElementByRoleIds(Integer[] roleIdArray) {
		return baseMapRoleElementDao.selectRoleElementByRoleIds(roleIdArray);
	}

	@Override
	public List<BaseMapRolePanelEntity> selectRolePanelByRoleIds(Integer[] roleIdArray) {
		return baseMapRolePanelDao.selectRolePanelByRoleIds(roleIdArray);
	}

	@Override
	public List<BaseMapRoleDimAuthEntity> selectRoleDimAuthByRoleIds(Integer[] roleIdArray) {
		return baseMapRoleDimAuthDao.selectRoleDimAuthByRoleIds(roleIdArray);
	}

}
