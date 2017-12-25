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
import com.flchy.blog.privilege.config.entity.BaseMapRoleElementEntity;
import com.flchy.blog.privilege.config.service.IBaseMapRoleElementService;
import com.flchy.blog.privilege.enums.StatusEnum;
import com.flchy.blog.privilege.support.ITokenPrivilegeService;
import com.flchy.blog.utils.NewMapUtil;
/**
 * 角色元素
 * @author nieqs
 *
 */
@RestController
@RequestMapping("/roleElement")
public class BaseMapRoleElementController {
	@Autowired
	private ITokenPrivilegeService tokenPrivilegeService;
	@Autowired
	private IBaseMapRoleElementService iBaseMapRoleElementService;
	
	
	
	
	@PostMapping("queryBySelected")
	public Object query( @RequestParam(value = "roleId", required = false) Integer roleId,@RequestParam(value = "menuId", required = false) Integer menuId,@RequestParam(value = "panelId", required = false) Integer panelId) {
		if(roleId==null){
			return new ResponseCommand(ResponseCommand.STATUS_ERROR, new VisitsMapResult(new NewMapUtil("message", "roleId can't be empty ！").get()));
		}
			List<BaseMapRoleElementEntity> selectRoleElementBySelected = iBaseMapRoleElementService.selectRoleElementBySelected(roleId, menuId, panelId);
		return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, new VisitsListResult(selectRoleElementBySelected));
	}
	
	
	/**
	 * 添加
	 * @param baseMapRolePrivEntity
	 * @param adoptToken
	 * @param request
	 * @return
	 */
	@PostMapping
	public Object insert(BaseMapRoleElementEntity baseMapRoleElementEntity,@RequestParam(value = "adoptToken", required = false) String adoptToken,   HttpServletRequest request){
		baseMapRoleElementEntity.setCreateTime(new Date());
		BaseUser currentUser = tokenPrivilegeService.getCurrentUser(adoptToken);
		baseMapRoleElementEntity.setCreateUser(currentUser.getUserName());
		baseMapRoleElementEntity.setStatus(StatusEnum.NORMAL.getValues());
		boolean insert = iBaseMapRoleElementService.insert(baseMapRoleElementEntity);
		if(insert){
			return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, insert);
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
		return iBaseMapRoleElementService.delete(id);
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
	public Object insertCodeBatch(@RequestParam(value = "elementId", required = true)Integer[] elementId,@RequestParam(value = "roleId", required = true)Integer roleId,@RequestParam(value = "adoptToken", required = false) String adoptToken,   HttpServletRequest request){
			if(elementId==null || elementId.length<1){
				return new ResponseCommand(ResponseCommand.STATUS_ERROR,
						new VisitsMapResult(new NewMapUtil("message", "urlId can't be empty!").get()));
			}
			if(roleId==null){
				return new ResponseCommand(ResponseCommand.STATUS_ERROR,
						new VisitsMapResult(new NewMapUtil("message", "privVisitId can't be empty!").get()));
			}
		BaseUser currentUser = tokenPrivilegeService.getCurrentUser(adoptToken);
		boolean insertRole = iBaseMapRoleElementService.insertCodeBatch(elementId, roleId, currentUser.getUserName());
		return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, insertRole);
	}
}
