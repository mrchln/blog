package com.flchy.blog.privilege.config.bean;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;
import java.util.Set;

import com.flchy.blog.utils.Collections3;
import com.google.common.base.Objects;

/**
 * 用户数据
 * 
 * @author Administrator
 *
 */
public class BaseUser implements Serializable {
	private static final long serialVersionUID = -5612749126100987887L;
	private Integer userId;// 系统自增的用户主键
	private String userName;// 登录使用
	private String passWord;// MD5加密
	private String nickName;// 昵称
	private String nickNameQp;
	private Integer sex;// 1：男 2：女 -1:未知
	private String email;
	private String phoneNo;
	private String address;
	private String postcode;
	private String remark;
	private Date validBegin;
	private Date validEnd;
	private String photoPath;// 用户头像照片路径
	private Date birthday;
	private List<BaseRole> roles;      // 授权的用户角色
	private List<BaseDim> dimAuths;    // 授权的用户维值
	private Set<String> permissions;   // 授权的访问权限
	private Set<ConfUrlBean> urlPermis;     // 授权的可访问地址
	private Set<Integer> authMenuIds;  // 用户授权的所有菜单
	private Set<Integer> authElemIds;  // 用户元素的操作权限
	private Set<Integer> authPanelIds; // 用户面板的操作权限

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getNickNameQp() {
		return nickNameQp;
	}

	public void setNickNameQp(String nickNameQp) {
		this.nickNameQp = nickNameQp;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getValidBegin() {
		return validBegin;
	}

	public void setValidBegin(Date validBegin) {
		this.validBegin = validBegin;
	}

	public Date getValidEnd() {
		return validEnd;
	}

	public void setValidEnd(Date validEnd) {
		this.validEnd = validEnd;
	}

	public String getPhotoPath() {
		return photoPath;
	}

	public void setPhotoPath(String photoPath) {
		this.photoPath = photoPath;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public void setRoles(List<BaseRole> roles) {
		this.roles = roles;
	}

	public void setDimAuths(List<BaseDim> dimAuths) {
		this.dimAuths = dimAuths;
	}

	public Set<String> getPermissions() {
		return permissions;
	}

	public void setPermissions(Set<String> permissions) {
		this.permissions = permissions;
	}

	public Set<ConfUrlBean> getUrlPermis() {
		return urlPermis;
	}

	public void setUrlPermis(Set<ConfUrlBean> urlPermis) {
		this.urlPermis = urlPermis;
	}

	public Set<Integer> getAuthMenuIds() {
		return authMenuIds;
	}

	public void setAuthMenuIds(Set<Integer> authMenuIds) {
		this.authMenuIds = authMenuIds;
	}

	public Set<Integer> getAuthElemIds() {
		return authElemIds;
	}

	public void setAuthElemIds(Set<Integer> authElemIds) {
		this.authElemIds = authElemIds;
	}

	public Set<Integer> getAuthPanelIds() {
		return authPanelIds;
	}

	public void setAuthPanelIds(Set<Integer> authPanelIds) {
		this.authPanelIds = authPanelIds;
	}

	public Set<String> getRoleIds() {
		return (Set<String>) Collections3.extractToSet(this.roles, "roleId");
	}

	public Set<String> getRoleNames() {
		return (Set<String>) Collections3.extractToSet(this.roles, "roleName");
	}

	public Set<String> getDimAuths() {
		return (Set<String>) Collections3.extractToSet(this.dimAuths, "jsonInfo");
	}

	public String toString() {
		return this.userId.toString();
	}

	public int hashCode() {
		return Objects.hashCode(this.userId);
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BaseUser other = (BaseUser) obj;
		if (this.userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}
}
