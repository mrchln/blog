package com.flchy.blog.privilege.support;

import java.util.List;
import java.util.Set;

import com.flchy.blog.privilege.config.bean.ConfUrlBean;
import com.flchy.blog.privilege.config.entity.BaseInfoDimAuthEntity;
import com.flchy.blog.privilege.config.entity.BaseInfoMenuEntity;
import com.flchy.blog.privilege.config.entity.BaseInfoRoleEntity;
import com.flchy.blog.privilege.config.entity.BaseInfoUserEntity;

public interface IAuthServeService {
	
	/**
	 * 根据用户名获取用户对象登录
	 * @param userName
	 * @return
	 */
    public BaseInfoUserEntity selectUserByUserName(String userName);

    /**
     * 依据菜单资源ID获取菜单对象
     * @param menuId
     * @return
     */
    public BaseInfoMenuEntity selectMenuInfoById(int menuId);

    /**
     * 依据账号ID，获取对应的角色集合
     * @param userId
     * @return
     */
    public List<BaseInfoRoleEntity> selectRolesByUserId(int userId);

    /**
     *  获取所有的授权菜单资源Id
     * @param userId
     * @return
     */
    public Set<Integer> getAuthMenuIds(int userId);
    
    /**
     *  获取所有的访问权限Id
     * @param userId
     * @return
     */
    public Set<String> getAuthPrivIds(int userId);
    
    /**
     *  获取所有的授权访问Url
     * @param userId
     * @return
     */
    public Set<ConfUrlBean> getAuthUrlPaths(int userId);

    /**
     * 获取所有的授权元素资源Id
     * @param userId
     * @return
     */
    public Set<Integer> getAuthElemIds(int userId);

    /**
     * 获取所有的授权面板资源Id
     * @param userId
     * @return
     */
    public Set<Integer> getAuthPanelIds(int userId);

    /**
     * 根据账号编码查询返回所有的角色ID
     * @param userId
     * @return
     */
    public Set<Integer> getRolesSetByUserId(int userId);
    
    /**
     * 获取所有的授权维表资源数据
     * @param userId
     * @return
     */
    public List<BaseInfoDimAuthEntity> selectDimAuthByUserId(int userId);
    
}
