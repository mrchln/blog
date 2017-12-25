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
import com.flchy.blog.privilege.config.entity.BaseMapRoleDimAuthEntity;
import com.flchy.blog.privilege.config.service.IBaseMapRoleDimAuthService;
import com.flchy.blog.privilege.enums.StatusEnum;
import com.flchy.blog.privilege.support.ITokenPrivilegeService;
import com.flchy.blog.utils.NewMapUtil;
/**
 * 角色数据权限
 * @author nieqs
 *
 */
@RestController
@RequestMapping("/roleDataauth")
public class BaseMapRoleDimAuthController {
	@Autowired
	private ITokenPrivilegeService tokenPrivilegeService;
	@Autowired
	private IBaseMapRoleDimAuthService iBaseMapRoleDimAuthService;

	@PostMapping("queryBySelected")
	public Object query(@RequestParam(value = "roleId", required = false) Integer roleId) {
		if (roleId == null) {
			return new ResponseCommand(ResponseCommand.STATUS_ERROR, new VisitsMapResult(new NewMapUtil("message", "roleId can't be empty ！").get()));
		}
		List<BaseMapRoleDimAuthEntity> selectRoleDimAuthBySelected = iBaseMapRoleDimAuthService.selectRoleDimAuthBySelected(roleId);
		return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, new VisitsListResult(selectRoleDimAuthBySelected));
	}

	/**
	 * 添加
	 * 
	 * @param baseMapRoleDimAuthEntity
	 * @param adoptToken
	 * @param request
	 * @return
	 */
	@PostMapping
	public Object insert(BaseMapRoleDimAuthEntity baseMapRoleDimAuthEntity, @RequestParam(value = "adoptToken", required = false) String adoptToken, HttpServletRequest request) {
		baseMapRoleDimAuthEntity.setCreateTime(new Date());
		BaseUser currentUser = tokenPrivilegeService.getCurrentUser(adoptToken);
		baseMapRoleDimAuthEntity.setCreateUser(currentUser.getUserName());
		baseMapRoleDimAuthEntity.setStatus(StatusEnum.NORMAL.getValues());
		boolean insert = iBaseMapRoleDimAuthService.insert(baseMapRoleDimAuthEntity);
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
		return iBaseMapRoleDimAuthService.delete(id);
	}
	
	
	/**
	 * 批量添加
	 * @param dimauthId 数据ID
	 * @param roleId 角色ID
	 * @param adoptToken
	 * @param request
	 * @return
	 */
	@PostMapping("insertCodeBatch")
	public Object insertCodeBatch(@RequestParam(value = "dimauthId", required = true)Integer[] dimauthId,@RequestParam(value = "roleId", required = true)Integer roleId,@RequestParam(value = "adoptToken", required = false) String adoptToken,   HttpServletRequest request){
			if(dimauthId==null || dimauthId.length<1){
				return new ResponseCommand(ResponseCommand.STATUS_ERROR,
						new VisitsMapResult(new NewMapUtil("message", "dimauthId can't be empty!").get()));
			}
			if(roleId==null){
				return new ResponseCommand(ResponseCommand.STATUS_ERROR,
						new VisitsMapResult(new NewMapUtil("message", "roleId can't be empty!").get()));
			}
		BaseUser currentUser = tokenPrivilegeService.getCurrentUser(adoptToken);
		boolean insertdimauth = iBaseMapRoleDimAuthService.insertCodeBatch(dimauthId, roleId, currentUser.getUserName());
		return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, insertdimauth);
	}

}
