package com.flchy.blog.privilege.config.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.flchy.blog.base.response.PageHelperResult;
import com.flchy.blog.base.response.ResponseCommand;
import com.flchy.blog.base.response.VisitsListResult;
import com.flchy.blog.base.response.VisitsMapResult;
import com.flchy.blog.privilege.config.entity.BaseConfUrlEntity;
import com.flchy.blog.privilege.config.service.IBaseConfUrlService;
import com.flchy.blog.utils.NewMapUtil;

/**
 * 访问Url路径
 * 
 * @author nieqs
 *
 */
@RestController
@RequestMapping("/confUrl")
public class BaseConfUrlController {
	@Autowired
	private IBaseConfUrlService iBaseConfUrlService;
	
	/**
	 * 分页查询
	 * 
	 * @param pageSize
	 * @param currentPage
	 * @return
	 */
	@PostMapping("/page")
	public Object userPage(@RequestParam(value = "pageSize", required = true) int pageSize, @RequestParam(value = "currentPage", required = true) int currentPage, BaseConfUrlEntity baseConfUrlEntity) {
		PageHelperResult selectPage = iBaseConfUrlService.selectPage(pageSize, currentPage, baseConfUrlEntity);
		return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, selectPage);
	}
	
	/**
	 * 删除Url路径
	 * @param urlId urlID
	 * @param adoptToken
	 * @param request
	 * @return
	 */  
	@DeleteMapping
	public Object delRole(String urlId,@RequestParam(value = "adoptToken", required = false) String adoptToken,   HttpServletRequest request){
		if(urlId==null){
			return new ResponseCommand(ResponseCommand.STATUS_ERROR,
					new VisitsMapResult(new NewMapUtil("message", "urlId can't be empty!").get()));
		}
		return  iBaseConfUrlService.deleteUrl(urlId);
	}
	
	/**
	 * 通过菜单ID查询url已选
	 * @param pageSize
	 * @param currentPage
	 * @param privVisitId
	 * @param urlPath
	 * @return
	 */
	@PostMapping("/queryUrlSelected")
	public Object queryUrlSelected(@RequestParam(value = "pageSize", required = true) int pageSize, @RequestParam(value = "currentPage", required = true) int currentPage,@RequestParam(value = "privVisitId", required = true)String privVisitId, @RequestParam(value = "urlPath", required = false)String urlPath) {
		PageHelperResult selectPage = iBaseConfUrlService.selectUrlSelected(privVisitId, urlPath, pageSize, currentPage);
		return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, selectPage);
	}
	/**
	 * 通过菜单ID查询url未选
	 * @param pageSize
	 * @param currentPage
	 * @param privVisitId
	 * @param urlPath
	 * @return
	 */
	@PostMapping("/queryUrlNotSelected")
	public Object queryUrlNotSelected(@RequestParam(value = "privVisitId", required = true)String privVisitId, @RequestParam(value = "urlPath", required = false)String urlPath) {
		List<BaseConfUrlEntity> selectPage = iBaseConfUrlService.selectUrlNotSelected(privVisitId, urlPath);
		return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, new VisitsListResult(selectPage));
	}

}
