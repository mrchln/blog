package com.flchy.blog.privilege.authentication;

import javax.servlet.http.HttpServletRequest;

import com.flchy.blog.base.response.VisitsMapResult;

public interface IAuthTokenLoginService {

	/***
	 * 登录验证方法
	 */
	public VisitsMapResult authc(String loginName, String loginPass, HttpServletRequest request);

	/**
	 * 用户注销方法
	 */
	public VisitsMapResult logout(String adoptToken);
}
