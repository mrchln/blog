package com.flchy.blog.privilege.support;

import java.util.List;

import com.flchy.blog.privilege.config.bean.BaseUser;

public interface ITokenPrivilegeService {

	BaseUser getCurrentUser(String adoptToken);

	boolean hasRole(String adoptToken, String roleId);

	boolean hasRole(String adoptToken, List<String> roles);

	boolean isPermitted(String adoptToken, String permission);

	boolean isPermitted(String adoptToken, List<String> permissions);

	boolean isSuperAdmin(String adoptToken);

	boolean isAuthenticated(String adoptToken);
	
	boolean logout(String adoptToken);
}
