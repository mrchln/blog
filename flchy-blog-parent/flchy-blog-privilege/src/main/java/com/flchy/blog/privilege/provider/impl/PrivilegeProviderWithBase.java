package com.flchy.blog.privilege.provider.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.flchy.blog.privilege.config.bean.BaseDim;
import com.flchy.blog.privilege.config.bean.BaseRole;
import com.flchy.blog.privilege.config.bean.BaseUser;
import com.flchy.blog.privilege.config.bean.ConfUrlBean;
import com.flchy.blog.privilege.config.entity.BaseInfoDimAuthEntity;
import com.flchy.blog.privilege.config.entity.BaseInfoRoleEntity;
import com.flchy.blog.privilege.config.entity.BaseInfoUserEntity;
import com.flchy.blog.privilege.provider.AbstractPrivilegeProvider;
import com.flchy.blog.privilege.support.IAuthServeService;
import com.flchy.blog.utils.convert.BeanConvertUtil;
import com.google.common.collect.Lists;

/**
 * 本地权限服务实现类（支持融合远程服务，权限由远端服务控制）
 */
@Service
public class PrivilegeProviderWithBase extends AbstractPrivilegeProvider {
	private static Logger logger = LoggerFactory.getLogger(PrivilegeProviderWithBase.class);
	@Resource
	IAuthServeService authServeService;

	/**
	 * 解密接口
	 */
	public String decrypt(String encryptStr) {
		return null;
	}

	/**
	 * 加密接口 <br/>
	 * 接口名称：encryption
	 */
	public String encrypt(String str) {
		if (null != str && !"".equals(str)) {
			return str;
		}
		return null;
	}

	/**
	 * 本地加密 <br/>
	 * 接口名称：encryption
	 */
	public String encryptLoacl(String str) {
		if (null != str && !"".equals(str)) {
			return str;
		}
		return null;
	}

	/**
	 * 根据用户ID获取用户对象<br/>
	 * 根据用户ID获取用户基本信息<br/>
	 * 
	 * @param userName
	 *            用户id
	 */
	public BaseUser getUserByUserName(String userName) {
		// 本地注册登录
		BaseInfoUserEntity baseInfoUserEntity = authServeService.selectUserByUserName(userName);
		if (baseInfoUserEntity != null) {
			BaseUser baseUser = BeanConvertUtil.map(baseInfoUserEntity, BaseUser.class);
			if (null != baseUser && null != baseUser.getUserId()) {
				baseUser.setAuthMenuIds(authServeService.getAuthMenuIds(baseUser.getUserId()));
				baseUser.setAuthElemIds(authServeService.getAuthElemIds(baseUser.getUserId()));
				baseUser.setAuthPanelIds(authServeService.getAuthPanelIds(baseUser.getUserId()));
			}
			return baseUser;
		}
		// 授权注册登录
		return null;
	}

	/**
	 * 根据用户ID获取用户分配的所有角色 <br/>
	 * 根据用户id查询用户拥有的角色 <br/>
	 */
	public List<BaseRole> getRolesByUserId(int userId) {
		List<BaseInfoRoleEntity> list = authServeService.selectRolesByUserId(userId);
		if (CollectionUtils.isEmpty(list)) {
			return null;
		}
		List<BaseRole> resultList = Lists.newArrayList();
		for (BaseInfoRoleEntity baseRoleInfoEntity : list) {
			resultList.add(BeanConvertUtil.map(baseRoleInfoEntity, BaseRole.class));
		}
		return resultList;
	}

	/**
	 * 根据用户ID，获取所有的Shiro用户权限字符串
	 */
	public Set<String> getPermissionsByUserId(int userId) {
		Set<String> privIds = this.authServeService.getAuthPrivIds(userId);
		if (null != privIds && !privIds.isEmpty()) {
			Set<String> permissionsSet = new HashSet<>();
			for (String privId : privIds) {
				permissionsSet.add(privId);
			}
			return permissionsSet;
		}
		return null;
	}

	/**
	 * 根据用户ID获取用户授权的元素资源
	 */
	public Set<Integer> getAuthElemIdsByUserId(int userId) {
		return this.authServeService.getAuthElemIds(userId);
	}

	/**
	 * 根据用户ID获取用户授权的面板资源
	 */
	public Set<Integer> getAuthPanelIdsByUserId(int userId) {
		return this.authServeService.getAuthPanelIds(userId);
	}

	/**
	 * 根据用户ID获取用户授权的数据资源
	 */
	@Override
	public List<BaseDim> getAuthDimInfoByUserId(int userId) {
		List<BaseInfoDimAuthEntity> list = authServeService.selectDimAuthByUserId(userId);
		if (CollectionUtils.isEmpty(list)) {
			return null;
		}
		List<BaseDim> resultList = Lists.newArrayList();
		for (BaseInfoDimAuthEntity baseInfoDimAuthEntity : list) {
			resultList.add(BeanConvertUtil.map(baseInfoDimAuthEntity, BaseDim.class));
		}
		return resultList;
	}

	/**
	 * 根据账号ID，获取所有的授权的访问地址
	 */
	@Override
	public Set<ConfUrlBean> getUrlPermisByUserId(int userId) {
		return authServeService.getAuthUrlPaths(userId);
	}
}
