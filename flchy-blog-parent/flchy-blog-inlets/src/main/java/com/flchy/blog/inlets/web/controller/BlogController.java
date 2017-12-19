package com.flchy.blog.inlets.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.QueryParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.flchy.blog.base.annotation.Log;
import com.flchy.blog.base.enums.OperateCodeEnum;
import com.flchy.blog.base.exception.BusinessException;
import com.flchy.blog.base.response.ResponseCommand;
import com.flchy.blog.base.response.ResultPage;
import com.flchy.blog.inlets.config.Sample;
import com.flchy.blog.inlets.enums.Keys;
import com.flchy.blog.inlets.enums.StatusEnum;
import com.flchy.blog.inlets.holder.ArticleTypeHolder;
import com.flchy.blog.inlets.holder.ConfigHolder;
import com.flchy.blog.inlets.service.IArticleService;
import com.flchy.blog.inlets.service.ICommentService;
import com.flchy.blog.inlets.service.ILinkService;
import com.flchy.blog.pojo.Article;
import com.flchy.blog.pojo.ArticleType;
import com.flchy.blog.pojo.Comment;
import com.flchy.blog.pojo.Link;
import com.flchy.blog.utils.ip.InternetProtocol;

import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;

/**
 * 
 * @author 1st
 *
 */
@RestController
@RequestMapping("flchy")
public class BlogController {
	private static Logger logger = LoggerFactory.getLogger(BlogController.class);
	
	@Autowired
	private IArticleService iArticleService;

	@Autowired
	private ILinkService iLinkService;
	@Autowired
	private ICommentService iCommentService;
	@Autowired
	private Sample sample;
	@Log(value="查询文章分页",type=OperateCodeEnum.PUBLIC)
	@PostMapping(value = "/article/page")
	public Object selectArticlePage(@RequestParam(value = "current", required = true) Integer current,
			@RequestParam(value = "size", required = true) Integer size,@RequestParam(value = "order", required =false) Integer order, Article article) {
//		System.out.println(100/0);
		Integer typeId = article.getTypeId();
		article = new Article();
		article.setTypeId(typeId);
		article.setStatus(StatusEnum.NORMAL.getCode());
		article.setOrder(order);
		Page<Article> page = new Page<>(Integer.valueOf(current), Integer.valueOf(size));
//		iArticleService.selectPage(page,
//				new EntityWrapper<Article>(article).where(" status={0} ", StatusEnum.NORMAL.getCode()));
		iArticleService.selectArticlePage(page, article);
		return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, new ResultPage(page));
	}

	@Log(value="查询文章",type=OperateCodeEnum.PUBLIC)
	@GetMapping(value = "/article/{id}")
	public Object selectArticleKey(@PathVariable Integer id) {
		Article article = iArticleService.selectById(id);
		if (article == null) {
			throw new BusinessException("isNull");
		}
		if (article.getStatus() != StatusEnum.NORMAL.getCode()) {
			throw new BusinessException("未找到文章！");
		}
		new Thread(new Runnable() {
			@Override
			public void run() {
				Article updateArticle=new  Article();
				updateArticle.setId(article.getId());
				updateArticle.setSee(article.getSee() + 1);
				updateArticle.updateById();
			}
		}).start();
		return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, article);
	}
	
	@Log(value="查询关于我",type=OperateCodeEnum.PUBLIC)
	@GetMapping(value = "/about")
	public Object selectAbout() {
		Article article = iArticleService.selectById(-1);
		new Thread(new Runnable() {
			@Override
			public void run() {
				article.setSee(article.getSee() + 1);
				article.updateById();
			}
		}).start();
		return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, article);
	}
	@Log(value="查询文章类型",type=OperateCodeEnum.PUBLIC)
	@GetMapping("/articleType")
	public Object selectarticleType(@QueryParam("id") Integer id) {
		List<ArticleType> response = ArticleTypeHolder.getArticleType(id);
		return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, response);
	}
	@Log(value="查询友情链接",type=OperateCodeEnum.PUBLIC)
	@GetMapping(value = "/link")
	public Object selectLink(@QueryParam("id") Integer id) {
		if (id != null) {
			return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, iLinkService.selectById(id));
		}
		List<Link> response = iLinkService
				.selectList(new EntityWrapper<Link>().where("status={0}", StatusEnum.NORMAL.getCode()));
		return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, response);
	}
	@Log(value="查询评论",type=OperateCodeEnum.PUBLIC)
	@PostMapping(value = "/comment/page")
	public Object selectWebCommentPage(@RequestParam(value = "current", required = true) Integer current,
			@RequestParam(value = "size", required = true) Integer size, Integer articleId,@RequestParam(value = "nickName", required = false) String nickName) {
		ResultPage selectWebComment = iCommentService.selectWebComment(articleId, current, size,nickName);
		return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, selectWebComment);
	}
	@Log(value="评论 文章",type=OperateCodeEnum.PUBLIC)
	@PostMapping(value = "/comment")
	public Object insertComment(@ModelAttribute Comment comment, HttpServletRequest request) {
		
		String ua = request.getHeader("User-Agent");
		// 转成UserAgent对象
		UserAgent userAgent = UserAgent.parseUserAgentString(ua);
		// 获取浏览器信息
		Browser browser = userAgent.getBrowser();
		// 获取系统信息
		OperatingSystem os = userAgent.getOperatingSystem();
		// 系统名称
		String system = os.getName();
		// 浏览器名称
		String browserName = browser.getName();
		comment.setIsAdmin(false);
		comment.setUa(ua);
		comment.setBrowserName(browserName);
		comment.setOsName(system);
		comment.setClientIp(InternetProtocol.getRemoteAddr(request));
		Comment saveComment = iCommentService.saveComment(comment);
		return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, saveComment);
	}
	
	@Log(value="测试",type=OperateCodeEnum.PUBLIC)
	@GetMapping(value = "/verific")
	public Object verific(HttpServletRequest request) {
		return sample.getVerific().getData();
	}
	@Log(value="查询最新评论",type=OperateCodeEnum.PUBLIC)
	@GetMapping(value = "/getNewestComment")
	public Object getNewestComment(){
		Page<Comment> page = new Page<>(1, 10);
		iCommentService.selectPage(page,new EntityWrapper<Comment>().where(" status = {0}", StatusEnum.NORMAL.getCode()).orderBy("create_time",false));
		return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, new ResultPage(page));
	}
	@Log(value="查询Blog配置",type=OperateCodeEnum.PUBLIC)
	@GetMapping(value = "/getBlogConfig")
	public Object getBlogConfig(){
		return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, JSON.parseArray(ConfigHolder.getConfig(Keys.BLOG_CONFIG.getKey())));
	}
	

}
