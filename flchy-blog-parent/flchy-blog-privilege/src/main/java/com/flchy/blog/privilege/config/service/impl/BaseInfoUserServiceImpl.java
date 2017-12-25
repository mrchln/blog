package com.flchy.blog.privilege.config.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flchy.blog.base.response.PageHelperResult;
import com.flchy.blog.base.response.ResponseCommand;
import com.flchy.blog.base.response.VisitsMapResult;
import com.flchy.blog.privilege.config.dao.IBaseInfoUserDao;
import com.flchy.blog.privilege.config.entity.BaseInfoUserEntity;
import com.flchy.blog.privilege.config.service.IBaseInfoUserService;
import com.flchy.blog.utils.NewMapUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
/**
 * 用户业务类
 */
@Service
public class BaseInfoUserServiceImpl implements IBaseInfoUserService {
	private static Logger logger = LoggerFactory.getLogger(BaseInfoUserServiceImpl.class);
	@Autowired
	private IBaseInfoUserDao baseInfoUserDao;

	@Override
	public List<BaseInfoUserEntity> selectUserByUserName(String userName) {
		return baseInfoUserDao.selectUserByUserName(userName);
	}
	@Override
	public List<BaseInfoUserEntity> verificationUserByUser(BaseInfoUserEntity baseInfoUserEntity) {
		return baseInfoUserDao.verificationUserByUser(baseInfoUserEntity);
	}

	@Override
	public PageHelperResult selectUserByUser(BaseInfoUserEntity baseInfoUserEntity, int pageSize, int currentPage) {
		Page<BaseInfoUserEntity> page = PageHelper.startPage(currentPage, pageSize).doSelectPage(() -> baseInfoUserDao.selectUserByUser(baseInfoUserEntity));
		return new PageHelperResult(page);

	}

	@Override
	public PageHelperResult selectDeletedUser(BaseInfoUserEntity baseInfoUserEntity, int pageSize, int currentPage) {
		Page<BaseInfoUserEntity> page = PageHelper.startPage(currentPage, pageSize).doSelectPage(() -> baseInfoUserDao.selectDeletedUser(baseInfoUserEntity));
		return new PageHelperResult(page);

	}

	@Override
	public boolean insertUser(BaseInfoUserEntity baseInfoUserEntity) {
		baseInfoUserEntity.setPassWord(new Md5Hash(baseInfoUserEntity.getPassWord()).toHex());
		Integer insertUser = baseInfoUserDao.insertUser(baseInfoUserEntity);
		return !(insertUser == null);
	}

	@Override
	public boolean updateUser(BaseInfoUserEntity baseInfoUserEntity) {
		boolean updateUser = baseInfoUserDao.updateUser(baseInfoUserEntity);
		return updateUser;
	}

	@Override
	public boolean deleteUser(String userId) {
		String subfirst = userId.substring(0, 1);
		String subLast = userId.substring(userId.length() - 1, userId.length());
		if (",".equals(subfirst)) {
			userId = userId.substring(1, userId.length());
		}
		if (",".equals(subLast)) {
			userId = userId.substring(0, userId.length() - 1);
		}
		String[] split = userId.split(",");
		Integer[] arrInteger = new Integer[split.length];

		try {
			for (int i = 0; i < split.length; i++) {
				if (!split[i].trim().equals("") && split[i] != null){
					arrInteger[i] = Integer.valueOf(split[i].trim().toString());
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		if (split.length < 1) {
			return false;
		}

		boolean updateUser = baseInfoUserDao.deleteUser(Arrays.asList(arrInteger));
		return updateUser;

	}

	@Override
	public boolean restoreUser(String userId) {
		String subfirst = userId.substring(0, 1);
		String subLast = userId.substring(userId.length() - 1, userId.length());
		if (",".equals(subfirst)) {
			userId = userId.substring(1, userId.length());
		}
		if (",".equals(subLast)) {
			userId = userId.substring(0, userId.length() - 1);
		}
		String[] split = userId.split(",");
		Integer[] arrInteger = new Integer[split.length];

		try {
			for (int i = 0; i < split.length; i++) {
				if (!split[i].trim().equals("") && split[i] != null)
				{
					arrInteger[i] = Integer.valueOf(split[i].trim().toString());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		if (split.length < 1) {
			return false;
		}
		boolean restoreUser = baseInfoUserDao.restoreUser(Arrays.asList(arrInteger));
		return restoreUser;
	}

	@Override
	public Object updatePwd(Integer userId, String passWord, String newPassword, String repeatPassword) {
		if (!newPassword.equals(repeatPassword)) {
			return new ResponseCommand(ResponseCommand.STATUS_ERROR, new VisitsMapResult(new NewMapUtil("message", "The two password is inconsistent！").get()));
		}
		BaseInfoUserEntity user = baseInfoUserDao.selectUserByUserId(userId);
		String userPassWord = user.getPassWord();
		if ((new Md5Hash(passWord).toHex()).equals(userPassWord)) {
			if (userPassWord.equals(new Md5Hash(newPassword).toHex())) {
				return new ResponseCommand(ResponseCommand.STATUS_ERROR, new VisitsMapResult(new NewMapUtil("message", "The new password and the old password is not the same！").get()));
			}
			user.setPassWord(new Md5Hash(newPassword).toHex());
			boolean updatePwd = baseInfoUserDao.updatePwd(user);
			if (updatePwd){
				return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, userId);
			}
			return new ResponseCommand(ResponseCommand.STATUS_ERROR, new VisitsMapResult(new NewMapUtil("message", "Failed to modify password！").get()));
		} else {
			return new ResponseCommand(ResponseCommand.STATUS_ERROR, new VisitsMapResult(new NewMapUtil("message", "The old password is incorrect！").get()));
		}

	}

	@Override
	public PageHelperResult selectUserByRoleIdSelected(Integer roleId, String nickName, int pageSize, int currentPage) {
		Page<Map<String, Object>> page = PageHelper.startPage(currentPage, pageSize).doSelectPage(() -> baseInfoUserDao.selectUserByRoleIdSelected(roleId, nickName));
		return new PageHelperResult(page);
	}

	@Override
	public List<BaseInfoUserEntity> selectUserByRoleIdNotSelected(Integer roleId, String nickName) {
		List<BaseInfoUserEntity> selectUserByRoleIdNotSelected = baseInfoUserDao.selectUserByRoleIdNotSelected(roleId, nickName);
		for (int i = 0; i < selectUserByRoleIdNotSelected.size(); i++) {
			BaseInfoUserEntity baseInfoUserEntity = selectUserByRoleIdNotSelected.get(i);
			baseInfoUserEntity.setIndex(i);
		}
		return selectUserByRoleIdNotSelected;
	}

	@Override
	public List<BaseInfoUserEntity> selectUserByUser(BaseInfoUserEntity baseInfoUserEntity) {
		return baseInfoUserDao.selectUserByUser(baseInfoUserEntity);
	}
}
