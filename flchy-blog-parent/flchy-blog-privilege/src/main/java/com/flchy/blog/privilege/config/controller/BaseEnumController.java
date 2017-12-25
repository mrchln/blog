package com.flchy.blog.privilege.config.controller;

import java.util.Arrays;
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

import com.flchy.blog.base.dbconfig.bean.EnumDicBean;
import com.flchy.blog.base.dbconfig.holder.EnumDicHolder;
import com.flchy.blog.base.dbconfig.service.IEnumDicService;
import com.flchy.blog.base.response.PageHelperResult;
import com.flchy.blog.base.response.ResponseCommand;
import com.flchy.blog.base.response.VisitsListResult;
import com.flchy.blog.base.response.VisitsMapResult;
import com.flchy.blog.privilege.config.bean.BaseUser;
import com.flchy.blog.privilege.config.entity.BaseConfUrlEntity;
import com.flchy.blog.privilege.enums.DicEnumEnum;
import com.flchy.blog.privilege.enums.StatusEnum;
import com.flchy.blog.privilege.support.ITokenPrivilegeService;
import com.flchy.blog.utils.NewMapUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

/**
 * 获取枚举值
 * 
 * @author nieqs
 *
 */
@RestController
@RequestMapping("/dicenum")
public class BaseEnumController {

	@Autowired
	private IEnumDicService iEnumDicService;
	@Autowired
	private ITokenPrivilegeService tokenPrivilegeService;

	/**
	 * 查询指定的组枚举
	 * 
	 * @param type
	 * @return
	 */
	@GetMapping
	public Object getEnum(@RequestParam(value = "type", required = true) Integer type){
		if(type==null){
			return new ResponseCommand(ResponseCommand.STATUS_ERROR,
					new VisitsMapResult(new NewMapUtil("message", "type can't be empty!").get()));
		}
		DicEnumEnum dicEnumEnum = getDicEnumEnum(type);
		if(dicEnumEnum==null){
			return new ResponseCommand(ResponseCommand.STATUS_ERROR,
					new VisitsMapResult(new NewMapUtil("message", "没有找到当前枚举值！！！").get()));
		}
		return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, new VisitsListResult(EnumDicHolder.getEnums(dicEnumEnum.getEnumGroup())));
	}

	public DicEnumEnum getDicEnumEnum(Integer code) {
		DicEnumEnum[] values = DicEnumEnum.values();
		for (int i = 0; i < values.length; i++) {
			if (values[i].getCode() == code) {
				return values[i];
			}
		}
		return null;
	}

	/**
	 * 分页查询
	 * 
	 * @param pageSize
	 * @param currentPage
	 * @return
	 */
	@PostMapping("/page")
	public Object userPage(@RequestParam(value = "pageSize", required = true) int pageSize, @RequestParam(value = "currentPage", required = true) int currentPage, EnumDicBean enumDicBean) {
		Page<BaseConfUrlEntity> page = PageHelper.startPage(currentPage, pageSize).doSelectPage(() -> iEnumDicService.selectEnumsListByObj(enumDicBean));
		return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, new PageHelperResult(page));
	}

	@PostMapping
	public Object insert(EnumDicBean enumDicBean, @RequestParam(value = "adoptToken", required = false) String adoptToken, HttpServletRequest request) {
		enumDicBean.setCreateTime(new Date());
		BaseUser currentUser = tokenPrivilegeService.getCurrentUser(adoptToken);
		enumDicBean.setCreateUser(currentUser.getUserName());
		enumDicBean.setStatus(StatusEnum.NORMAL.getValues());
		boolean insertEnum = iEnumDicService.insertEnum(enumDicBean);
		if (insertEnum) {
			return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, insertEnum);
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
	public Object updateRole(EnumDicBean enumDicBean, @RequestParam(value = "adoptToken", required = false) String adoptToken, HttpServletRequest request) {
		if (enumDicBean.getId() == null) {
			return new ResponseCommand(ResponseCommand.STATUS_ERROR, new VisitsMapResult(new NewMapUtil("message", "id can't be empty!").get()));
		}
		BaseUser currentUser = tokenPrivilegeService.getCurrentUser(adoptToken);
		enumDicBean.setUpdateUser(currentUser.getUserName());
		boolean update = iEnumDicService.updateEnum(enumDicBean);
		if (update) {
			return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, update);
		}
		return new ResponseCommand(ResponseCommand.STATUS_ERROR, new VisitsMapResult(new NewMapUtil("message", "Update failure！").get()));
	}

	@DeleteMapping
	public Object delMenu(Integer[] id, @RequestParam(value = "adoptToken", required = false) String adoptToken, HttpServletRequest request) {
		if (id == null || id.length < 1) {
			return new ResponseCommand(ResponseCommand.STATUS_ERROR, new VisitsMapResult(new NewMapUtil("message", "id can't be empty!").get()));
		}
		Boolean deleteEnum = iEnumDicService.deleteEnum(Arrays.asList(id));
		return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, deleteEnum);
	}

}
