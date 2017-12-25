package com.flchy.blog.privilege.support.impl;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.flchy.blog.base.holder.PropertiesHolder;
import com.flchy.blog.plugin.redis.RedisBusines;
import com.flchy.blog.privilege.config.bean.BaseUser;
import com.flchy.blog.privilege.support.ITokenPrivilegeService;
import com.flchy.blog.utils.BeanUtil;

/**
 * 对外提供自定义控制的登录数据获取
 */
@Service("tokenPrivilegeService")
public class TokenPrivilegeServiceImpl implements ITokenPrivilegeService {
	private static Logger logger = LoggerFactory.getLogger(TokenPrivilegeServiceImpl.class);

	@Autowired
	private RedisBusines redisBusines;
	@Override
	public BaseUser getCurrentUser(String adoptToken) {
		byte[] userBytesInfo = null;
		if (null != redisBusines) {
			try {
				userBytesInfo = redisBusines.get(adoptToken.getBytes("utf-8"));
			} catch (UnsupportedEncodingException e) {
				logger.error("Failed to get redis redis , error message is" + e.getMessage() + " ;Please contact the administrator");
			}
		} else {
			logger.error("Failed to get the distributed redis connection , connection is null; Please contact the administrator");
		}
		if (null == userBytesInfo || userBytesInfo.length == 0) {
			return null;
		}
		return (BaseUser) BeanUtil.byteToObject(userBytesInfo);
	}

	/**
	 * 是否拥有某一个角色.<br/>
	 */
	@Override
	public boolean hasRole(String adoptToken, String roleId) {
		BaseUser userInfo = this.getCurrentUser(adoptToken);
		if (null != userInfo) {
			Set<String> roleIds = userInfo.getRoleIds();
			if (null != roleIds) {
				if (roleIds.contains(roleId)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 是否拥有某集合角色.<br/>
	 */
	@Override
	public boolean hasRole(String adoptToken, List<String> roleIdList) {
		BaseUser userInfo = this.getCurrentUser(adoptToken);
		if (null == userInfo) {
			return false;
		}
		Set<String> roleIds = userInfo.getRoleIds();
		if (null == roleIds) {
			return false;
		}
		boolean hasAllRoles = true;
		for (String roleId : roleIdList) {
			// 一个不含有则false
			if (!roleIds.contains(roleId)) {
				hasAllRoles = false;
				break;
			}
		}
		return hasAllRoles;
	}

	/**
	 * 是否拥有某一个权限.<br/>
	 */
	@Override
	public boolean isPermitted(String adoptToken, String permission) {
		BaseUser userInfo = this.getCurrentUser(adoptToken);
		if (null != userInfo) {
			Set<String> permissions = userInfo.getPermissions();
			if (null != permissions) {
				if (permissions.contains(permission)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 是否拥有某集合权限.<br/>
	 */
	@Override
	public boolean isPermitted(String adoptToken, List<String> permissionList) {
		BaseUser userInfo = this.getCurrentUser(adoptToken);
		if (null == userInfo) {
			return false;
		}
		Set<String> permissions = userInfo.getPermissions();
		if (null == permissions) {
			return false;
		}
		boolean hasAllPermitteds = true;
		for (String permission : permissionList) {
			// 一个不含有则false
			if (!permissions.contains(permission)) {
				hasAllPermitteds = false;
				break;
			}
		}
		return hasAllPermitteds;
	}

	/**
	 * 是否超级管理员.<br/>
	 */
	@Override
	public boolean isSuperAdmin(String adoptToken) {
		String roleId = PropertiesHolder.getProperty("role.superadmin");
		if (StringUtils.hasText(roleId)) {
			BaseUser userInfo = this.getCurrentUser(adoptToken);
			if (null == userInfo) {
				return false;
			}
			Set<String> roleIds = userInfo.getRoleIds();
			if (null == roleIds) {
				return false;
			}
			if(roleIds.contains(roleId)){
				return true;
			}
		}
		return false;
	}

	/**
	 * 校验当前用户是否已验证<br/>
	 */
	@Override
	public boolean isAuthenticated(String adoptToken) {
		BaseUser userInfo = this.getCurrentUser(adoptToken);
		if (null == userInfo) {
			return false;
		}
		return true;
	}

	/**
	 * 注销登录令牌<br/>
	 */
	@Override
	public boolean logout(String adoptToken) {
		if (null != redisBusines) {
			try {
				redisBusines.del(adoptToken.getBytes("utf-8"));
			} catch (UnsupportedEncodingException e) {
				logger.error("Failed to delete redis value , error message is" + e.getMessage() + " ;Please contact the administrator");
				return false;
			}
		} else {
			logger.error("Failed to get the distributed redis connection , connection is null; Please contact the administrator");
			return false;
		}
		return true;
	}
}
