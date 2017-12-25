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
import com.flchy.blog.privilege.config.entity.BaseMapRolePrivEntity;
import com.flchy.blog.privilege.config.service.IBaseMapPrivRoleService;
import com.flchy.blog.privilege.enums.StatusEnum;
import com.flchy.blog.privilege.support.ITokenPrivilegeService;
import com.flchy.blog.utils.NewMapUtil;

/**
 *  角色访问权限映射表
 * @author nieqs
 *
 */
@RestController
@RequestMapping("/roleVisit")
public class BaseMapPrivRoleController {
	@Autowired
	private IBaseMapPrivRoleService iBaseMapPrivRoleService;
	@Autowired
	private ITokenPrivilegeService tokenPrivilegeService;
	/**
	 * 查询元素权限
	 * 
	 * @param pageSize
	 * @param currentPage
	 * @return
	 */
	@PostMapping("queryByroleId")
	public Object query( @RequestParam(value = "roleId", required = false) Integer roleId) {
		if(roleId==null){
			return new ResponseCommand(ResponseCommand.STATUS_ERROR, new VisitsMapResult(new NewMapUtil("message", "roleId can't be empty ！").get()));
		}
		Integer[] roleIds=new Integer[1];
		roleIds[0]=roleId;
		List<BaseMapRolePrivEntity> selectConfPrivByRoleIds = iBaseMapPrivRoleService.selectConfPrivByRoleIds(roleIds);
		return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, new VisitsListResult(selectConfPrivByRoleIds));
	}
	
	/**
	 * 添加
	 * @param baseMapRolePrivEntity
	 * @param adoptToken
	 * @param request
	 * @return
	 */
	@PostMapping
	public Object insert(BaseMapRolePrivEntity baseMapRolePrivEntity,@RequestParam(value = "adoptToken", required = false) String adoptToken,   HttpServletRequest request){
		baseMapRolePrivEntity.setCreateTime(new Date());
		BaseUser currentUser = tokenPrivilegeService.getCurrentUser(adoptToken);
		baseMapRolePrivEntity.setCreateUser(currentUser.getUserName());
		baseMapRolePrivEntity.setStatus(StatusEnum.NORMAL.getValues());
		boolean insertRole = iBaseMapPrivRoleService.insert(baseMapRolePrivEntity);
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
	public Object delRole(String id, @RequestParam(value = "adoptToken", required = false) String adoptToken, HttpServletRequest request) {
		if (id == null) {
			return new ResponseCommand(ResponseCommand.STATUS_ERROR, new VisitsMapResult(new NewMapUtil("message", "id can't be empty!").get()));
		}
		return iBaseMapPrivRoleService.delete(id);
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
	public Object insertCodeBatch(@RequestParam(value = "privVisitId", required = true)String[] privVisitId,@RequestParam(value = "roleId", required = true)Integer roleId,@RequestParam(value = "adoptToken", required = false) String adoptToken,   HttpServletRequest request){
			if(privVisitId==null || privVisitId.length<1){
				return new ResponseCommand(ResponseCommand.STATUS_ERROR,
						new VisitsMapResult(new NewMapUtil("message", "privVisitId can't be empty!").get()));
			}
			if(roleId==null){
				return new ResponseCommand(ResponseCommand.STATUS_ERROR,
						new VisitsMapResult(new NewMapUtil("message", "roleId can't be empty!").get()));
			}
		BaseUser currentUser = tokenPrivilegeService.getCurrentUser(adoptToken);
		boolean insertRole = iBaseMapPrivRoleService.insertCodeBatch(privVisitId, roleId, currentUser.getUserName());
		return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, insertRole);
	}
	

}
