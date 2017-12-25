/**
 * 
 */
package com.flchy.blog.privilege.config.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.flchy.blog.base.holder.PropertiesHolder;
import com.flchy.blog.base.response.PageHelperResult;
import com.flchy.blog.base.response.ResponseCommand;
import com.flchy.blog.base.response.VisitsMapResult;
import com.flchy.blog.privilege.config.dao.IBaseInfoDimAuthDao;
import com.flchy.blog.privilege.config.entity.BaseInfoDimAuthEntity;
import com.flchy.blog.privilege.config.service.IBaseInfoDimAuthService;
import com.flchy.blog.utils.NewMapUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

/**
 * @author nieqs
 *
 */
@Service
public class BaseInfoDimAuthServiceImpl implements  IBaseInfoDimAuthService{
	@Autowired
	private IBaseInfoDimAuthDao iBaseInfoDimAuthDao;
	
	@Override
	public PageHelperResult selectDimAuth(int pageSize, int currentPage,BaseInfoDimAuthEntity baseInfoDimAuthEntity) {
		Page<BaseInfoDimAuthEntity> page = PageHelper.startPage(currentPage, pageSize).doSelectPage(() ->iBaseInfoDimAuthDao.selectDimAuth(baseInfoDimAuthEntity));
		return new PageHelperResult(page);
	}


	@Override
	public boolean insert(BaseInfoDimAuthEntity baseInfoDimAuthEntity) {
		return iBaseInfoDimAuthDao.insert(baseInfoDimAuthEntity);
	}

	@Override
	public boolean update(BaseInfoDimAuthEntity baseInfoDimAuthEntity) {
		boolean update = iBaseInfoDimAuthDao.update(baseInfoDimAuthEntity);
		return update;
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
		boolean delete = iBaseInfoDimAuthDao.delete(Arrays.asList(arrInteger));
		if (delete) {
			return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, id);
		} else {
			return new ResponseCommand(ResponseCommand.STATUS_ERROR, new VisitsMapResult(new NewMapUtil("message", "Delete failure！").get()));
		}
	}
	
	
	@Override
	public PageHelperResult selectByUser(int pageSize, int currentPage,Integer  userId,String remark) {
		Page<BaseInfoDimAuthEntity> page = PageHelper.startPage(currentPage, pageSize).doSelectPage(() ->iBaseInfoDimAuthDao.selectByUser(userId, remark));
		return new PageHelperResult(page);
	}
	@Override
	public PageHelperResult selectByRole(int pageSize, int currentPage,Integer  roleId,String remark) {
		String adminroleId = PropertiesHolder.getProperty("role.superadmin");
		if (StringUtils.hasText(adminroleId) && roleId.toString().equals(adminroleId)) {
			BaseInfoDimAuthEntity authEntity=new BaseInfoDimAuthEntity();
			Page<BaseInfoDimAuthEntity> page = PageHelper.startPage(currentPage, pageSize).doSelectPage(() ->iBaseInfoDimAuthDao.selectDimAuth(authEntity));
			return new PageHelperResult(page);
		}
		Page<Map<String, Object>> page = PageHelper.startPage(currentPage, pageSize).doSelectPage(() ->iBaseInfoDimAuthDao.selectByRole(roleId, remark));
		return new PageHelperResult(page);
	}
	@Override
	public List<BaseInfoDimAuthEntity> selectByRoleNotSelected(Integer  roleId,String remark) {
		List<BaseInfoDimAuthEntity> selectByRole = iBaseInfoDimAuthDao.selectByRoleNotSelected(roleId, remark);
	 	for (int i = 0; i < selectByRole.size(); i++) {
	 		BaseInfoDimAuthEntity baseInfoDimAuthEntity = selectByRole.get(i);
	 		baseInfoDimAuthEntity.setIndex(i);
		}
		return selectByRole;
	}

}
