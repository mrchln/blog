package com.flchy.blog.privilege.config.controller;

import java.util.Date;
import java.util.List;

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
import com.flchy.blog.base.response.VisitsListResult;
import com.flchy.blog.base.response.VisitsMapResult;
import com.flchy.blog.privilege.config.bean.BaseUser;
import com.flchy.blog.privilege.config.entity.BaseInfoUserEntity;
import com.flchy.blog.privilege.config.service.IBaseInfoUserService;
import com.flchy.blog.privilege.enums.StatusEnum;
import com.flchy.blog.privilege.support.ITokenPrivilegeService;
import com.flchy.blog.utils.CnToSpellUtils;
import com.flchy.blog.utils.MailCheckUtil;
import com.flchy.blog.utils.NewMapUtil;

/**
 * @author nieqs
 *
 */
@RestController
@RequestMapping("/user")
public class InfoUserController {
	@Autowired
	private IBaseInfoUserService iBaseInfoUserService;
	@Autowired
	private ITokenPrivilegeService tokenPrivilegeService;

	/**
	 * 分页查询
	 * 
	 * @param pageSize
	 * @param currentPage
	 * @param baseInfoUser
	 * @return
	 */
	@PostMapping("/page")
	public Object userPage(@RequestParam(value = "pageSize", required = true) int pageSize, @RequestParam(value = "currentPage", required = true) int currentPage, BaseInfoUserEntity baseInfoUser) {
		PageHelperResult selectUserByUser = iBaseInfoUserService.selectUserByUser(baseInfoUser, pageSize, currentPage);
		return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, selectUserByUser);
	}

	/**
	 * 查询已删除分页
	 * 
	 * @param pageSize
	 * @param currentPage
	 * @param baseInfoUser
	 * @return
	 */
	@PostMapping("/pageDeleted")
	public Object selectDeletedUser(@RequestParam(value = "pageSize", required = true) int pageSize, @RequestParam(value = "currentPage", required = true) int currentPage, BaseInfoUserEntity baseInfoUser) {
		PageHelperResult selectUserByUser = iBaseInfoUserService.selectDeletedUser(baseInfoUser, pageSize, currentPage);
		return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, selectUserByUser);
	}

	/**
	 * 添加user
	 * 
	 * @param baseInfoUser
	 * @param adoptToken
	 * @param request
	 * @return
	 */
	@PostMapping
	public Object insertUser(BaseInfoUserEntity baseInfoUser, @RequestParam(value = "adoptToken", required = false) String adoptToken, HttpServletRequest request) {
		baseInfoUser.setCreateTime(new Date());
		BaseUser currentUser = tokenPrivilegeService.getCurrentUser(adoptToken);
		baseInfoUser.setCreateUser(currentUser.getUserName());
		// baseInfoUser.setPassWord((new
		// Md5Hash(baseInfoUser.getPassWord()).toHex()));
		baseInfoUser.setStatus(StatusEnum.NORMAL.getValues());
		ResponseCommand verificationUser = verificationUser(baseInfoUser);
		if(verificationUser.getStatus()==ResponseCommand.STATUS_ERROR){
			return verificationUser;
		}
		baseInfoUser.setNickNameQp(CnToSpellUtils.converterToSpell(baseInfoUser.getNickName()));
		List<BaseInfoUserEntity> selectUserByUserName = iBaseInfoUserService.selectUserByUserName(baseInfoUser.getUserName());
		if(selectUserByUserName.size()>0){
			return new ResponseCommand(ResponseCommand.STATUS_ERROR, new VisitsMapResult(new NewMapUtil("message", "不能添加已有的登录名！！！").get()));
		}
		List<BaseInfoUserEntity> verificationUserByUser = iBaseInfoUserService.verificationUserByUser(baseInfoUser);
		if(verificationUserByUser!= null && verificationUserByUser.size()>0){
			return new ResponseCommand(ResponseCommand.STATUS_ERROR, new VisitsMapResult(new NewMapUtil("message", "不能添加已有的邮箱或者手机号！！！").get()));
		}
		boolean insertUser = iBaseInfoUserService.insertUser(baseInfoUser);
		if (insertUser) {
			return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, insertUser);
		}
		return new ResponseCommand(ResponseCommand.STATUS_ERROR, new VisitsMapResult(new NewMapUtil("message", "Add failure！").get()));
	}

	/**
	 * 验证用户字段
	 * @param baseInfoUser
	 * @return
	 */
	private ResponseCommand verificationUser(BaseInfoUserEntity baseInfoUser) {
		if(baseInfoUser.getUserName() == null || "".equals(baseInfoUser.getUserName())){
			return new ResponseCommand(ResponseCommand.STATUS_ERROR, new VisitsMapResult(new NewMapUtil("message", "登录名不能为空！！！").get()));
		}
		if(baseInfoUser.getNickName() == null || "".equals(baseInfoUser.getNickName())){
			return new ResponseCommand(ResponseCommand.STATUS_ERROR, new VisitsMapResult(new NewMapUtil("message", "昵称名不能为空！！！").get()));
		}
		if(baseInfoUser.getSex()== null ){
			return new ResponseCommand(ResponseCommand.STATUS_ERROR, new VisitsMapResult(new NewMapUtil("message", "性别不能为空！！！").get()));
		}
		if(baseInfoUser.getEmail()== null || "".equals(baseInfoUser.getEmail())){
			return new ResponseCommand(ResponseCommand.STATUS_ERROR, new VisitsMapResult(new NewMapUtil("message", "邮件不能为空！！！").get()));
		}
		if(baseInfoUser.getPhoneNo()== null || "".equals(baseInfoUser.getPhoneNo())){
			return new ResponseCommand(ResponseCommand.STATUS_ERROR, new VisitsMapResult(new NewMapUtil("message", "手机不能为空！！！").get()));
		}
		if(baseInfoUser.getValidBegin()== null || "".equals(baseInfoUser.getValidBegin())){
			return new ResponseCommand(ResponseCommand.STATUS_ERROR, new VisitsMapResult(new NewMapUtil("message", "开始时间不能为空！！！").get()));
		}
		if(baseInfoUser.getValidEnd()== null || "".equals(baseInfoUser.getValidEnd())){
			return new ResponseCommand(ResponseCommand.STATUS_ERROR, new VisitsMapResult(new NewMapUtil("message", "结束时间不能为空！！！").get()));
		}
		 return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, null);
	}

	/**
	 * 修改user
	 * 
	 * @param baseInfoUser
	 * @param adoptToken
	 * @param request
	 * @return
	 */
	@PutMapping
	public Object updateUser(BaseInfoUserEntity baseInfoUser, @RequestParam(value = "adoptToken", required = false) String adoptToken, HttpServletRequest request) {
		if (baseInfoUser.getUserId() == null) {
			return new ResponseCommand(ResponseCommand.STATUS_ERROR, new VisitsMapResult(new NewMapUtil("message", "UserId can't be empty!").get()));
		}
		baseInfoUser.setNickNameQp(CnToSpellUtils.converterToSpell(baseInfoUser.getNickName()));
		BaseUser currentUser = tokenPrivilegeService.getCurrentUser(adoptToken);
		baseInfoUser.setUpdateUser(currentUser.getUserName());
		
		List<BaseInfoUserEntity> verificationUserByUser = iBaseInfoUserService.verificationUserByUser(baseInfoUser);
		if(verificationUserByUser!= null && verificationUserByUser.size()>0){
			return new ResponseCommand(ResponseCommand.STATUS_ERROR, new VisitsMapResult(new NewMapUtil("message", "不能修改为已有的邮箱or手机号or用户名！！！").get()));
		}
		boolean updateUser = iBaseInfoUserService.updateUser(baseInfoUser);
		if (updateUser) {
			return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, baseInfoUser);
		}
		return new ResponseCommand(ResponseCommand.STATUS_ERROR, new VisitsMapResult(new NewMapUtil("message", "Update failure！").get()));
	}

	/**
	 * 删除User
	 * 
	 * @param userId
	 * @param adoptToken
	 * @param request
	 * @return
	 */
	@DeleteMapping
	public Object delUser(String userId, @RequestParam(value = "adoptToken", required = false) String adoptToken, HttpServletRequest request) {
		if (userId == null) {
			return new ResponseCommand(ResponseCommand.STATUS_ERROR, new VisitsMapResult(new NewMapUtil("message", "UserId can't be empty!").get()));
		}
		boolean deleteUser = iBaseInfoUserService.deleteUser(userId);
		if (deleteUser) {
			return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, userId);
		} else {
			return new ResponseCommand(ResponseCommand.STATUS_ERROR, new VisitsMapResult(new NewMapUtil("message", "Delete failure！").get()));
		}
	}

	/**
	 * 复原User
	 * 
	 * @param userId
	 * @param adoptToken
	 * @param request
	 * @return
	 */
	@DeleteMapping(value = "restore")
	public Object restoreUser(String userId, @RequestParam(value = "adoptToken", required = false) String adoptToken, HttpServletRequest request) {
		if (userId == null) {
			return new ResponseCommand(ResponseCommand.STATUS_ERROR, new VisitsMapResult(new NewMapUtil("message", "UserId can't be empty!").get()));
		}
		boolean deleteUser = iBaseInfoUserService.restoreUser(userId);
		if (deleteUser) {
			return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, userId);
		} else {
			return new ResponseCommand(ResponseCommand.STATUS_ERROR, new VisitsMapResult(new NewMapUtil("message", "Restore failure！").get()));
		}

	}

	/**
	 * 修改密码
	 * 
	 * @param userId
	 *            用户ID
	 * @param passWord
	 *            旧密码
	 * @param newPassword
	 *            新密码
	 * @param repeatPassword
	 *            重复新密码
	 * @return
	 */
	@PostMapping(value = "updatePwd")
	public Object updatePwd(Integer userId, String passWord, String newPassword, String repeatPassword) {
		return iBaseInfoUserService.updatePwd(userId, passWord, newPassword, repeatPassword);
	}

	/**
	 * 按角色ID查询已选用户
	 * 
	 * @param roleId
	 * @param request
	 * @return
	 */
	@GetMapping(value = "queryByRoleIdSelected")
	public Object selectUserByRoleIdSelected(@RequestParam(value = "pageSize", required = true) int pageSize, @RequestParam(value = "currentPage", required = true) int currentPage, @RequestParam(value = "roleId", required = true) Integer roleId, @RequestParam(value = "nickName", required = false) String nickName, HttpServletRequest request) {
		if (roleId == null) {
			return new ResponseCommand(ResponseCommand.STATUS_ERROR, new VisitsMapResult(new NewMapUtil("message", "roleId can't be empty!").get()));
		}
		PageHelperResult selectPage = iBaseInfoUserService.selectUserByRoleIdSelected(roleId, nickName, pageSize, currentPage);
		return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, selectPage);
	}

	/**
	 * 按角色ID查询未选用户
	 * 
	 * @param roleId
	 * @param request
	 * @return
	 */
	@GetMapping(value = "queryByRoleIdNotSelected")
	public Object selectUserByRoleIdNotSelected( @RequestParam(value = "roleId", required = true) Integer roleId, @RequestParam(value = "nickName", required = false) String nickName, HttpServletRequest request) {
		if (roleId == null) {
			return new ResponseCommand(ResponseCommand.STATUS_ERROR, new VisitsMapResult(new NewMapUtil("message", "roleId can't be empty!").get()));
		}
		List<BaseInfoUserEntity> baseinfouser = iBaseInfoUserService.selectUserByRoleIdNotSelected(roleId, nickName);
		return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, new VisitsListResult(baseinfouser));
	}
	
	/**
	 * 验证邮箱是否合法 使用
	 * @param email
	 * @return
	 */
	@GetMapping(value = "verificationEmail")
	public Object verificationEmail(@RequestParam(value = "email", required = true) String  email ,@RequestParam(value = "userId", required = false) Integer  userId){
		if(!MailCheckUtil.checkEmail(email)){
			return new ResponseCommand(ResponseCommand.STATUS_ERROR, new VisitsMapResult(new NewMapUtil("message", "邮箱格式不合格,不能使用!").get()));
		}
		BaseInfoUserEntity baseInfoUserEntity=new BaseInfoUserEntity();
		baseInfoUserEntity.setEmail(email);
		List<BaseInfoUserEntity> selectUserByUser = iBaseInfoUserService.selectUserByUser(baseInfoUserEntity);
		if(selectUserByUser==null || selectUserByUser.size()<1){
			return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, new VisitsMapResult(new NewMapUtil("message", "验证成功,可以使用!").get()));
		}
		BaseInfoUserEntity baseInfoUserEntity2 = selectUserByUser.get(0);
		if(selectUserByUser.size()==1 && baseInfoUserEntity2.getUserId()==userId){
			return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, new VisitsMapResult(new NewMapUtil("message", "验证成功,可以使用!").get()));
		}
		return new ResponseCommand(ResponseCommand.STATUS_ERROR, new VisitsMapResult(new NewMapUtil("message", "验证已有当前邮箱或者邮箱格式不合格,不能使用!").get()));
	}
	
	/**
	 * 验证用户名是否合法 使用
	 * @param email
	 * @return
	 */
	@GetMapping(value = "verificationUserName")
	public Object verificationUserName(@RequestParam(value = "userName", required = true) String  userName ,@RequestParam(value = "userId", required = false) Integer  userId ){
		BaseInfoUserEntity baseInfoUserEntity=new BaseInfoUserEntity();
		baseInfoUserEntity.setUserName(userName);
		List<BaseInfoUserEntity> selectUserByUser = iBaseInfoUserService.selectUserByUser(baseInfoUserEntity);
		if(selectUserByUser==null || selectUserByUser.size()<1){
			return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, new VisitsMapResult(new NewMapUtil("message", "验证成功,可以使用!").get()));
		}
		BaseInfoUserEntity baseInfoUserEntity2 = selectUserByUser.get(0);
		if(selectUserByUser.size()==1 && baseInfoUserEntity2.getUserId()==userId){
			return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, new VisitsMapResult(new NewMapUtil("message", "验证成功,可以使用!").get()));
		}
		return new ResponseCommand(ResponseCommand.STATUS_ERROR, new VisitsMapResult(new NewMapUtil("message", "验证已有当前用户名,不能使用!").get()));
	}
	
	/**
	 * 验证手机号码是否可以合法   使用
	 * @param phone
	 * @return
	 */
	@GetMapping(value = "verificationPhone")
	public Object verificationPhone(@RequestParam(value = "phone", required = true) String  phone,@RequestParam(value = "userId", required = false) Integer  userId){
		if(!MailCheckUtil.checkMobileNumber(phone)){
			return new ResponseCommand(ResponseCommand.STATUS_ERROR, new VisitsMapResult(new NewMapUtil("message", "手机号码格式不合格,不能使用!").get()));
		}
		BaseInfoUserEntity baseInfoUserEntity=new BaseInfoUserEntity();
		baseInfoUserEntity.setPhoneNo(phone);
		List<BaseInfoUserEntity> selectUserByUser = iBaseInfoUserService.selectUserByUser(baseInfoUserEntity);
		if(selectUserByUser==null || selectUserByUser.size()<1){
			return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, new VisitsMapResult(new NewMapUtil("message", "验证成功,可以使用!").get()));
		}
		BaseInfoUserEntity baseInfoUserEntity2 = selectUserByUser.get(0);
		if(selectUserByUser.size()==1 && baseInfoUserEntity2.getUserId()==userId){
			return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, new VisitsMapResult(new NewMapUtil("message", "验证成功,可以使用!").get()));
		}
		return new ResponseCommand(ResponseCommand.STATUS_ERROR, new VisitsMapResult(new NewMapUtil("message", "验证已有当前手机号码或者手机号码格式不合格,不能使用!").get()));
	}

}
