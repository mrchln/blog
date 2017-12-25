package com.flchy.blog.privilege.config.service;

import java.util.List;

import com.flchy.blog.base.response.PageHelperResult;
import com.flchy.blog.privilege.config.entity.BaseInfoUserEntity;

/**
 * CONF_INFO_USER: 用户数据操作类
 */
public interface IBaseInfoUserService {

	/**
	 * 按用户名称查询用户
	 * @param userName
	 * @return
	 */
	public List<BaseInfoUserEntity> selectUserByUserName(String userName);

	/**查询永福分页
	 * @param baseInfoUserEntity
	 * @param pageSize
	 * @param currentPage
	 * @return
	 */
	PageHelperResult selectUserByUser(BaseInfoUserEntity baseInfoUserEntity, int pageSize, int currentPage);

	/**
	 * 修改用户
	 * @param baseInfoUserEntity
	 * @return
	 */
	boolean updateUser(BaseInfoUserEntity baseInfoUserEntity);

	/**
	 * 添加用户
	 * @param baseInfoUserEntity
	 * @return
	 */
	boolean insertUser(BaseInfoUserEntity baseInfoUserEntity);

	/**
	 * 删除用户
	 * @param userId
	 * @return
	 */
	boolean deleteUser(String userId);

	/**
	 *还原用户
	 * @param userId
	 * @return
	 */
	boolean restoreUser(String userId);

	/**
	 * 查询已删除分页
	 * @param baseInfoUserEntity
	 * @param pageSize
	 * @param currentPage
	 * @return
	 */
	PageHelperResult selectDeletedUser(BaseInfoUserEntity baseInfoUserEntity, int pageSize, int currentPage);

	/**
	 * 修改用户密码
	 * @param userId
	 * @param passWord
	 * @param newPassword
	 * @param repeatPassword
	 * @return
	 */
	Object updatePwd(Integer userId, String passWord, String newPassword, String repeatPassword);


	PageHelperResult selectUserByRoleIdSelected(Integer roleId, String nickName, int pageSize, int currentPage);


	public List<BaseInfoUserEntity> selectUserByRoleIdNotSelected(Integer roleId, String nickName);

	List<BaseInfoUserEntity> selectUserByUser(BaseInfoUserEntity baseInfoUserEntity);

	List<BaseInfoUserEntity> verificationUserByUser(BaseInfoUserEntity baseInfoUserEntity);

}
