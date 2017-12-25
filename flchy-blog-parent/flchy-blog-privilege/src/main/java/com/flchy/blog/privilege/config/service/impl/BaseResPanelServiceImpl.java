package com.flchy.blog.privilege.config.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.flchy.blog.base.holder.PropertiesHolder;
import com.flchy.blog.base.response.PageHelperResult;
import com.flchy.blog.base.response.ResponseCommand;
import com.flchy.blog.base.response.VisitsMapResult;
import com.flchy.blog.privilege.config.dao.IBaseMapRoleUserDao;
import com.flchy.blog.privilege.config.dao.IBaseResElementDao;
import com.flchy.blog.privilege.config.dao.IBaseResPanelDao;
import com.flchy.blog.privilege.config.entity.BaseMapRoleUserEntity;
import com.flchy.blog.privilege.config.entity.BaseResPanelEntity;
import com.flchy.blog.privilege.config.service.IBaseResPanelService;
import com.flchy.blog.utils.NewMapUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

/**
 * @author nieqs
 *
 */
@Service
public class BaseResPanelServiceImpl implements IBaseResPanelService {
	@Autowired
	private IBaseResPanelDao iBaseResPanelDao;
	@Autowired
	private IBaseMapRoleUserDao iBaseMapRoleUserDao;
	@Autowired
	private IBaseResElementDao iBaseResElementDao;
	@Override
	public PageHelperResult selectResPanel(int pageSize, int currentPage, BaseResPanelEntity baseResPanelEntity) {
		Page<BaseResPanelEntity> page = PageHelper.startPage(currentPage, pageSize).doSelectPage(() -> iBaseResPanelDao.selectResPanel(baseResPanelEntity));
		List<BaseResPanelEntity> result = page.getResult();
		for (int i = 0; i < result.size(); i++) {
			BaseResPanelEntity baseResPanelEntity2 = result.get(i);
			baseResPanelEntity2.setIndex(i);
		}
		return new PageHelperResult(page);
	}

	@Override
	public List<BaseResPanelEntity> selectResPanel(BaseResPanelEntity baseResPanelEntity) {
		List<BaseResPanelEntity> selectResPanel = iBaseResPanelDao.selectResPanel(baseResPanelEntity);
		return selectResPanel;
	}

	@Override
	public boolean insert(BaseResPanelEntity baseResPanelEntity) {
		boolean menuId = iBaseResPanelDao.insert(baseResPanelEntity);
		return menuId;
	}

	@Override
	public boolean update(BaseResPanelEntity baseResPanelEntity) {
		boolean update = iBaseResPanelDao.update(baseResPanelEntity);
		return update;
	}


	@Override
	@Transactional
	public Object delete(String panelId) {
		String subfirst = panelId.substring(0, 1);
		String subLast = panelId.substring(panelId.length() - 1, panelId.length());
		if (",".equals(subfirst)) {
			panelId = panelId.substring(1, panelId.length());
		}
		if (",".equals(subLast)) {
			panelId = panelId.substring(0, panelId.length() - 1);
		}
		String[] split = panelId.split(",");
		Integer[] arrInteger = new Integer[split.length];
		try {
			for (int i = 0; i < split.length; i++) {
				if (!split[i].trim().equals("") && split[i] != null)
					arrInteger[i] = Integer.valueOf(split[i].trim().toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseCommand(ResponseCommand.STATUS_ERROR, new VisitsMapResult(new NewMapUtil("message", "The roleId format is incorrect!").get()));
		}
		if (split.length < 1) {
			return new ResponseCommand(ResponseCommand.STATUS_ERROR, new VisitsMapResult(new NewMapUtil("message", "Please choose the option to delete!").get()));
		}
		boolean updateUser = deleteResPanels(Arrays.asList(arrInteger));
		if (updateUser) {
			return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, panelId);
		} else {
			return new ResponseCommand(ResponseCommand.STATUS_ERROR, new VisitsMapResult(new NewMapUtil("message", "Delete failure！").get()));
		}
	}
	@Override
	@Transactional
	public boolean deleteResPanels(List<Integer> panelId) {
		if (panelId!=null && panelId.size()>0) {
			boolean delete = iBaseResPanelDao.delete(panelId);
			iBaseResElementDao.deleteByPanelId(panelId);
		}
		return true;
	}

	@Override
	public List<BaseResPanelEntity> selectVisitPanel(Integer userId, String privVisitId, Integer panelId, String panelName) {
		String roleId = PropertiesHolder.getProperty("role.superadmin");
		if (StringUtils.hasText(roleId)) {
			List<BaseMapRoleUserEntity> selectRolesByUserId = iBaseMapRoleUserDao.selectRolesByUserId(userId);
			List<BaseMapRoleUserEntity> collect = selectRolesByUserId.stream().filter(l -> {
				return roleId.equals(String.valueOf(l.getRoleId()));
			}).collect(Collectors.toList());
			if (collect.size() > 0) {
				return iBaseResPanelDao.selectByAllVisitPanel(privVisitId, panelId,null, panelName);
			}
		}
		return iBaseResPanelDao.selectVisitPanel(userId, privVisitId, panelId, panelName);
	}

	@Override
	public PageHelperResult selectByRoleSelected(int pageSize, int currentPage, Integer roleId, Integer menuId, String panelName) {
		String adminRoleId = PropertiesHolder.getProperty("role.superadmin");
		if (StringUtils.hasText(adminRoleId)) {
			if(adminRoleId.equals(roleId.toString())){
				Page<Map<String, Object>> page = PageHelper.startPage(currentPage, pageSize).doSelectPage(() -> iBaseResPanelDao.selectByAllVisitPanel(null, null,menuId, panelName));
				return new PageHelperResult(page);
			}
		}
		Page<Map<String, Object>> page = PageHelper.startPage(currentPage, pageSize).doSelectPage(() -> iBaseResPanelDao.selectByRoleSelected(roleId, menuId, panelName));
		List<Map<String, Object>> result = page.getResult();
		for (int i = 0; i < result.size(); i++) {
			Map<String, Object> map = result.get(i);
			map.put("index", i);
		}
		return new PageHelperResult(page);
	}

	
	@Override
	public List<BaseResPanelEntity> selectByRoleNotSelected(Integer roleId, Integer menuId, String panelName) {
		String adminRoleId = PropertiesHolder.getProperty("role.superadmin");
		if (StringUtils.hasText(adminRoleId)) {
			if(adminRoleId.equals(roleId.toString())){
				return new ArrayList<>();
			}
		}
		List<BaseResPanelEntity> selectByRoleNotSelected = iBaseResPanelDao.selectByRoleNotSelected(roleId, menuId, panelName);
		for (int i = 0; i < selectByRoleNotSelected.size(); i++) {
			BaseResPanelEntity baseResPanelEntity = selectByRoleNotSelected.get(i);
			baseResPanelEntity.setIndex(i);
		}
		return selectByRoleNotSelected;
	}

}
