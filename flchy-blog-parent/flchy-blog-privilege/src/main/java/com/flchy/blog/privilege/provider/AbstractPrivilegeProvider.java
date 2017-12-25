package com.flchy.blog.privilege.provider;

import java.util.List;
import java.util.Set;

import com.flchy.blog.privilege.config.bean.BaseDim;
import com.flchy.blog.privilege.config.bean.BaseRole;
import com.flchy.blog.privilege.config.bean.BaseUser;
import com.flchy.blog.privilege.config.bean.ConfUrlBean;

/**
 * EAI企业应用集成接口
 *
 */
public abstract class AbstractPrivilegeProvider {
    /**
     * 参数解密接口
     **/
    public abstract String decrypt(String encryptStr);

    /**
     * 参数加密接口
     **/
    public abstract String encrypt(String str);

    /**
     * 参数本地加密
     **/
    public abstract String encryptLoacl(String str);

    /**
     * 根据userName获取用户对象
     **/
    public abstract BaseUser getUserByUserName(String username);

    /**
     * 根据账号ID获取用户分配的所有角色
     **/
    public abstract List<BaseRole> getRolesByUserId(int userId);

    /**
     * 根据账号ID，获取所有的授权的菜单资源
     **/
    public abstract Set<String> getPermissionsByUserId(int userId);

    /**
     * 根据账号ID获取用户授权的元素资源
     **/
    public abstract Set<Integer> getAuthElemIdsByUserId(int userId);

    /**
     * 根据账号ID获取用户授权的面板资源
     **/
    public abstract Set<Integer> getAuthPanelIdsByUserId(int userId);
    
    /**
     * 根据账号ID获取用户授权的维表资源
     */
    public abstract List<BaseDim> getAuthDimInfoByUserId(int userId);
    
    /**
     * 根据账号ID，获取所有的授权的访问地址
     **/
    public abstract Set<ConfUrlBean> getUrlPermisByUserId(int userId);
    
}
