package com.flchy.blog.privilege.config.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.flchy.blog.base.annotation.Log;
import com.flchy.blog.base.enums.OperateCodeEnum;
import com.flchy.blog.base.exception.BusinessException;
import com.flchy.blog.base.response.ResponseCommand;
import com.flchy.blog.plugin.redis.RedisBusines;
import com.flchy.blog.pojo.InfoUser;
import com.flchy.blog.privilege.config.service.IInfoUserService;
import com.flchy.blog.utils.MD5;
import com.flchy.blog.utils.NewMapUtil;
import com.flchy.blog.utils.StringUtil;
import com.flchy.blog.utils.UncString;

@RestController
@RequestMapping("user")
public class TokenPortalController {
	private static final Logger logger = LoggerFactory.getLogger(TokenPortalController.class);
	@Autowired
	private IInfoUserService iInfoUserService;
	@Autowired
	private RedisBusines redisBusines;
	private static int expire = 60 * 60 * 2;
	@PostMapping("/login")
	public Object login(@RequestParam(value = "userName", required = false) String userName,
			@RequestParam(value = "passWord", required = false) String passWord,
			@RequestParam(value = "token", required = false) String requestToken,
			HttpServletRequest request) {
		// 用户名或者密码为空
		if (!StringUtils.hasText(userName) || !StringUtils.hasText(passWord)) {
			throw new BusinessException("用户名密码不能为空");
		}
		List<InfoUser> list = iInfoUserService.selectList(new EntityWrapper<InfoUser>().where("user_name={0}", userName));
		if (list.size() > 1) {
			logger.error(userName + ":用户名重复！！！！");
		}
		if (list.isEmpty()) {
			throw new BusinessException("不存在当前用户");
		}
		InfoUser infoUser = list.get(0);
		// 获取用户密码
		String passWords = infoUser.getPassword();
		if (!StringUtil.isNullOrEmpty(passWords) && !StringUtil.isNullOrEmpty(userName)) {
			// 数据库存储MD5密码
			if (MD5.encryptToHex(passWord).equals(passWords)) {
				String token = sendRedis(infoUser);
				HttpSession session = request.getSession();
				session.setAttribute("token", token);
				return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, new NewMapUtil().set("token", token).get());
			} else {
				throw new BusinessException("账号或密码不正确");
			}
		} else {
			throw new BusinessException("账号或密码不正确");
		}
	}

	/**
	 * 注销
	 * 
	 * @param token
	 * @param request
	 * @return
	 */
	@GetMapping("/logout")
	public Object logout(@RequestParam(value = "token") String token, HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.setAttribute("token", null);
		try {
			redisBusines.del(token);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new BusinessException("注销账户失败:" + e.getMessage());
		}
		return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, new NewMapUtil().set("message", "注销成功").get());
	}

	/**
	 * 验证登录状态
	 * @param token
	 * @param request
	 * @return
	 */
	@GetMapping("/isAuthenticated")
	public Object isAuthenticated(@RequestParam(value = "token", required = false) String token,
			HttpServletRequest request) {
		if (StringUtil.isNullOrEmpty(token)) {
			throw new BusinessException("request was aborted, token is null");
		}
		try {
			if (redisBusines.get(token) != null) {
				return new ResponseCommand(ResponseCommand.STATUS_SUCCESS,
						new NewMapUtil().set("message", "登录正常！").get());
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		throw new BusinessException("request was aborted, token is null");

	}

	/**
	 * 存入redis
	 * 
	 * @param infoUser
	 * @return
	 */
	public String sendRedis(InfoUser infoUser) {
		String token = generateToken(infoUser.getUserName(), infoUser.getPassword());
		try {
			// 已有token重新获取
			if (redisBusines.get(token) != null) {
				token = generateToken(infoUser.getUserName(), infoUser.getPassword());
			}
			redisBusines.setEx(token, JSON.toJSONString(infoUser), expire);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new BusinessException(e.getMessage());
		}
		return token;
	}

	/**
	 * token生成规则: 16位随机码 + 2位userName校验位 + 2位passWord校验位
	 * 
	 * @param userName
	 * @param passWord
	 * @return
	 */
	private String generateToken(String userName, String passWord) {
		String userNameMd5 = MD5.encryptToHex(userName);
		String passWordMd5 = MD5.encryptToHex(passWord);
		String userNameMd5Verified = userNameMd5.substring(userNameMd5.length() - 2);
		String passWordMd5Verified = passWordMd5.substring(passWordMd5.length() - 2);
		StringBuilder dataToken = new StringBuilder();
		dataToken.append(UncString.randomString(16)).append(userNameMd5Verified).append(passWordMd5Verified);
		return dataToken.toString();
	}

	/**
	 * 密码生成
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(MD5.encryptToHex("admin"));
	}
}
