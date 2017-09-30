package com.flchy.blog.privilege.config.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.flchy.blog.base.exception.BusinessException;
import com.flchy.blog.plugin.redis.RedisBusines;
import com.flchy.blog.pojo.InfoUser;

public abstract class ControllerAbstract {
	@Autowired
	private RedisBusines redisBusines;
	private static final Logger logger = LoggerFactory.getLogger(ControllerAbstract.class);

	public InfoUser getUser(String token, HttpServletRequest request) {
		InfoUser user = null;
		String tk = "";
		if (token == null || token.isEmpty() || token.equals("")) {
			HttpSession session = request.getSession();
			Object sessionToken = session.getAttribute("token");
			if (sessionToken == null || token.isEmpty() || sessionToken.equals("")) {
				throw new BusinessException("清先登录！");
			} else {
				tk = sessionToken.toString();
			}
		} else {
			tk = token;
		}
		try {
			String userString = redisBusines.get(tk);
			user = JSON.parseObject(userString, InfoUser.class);
			if (user == null) {
				throw new BusinessException("清先登录！");
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return user;
	}

}
