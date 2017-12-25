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

import com.flchy.blog.base.response.ResponseCommand;
import com.flchy.blog.base.response.VisitsListResult;
import com.flchy.blog.base.response.VisitsMapResult;
import com.flchy.blog.privilege.config.bean.BaseUser;
import com.flchy.blog.privilege.config.entity.BaseMapRolePanelEntity;
import com.flchy.blog.privilege.config.service.IBaseMapRolePanelService;
import com.flchy.blog.privilege.enums.StatusEnum;
import com.flchy.blog.privilege.support.ITokenPrivilegeService;
import com.flchy.blog.utils.NewMapUtil;

/**
 * 角色面板资源映射
 * 
 * @author nieqs
 *
 */
@RestController
@RequestMapping("/rolePanel")
public class BaseMapRolePanelController {
	@Autowired
	private ITokenPrivilegeService tokenPrivilegeService;
	@Autowired
	private IBaseMapRolePanelService iBaseMapRolePanelService;

	
	/**
	 * 查询查询已选
	 * 
	 * @return
	 */
	@PostMapping("queryBySelected")
	public Object query( @RequestParam(value = "roleId", required = false) Integer roleId,@RequestParam(value = "menuId", required = false) Integer menuId) {
		if(roleId==null){
			return new ResponseCommand(ResponseCommand.STATUS_ERROR, new VisitsMapResult(new NewMapUtil("message", "roleId can't be empty ！").get()));
		}
		Integer[] roleIds=new Integer[1];
		roleIds[0]=roleId;
		List<BaseMapRolePanelEntity> selectRolePanelBySelected = iBaseMapRolePanelService.selectRolePanelBySelected(roleId, menuId);
		return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, new VisitsListResult(selectRolePanelBySelected));
	}
	
	
	/**
	 * 添加
	 * @param baseMapRolePrivEntity
	 * @param adoptToken
	 * @param request
	 * @return
	 */
	@PostMapping
	public Object insert(BaseMapRolePanelEntity baseMapRolePanelEntity,@RequestParam(value = "adoptToken", required = false) String adoptToken,   HttpServletRequest request){
		baseMapRolePanelEntity.setCreateTime(new Date());
		BaseUser currentUser = tokenPrivilegeService.getCurrentUser(adoptToken);
		baseMapRolePanelEntity.setCreateUser(currentUser.getUserName());
		baseMapRolePanelEntity.setStatus(StatusEnum.NORMAL.getValues());
		boolean insertRole = iBaseMapRolePanelService.insert(baseMapRolePanelEntity);
		if(insertRole){
			return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, insertRole);
		}
		return new ResponseCommand(ResponseCommand.STATUS_ERROR,
				new VisitsMapResult(new NewMapUtil("message", "Add failure！").get()));
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
		return iBaseMapRolePanelService.delete(id);
	}
	/**
	 * 修改角色面板权限
	 * @param roleId 角色ID
	 * @param menuId 菜单ID
	 * @param panelId 面板数组
	 * @return
	 */
	@PutMapping("updateRolePanel")
	public Object updateRolePanel(@RequestParam(value = "adoptToken", required = false) String adoptToken,@RequestParam(value = "roleId", required = true)Integer roleId,@RequestParam(value = "menuId", required = true) Integer menuId,@RequestParam(value = "panelId", required = true) Integer[] panelId){
		BaseUser currentUser = tokenPrivilegeService.getCurrentUser(adoptToken);
		System.out.println(panelId);
		return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, iBaseMapRolePanelService.updateRolePanel(roleId, menuId, panelId, currentUser.getUserName()));
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
	public Object insertCodeBatch(@RequestParam(value = "panelId", required = true)Integer[] panelId,@RequestParam(value = "roleId", required = true)Integer roleId,@RequestParam(value = "adoptToken", required = false) String adoptToken,   HttpServletRequest request){
			if(panelId==null || panelId.length<1){
				return new ResponseCommand(ResponseCommand.STATUS_ERROR,
						new VisitsMapResult(new NewMapUtil("message", "urlId can't be empty!").get()));
			}
			if(roleId==null){
				return new ResponseCommand(ResponseCommand.STATUS_ERROR,
						new VisitsMapResult(new NewMapUtil("message", "privVisitId can't be empty!").get()));
			}
		BaseUser currentUser = tokenPrivilegeService.getCurrentUser(adoptToken);
		boolean insertRole = iBaseMapRolePanelService.insertCodeBatch(panelId, roleId, currentUser.getUserName());
		return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, insertRole);
	}
}
