package com.flchy.blog.privilege.portal;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.flchy.blog.base.holder.PropertiesHolder;
import com.flchy.blog.base.response.ResponseCommand;
import com.flchy.blog.base.response.VisitsMapResult;
import com.flchy.blog.plugin.redis.RedisBusines;
import com.flchy.blog.plugin.redis.util.StringUtil;
import com.flchy.blog.privilege.authentication.IAuthTokenLoginService;
import com.flchy.blog.privilege.authentication.ITokenAuthentService;
import com.flchy.blog.privilege.config.bean.BaseMenu;
import com.flchy.blog.privilege.config.bean.BaseUser;
import com.flchy.blog.privilege.enums.LoginAuthResultEnums;
import com.flchy.blog.privilege.enums.LogoutResultEnums;
import com.flchy.blog.privilege.provider.holder.PrivilegeHolder;
import com.flchy.blog.privilege.support.ITokenPrivilegeService;
import com.flchy.blog.utils.NewMapUtil;

/**
 * <用于Portal Web端基础控制层方法编写> 
 * 
 * @since 0.0.1-SNAPSHOT
 */
@Controller
@RequestMapping(value = "/")
public class TokenPortalController {
	private static Logger logger = LoggerFactory.getLogger(TokenPortalController.class);
	@Autowired
	private IAuthTokenLoginService loginAuthcService;
	@Autowired
	private ITokenPrivilegeService tokenPrivilegeService;
	@Autowired
	private ITokenAuthentService tokenAuthentService;
	/**
	 * 登录验证,登录成功返回令牌token
	 */
	@RequestMapping(value = "/authc/login")
	@ResponseBody
	public Object loginAuth(@RequestParam(value = "adoptToken", required = false) String adoptToken, 
			                @RequestParam(value = "loginName", required = false) String loginName, 
			                @RequestParam(value = "password", required = false) String loginPass, 
			                HttpServletRequest request) {
		// 用户名或者密码为空
		if (!StringUtils.hasText(loginName) || !StringUtils.hasText(loginPass)) {
			return new ResponseCommand(ResponseCommand.STATUS_ERROR, new VisitsMapResult(new NewMapUtil("message", LoginAuthResultEnums.LOGIN_FAIL_PARAM_NULL.toString()).get()));
		}
		// 登录名参数加密则解密
		if (PropertiesHolder.getBooleanProperty("userName.isEncrypt")) {
			loginName = PrivilegeHolder.getProvider().decrypt(loginName);
		}
		// 密码参数加密则解密
		if (PropertiesHolder.getBooleanProperty("passWord.isEncrypt")) {
			loginPass = PrivilegeHolder.getProvider().decrypt(loginPass);
		}
		VisitsMapResult loginCommand = null;
		if (StringUtil.isNullOrEmpty(adoptToken)) {
			// 令牌为空，直接执行登录
			loginCommand = loginAuthcService.authc(loginName, loginPass, request);
		} else {
			// 获取令牌对应登录用户
			BaseUser baseUser = tokenPrivilegeService.getCurrentUser(adoptToken);
			String userName = null;
			if (null != baseUser) {
				userName = baseUser.getUserName();
			}
			// 令牌是否已登录，令牌用户的登录名 == 请求登录名参数
			if (tokenPrivilegeService.isAuthenticated(adoptToken) && !StringUtil.isNullOrEmpty(loginName) && !StringUtil.isNullOrEmpty(userName)) {
				// 已登录，当前令牌有效状态
				if (userName.equals(loginName)) {
					return new ResponseCommand(ResponseCommand.STATUS_ERROR,  new VisitsMapResult(new NewMapUtil("message", LoginAuthResultEnums.LOGIN_FAIL_REPEAT_LOGIN.toString()).get()));
				}
			}
			// 新登录，执行登录校验
			loginCommand = loginAuthcService.authc(loginName, loginPass, request);
		}
		if (null == loginCommand) {
			//LoggingEventPublish.getInstance().loginEvent(LoginAuthResultEnums.LOGIN_FAIL_EXCEPTION);
			return new ResponseCommand(ResponseCommand.STATUS_ERROR,  new VisitsMapResult(new NewMapUtil("message", LoginAuthResultEnums.LOGIN_FAIL_EXCEPTION.toString()).get()));
		}  else {
			//LoggingEventPublish.getInstance().loginEvent(LoginAuthResultEnums.LOGIN_SUCCESS);
			if(!StringUtil.isNullOrEmpty(loginCommand.getAdoptToken())){
				return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, loginCommand);
			}
			if(loginCommand.getResult().get("message").equals(LoginAuthResultEnums.LOGIN_FAIL_USER_NOTEXSIST.getMsg())){
				return new ResponseCommand(ResponseCommand.STATUS_ERROR, loginCommand);
			}else {
				return new ResponseCommand(ResponseCommand.STATUS_LOGIN_ERROR, loginCommand);	
			}
		}
		
	}
		
	/**
	 * 令牌注销登录
	 */
	@RequestMapping(value = "/logout", method = RequestMethod.POST)
	@ResponseBody
	public Object logout(@RequestParam(value = "adoptToken") String adoptToken, 
			             HttpServletRequest request) {
		VisitsMapResult logoutResult = null;
		if (StringUtil.isNullOrEmpty(adoptToken)) {
			return new ResponseCommand(ResponseCommand.STATUS_ERROR, new VisitsMapResult(new NewMapUtil("message", LogoutResultEnums.LOGOUT_FAIL_TOKEN.toString()).get()));
		} else {
			logoutResult = loginAuthcService.logout(adoptToken);
		}
		return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, logoutResult);
	}

	/**
	 * 获取授权的菜单
	 * @param adoptToken
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getAuthMenus", method = RequestMethod.POST)
	@ResponseBody
	public Object getAuthMenuSet(@RequestParam(value = "adoptToken") String adoptToken, 
			                     HttpServletRequest request) {
		if (StringUtil.isNullOrEmpty(adoptToken)) {
			return new ResponseCommand(ResponseCommand.STATUS_ERROR,  new VisitsMapResult(new NewMapUtil("message", "request was aborted, adoptToken is null ").get()));
		}
		Collection<BaseMenu> authMenuSet = tokenAuthentService.getAuthPortalMenus(adoptToken);
		if (CollectionUtils.isEmpty(authMenuSet)) {
			return new ResponseCommand(ResponseCommand.STATUS_ERROR, new VisitsMapResult(new NewMapUtil("message", "request was aborted, get auth menu is null ").get()));
		}
		return authMenuSet;
	}
	
	/**
	 * 获取授权的资源
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getAuthResours", method = RequestMethod.POST)
	@ResponseBody
	public Object getAuthResources(@RequestParam(value = "adoptToken") String adoptToken,
			                       HttpServletRequest request) {
		if (StringUtil.isNullOrEmpty(adoptToken)) {
			return new ResponseCommand(ResponseCommand.STATUS_ERROR, new VisitsMapResult(new NewMapUtil("message", "request was aborted, adoptToken is null ").get()));
		}
		BaseUser tokenUser = tokenAuthentService.getAuthTokenUser(adoptToken);
		if(null== tokenUser){
			return new ResponseCommand(ResponseCommand.STATUS_ERROR, new VisitsMapResult(new NewMapUtil("message", "Invalid token, please log in again").get()));
		}
		return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, new VisitsMapResult(new NewMapUtil().set("panelIds", tokenUser.getAuthPanelIds())
				                                                                                       .set("elemIds", tokenUser.getAuthElemIds())
				                                                                                       .get()));
	}
	
	/**
	 *获取令牌用户
	 */
	@RequestMapping(value = "/getTokenUser", method = RequestMethod.POST)
	@ResponseBody
	public Object getTokenUser(@RequestParam(value="adoptToken")String adoptToken,
							   HttpServletRequest request) {
		if (StringUtil.isNullOrEmpty(adoptToken)) {
			return new ResponseCommand(ResponseCommand.STATUS_ERROR, new VisitsMapResult(new NewMapUtil("message", "request was aborted, adoptToken is null ").get()));
		}
		BaseUser tokenUser = tokenAuthentService.getAuthTokenUser(adoptToken);
		if(null== tokenUser){
			return new ResponseCommand(ResponseCommand.STATUS_ERROR, new VisitsMapResult(new NewMapUtil("message", "Invalid token, please log in again").get()));
		}
		return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, tokenUser);
	}
	
	
	
	 @RequestMapping(value={"/isAuthenticated"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
	  @ResponseBody
	  public Object isAuthenticated(@RequestParam(value="adoptToken", required=false) String adoptToken, HttpServletRequest request)
	  {
	    if (StringUtil.isNullOrEmpty(adoptToken))
	      return new ResponseCommand(ResponseCommand.STATUS_ERROR, new VisitsMapResult(new NewMapUtil("message", "request was aborted, adoptToken is null ").get()));

	    return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, Boolean.valueOf(this.tokenPrivilegeService.isAuthenticated(adoptToken)));
	  }
}