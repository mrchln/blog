package com.flchy.blog.privilege.config.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flchy.blog.base.response.PageHelperResult;
import com.flchy.blog.base.response.ResponseCommand;
import com.flchy.blog.base.response.VisitsMapResult;
import com.flchy.blog.privilege.config.dao.IBaseConfUrlDao;
import com.flchy.blog.privilege.config.entity.BaseConfUrlEntity;
import com.flchy.blog.privilege.config.service.IBaseConfUrlService;
import com.flchy.blog.utils.NewMapUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

@Service
public class  BaseConfUrlServiceImpl implements IBaseConfUrlService {
	@Autowired
	private IBaseConfUrlDao baseConfUrlDao;

	@Override
	public List<BaseConfUrlEntity> selectConfUrlByUrlIds(Integer[] urlIdArray) {
		return baseConfUrlDao.selectConfUrlByUrlIds(urlIdArray);
	}

	@Override
	public BaseConfUrlEntity saveConfUrlByUrlIds(String urlPath, String method) {
		List<BaseConfUrlEntity> confUrlList = baseConfUrlDao.selectConfUrlByUrlPath(urlPath, method);
		if (null == confUrlList || confUrlList.isEmpty()) {
			baseConfUrlDao.saveConfUrlByUrlIds(new NewMapUtil().set("urlPath", urlPath).set("method", method).set("status", 1).set("createUser", "admin").set("createTime", new Date()).get());

			confUrlList = baseConfUrlDao.selectConfUrlByUrlPath(urlPath, method);
			if (null != confUrlList && confUrlList.size() > 0) {
				return confUrlList.get(0);
			} else {
				return null;
			}
		} else {
			return confUrlList.get(0);
		}
	}

	@Override
	public PageHelperResult selectPage(int pageSize, int currentPage, BaseConfUrlEntity baseConfUrlEntity) {
		Page<BaseConfUrlEntity> page = PageHelper.startPage(currentPage, pageSize).doSelectPage(() -> baseConfUrlDao.select(baseConfUrlEntity));
		return new PageHelperResult(page);
	}

	@Override
	public Object deleteUrl(String urlId) {
		String subfirst = urlId.substring(0, 1);
		String subLast = urlId.substring(urlId.length() - 1, urlId.length());
		if (",".equals(subfirst)) {
			urlId = urlId.substring(1, urlId.length());
		}
		if (",".equals(subLast)) {
			urlId = urlId.substring(0, urlId.length() - 1);
		}
		String[] split = urlId.split(",");
		Integer[] arrInteger = new Integer[split.length];

		try {
			for (int i = 0; i < split.length; i++) {
				if (!split[i].trim().equals("") && split[i] != null)
					arrInteger[i] = Integer.valueOf(split[i].trim().toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseCommand(ResponseCommand.STATUS_ERROR, new VisitsMapResult(new NewMapUtil("message", "The urlId format is incorrect!").get()));
		}
		if (split.length < 1) {
			return new ResponseCommand(ResponseCommand.STATUS_ERROR, new VisitsMapResult(new NewMapUtil("message", "Please choose the option to delete!").get()));
		}

		boolean updateUser = baseConfUrlDao.delete(Arrays.asList(arrInteger));
		if (updateUser) {
			return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, urlId);
		} else {
			return new ResponseCommand(ResponseCommand.STATUS_ERROR, new VisitsMapResult(new NewMapUtil("message", "Delete failure！").get()));
		}
	}

	@Override
	public PageHelperResult selectUrlSelected(String privVisitId, String urlPath, int pageSize, int currentPage) {
		Page<Map<String, Object>> page = PageHelper.startPage(currentPage, pageSize).doSelectPage(() -> baseConfUrlDao.selectUrlSelected(privVisitId, urlPath));
		return new PageHelperResult(page);
	}

	@Override
	public List<BaseConfUrlEntity> selectUrlNotSelected(String privVisitId, String urlPath) {
		List<BaseConfUrlEntity> selectUrlNotSelected = baseConfUrlDao.selectUrlNotSelected(privVisitId, urlPath);
		for (int i = 0; i < selectUrlNotSelected.size(); i++) {
			BaseConfUrlEntity baseConfUrlEntity = selectUrlNotSelected.get(i);
			baseConfUrlEntity.setIndex(i);
			
		}
		return selectUrlNotSelected;
	}
}