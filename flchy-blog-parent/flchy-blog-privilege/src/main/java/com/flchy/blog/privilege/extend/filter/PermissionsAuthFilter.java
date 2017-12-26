package com.flchy.blog.privilege.extend.filter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.flchy.blog.base.holder.PropertiesHolder;
import com.flchy.blog.base.holder.SpringContextHolder;
import com.flchy.blog.base.response.ResponseCommand;
import com.flchy.blog.base.response.VisitsMapResult;
import com.flchy.blog.plugin.redis.RedisBusines;
import com.flchy.blog.plugin.redis.util.StringUtil;
import com.flchy.blog.privilege.config.bean.BaseUser;
import com.flchy.blog.privilege.config.bean.ConfUrlBean;
import com.flchy.blog.privilege.support.ITokenPrivilegeService;
import com.flchy.blog.utils.BeanUtil;
import com.flchy.blog.utils.MD5;
import com.flchy.blog.utils.NewMapUtil;
import com.flchy.blog.utils.ip.InternetProtocol;

@Component
public class PermissionsAuthFilter implements Filter {
	private static Logger logger = LoggerFactory.getLogger(PermissionsAuthFilter.class);
	private static String loginAction = "/authc/login";
	private static String[] allowedUrls;
	@Autowired
	private RedisBusines redisBusines;
	@Override
	public void destroy() {
		System.out.println("请求访问权限过滤器销毁");
	}
	static{
		getAllowedUrls();
	}
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		ITokenPrivilegeService tokenPrivilegeService = SpringContextHolder.getBean("tokenPrivilegeService", ITokenPrivilegeService.class);
		// 获取token令牌
		String adoptToken = request.getParameter("adoptToken");
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		String requestUri = WebUtils.getPathWithinApplication(WebUtils.toHttp(request));
		//跨域返回
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		//判断OPTIONS 直接返回
		if(httpRequest.getMethod().equals("OPTIONS")){
			chain.doFilter(request, response);
			return ;
		}
		String curOrigin = httpResponse.getHeader("Origin");
		httpResponse.setHeader("Access-Control-Allow-Origin", curOrigin);
		httpResponse.setHeader("Access-Control-Allow-Headers", "*");
		httpResponse.setHeader("Access-Control-Allow-Methods", "*");
		httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
	
		//判断adoptToken是否为空  不是登录接口    否则 返回
		if (StringUtil.isNullOrEmpty(adoptToken) && !requestUri.equals(loginAction) && (!requestUri.startsWith("/flchy/"))) {
			httpResponse.getWriter().write(JSON.toJSONString(new ResponseCommand(ResponseCommand.STATUS_ERROR, new VisitsMapResult(new NewMapUtil("message", "Request token is not invalid, Please login again to get the new token ").get()))));
			return;
		}
		// 未传递令牌且非登录请求
		if (!StringUtil.isNullOrEmpty(adoptToken) && !requestUri.equals(loginAction)  && (!requestUri.startsWith("/flchy/"))) {
			//判断 请求token是否恶意
			String md5Addr = MD5.encryptToHex(InternetProtocol.getRemoteAddr(WebUtils.toHttp(request)));
			if(!StringUtil.isNullOrEmpty(md5Addr)){
				if(!adoptToken.startsWith(md5Addr)){
					httpResponse.getWriter().write(JSON.toJSONString(new ResponseCommand(ResponseCommand.STATUS_ERROR, new VisitsMapResult(new NewMapUtil("message", "Request token is not invalid, Please login again to get the new token ").get()))));
					return;
				}
			}
			if(!tokenPrivilegeService.isAuthenticated(adoptToken)){
				httpResponse.getWriter().write(JSON.toJSONString(new ResponseCommand(ResponseCommand.STATUS_ERROR, new VisitsMapResult(new NewMapUtil("message", "Request token is not invalid, Please login again to get the new token ").get()))));
				return;
			}

			if (null != allowedUrls && !java.util.Arrays.asList(allowedUrls).contains(requestUri) && (null != allowedUrls && !java.util.Arrays.asList(allowedUrls).contains(requestUri))) {
				if (!tokenPrivilegeService.isSuperAdmin(adoptToken)) {
					//httpRequest.getMethod() 获取请求类型  如get  post delete等
					boolean isAccessAllowed =this.isAccessAllowed(requestUri, adoptToken,httpRequest.getMethod());
					if (!isAccessAllowed) {
						httpResponse.getWriter().write(JSON.toJSONString(new ResponseCommand(ResponseCommand.STATUS_AUTHORITY_ERROR, new VisitsMapResult(new NewMapUtil("message", "Request address not authorized, Please contact the administrator ").get()))));
						return;
					}
				}
			}
		}
		
		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
			System.out.println("请求访问权限过滤器初始化");
	}
	/**
	 * 获取全局受信任请求地址
	 */
	@SuppressWarnings("unused")
	private static void getAllowedUrls(){
		String allowedUrl = PropertiesHolder.getProperty("access.allowed");
		if(!StringUtil.isNullOrEmpty(allowedUrl)){
			allowedUrls = allowedUrl.split(";");
		}
	}
	
	private boolean isAccessAllowed(String requestUri, String adoptToken, String method) {
		// 校验Redis客户端
		if (null == redisBusines) {
			return false;
		}
		// 获取令牌缓存数据
		byte[] bytesInfo = null;
		try {
			bytesInfo = redisBusines.get(adoptToken.getBytes("utf-8"));
			if (StringUtil.isNullOrEmpty(bytesInfo)) {
				return false;
			}
		} catch (UnsupportedEncodingException e) {
			logger.error("Failed to get redis redis , error message is" + e.getMessage() + " ;Please contact the administrator");
			return false;
		}
		// 数据转Bean对象
		BaseUser baseUser = (BaseUser) BeanUtil.byteToObject(bytesInfo);
		if (null == baseUser) {
			return false;
		}
		// 校验令牌用户权限
		Set<ConfUrlBean> urlPermis = baseUser.getUrlPermis();
		if (null == urlPermis) {
			return false;
		}
		boolean isAccessAllowed = true;
		Pattern pattern = Pattern.compile("(?<=\\{)(.+?)(?=\\})");
		List<ConfUrlBean> containsList = urlPermis.stream().filter(l->{
				if(l.getMethod()==null ){
					if(l.getUrlPath().equals(requestUri)){
						return true;
					}
				}else{
					if(l.getMethod().equals(method) && l.getUrlPath().equals(requestUri)){
						return true;
					}
					if(!l.getMethod().equals(method)){
						return false;
					}
				}
				//验证/abc/{id}这种形式
				String str=l.getUrlPath();
				List<String> ls=new ArrayList<String>();
				String expression = null;
				Matcher matcher = pattern.matcher(str);
				while(matcher.find()){
				ls.add(matcher.group());
				}
				if(ls.size()<1){
					return false;
				}
				for (String string : ls) {
					expression=	str.replace("{"+string+"}", "(\\S+)");
				}
				 return Pattern.compile(expression).matcher(requestUri).matches();
		}).collect(Collectors.toList());
		
		if (containsList.size()<1) {
			isAccessAllowed = false;
		}
		return isAccessAllowed;

	}
	
	//验证/abc/{id}这种形式
	public boolean isAccessAllowedRestful(Set<String> urlPermis ,String requestUri){
		Pattern pattern = Pattern.compile("(?<=\\{)(.+?)(?=\\})");
		List<String> collect = urlPermis.stream().filter(str->{
			List<String> ls=new ArrayList<String>();
			String expression = null;
			Matcher matcher = pattern.matcher(str);
			while(matcher.find()){
			ls.add(matcher.group());
			}
			if(ls.size()<1){
				return false;
			}
			for (String string : ls) {
				expression=	str.replace("{"+string+"}", "(\\S+)");
			}
			 return Pattern.compile(expression).matcher(requestUri).matches();
			}).collect(Collectors.toList());
		
		return collect.size()>0;
	}
	

}
