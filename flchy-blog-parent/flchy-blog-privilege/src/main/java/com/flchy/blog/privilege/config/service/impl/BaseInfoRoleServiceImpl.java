package com.flchy.blog.privilege.config.service.impl;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flchy.blog.base.response.PageHelperResult;
import com.flchy.blog.base.response.ResponseCommand;
import com.flchy.blog.base.response.VisitsMapResult;
import com.flchy.blog.privilege.config.dao.IBaseInfoRoleDao;
import com.flchy.blog.privilege.config.entity.BaseInfoRoleEntity;
import com.flchy.blog.privilege.config.entity.BaseInfoUserEntity;
import com.flchy.blog.privilege.config.service.IBaseInfoRoleService;
import com.flchy.blog.utils.NewMapUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
/**
 * 角色业务逻辑
 * @author nieqs
 *
 */
@Service
public class BaseInfoRoleServiceImpl implements IBaseInfoRoleService {
	private static Logger logger = LoggerFactory.getLogger(BaseInfoRoleServiceImpl.class);
	@Autowired
	private IBaseInfoRoleDao baseInfoRoleDao;

	@Override
	public BaseInfoRoleEntity getRole(Integer roleId){
		return baseInfoRoleDao.getRole(roleId);
	}
	
	
	@Override
	public List<BaseInfoRoleEntity> selectRolesByRoleIds(Integer[] roleIds) {
		return baseInfoRoleDao.selectRolesByRoleIds(roleIds);
	}
	
	@Override
	public PageHelperResult selectRoles(int pageSize, int currentPage, BaseInfoRoleEntity infoRoleEntity){
		Page<BaseInfoUserEntity> page = PageHelper.startPage(currentPage, pageSize).doSelectPage(() ->baseInfoRoleDao.selectRoles(infoRoleEntity));
		return new PageHelperResult(page);
	}
	
	@Override
	public boolean insertRole(BaseInfoRoleEntity baseInfoRoleEntity) {
		Integer insertRole = baseInfoRoleDao.insertRole(baseInfoRoleEntity);
		return !(insertRole == null);
	}

	/* (non-Javadoc)
	 * 修改角色
	 * @see com.zuobiao.analysis.privilege.config.service.IBaseInfoRoleService#updateRole(com.zuobiao.analysis.privilege.config.entity.BaseInfoRoleEntity)
	 */
	@Override
	public boolean updateRole(BaseInfoRoleEntity infoRoleEntity) {
		boolean updateRole = baseInfoRoleDao.updateRole(infoRoleEntity);
		return updateRole;
	}
	
	

	@Override
	public Object deleteRole(String roleId) {
		String subfirst = roleId.substring(0, 1);
		String subLast = roleId.substring(roleId.length() - 1, roleId.length());
		if (",".equals(subfirst)) {
			roleId = roleId.substring(1, roleId.length());
		}
		if (",".equals(subLast)) {
			roleId = roleId.substring(0, roleId.length() - 1);
		}
		String[] split = roleId.split(",");
		Integer[] arrInteger = new Integer[split.length];

		try {
			for (int i = 0; i < split.length; i++) {
				if (!split[i].trim().equals("") && split[i] != null)
					arrInteger[i] = Integer.valueOf(split[i].trim().toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseCommand(ResponseCommand.STATUS_ERROR,
					new VisitsMapResult(new NewMapUtil("message", "The roleId format is incorrect!").get()));
		}
		if (split.length < 1) {
			return new ResponseCommand(ResponseCommand.STATUS_ERROR,
					new VisitsMapResult(new NewMapUtil("message", "Please choose the option to delete!").get()));
		}
		
		boolean updateUser = baseInfoRoleDao.deleteRole(Arrays.asList(arrInteger));
		if(updateUser){
			return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, roleId);
		}else{
			return new ResponseCommand(ResponseCommand.STATUS_ERROR,
					new VisitsMapResult(new NewMapUtil("message", "Delete failure！").get()));
		}

	}
	

}
