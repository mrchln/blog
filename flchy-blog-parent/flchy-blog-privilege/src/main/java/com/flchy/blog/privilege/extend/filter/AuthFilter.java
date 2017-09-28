package com.flchy.blog.privilege.extend.filter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.flchy.blog.base.response.ResponseCommand;
import com.flchy.blog.plugin.redis.RedisBusines;
import com.flchy.blog.pojo.ConfUrl;
import com.flchy.blog.privilege.config.controller.TokenPortalController;
import com.flchy.blog.privilege.config.emun.UrlPathType;
import com.flchy.blog.privilege.config.service.IConfUrlService;
import com.flchy.blog.utils.NewMapUtil;
@Component
public class AuthFilter implements Filter {
	private static final Logger logger = LoggerFactory.getLogger(TokenPortalController.class);
	@Autowired
	private RedisBusines redisBusines;
	@Autowired
	private IConfUrlService iConfUrlService;

	@Override
	public void destroy() {
		System.out.println("请求访问权限过滤器销毁");
	}

	@Override
	public void doFilter(ServletRequest httpRequest, ServletResponse httpResponse, FilterChain chain)
			throws IOException, ServletException {
		HttpServletResponse response = (HttpServletResponse) httpResponse;
		HttpServletRequest request = (HttpServletRequest) httpRequest;
		if (request.getMethod().equals("OPTIONS")) {
			chain.doFilter(request, response);
			return;
		}
		String requestPath=request.getRequestURI();
		List<ConfUrl> typeConfUrl = iConfUrlService.getTypeConfUrl(UrlPathType.ALL.getCode());
		List<String> arrayUrl= typeConfUrl.stream().map(u->u.getUrlPath()).collect(Collectors.toList());
		//判断是登录接口
		if(arrayUrl.contains(requestPath)){
			chain.doFilter(request, response);
			return ;
		}
		String token = "";
		Object requesttoken = request.getParameter("token");
		if (requesttoken == null || requesttoken.equals("")) {
			HttpSession session = request.getSession();
			Object sessionToken = session.getAttribute("token");
			if (sessionToken == null || sessionToken.equals("")) {
				httpResponse.setContentType("json/html; charset=utf-8");
				String curOrigin = request.getHeader("Origin");
				response.setHeader("Access-Control-Allow-Origin", curOrigin);
				response.setHeader("Access-Control-Allow-Headers", "*");
				response.setHeader("Access-Control-Allow-Methods", "*");
				response.setHeader("Access-Control-Allow-Credentials", "true");
				httpResponse.getWriter().write(JSON.toJSONString(new ResponseCommand(ResponseCommand.STATUS_LOGIN_ERROR,
						new NewMapUtil("message", "请传入令牌！").get())));
				return;
			} else {
				token = sessionToken.toString();
			}
		} else {
			token = requesttoken.toString();
		}
		try {
			String user = redisBusines.get(token);
			if (user == null) {
				httpResponse.setContentType("json/html; charset=utf-8");
				String curOrigin = request.getHeader("Origin");
				response.setHeader("Access-Control-Allow-Origin", curOrigin);
				response.setHeader("Access-Control-Allow-Headers", "*");
				response.setHeader("Access-Control-Allow-Methods", "*");
				response.setHeader("Access-Control-Allow-Credentials", "true");
				httpResponse.getWriter().write(JSON.toJSONString(new ResponseCommand(ResponseCommand.STATUS_LOGIN_ERROR,
						new NewMapUtil("message", "登录失效,请重新登录！").get())));
				return;
			}
			//权限    待添加

		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		System.out.println("请求访问权限过滤器初始化");
	}
	
	
	

}
