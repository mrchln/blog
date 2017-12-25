package com.flchy.blog.base.dbconfig.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flchy.blog.base.dbconfig.bean.EnumDicBean;
import com.flchy.blog.base.dbconfig.dao.IEnumDicDao;
import com.flchy.blog.base.dbconfig.entity.EnumDicEntity;
import com.flchy.blog.base.dbconfig.holder.EnumDicHolder;
import com.flchy.blog.base.dbconfig.service.IEnumDicService;

@Service
public class EnumDicServiceImpl implements IEnumDicService {

	@Autowired
	private IEnumDicDao enumdicDao;

	@Override
	public List<EnumDicEntity> selectEnumsListByObj(EnumDicBean enumDicBean) {
		return enumdicDao.selectEnumsListByObj(enumDicBean);
	}

	@Override
	public boolean insertEnum(EnumDicBean enumDicBean) {
		boolean insertEnum = enumdicDao.insertEnum(enumDicBean);
		EnumDicHolder.refreshCache();
		return insertEnum;
	}

	@Override
	public boolean updateEnum(EnumDicBean enumDicBean) {
		boolean update = enumdicDao.updateEnum(enumDicBean);
		EnumDicHolder.refreshCache();
		return update;
	}

	@Override
	public Boolean deleteEnum(List<Integer> enumId) {
		boolean deleteEnum = enumdicDao.deleteEnum(enumId);
		EnumDicHolder.refreshCache();
		return deleteEnum;
	}

}
