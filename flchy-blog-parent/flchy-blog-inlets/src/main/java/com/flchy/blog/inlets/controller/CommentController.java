package com.flchy.blog.inlets.controller;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.flchy.blog.base.annotation.Log;
import com.flchy.blog.base.enums.OperateCodeEnum;
import com.flchy.blog.base.exception.BusinessException;
import com.flchy.blog.base.response.ResponseCommand;
import com.flchy.blog.base.response.ResultPage;
import com.flchy.blog.inlets.enums.Keys;
import com.flchy.blog.inlets.enums.StatusEnum;
import com.flchy.blog.inlets.holder.ConfigHolder;
import com.flchy.blog.inlets.service.ICommentService;
import com.flchy.blog.pojo.Comment;
import com.flchy.blog.utils.ip.InternetProtocol;

import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;

/**
 * <p>
 * 评论表 前端控制器
 * </p>
 *
 * @author nieqs
 * @since 2017-11-02
 */
@RestController
@RequestMapping("/comment")
public class CommentController {
	@Autowired
	private ICommentService iCommentService;
	
	@PostMapping(value = "/page")
	@Log(value="查询评论分页",type=OperateCodeEnum.SELECT)
	public Object selectArticlePage(@RequestParam(value = "current", required = true) Integer current,
			@RequestParam(value = "size", required = true) Integer size, Comment comment) {
		if(comment.getStatus()==null){
			comment.setStatus(StatusEnum.NORMAL.getCode());
		}
		Page<Map<String, Object>> page = new Page<>(Integer.valueOf(current), Integer.valueOf(size));
		iCommentService.selectCommentPage(page, comment);
//		iCommentService.selectPage(page, new EntityWrapper<Comment>(comment).orderBy("create_time", false));
		return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, new ResultPage(page));
	}

	@GetMapping(value="/{id}")
	@Log(value="通过ID查询评论",type=OperateCodeEnum.SELECT)
	public Object selectArticleKey(@PathVariable Integer id,@RequestParam(value = "token", required = false) String token) {
		Comment article = iCommentService.selectById(id);
		if (article == null) {
			throw new BusinessException("isNull");
		}
		return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, article);
	}
	
	

	/**
	 * 添加
	 * 
	 * @param article
	 * @return
	 */
	@PostMapping
	@Log(value="添加评论",type=OperateCodeEnum.INSERT)
	public Object insert(@ModelAttribute Comment comment) {
		if (comment == null) {
			throw new BusinessException("添加为空！");
		}
		comment.setCreateTime(new Date());
		boolean isok = iCommentService.insert(comment);
		if (!isok) {
			throw new BusinessException("Add failed");
		}
		return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, comment);
	}
	
	
	/**
	 * 回复
	 * 
	 * @param article
	 * @return
	 */
	@PostMapping("reply")
	@Log(value="回复评论",type=OperateCodeEnum.INSERT)
	public Object reply(@ModelAttribute Comment comment, HttpServletRequest request) {
		if (comment == null) {
			throw new BusinessException("添加为空！");
		}
		comment.setNickname(ConfigHolder.getConfig(Keys.ADMIN_NICK_NAME.getKey()));
		comment.setMail(ConfigHolder.getConfig(Keys.ADMIN_MAIL.getKey()));
		comment.setUrl(ConfigHolder.getConfig(Keys.WEBSITE_URL.getKey()));
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
		comment.setIsAdmin(true);
		comment.setUa(ua);
		comment.setBrowserName(browserName);
		comment.setOsName(system);
		comment.setClientIp(InternetProtocol.getRemoteAddr(request));
		Comment saveComment = iCommentService.saveComment(comment);
		return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, saveComment);
	}


	
	/**
	 * 修改
	 * 
	 * @param article
	 * @return
	 */
	@PutMapping
	@Log(value="修改评论",type=OperateCodeEnum.UPDATE)
	public Object update(Comment comment) {
		if (comment.getId() == null) {
			throw new BusinessException("ID must preach");
		}
		boolean isok = iCommentService.update(comment, new EntityWrapper<Comment>().where("id={0}", comment.getId()));
		if (!isok) {
			throw new BusinessException("Update failed");
		}
		return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, comment);
	}

	@DeleteMapping
	@Log(value="删除评论",type=OperateCodeEnum.DELETE)
	public Object delete(Comment comment) {
		Comment selectById = comment.selectById();
		if(selectById.getStatus()==StatusEnum.DELETE.getCode()){
			return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, selectById.deleteById());
		}
		Comment ar = new Comment();
		ar.setStatus(StatusEnum.DELETE.getCode());
		boolean isok = iCommentService.update(ar, new EntityWrapper<Comment>().where("id={0}", comment.getId()));
		if (!isok) {
			throw new BusinessException("Delete failed");
		}
		return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, comment);
	}
}
