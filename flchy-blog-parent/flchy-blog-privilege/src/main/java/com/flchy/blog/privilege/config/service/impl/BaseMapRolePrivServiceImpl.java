package com.flchy.blog.privilege.config.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flchy.blog.base.response.ResponseCommand;
import com.flchy.blog.base.response.VisitsMapResult;
import com.flchy.blog.privilege.config.bean.RoleVisitBean;
import com.flchy.blog.privilege.config.dao.IBaseMapRoleElementDao;
import com.flchy.blog.privilege.config.dao.IBaseMapRolePanelDao;
import com.flchy.blog.privilege.config.dao.IBaseMapRolePrivDao;
import com.flchy.blog.privilege.config.entity.BaseMapRoleElementEntity;
import com.flchy.blog.privilege.config.entity.BaseMapRolePanelEntity;
import com.flchy.blog.privilege.config.entity.BaseMapRolePrivEntity;
import com.flchy.blog.privilege.config.service.IBaseMapPrivRoleService;
import com.flchy.blog.privilege.enums.StatusEnum;
import com.flchy.blog.utils.NewMapUtil;

@Service
public class BaseMapRolePrivServiceImpl implements IBaseMapPrivRoleService {
	private static Logger logger = LoggerFactory.getLogger(BaseMapRolePrivServiceImpl.class);
	@Autowired
	private IBaseMapRolePrivDao IBaseMapRolePrivDao;
	@Autowired
	private IBaseMapRolePanelDao iBaseMapRolePanelDao;
	@Autowired
	private IBaseMapRoleElementDao iBaseMapRoleElementDao;

	@Override
	public List<BaseMapRolePrivEntity> selectConfPrivByRoleIds(Integer[] roleIds) {
		return IBaseMapRolePrivDao.selectConfPrivByRoleIds(roleIds);
	}

	@Override
	public Object delete(String id) {
		String subfirst = id.substring(0, 1);
		String subLast = id.substring(id.length() - 1, id.length());
		if (",".equals(subfirst)) {
			id = id.substring(1, id.length());
		}
		if (",".equals(subLast)) {
			id = id.substring(0, id.length() - 1);
		}
		String[] split = id.split(",");
		Integer[] arrInteger = new Integer[split.length];
		try {
			for (int i = 0; i < split.length; i++) {
				if (!split[i].trim().equals("") && split[i] != null)
					arrInteger[i] = Integer.valueOf(split[i].trim().toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseCommand(ResponseCommand.STATUS_ERROR, new VisitsMapResult(new NewMapUtil("message", "The id format is incorrect!").get()));
		}
		if (split.length < 1) {
			return new ResponseCommand(ResponseCommand.STATUS_ERROR, new VisitsMapResult(new NewMapUtil("message", "Please choose the option to delete!").get()));
		}
		boolean updateUser = deleteRolePrivs(Arrays.asList(arrInteger));
		if (updateUser) {
			return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, id);
		} else {
			return new ResponseCommand(ResponseCommand.STATUS_ERROR, new VisitsMapResult(new NewMapUtil("message", "Delete failure！").get()));
		}
	}
	/**
	 * 级联删除
	 * @param ids
	 * @return
	 */
	public boolean deleteRolePrivs(List<Integer> ids) {
		List<RoleVisitBean> selectByIdMenuRole = IBaseMapRolePrivDao.selectByIdMenuRole(ids);
		Map<Integer, List<RoleVisitBean>> collect = selectByIdMenuRole.stream().collect(Collectors.groupingBy(RoleVisitBean::getRoleId));
		for (Integer roleId : collect.keySet()) {
			List<RoleVisitBean> list = collect.get(roleId);
			List<Integer> menuIds = list.stream().map(l->{return l.getMenuId();}).collect(Collectors.toList());
			//删除角色面板权限 start
			List<BaseMapRolePanelEntity> selectRolePanelByRoleIdMenuIds = iBaseMapRolePanelDao.selectRolePanelByRoleIdMenuIds(roleId, menuIds);
			List<Integer> rolePanelIds = selectRolePanelByRoleIdMenuIds!=null && selectRolePanelByRoleIdMenuIds.size()>0?selectRolePanelByRoleIdMenuIds.stream().map(l->{return l.getId();}).collect(Collectors.toList()):null;
			if(rolePanelIds!=null && rolePanelIds.size()>0){
				iBaseMapRolePanelDao.delete(rolePanelIds);
			}
			//删除角色面板权限 stop
			
			//删除角元素权限 start
			List<BaseMapRoleElementEntity> selectRoleElementByRoleIdMenuIds = iBaseMapRoleElementDao.selectRoleElementByRoleIdMenuIds(roleId, menuIds);
			List<Integer> roleElementIds = selectRoleElementByRoleIdMenuIds!=null && selectRoleElementByRoleIdMenuIds.size()>0?selectRoleElementByRoleIdMenuIds.stream().map(l->{return l.getId();}).collect(Collectors.toList()):null;
			if(roleElementIds!=null && roleElementIds.size()>0){
				iBaseMapRoleElementDao.delete(roleElementIds);
			}
			//删除角元素权限 stop
		}
		
		boolean updateUser = IBaseMapRolePrivDao.delete(ids);
		return updateUser;
	}

	@Override
	public boolean insert(BaseMapRolePrivEntity baseMapRolePrivEntity) {
		return IBaseMapRolePrivDao.insert(baseMapRolePrivEntity);
	}

	@Override
	public boolean insertCodeBatch(String[] privVisitId, Integer roleId, String userName) {
		Integer[] roleIds = { roleId };
		List<BaseMapRolePrivEntity> selectConfPrivByRoleIds = IBaseMapRolePrivDao.selectConfPrivByRoleIds(roleIds);
		List<String> selected=selectConfPrivByRoleIds.stream().map(l->l.getPrivVisitId()).collect(Collectors.toList());
		Set<String> userIds = new HashSet<String>(Arrays.asList(privVisitId));
		userIds = userIds.stream().filter(l -> {
			return !selected.contains(l);
		}).collect(Collectors.toSet());
		if(userIds.size()<1){
			return false;
		}
		List<BaseMapRolePrivEntity> collect = userIds.stream().map(l -> {
			BaseMapRolePrivEntity baseMapRoleUserEntity = new BaseMapRolePrivEntity();
			baseMapRoleUserEntity.setCreateTime(new Date());
			baseMapRoleUserEntity.setRoleId(roleId);
			baseMapRoleUserEntity.setPrivVisitId(l);
			baseMapRoleUserEntity.setStatus(StatusEnum.NORMAL.getValues());
			baseMapRoleUserEntity.setCreateUser(userName);
			return baseMapRoleUserEntity;
		}).collect(Collectors.toList());
		return IBaseMapRolePrivDao.insertCodeBatch(collect);
	}

}
