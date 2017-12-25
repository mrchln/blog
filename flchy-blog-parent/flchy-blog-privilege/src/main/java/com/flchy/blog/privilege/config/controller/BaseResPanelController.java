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
import com.flchy.blog.privilege.config.entity.BaseResPanelEntity;
import com.flchy.blog.privilege.config.service.IBaseResPanelService;
import com.flchy.blog.privilege.enums.StatusEnum;
import com.flchy.blog.privilege.support.ITokenPrivilegeService;
import com.flchy.blog.utils.NewMapUtil;

/**
 * 面板权限，用于角色赋权
 * @author nieqs
 *
 */

@RestController
@RequestMapping("/resPanel")
public class BaseResPanelController {
	@Autowired
	private IBaseResPanelService iBaseResPanelService;
	@Autowired
	private ITokenPrivilegeService tokenPrivilegeService;
	
	/**
	 * 查询面板权限
	 * @param pageSize
	 * @param currentPage
	 * @return
	 */
	@PostMapping("page")
	public Object queryPage(@RequestParam(value = "pageSize", required = true) int pageSize, @RequestParam(value = "currentPage", required = true) int currentPage, BaseResPanelEntity baseResPanelEntity) {
		if(baseResPanelEntity.getMenuId()==0){
			return new ResponseCommand(ResponseCommand.STATUS_ERROR, new VisitsMapResult(new NewMapUtil("message", "menuId can't be empty ！").get()));
		}
		PageHelperResult selectResPanel = iBaseResPanelService.selectResPanel(pageSize, currentPage, baseResPanelEntity);
		return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, selectResPanel);
	}
	

	/**
	 * 查询面板权限
	 * @param pageSize
	 * @param currentPage
	 * @return
	 */
	@PostMapping("query")
	public Object query( BaseResPanelEntity baseResPanelEntity) {
		if(baseResPanelEntity.getMenuId()==0){
			return new ResponseCommand(ResponseCommand.STATUS_ERROR, new VisitsMapResult(new NewMapUtil("message", "menuId can't be empty ！").get()));
		}
		 List<BaseResPanelEntity> selectResPanel = iBaseResPanelService.selectResPanel( baseResPanelEntity);
		return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, new VisitsListResult(selectResPanel));
	}
	
	
	/**
	 * 添加
	 * @param baseResPanelEntity
	 * @param adoptToken
	 * @param request
	 * @return
	 */
	@PostMapping
	public Object insert(BaseResPanelEntity baseResPanelEntity,@RequestParam(value = "adoptToken", required = false) String adoptToken,   HttpServletRequest request){
		if(baseResPanelEntity.getMenuId()==0){
			return new ResponseCommand(ResponseCommand.STATUS_ERROR, new VisitsMapResult(new NewMapUtil("message", "menuId can't be empty ！").get()));
		}
		baseResPanelEntity.setCreateTime(new Date());
		BaseUser currentUser = tokenPrivilegeService.getCurrentUser(adoptToken);
		baseResPanelEntity.setCreateUser(currentUser.getUserName());
		baseResPanelEntity.setStatus(StatusEnum.NORMAL.getValues());
		boolean insert = iBaseResPanelService.insert(baseResPanelEntity);
		if(insert){
			return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, insert);
		}
		return new ResponseCommand(ResponseCommand.STATUS_ERROR,
				new VisitsMapResult(new NewMapUtil("message", "Add failure！").get()));
	}
	
	
	/**
	 * 修改
	 * @param baseInfoUser
	 * @param adoptToken
	 * @param request
	 * @return
	 */
	@PutMapping
	public Object update(BaseResPanelEntity baseResPanelEntity,@RequestParam(value = "adoptToken", required = false) String adoptToken,   HttpServletRequest request){
		if(baseResPanelEntity.getPanelId()==0){
			return new ResponseCommand(ResponseCommand.STATUS_ERROR,
					new VisitsMapResult(new NewMapUtil("message", "panelId can't be empty!").get()));
		}
		BaseUser currentUser = tokenPrivilegeService.getCurrentUser(adoptToken);
		baseResPanelEntity.setUpdateUser(currentUser.getUserName());
		boolean update= iBaseResPanelService.update(baseResPanelEntity);
		if(update){
			return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, update);
		}
		return new ResponseCommand(ResponseCommand.STATUS_ERROR,
				new VisitsMapResult(new NewMapUtil("message", "Update failure！").get()));
	}

	
	@DeleteMapping
	public Object delRole(String panelId,@RequestParam(value = "adoptToken", required = false) String adoptToken,   HttpServletRequest request){
		if(panelId==null){
			return new ResponseCommand(ResponseCommand.STATUS_ERROR,
					new VisitsMapResult(new NewMapUtil("message", "UserId can't be empty!").get()));
		}
		return  iBaseResPanelService.delete(panelId);
	}
	/**
	 * 按用户ID 和菜单权限 查询面板权限
	 * @param userId
	 * @param privVisitId
	 * @param panelId
	 * @param panelName
	 * @return
	 */
	@PostMapping("queryVisitPanel")
	public Object selectVisitPanel(@RequestParam(value = "userId", required = true) Integer userId,@RequestParam(value = "privVisitId", required = true)  String privVisitId,@RequestParam(value = "panelId", required = false) Integer panelId,@RequestParam(value = "panelName", required = false)String panelName){
		return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, new VisitsListResult(iBaseResPanelService.selectVisitPanel( userId, privVisitId, panelId, panelName)));
	}
	
	/**
	 * 按角色查询已选面板
	 * @param pageSize
	 * @param currentPage
	 * @param roleId
	 * @param menuId
	 * @param panelName
	 * @return
	 */
	@PostMapping("queryByRoleSelected")
	public Object selectByRoleSelected(@RequestParam(value = "pageSize", required = true) int pageSize, @RequestParam(value = "currentPage", required = true) int currentPage,@RequestParam(value = "roleId", required = true)Integer roleId, @RequestParam(value = "menuId", required = true)Integer menuId, @RequestParam(value = "panelName", required = false) String panelName) {
		PageHelperResult selectResPanel = iBaseResPanelService.selectByRoleSelected(pageSize, currentPage, roleId, menuId, panelName);
		return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, selectResPanel);
	}
	
	/**
	 * 按角色查询未选面板
	 * @param pageSize
	 * @param currentPage
	 * @param roleId
	 * @param menuId
	 * @param panelName
	 * @return
	 */
	@PostMapping("queryByRoleNotSelected")
	public Object selectByRoleNotSelected(@RequestParam(value = "roleId", required = true)Integer roleId, @RequestParam(value = "menuId", required = true)Integer menuId, @RequestParam(value = "panelName", required = false) String panelName) {
	 List<BaseResPanelEntity> selectByRoleNotSelected = iBaseResPanelService.selectByRoleNotSelected(roleId, menuId, panelName);
		return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, new VisitsListResult(selectByRoleNotSelected));
	}
}
