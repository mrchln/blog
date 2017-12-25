package com.flchy.blog.privilege.config.bean;

import java.io.Serializable;

public class BaseRole implements Serializable {

    private static final long serialVersionUID = 2171955888677615060L;

    /** roleId:角色id。 */
    private String roleId;

    /** roleName:角色名称。 */
    private String roleName;

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

}
