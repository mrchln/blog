package com.flchy.blog.privilege.config.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.flchy.blog.base.holder.PropertiesHolder;
import com.flchy.blog.base.response.PageHelperResult;
import com.flchy.blog.base.response.ResponseCommand;
import com.flchy.blog.base.response.VisitsMapResult;
import com.flchy.blog.privilege.config.dao.IBaseMapRoleUserDao;
import com.flchy.blog.privilege.config.dao.IBaseResElementDao;
import com.flchy.blog.privilege.config.entity.BaseConfUrlEntity;
import com.flchy.blog.privilege.config.entity.BaseMapRoleUserEntity;
import com.flchy.blog.privilege.config.entity.BaseResElementEntity;
import com.flchy.blog.privilege.config.service.IBaseResElementService;
import com.flchy.blog.utils.NewMapUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

/**
 * @author nieqs
 *
 */
@Service
public class BaseResElementServiceImpl implements IBaseResElementService {
	@Autowired
	private IBaseResElementDao iBaseResElementDao;
	@Autowired
	private IBaseMapRoleUserDao iBaseMapRoleUserDao;
	
	@Override
	public  PageHelperResult selectResElement(int pageSize, int currentPage,BaseResElementEntity baseResElementEntity) {
		Page<BaseConfUrlEntity> page = PageHelper.startPage(currentPage, pageSize).doSelectPage(() ->iBaseResElementDao.selectResElement(baseResElementEntity));
		return new PageHelperResult(page);
	}
	@Override
	public  	List<BaseResElementEntity>  selectResElement(BaseResElementEntity baseResElementEntity) {
	List<BaseResElementEntity> selectResElement = iBaseResElementDao.selectResElement(baseResElementEntity);
		return selectResElement;
	}

	@Override
	public boolean insert(BaseResElementEntity baseResElementEntity) {
		return iBaseResElementDao.insert(baseResElementEntity);
	}

	@Override
	public boolean update(BaseResElementEntity baseResElementEntity) {
		boolean update = iBaseResElementDao.update(baseResElementEntity);
		return update;
	}

	@Override
	public Object delete(String elementId) {
		String subfirst = elementId.substring(0, 1);
		String subLast = elementId.substring(elementId.length() - 1, elementId.length());
		if (",".equals(subfirst)) {
			elementId = elementId.substring(1, elementId.length());
		}
		if (",".equals(subLast)) {
			elementId = elementId.substring(0, elementId.length() - 1);
		}
		String[] split = elementId.split(",");
		Integer[] arrInteger = new Integer[split.length];
		try {
			for (int i = 0; i < split.length; i++) {
				if (!split[i].trim().equals("") && split[i] != null)
					arrInteger[i] = Integer.valueOf(split[i].trim().toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseCommand(ResponseCommand.STATUS_ERROR, new VisitsMapResult(new NewMapUtil("message", "The elementId format is incorrect!").get()));
		}
		if (split.length < 1) {
			return new ResponseCommand(ResponseCommand.STATUS_ERROR, new VisitsMapResult(new NewMapUtil("message", "Please choose the option to delete!").get()));
		}
		boolean updateUser = iBaseResElementDao.delete(Arrays.asList(arrInteger));
		if (updateUser) {
			return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, elementId);
		} else {
			return new ResponseCommand(ResponseCommand.STATUS_ERROR, new VisitsMapResult(new NewMapUtil("message", "Delete failure！").get()));
		}
	}
	@Override
	public List<BaseResElementEntity> selectVisitElement(Integer userId,String privVisitId,Integer elementId, Integer panelId,String elementName){
		String roleId = PropertiesHolder.getProperty("role.superadmin");
		if (StringUtils.hasText(roleId)) {
			List<BaseMapRoleUserEntity> selectRolesByUserId = iBaseMapRoleUserDao.selectRolesByUserId(userId);
			List<BaseMapRoleUserEntity> collect = selectRolesByUserId.stream().filter(l -> {
				return roleId.equals(String.valueOf(l.getRoleId()));
			}).collect(Collectors.toList());
			if (collect.size() > 0) {
				return iBaseResElementDao.selectByAdminRole(panelId, privVisitId, elementId, elementName);
			}
		}
		
		return iBaseResElementDao.selectVisitElement(userId, privVisitId, elementId,   panelId,elementName);
	}

	
	
	
	@Override
	public PageHelperResult selectByRoleSelected(int pageSize, int currentPage,Integer roleId, Integer menuId, Integer panelId, String elementName) {
		String adminRoleId = PropertiesHolder.getProperty("role.superadmin");
		if (StringUtils.hasText(adminRoleId)) {
			if(adminRoleId.equals(roleId.toString())){
				BaseResElementEntity baseResElementEntity=new BaseResElementEntity();
				baseResElementEntity.setMenuId(menuId);
				baseResElementEntity.setPanelId(panelId);
				baseResElementEntity.setElementName(elementName);
				Page<Map<String, Object>> page = PageHelper.startPage(currentPage, pageSize).doSelectPage(() -> iBaseResElementDao.selectResElement(baseResElementEntity));
				return new PageHelperResult(page);
			}
		}
		Page<Map<String, Object>> page = PageHelper.startPage(currentPage, pageSize).doSelectPage(() ->iBaseResElementDao.selectByRoleSelected(roleId, menuId, panelId, elementName));
		return new PageHelperResult(page);
	}
	
	@Override
	public List<BaseResElementEntity> selectByRoleNotSelected(Integer roleId, Integer menuId,   Integer panelId, String elementName){
		String adminRoleId = PropertiesHolder.getProperty("role.superadmin");
		if (StringUtils.hasText(adminRoleId)) {
			if(adminRoleId.equals(roleId.toString())){
				return new ArrayList<>();
			}
		}
		
	 List<BaseResElementEntity> selectByRoleNotSelected = iBaseResElementDao.selectByRoleNotSelected(roleId, menuId, panelId, elementName);
	 	for (int i = 0; i < selectByRoleNotSelected.size(); i++) {
	 		BaseResElementEntity baseResPanelEntity = selectByRoleNotSelected.get(i);
	 		baseResPanelEntity.setIndex(i);
		}
	 	return selectByRoleNotSelected;
	}

}
