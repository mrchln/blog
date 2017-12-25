package com.flchy.blog.privilege.config.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.flchy.blog.base.response.ResponseCommand;
import com.flchy.blog.base.response.VisitsListResult;
import com.flchy.blog.base.response.VisitsMapResult;
import com.flchy.blog.privilege.config.bean.BaseUser;
import com.flchy.blog.privilege.config.entity.BaseMapPrivUrlEntity;
import com.flchy.blog.privilege.config.service.IBaseMapPrivUrlService;
import com.flchy.blog.privilege.enums.StatusEnum;
import com.flchy.blog.privilege.support.ITokenPrivilegeService;
import com.flchy.blog.utils.NewMapUtil;
/**
 * 访问权限与url映射关系表，为每个菜单下所有url,需要配置，不配置则无法访问
 * @author nieqs
 *
 */
@RestController
@RequestMapping("/privUrl")
public class BaseMapPrivUrlController {
	@Autowired
	private ITokenPrivilegeService tokenPrivilegeService;
	@Autowired
	private IBaseMapPrivUrlService iBaseMapPrivUrlService;
	
	/**
	 * 查询已选择
	 * @param privVisitId 访问权限表ID 
	 * @return
	 */
	@PostMapping("queryBySelected")
	public Object query(@RequestParam(value = "privVisitId", required = false) String privVisitId) {
		if (privVisitId == null) {
			return new ResponseCommand(ResponseCommand.STATUS_ERROR, new VisitsMapResult(new NewMapUtil("message", "roleId can't be empty ！").get()));
		}
		 List<BaseMapPrivUrlEntity> selectPrivUrlMapBySelected = iBaseMapPrivUrlService.selectPrivUrlMapBySelected(privVisitId);
		return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, new VisitsListResult(selectPrivUrlMapBySelected));
	}

	/**
	 * 添加
	 * @param baseMapRoleDimAuthEntity
	 * @param adoptToken
	 * @param request
	 * @return
	 */
	@PostMapping
	public Object insert(BaseMapPrivUrlEntity baseMapPrivUrlEntity, @RequestParam(value = "adoptToken", required = false) String adoptToken, HttpServletRequest request) {
		baseMapPrivUrlEntity.setCreateTime(new Date());
		BaseUser currentUser = tokenPrivilegeService.getCurrentUser(adoptToken);
		baseMapPrivUrlEntity.setCreateUser(currentUser.getUserName());
		baseMapPrivUrlEntity.setStatus(StatusEnum.NORMAL.getValues());
		boolean insert = iBaseMapPrivUrlService.insert(baseMapPrivUrlEntity);
		if (insert) {
			return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, insert);
		}
		return new ResponseCommand(ResponseCommand.STATUS_ERROR, new VisitsMapResult(new NewMapUtil("message", "Add failure！").get()));
	}

	/**
	 * 删除
	 * @param id
	 * @param adoptToken
	 * @param request
	 * @return
	 */
	@DeleteMapping
	public Object del(String id, @RequestParam(value = "adoptToken", required = false) String adoptToken, HttpServletRequest request) {
		if (id == null) {
			return new ResponseCommand(ResponseCommand.STATUS_ERROR, new VisitsMapResult(new NewMapUtil("message", "id can't be empty!").get()));
		}
		return iBaseMapPrivUrlService.delete(id);
	}


	/**
	 * 批量添加
	 * @param userId 用户ID 数组 1，2，3
	 * @param roleId 角色ID
	 * @param adoptToken
	 * @param request
	 * @return
	 */
	@PostMapping("insertCodeBatch")
	public Object insertCodeBatch(@RequestParam(value = "urlId", required = true)Integer[] urlId,@RequestParam(value = "privVisitId", required = true)String privVisitId,@RequestParam(value = "adoptToken", required = false) String adoptToken,   HttpServletRequest request){
			if(urlId==null || urlId.length<1){
				return new ResponseCommand(ResponseCommand.STATUS_ERROR,
						new VisitsMapResult(new NewMapUtil("message", "urlId can't be empty!").get()));
			}
			if(privVisitId==null){
				return new ResponseCommand(ResponseCommand.STATUS_ERROR,
						new VisitsMapResult(new NewMapUtil("message", "privVisitId can't be empty!").get()));
			}
		BaseUser currentUser = tokenPrivilegeService.getCurrentUser(adoptToken);
		boolean insertRole = iBaseMapPrivUrlService.insertCodeBatch(urlId, privVisitId, currentUser.getUserName());
		return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, insertRole);
	}
	
}
