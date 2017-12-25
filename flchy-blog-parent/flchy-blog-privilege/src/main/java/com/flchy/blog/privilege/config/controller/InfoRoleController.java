/**
 * 
 */
package com.flchy.blog.privilege.config.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.flchy.blog.base.response.PageHelperResult;
import com.flchy.blog.base.response.ResponseCommand;
import com.flchy.blog.base.response.VisitsMapResult;
import com.flchy.blog.privilege.config.bean.BaseUser;
import com.flchy.blog.privilege.config.entity.BaseInfoRoleEntity;
import com.flchy.blog.privilege.config.service.IBaseInfoRoleService;
import com.flchy.blog.privilege.enums.StatusEnum;
import com.flchy.blog.privilege.support.ITokenPrivilegeService;
import com.flchy.blog.utils.CnToSpellUtils;
import com.flchy.blog.utils.NewMapUtil;

/**
 * 角色管理
 * 
 * @author nieqs
 *
 */
@RestController
@RequestMapping("/role")
public class InfoRoleController {

	@Autowired
	private IBaseInfoRoleService iBaseInfoRoleService;
	@Autowired
	private ITokenPrivilegeService tokenPrivilegeService;
	/**
	 * 角色分页查询
	 * 
	 * @param pageSize
	 * @param currentPage
	 * @param baseInfoUser
	 * @return
	 */
	@PostMapping("/page")
	public Object userPage(@RequestParam(value = "pageSize", required = true) int pageSize, @RequestParam(value = "currentPage", required = true) int currentPage,BaseInfoRoleEntity infoRoleEntity) {
		PageHelperResult roles = iBaseInfoRoleService.selectRoles(pageSize, currentPage, infoRoleEntity);
		return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, roles);
	}

	
	/**
	 * 添加Role
	 * @param baseInfoUser
	 * @param adoptToken
	 * @param request
	 * @return
	 */
	@PostMapping
	public Object insertRole(BaseInfoRoleEntity infoRoleEntity,@RequestParam(value = "adoptToken", required = false) String adoptToken,   HttpServletRequest request){
		infoRoleEntity.setCreateTime(new Date());
		infoRoleEntity.setRoleNameQp(CnToSpellUtils.converterToSpell(infoRoleEntity.getRoleName()));
		BaseUser currentUser = tokenPrivilegeService.getCurrentUser(adoptToken);
		infoRoleEntity.setCreateUser(currentUser.getUserName());
		infoRoleEntity.setStatus(StatusEnum.NORMAL.getValues());
		boolean insertRole = iBaseInfoRoleService.insertRole(infoRoleEntity);
		if(insertRole){
			return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, insertRole);
		}
		return new ResponseCommand(ResponseCommand.STATUS_ERROR,
				new VisitsMapResult(new NewMapUtil("message", "Add failure！").get()));
	}
	
	
	/**
	 * 修改角色
	 * @param baseInfoUser
	 * @param adoptToken
	 * @param request
	 * @return
	 */
	@PutMapping
	public Object updateRole(BaseInfoRoleEntity infoRoleEntity,@RequestParam(value = "adoptToken", required = false) String adoptToken,   HttpServletRequest request){
		if(infoRoleEntity.getRoleId()==0){
			return new ResponseCommand(ResponseCommand.STATUS_ERROR,
					new VisitsMapResult(new NewMapUtil("message", "roleId can't be empty!").get()));
		}
		infoRoleEntity.setRoleNameQp(CnToSpellUtils.converterToSpell(infoRoleEntity.getRoleName()));
		BaseUser currentUser = tokenPrivilegeService.getCurrentUser(adoptToken);
		infoRoleEntity.setUpdateUser(currentUser.getUserName());
		boolean updateRole = iBaseInfoRoleService.updateRole(infoRoleEntity);
		if(updateRole){
			return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, updateRole);
		}
		return new ResponseCommand(ResponseCommand.STATUS_ERROR,
				new VisitsMapResult(new NewMapUtil("message", "Update failure！").get()));
	}
	
	/**
	 * 删除角色
	 * @param roleId 角色ID 示例： 1,2,3
	 * @param adoptToken 
	 * @param request
	 * @return
	 */
	@DeleteMapping
	public Object delRole(String roleId,@RequestParam(value = "adoptToken", required = false) String adoptToken,   HttpServletRequest request){
		if(roleId==null){
			return new ResponseCommand(ResponseCommand.STATUS_ERROR,
					new VisitsMapResult(new NewMapUtil("message", "UserId can't be empty!").get()));
		}
		return  iBaseInfoRoleService.deleteRole(roleId);
	}
	@GetMapping("getRole")
	public Object getRole(@RequestParam(value = "roleId", required = true) Integer roleId){
		if(roleId==null){
			return new ResponseCommand(ResponseCommand.STATUS_ERROR,
					new VisitsMapResult(new NewMapUtil("message", "UserId can't be empty!").get()));
		}
		return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, iBaseInfoRoleService.getRole(roleId));
	}
}
