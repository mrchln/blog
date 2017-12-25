package com.flchy.blog.base.entity;

import java.io.Serializable;
import java.util.Date;
/**
 * 把系统实体中公共的成员变量抽成一个实体类，被其他需要使用的实体对象继承使用。
 * @since 0.0.1-SNAPSHOT
 */
public class BaseManagedEntity implements Serializable  {
    private static final long serialVersionUID = 534317832385566546L;

    /**
     * status:是否有效。
     */
    private Integer status;

    /**
     * createUser:创建人。
     */
    private String createUser;

    /**
     * createTime:创建时间。
     */
    private Date createTime;

    /**
     * updateUser:修改人。
     */
    private String updateUser;

    /**
     * updateTime:修改时间。
     */
    private Date updateTime;

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
