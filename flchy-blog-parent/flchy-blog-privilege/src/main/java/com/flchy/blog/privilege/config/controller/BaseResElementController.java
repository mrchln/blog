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
import com.flchy.blog.privilege.config.entity.BaseResElementEntity;
import com.flchy.blog.privilege.config.service.IBaseResElementService;
import com.flchy.blog.privilege.enums.StatusEnum;
import com.flchy.blog.privilege.support.ITokenPrivilegeService;
import com.flchy.blog.utils.NewMapUtil;

/**
 * 元素权限，用于角色赋权
 * 
 * @author nieqs
 *
 */
@RestController
@RequestMapping("/element")
public class BaseResElementController {
	@Autowired
	private IBaseResElementService iBaseResElementService;
	@Autowired
	private ITokenPrivilegeService tokenPrivilegeService;

	/**
	 * 查询元素权限
	 * 
	 * @param pageSize
	 * @param currentPage
	 * @return
	 */
	@PostMapping("page")
	public Object queryPage(@RequestParam(value = "pageSize", required = true) int pageSize, @RequestParam(value = "currentPage", required = true) int currentPage, BaseResElementEntity baseResElementEntity) {
		if (baseResElementEntity.getMenuId() == 0) {
			return new ResponseCommand(ResponseCommand.STATUS_ERROR, new VisitsMapResult(new NewMapUtil("message", "menuId can't be empty ！").get()));
		}
		PageHelperResult selectResElement = iBaseResElementService.selectResElement(pageSize, currentPage, baseResElementEntity);
		return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, selectResElement);
	}
	
	/**
	 * 查询元素权限
	 * 
	 * @param pageSize
	 * @param currentPage
	 * @return
	 */
	@PostMapping("query")
	public Object query( BaseResElementEntity baseResElementEntity) {
		if (baseResElementEntity.getMenuId() == 0) {
			return new ResponseCommand(ResponseCommand.STATUS_ERROR, new VisitsMapResult(new NewMapUtil("message", "menuId can't be empty ！").get()));
		}
		List<BaseResElementEntity> selectResElement = iBaseResElementService.selectResElement( baseResElementEntity);
		return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, new VisitsListResult(selectResElement));
	}

	/**
	 * 添加
	 * 
	 * @param baseResPanelEntity
	 * @param adoptToken
	 * @param request
	 * @return
	 */
	@PostMapping
	public Object insert(BaseResElementEntity baseResElementEntity, @RequestParam(value = "adoptToken", required = false) String adoptToken, HttpServletRequest request) {
		if (baseResElementEntity.getMenuId() == 0) {
			return new ResponseCommand(ResponseCommand.STATUS_ERROR, new VisitsMapResult(new NewMapUtil("message", "menuId can't be empty ！").get()));
		}
		baseResElementEntity.setCreateTime(new Date());
		BaseUser currentUser = tokenPrivilegeService.getCurrentUser(adoptToken);
		baseResElementEntity.setCreateUser(currentUser.getUserName());
		baseResElementEntity.setStatus(StatusEnum.NORMAL.getValues());
		boolean insert = iBaseResElementService.insert(baseResElementEntity);
		if (insert) {
			return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, insert);
		}
		return new ResponseCommand(ResponseCommand.STATUS_ERROR, new VisitsMapResult(new NewMapUtil("message", "Add failure！").get()));
	}

	/**
	 * 修改
	 * 
	 * @param baseInfoUser
	 * @param adoptToken
	 * @param request
	 * @return
	 */
	@PutMapping
	public Object update(BaseResElementEntity baseResElementEntity, @RequestParam(value = "adoptToken", required = false) String adoptToken, HttpServletRequest request) {
		if (baseResElementEntity.getElementId() == 0) {
			return new ResponseCommand(ResponseCommand.STATUS_ERROR, new VisitsMapResult(new NewMapUtil("message", "elementId can't be empty!").get()));
		}
		BaseUser currentUser = tokenPrivilegeService.getCurrentUser(adoptToken);
		baseResElementEntity.setUpdateUser(currentUser.getUserName());
		boolean update = iBaseResElementService.update(baseResElementEntity);
		if (update) {
			return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, update);
		}
		return new ResponseCommand(ResponseCommand.STATUS_ERROR, new VisitsMapResult(new NewMapUtil("message", "Update failure！").get()));
	}

	@DeleteMapping
	public Object delRole(String elementId, @RequestParam(value = "adoptToken", required = false) String adoptToken, HttpServletRequest request) {
		if (elementId == null) {
			return new ResponseCommand(ResponseCommand.STATUS_ERROR, new VisitsMapResult(new NewMapUtil("message", "elementId can't be empty!").get()));
		}
		return iBaseResElementService.delete(elementId);
	}

	/**
	 * 按用户ID 和菜单权限 查询元素权限
	 * 
	 * @param userId
	 * @param privVisitId
	 * @param panelId
	 * @param panelName
	 * @return
	 */
	@PostMapping("queryVisitElement")
	public Object selectVisitElement(@RequestParam(value = "userId", required = true) Integer userId, @RequestParam(value = "privVisitId", required = true) String privVisitId, @RequestParam(value = "elementId", required = false) Integer elementId, @RequestParam(value = "panelId", required = false) Integer panelId, @RequestParam(value = "elementName", required = false) String elementName) {
		return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, new VisitsListResult(iBaseResElementService.selectVisitElement(userId, privVisitId, elementId,panelId, elementName)));
	}
	
	
	/**
	 * 按角色查询已选元素
	 * @param pageSize
	 * @param currentPage
	 * @param roleId
	 * @param menuId
	 * @param panelName
	 * @return
	 */
	@PostMapping("queryByRoleSelected")
	public Object selectByRoleSelected(@RequestParam(value = "pageSize", required = true) int pageSize, @RequestParam(value = "currentPage", required = true) int currentPage,@RequestParam(value = "roleId", required = true)Integer roleId, @RequestParam(value = "menuId", required = true)Integer menuId, @RequestParam(value = "panelId", required = false) Integer panelId, @RequestParam(value = "elementName", required = false) String elementName) {
		PageHelperResult selectResPanel = iBaseResElementService.selectByRoleSelected(pageSize, currentPage, roleId, menuId, panelId, elementName);
		return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, selectResPanel);
	}
	
	/**
	 * 按角色查询未选元素
	 * @param pageSize
	 * @param currentPage
	 * @param roleId
	 * @param menuId
	 * @param panelName
	 * @return
	 */
	@PostMapping("queryByRoleNotSelected")
	
	public Object selectByRoleNotSelected(@RequestParam(value = "roleId", required = true)Integer roleId, @RequestParam(value = "menuId", required = true)Integer menuId, @RequestParam(value = "panelId", required = false) Integer panelId, @RequestParam(value = "elementName", required = false) String elementName) {
	 List<BaseResElementEntity> selectByRoleNotSelected = iBaseResElementService.selectByRoleNotSelected(roleId, menuId, panelId, elementName);
		return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, new VisitsListResult(selectByRoleNotSelected));
	}

}
