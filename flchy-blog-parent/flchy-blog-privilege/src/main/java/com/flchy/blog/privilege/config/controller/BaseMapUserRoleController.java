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
import com.flchy.blog.privilege.config.entity.BaseMapRoleUserEntity;
import com.flchy.blog.privilege.config.service.IBaseMapUserRoleService;
import com.flchy.blog.privilege.enums.StatusEnum;
import com.flchy.blog.privilege.support.ITokenPrivilegeService;
import com.flchy.blog.utils.NewMapUtil;
/**
 * 角色用户
 * @author nieqs
 *
 */
@RestController
@RequestMapping("/roleUser")
public class BaseMapUserRoleController {
	
	@Autowired
	private ITokenPrivilegeService tokenPrivilegeService;
	@Autowired
	private IBaseMapUserRoleService iBaseMapUserRoleService;
	/**
	 * 查询查询已选
	 * 
	 * @return
	 */
	@PostMapping("queryBySelected")
	public Object query( @RequestParam(value = "userId", required = false) Integer userId) {
		if(userId==null){
			return new ResponseCommand(ResponseCommand.STATUS_ERROR, new VisitsMapResult(new NewMapUtil("message", "userId can't be empty ！").get()));
		}
		List<BaseMapRoleUserEntity> selectRolesByUserId = iBaseMapUserRoleService.selectRolesByUserId(userId);
		return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, new VisitsListResult(selectRolesByUserId));
	}
	
	
	/**
	 * 添加
	 * @param baseMapRoleUserEntity
	 * @param adoptToken
	 * @param request
	 * @return
	 */
	@PostMapping
	public Object insert(BaseMapRoleUserEntity baseMapRoleUserEntity,@RequestParam(value = "adoptToken", required = false) String adoptToken,   HttpServletRequest request){
		baseMapRoleUserEntity.setCreateTime(new Date());
		BaseUser currentUser = tokenPrivilegeService.getCurrentUser(adoptToken);
		baseMapRoleUserEntity.setCreateUser(currentUser.getUserName());
		baseMapRoleUserEntity.setStatus(StatusEnum.NORMAL.getValues());
		boolean insertRole = iBaseMapUserRoleService.insert(baseMapRoleUserEntity);
		if(insertRole){
			return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, insertRole);
		}
		return new ResponseCommand(ResponseCommand.STATUS_ERROR,
				new VisitsMapResult(new NewMapUtil("message", "Add failure！").get()));
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
	public Object insertCodeBatch(@RequestParam(value = "userId", required = true)Integer[] userId,@RequestParam(value = "roleId", required = true)Integer roleId,@RequestParam(value = "adoptToken", required = false) String adoptToken,   HttpServletRequest request){
			if(userId==null || userId.length<1){
				return new ResponseCommand(ResponseCommand.STATUS_ERROR,
						new VisitsMapResult(new NewMapUtil("message", "userId can't be empty!").get()));
			}
			if(roleId==null){
				return new ResponseCommand(ResponseCommand.STATUS_ERROR,
						new VisitsMapResult(new NewMapUtil("message", "roleId can't be empty!").get()));
			}
		BaseUser currentUser = tokenPrivilegeService.getCurrentUser(adoptToken);
		boolean insertRole = iBaseMapUserRoleService.insertCodeBatch(userId, roleId, currentUser.getUserName());
		return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, insertRole);
	}
	
	/**
	 * 批量修改
	 * @param userId 用户ID 数组 1，2，3
	 * @param roleId 角色ID
	 * @param adoptToken
	 * @param request
	 * @return
	 */
	@PostMapping("updateCodeBatch")
	public Object updateCodeBatch(@RequestParam(value = "userId", required = true)Integer userId,@RequestParam(value = "roleId", required = false)Integer[] roleId,@RequestParam(value = "adoptToken", required = false) String adoptToken,   HttpServletRequest request){
//			if(roleId==null || roleId.length<1){
//				return new ResponseCommand(ResponseCommand.STATUS_ERROR,
//						new VisitsResult(new NewMapUtil("message", "roleId can't be empty!").get()));
//			}
			if(userId==null){
				return new ResponseCommand(ResponseCommand.STATUS_ERROR,
						new VisitsMapResult(new NewMapUtil("message", "userId can't be empty!").get()));
			}
		BaseUser currentUser = tokenPrivilegeService.getCurrentUser(adoptToken);
		boolean insertRole = iBaseMapUserRoleService.updateCodeBatch(userId, roleId, currentUser.getUserName());
		return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, insertRole);
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
		return iBaseMapUserRoleService.delete(id);
	}
	
	
	
	

}
