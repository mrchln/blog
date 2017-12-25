package com.flchy.blog.privilege.authentication.impl;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.flchy.blog.base.event.LoggingEventPublish;
import com.flchy.blog.base.response.VisitsMapResult;
import com.flchy.blog.plugin.redis.RedisBusines;
import com.flchy.blog.plugin.redis.util.StringUtil;
import com.flchy.blog.privilege.authentication.IAuthTokenLoginService;
import com.flchy.blog.privilege.authentication.ITokenAuthentService;
import com.flchy.blog.privilege.config.bean.BaseDim;
import com.flchy.blog.privilege.config.bean.BaseRole;
import com.flchy.blog.privilege.config.bean.BaseUser;
import com.flchy.blog.privilege.config.bean.ConfUrlBean;
import com.flchy.blog.privilege.enums.LoginAuthResultEnums;
import com.flchy.blog.privilege.enums.LogoutResultEnums;
import com.flchy.blog.privilege.provider.holder.PrivilegeHolder;
import com.flchy.blog.privilege.support.ITokenPrivilegeService;
import com.flchy.blog.utils.BeanUtil;
import com.flchy.blog.utils.MD5;
import com.flchy.blog.utils.NewMapUtil;
import com.flchy.blog.utils.ip.InternetProtocol;
import com.flchy.blog.utils.ip.copy.IpUtils;

import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.UserAgent;


/**
 * <系统的登录验证实现类>
 */
@Service
public class AuthTokenLoginServiceImpl implements IAuthTokenLoginService {
	private static Logger logger = LoggerFactory.getLogger(AuthTokenLoginServiceImpl.class);
	private static final String tokenServerKey = "com.flchy.blog";
	// TODO 测试令牌存货时间设为2小时，正式时记得改回来
	private static final int DEFAULT_INVALID_TIME = 60* 30 * 2 ;//* 30 * 24
	//登录次数存储时间
	private static final int DEFAULT_NUM_TIME = 60* 30 * 24 ;//* 30 * 24
	
	@Autowired
	private ITokenPrivilegeService tokenPrivilegeService;
	@Autowired
	private ITokenAuthentService tokenAuthentService;
	@Autowired
	private RedisBusines redisBusines;
	/**
	 * 执行登录校验
	 */
	public VisitsMapResult authc(String loginName, String loginPass, HttpServletRequest request) {
		String remoteAddr = InternetProtocol.getRemoteAddr(request);
		String ipkey="login"+remoteAddr;
		String string = redisBusines.get(ipkey);
		Integer loginNum=0;
		if(string!=null){
			 loginNum = Integer.valueOf(string);
			 if(loginNum>5){
				 return new VisitsMapResult(new NewMapUtil("message", LoginAuthResultEnums.LOGIN_RESTRICTED.toString()).get());
			 }
		}
		
		String ua = request.getHeader("User-Agent");
		//转成UserAgent对象
		UserAgent userAgent = UserAgent.parseUserAgentString(ua); 
		//获取浏览器信息
		Browser browser = userAgent.getBrowser();  
		//浏览器名称
		String browserName = browser.getName();
		// 查询用户表数据
		BaseUser userInfo = PrivilegeHolder.getProvider().getUserByUserName(loginName);
		if (null == userInfo) {
			LoggingEventPublish.getInstance().loginEvent(null,null, null, IpUtils.getIPAddress(), remoteAddr, ua, browserName, new Date(),-1,JSON.toJSONString(request.getParameterMap()));
			redisBusines.setEx(ipkey, String.valueOf(loginNum+=1),DEFAULT_NUM_TIME);
			return new VisitsMapResult(new NewMapUtil("message", LoginAuthResultEnums.LOGIN_FAIL_USER_NOTEXSIST.toString()).get());
		}
		// 获取用户密码
		String passWord = userInfo.getPassWord();
		if (!StringUtil.isNullOrEmpty(passWord) && !StringUtil.isNullOrEmpty(loginPass)) {
			// 数据库存储MD5密码
			if ((new Md5Hash(loginPass).toHex()).equals(passWord)) {
				String adoptToken = this.sessionLoginCache(userInfo, remoteAddr);
				LoggingEventPublish.getInstance().loginEvent(userInfo.getUserId().toString(),adoptToken, userInfo.getUserName(), IpUtils.getIPAddress(), remoteAddr, ua, browserName, new Date(),1,null);
				return new VisitsMapResult(new NewMapUtil("message", LoginAuthResultEnums.LOGIN_SUCCESS.toString()).get(),adoptToken);
			} else {
				LoggingEventPublish.getInstance().loginEvent(String.valueOf(userInfo.getUserId()),null, userInfo.getUserName(), IpUtils.getIPAddress(), remoteAddr, ua, browserName, new Date(),-1,JSON.toJSONString(request.getParameterMap()));
				redisBusines.setEx(ipkey, String.valueOf(loginNum+=1),DEFAULT_NUM_TIME);
				return new VisitsMapResult(new NewMapUtil("message", LoginAuthResultEnums.LOGIN_FAIL_PWD_INCORRECT.toString()).get());
			}
		} else {
			LoggingEventPublish.getInstance().loginEvent(String.valueOf(userInfo.getUserId()),null, userInfo.getUserName(), IpUtils.getIPAddress(), remoteAddr, ua, browserName, new Date(),-1,JSON.toJSONString(request.getParameterMap()));
			redisBusines.setEx(ipkey, String.valueOf(loginNum+=1),DEFAULT_NUM_TIME);
			return new VisitsMapResult(new NewMapUtil("message", LoginAuthResultEnums.LOGIN_FAIL_PWD_INCORRECT.toString()).get());
		}
	}

	
	/**
	 * <用户注销方法>
	 **/
	public VisitsMapResult logout(String adoptToken) {
		if (!tokenPrivilegeService.isAuthenticated(adoptToken)) {
			return new VisitsMapResult(new NewMapUtil("message", LogoutResultEnums.LOGOUT_SUCCESS.toString()).get());
		} else {
			if (tokenPrivilegeService.logout(adoptToken)) {
				return new VisitsMapResult(new NewMapUtil("message", LogoutResultEnums.LOGOUT_SUCCESS.toString()).get());
			} else {
				return new VisitsMapResult(new NewMapUtil("message", LogoutResultEnums.LOGOUT_FAIL_EXCEPTION.toString()).get());
			}
		}
	}

	/**
	 * <登录用户session缓存>
	 */
	private String sessionLoginCache(BaseUser userInfo, String remoteAddr) {
		// 获取用户所有的角色授权
		List<BaseRole> roleList = PrivilegeHolder.getProvider().getRolesByUserId(userInfo.getUserId());
		if (!CollectionUtils.isEmpty(roleList)) {
			userInfo.setRoles(roleList);
		}
		// 获取用户所有的数据权限
		List<BaseDim> dimList = PrivilegeHolder.getProvider().getAuthDimInfoByUserId(userInfo.getUserId());
		if (!CollectionUtils.isEmpty(dimList)) {
			userInfo.setDimAuths(dimList);
		}
		// 获取用户所有的访问权限
		Set<String> permissions = PrivilegeHolder.getProvider().getPermissionsByUserId(userInfo.getUserId());
		if (!CollectionUtils.isEmpty(permissions)) {
			userInfo.setPermissions(permissions);
		}
		// 获取用户所有的访问地址
		Set<ConfUrlBean> urlPermis = PrivilegeHolder.getProvider().getUrlPermisByUserId(userInfo.getUserId());
		if (!CollectionUtils.isEmpty(urlPermis)) {
			userInfo.setUrlPermis(urlPermis);
		}
		
		// 用户元素的操作权限
		Set<Integer> authElemIds = PrivilegeHolder.getProvider().getAuthElemIdsByUserId(userInfo.getUserId());
		if (!CollectionUtils.isEmpty(authElemIds)) {
			userInfo.setAuthElemIds(authElemIds);
		}
		// 用户面板的操作权限
		Set<Integer> authPanelIds = PrivilegeHolder.getProvider().getAuthPanelIdsByUserId(userInfo.getUserId());
		if (!CollectionUtils.isEmpty(authPanelIds)) {
			userInfo.setAuthPanelIds(authPanelIds);
		}
		
		// 生成登录用户令牌
		String adoptToken = this.generateToken(userInfo.getUserName(), userInfo.getPassWord(), remoteAddr);
		// 入库redis公有缓存
		try {
			if (null != redisBusines) {
				// 重复的令牌，再次生成
				if (null != redisBusines.get(adoptToken.getBytes("utf-8"))) {
					adoptToken = this.generateToken(userInfo.getUserName(), userInfo.getPassWord(), remoteAddr);
				}
				// session到Redis库
				redisBusines.setEx(adoptToken.getBytes(), BeanUtil.objectToByte(userInfo), DEFAULT_INVALID_TIME);
			} else {
				logger.error("Failed to get the distributed redis connection , connection is null; Please contact the administrator");
			}

		} catch (UnsupportedEncodingException e) {
			logger.error("Failed to get the distributed redis connection , error message is" + e.getMessage() + " ;Please contact the administrator");
		}

		return adoptToken;
	}

	/**
	 * token生成规则: 远端地址+ 2位userName校验位 + 2位passWord校验位
	 * 
	 * @param userName
	 * @param passWord
	 * @return
	 */
	private String generateToken(String userName, String passWord , String remoteAddr) {
		String userNameMd5 = MD5.encryptToHex(userName);
		String passWordMd5 = MD5.encryptToHex(passWord);
		String userNameMd5Verified = userNameMd5.substring(userNameMd5.length() - 2);
		String passWordMd5Verified = passWordMd5.substring(passWordMd5.length() - 2);
		StringBuilder dataToken = new StringBuilder();
		dataToken.append(MD5.encryptToHex(remoteAddr)).append(userNameMd5Verified).append(passWordMd5Verified).append(MD5.encryptToHex(tokenServerKey));
		return dataToken.toString();
	}
}
