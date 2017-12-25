package com.flchy.blog.privilege.config.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.flchy.blog.base.response.ResponseCommand;
import com.flchy.blog.base.response.VisitsListResult;
import com.flchy.blog.base.response.VisitsMapResult;
import com.flchy.blog.privilege.config.bean.BaseMenu;
import com.flchy.blog.privilege.config.bean.BaseUser;
import com.flchy.blog.privilege.config.cache.MenuResourceCache;
import com.flchy.blog.privilege.config.entity.BaseInfoMenuEntity;
import com.flchy.blog.privilege.config.service.IBaseInfoMenuService;
import com.flchy.blog.privilege.enums.StatusEnum;
import com.flchy.blog.privilege.support.IResourceService;
import com.flchy.blog.privilege.support.ITokenPrivilegeService;
import com.flchy.blog.utils.CnToSpellUtils;
import com.flchy.blog.utils.NewMapUtil;
/**
 * 菜单
 * @author nieqs
 *
 */
@RestController
@RequestMapping("/menu")
public class InfoMenuController {
	@Resource
	private IResourceService iResourceService;
	@Autowired
	private ITokenPrivilegeService tokenPrivilegeService;
	@Autowired
	private IBaseInfoMenuService iBaseInfoMenuService;

	@GetMapping
	public Object getMenu() {
//		BaseMenu portalMenu = iResourceService.getAllMenuMap().getPortalMenu();
		Map<Integer, BaseMenu> allMenu = MenuResourceCache.getInstance().getAllMenu();
		return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, new VisitsListResult(allMenu.get(-1).getChildren()));
	}
	/**
	 * 根据菜单ID查询菜单信息
	 * @param menuId
	 * @return
	 */
	@GetMapping("queryMenu")
	public Object getMenuById(@RequestParam(value = "menuId", required = true) Integer menuId) {
		Map<String, Object> resourceProperties = iResourceService.getResourceProperties(menuId);
		return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, new VisitsMapResult(resourceProperties));
	}

	@PostMapping
	public Object insert(BaseInfoMenuEntity baseInfoMenuEntity, @RequestParam(value = "adoptToken", required = false) String adoptToken, HttpServletRequest request) {
		if (baseInfoMenuEntity.getMenuPid() == null) {
			return new ResponseCommand(ResponseCommand.STATUS_ERROR, new VisitsMapResult(new NewMapUtil("message", "menuPid can't be empty!").get()));
		}
		baseInfoMenuEntity.setMenuNameQp(CnToSpellUtils.converterToSpell(baseInfoMenuEntity.getMenuName()));
		baseInfoMenuEntity.setCreateTime(new Date());
		BaseUser currentUser = tokenPrivilegeService.getCurrentUser(adoptToken);
		baseInfoMenuEntity.setCreateUser(currentUser.getUserName());
		baseInfoMenuEntity.setStatus(StatusEnum.NORMAL.getValues());
		boolean insertRole = iBaseInfoMenuService.insertMenu(baseInfoMenuEntity);
		if (insertRole) {
			return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, insertRole);
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
	public Object updateRole(BaseInfoMenuEntity baseInfoMenuEntity, @RequestParam(value = "adoptToken", required = false) String adoptToken, HttpServletRequest request) {
		if (baseInfoMenuEntity.getMenuId() == 0) {
			return new ResponseCommand(ResponseCommand.STATUS_ERROR, new VisitsMapResult(new NewMapUtil("message", "menuId can't be empty!").get()));
		}
		baseInfoMenuEntity.setMenuNameQp(CnToSpellUtils.converterToSpell(baseInfoMenuEntity.getMenuName()));
		BaseUser currentUser = tokenPrivilegeService.getCurrentUser(adoptToken);
		baseInfoMenuEntity.setUpdateUser(currentUser.getUserName());
		boolean update = iBaseInfoMenuService.update(baseInfoMenuEntity);
		if (update) {
			return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, update);
		}
		return new ResponseCommand(ResponseCommand.STATUS_ERROR, new VisitsMapResult(new NewMapUtil("message", "Update failure！").get()));
	}

	@GetMapping("queryByUserId")
	public Object queryByUserId(@RequestParam(value = "userId", required = true) int userId) {
		BaseMenu portalMenu = iBaseInfoMenuService.selectMenuByUserId(userId);
		return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, new VisitsListResult(portalMenu == null ? new ArrayList<>() : portalMenu.getChildren()));
	}

	@GetMapping("queryByRoleId")
	public Object selectMenuByRoleId(@RequestParam(value = "roleId", required = true) int roleId) {
		return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, new VisitsListResult(iBaseInfoMenuService.selectMenuByRoleId(roleId)));
	}

	/**
	 * 按角色ID查询菜单 菜单带是否被已选中
	 * 
	 * @param roleId
	 * @return
	 */
	@GetMapping("queryByRoleIdAllMenu")
	public Object selectByRoleIdAllMenu(@RequestParam(value = "roleId", required = true) int roleId) {
		BaseMenu selectByRoleIdAllMenu = iBaseInfoMenuService.selectByRoleIdAllMenu(roleId);
		List<BaseMenu> children = selectByRoleIdAllMenu.getChildren();
		return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, new VisitsListResult(children));
	}

	@DeleteMapping
	public Object delMenu(String menuId, @RequestParam(value = "adoptToken", required = false) String adoptToken, HttpServletRequest request) {
		if (menuId == null) {
			return new ResponseCommand(ResponseCommand.STATUS_ERROR, new VisitsMapResult(new NewMapUtil("message", "menuId can't be empty!").get()));
		}
		return iBaseInfoMenuService.delete(menuId);
	}

}
