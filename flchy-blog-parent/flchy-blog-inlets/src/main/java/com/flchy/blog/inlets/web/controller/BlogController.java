package com.flchy.blog.inlets.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.flchy.blog.base.exception.BusinessException;
import com.flchy.blog.base.response.ResponseCommand;
import com.flchy.blog.base.response.ResultPage;
import com.flchy.blog.inlets.config.Sample;
import com.flchy.blog.inlets.enums.StatusEnum;
import com.flchy.blog.inlets.holder.ArticleTypeHolder;
import com.flchy.blog.inlets.service.IArticleService;
import com.flchy.blog.inlets.service.ICommentService;
import com.flchy.blog.inlets.service.ILinkService;
import com.flchy.blog.pojo.Article;
import com.flchy.blog.pojo.ArticleType;
import com.flchy.blog.pojo.Comment;
import com.flchy.blog.pojo.Link;

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
	@Autowired
	private IArticleService iArticleService;

	@Autowired
	private ILinkService iLinkService;
	@Autowired
	private ICommentService iCommentService;
	@Autowired
	private Sample sample;

	@PostMapping(value = "/article/page")
	public Object selectArticlePage(@RequestParam(value = "current", required = true) Integer current,
			@RequestParam(value = "size", required = true) Integer size, Article article) {
		Integer typeId = article.getTypeId();
		article = new Article();
		article.setTypeId(typeId);
		Page<Article> page = new Page<>(Integer.valueOf(current), Integer.valueOf(size));
		iArticleService.selectPage(page,
				new EntityWrapper<Article>(article).where("status={0}", StatusEnum.NORMAL.getCode()));
		return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, new ResultPage(page));
	}

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
				article.setSee(article.getSee() + 1);
				article.updateById();
			}
		}).start();
		return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, article);
	}

	@GetMapping("/articleType")
	public Object selectarticleType(@QueryParam("id") Integer id) {
		List<ArticleType> response = ArticleTypeHolder.getArticleType(id);
		return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, response);
	}

	@GetMapping(value = "/link")
	public Object selectLink(@QueryParam("id") Integer id) {
		if (id != null) {
			return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, iLinkService.selectById(id));
		}
		List<Link> response = iLinkService
				.selectList(new EntityWrapper<Link>().where("status={0}", StatusEnum.NORMAL.getCode()));
		return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, response);
	}

	@PostMapping(value = "/comment/page")
	public Object selectWebCommentPage(@RequestParam(value = "current", required = true) Integer current,
			@RequestParam(value = "size", required = true) Integer size, Integer articleId) {
		ResultPage selectWebComment = iCommentService.selectWebComment(articleId, current, size);
		return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, selectWebComment);
	}

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
		System.out.println(system);
		System.out.println(browserName);
		Comment saveComment = iCommentService.saveComment(comment);
		return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, saveComment);
	}

	@GetMapping(value = "/verific")
	public Object verific(HttpServletRequest request) {
		return sample.getVerific().getData();
	}

}
