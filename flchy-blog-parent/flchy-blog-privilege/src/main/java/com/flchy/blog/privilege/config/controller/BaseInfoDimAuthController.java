package com.flchy.blog.privilege.config.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.flchy.blog.base.response.PageHelperResult;
import com.flchy.blog.base.response.ResponseCommand;
import com.flchy.blog.base.response.VisitsListResult;
import com.flchy.blog.base.response.VisitsMapResult;
import com.flchy.blog.privilege.config.bean.BaseUser;
import com.flchy.blog.privilege.config.entity.BaseInfoDimAuthEntity;
import com.flchy.blog.privilege.config.service.IBaseInfoDimAuthService;
import com.flchy.blog.privilege.enums.StatusEnum;
import com.flchy.blog.privilege.support.ITokenPrivilegeService;
import com.flchy.blog.utils.NewMapUtil;

/**
 * 用户数据权限，直接存储用户对应的数据权限
 * 
 * @author nieqs
 *
 */

@RestController
@RequestMapping("/dataauth")
public class BaseInfoDimAuthController {
	@Autowired
	private IBaseInfoDimAuthService iBaseInfoDimAuthService;
	@Autowired
	private ITokenPrivilegeService tokenPrivilegeService;

	/**
	 * 查询元素权限
	 * 
	 * @param pageSize
	 * @param currentPage
	 * @return
	 */
	@PostMapping("query")
	public Object userPage(@RequestParam(value = "pageSize", required = true) int pageSize, @RequestParam(value = "currentPage", required = true) int currentPage, BaseInfoDimAuthEntity baseInfoDimAuthEntity) {
		PageHelperResult selectDimAuth = iBaseInfoDimAuthService.selectDimAuth(pageSize, currentPage, baseInfoDimAuthEntity);
		return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, selectDimAuth);
	}

	/**
	 * 添加
	 * 
	 * @param adoptToken
	 * @param request
	 * @return
	 */
	@PostMapping
	public Object insert(BaseInfoDimAuthEntity baseInfoDimAuthEntity, @RequestParam(value = "adoptToken", required = false) String adoptToken, HttpServletRequest request) {
		baseInfoDimAuthEntity.setCreateTime(new Date());
		BaseUser currentUser = tokenPrivilegeService.getCurrentUser(adoptToken);
		baseInfoDimAuthEntity.setCreateUser(currentUser.getUserName());
		baseInfoDimAuthEntity.setStatus(StatusEnum.NORMAL.getValues());
		boolean insert = iBaseInfoDimAuthService.insert(baseInfoDimAuthEntity);
		if (insert) {
			return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, insert);
		}
		return new ResponseCommand(ResponseCommand.STATUS_ERROR, new VisitsMapResult(new NewMapUtil("message", "Add failure！").get()));
	}

	/**
	 * 修改
	 * 
	 * @param adoptToken
	 * @param request
	 * @return
	 */
	@PutMapping
	public Object update(BaseInfoDimAuthEntity baseInfoDimAuthEntity, @RequestParam(value = "adoptToken", required = false) String adoptToken, HttpServletRequest request) {
		if (baseInfoDimAuthEntity.getId() == null) {
			return new ResponseCommand(ResponseCommand.STATUS_ERROR, new VisitsMapResult(new NewMapUtil("message", "id can't be empty!").get()));
		}
		BaseUser currentUser = tokenPrivilegeService.getCurrentUser(adoptToken);
		baseInfoDimAuthEntity.setUpdateUser(currentUser.getUserName());
		boolean update = iBaseInfoDimAuthService.update(baseInfoDimAuthEntity);
		if (update) {
			return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, update);
		}
		return new ResponseCommand(ResponseCommand.STATUS_ERROR, new VisitsMapResult(new NewMapUtil("message", "Update failure！").get()));
	}

	@DeleteMapping
	public Object delRole(String id, @RequestParam(value = "adoptToken", required = false) String adoptToken, HttpServletRequest request) {
		if (id == null) {
			return new ResponseCommand(ResponseCommand.STATUS_ERROR, new VisitsMapResult(new NewMapUtil("message", "id can't be empty!").get()));
		}
		return iBaseInfoDimAuthService.delete(id);
	}

	/**
	 * 按用户id查询数据权限
	 * 
	 * @param userId
	 * @param remark
	 * @param pageSize
	 * @param currentPage
	 * @param adoptToken
	 * @return
	 */
	@PostMapping("/queryByUser")
	public Object selectByUser(@RequestParam(value = "userId", required = true) Integer userId, @RequestParam(value = "remark", required = false) String remark, @RequestParam(value = "pageSize", required = true) int pageSize, @RequestParam(value = "currentPage", required = true) int currentPage, @RequestParam(value = "adoptToken", required = false) String adoptToken) {
		PageHelperResult selectByUser = iBaseInfoDimAuthService.selectByUser(pageSize, currentPage, userId, remark);
		return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, selectByUser);
	}

	/**
	 * 按角色id查询数据权限
	 * 
	 * @param roleId
	 * @param remark
	 * @param pageSize
	 * @param currentPage
	 * @param adoptToken
	 * @return
	 */
	@PostMapping("/queryByRole")
	public Object selectByRole(@RequestParam(value = "roleId", required = true) Integer roleId, @RequestParam(value = "remark", required = false) String remark, @RequestParam(value = "pageSize", required = true) int pageSize, @RequestParam(value = "currentPage", required = true) int currentPage, @RequestParam(value = "adoptToken", required = false) String adoptToken) {
		PageHelperResult selectByUser = iBaseInfoDimAuthService.selectByRole(pageSize, currentPage, roleId, remark);
		return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, selectByUser);
	}

	/**
	 * 按角色id查询未选数据权限
	 * @param roleId
	 * @param remark
	 * @return
	 */
	@PostMapping("/queryByRoleNotSelected")
	public Object selectByRoleNotSelected(@RequestParam(value = "roleId", required = true) Integer roleId, @RequestParam(value = "remark", required = false) String remark) {
		List<BaseInfoDimAuthEntity> selectByRoleNotSelected = iBaseInfoDimAuthService.selectByRoleNotSelected(roleId, remark);
		return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, new VisitsListResult(selectByRoleNotSelected));
	}

}
