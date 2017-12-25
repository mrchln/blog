package com.flchy.blog.privilege.config.entity;


import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.flchy.blog.base.entity.BaseManagedEntity;

/**
 * CONF_INFO_USER
 * 用户信息表，存储用户的账户，密码，以及用户的身份信息等
 */
public class BaseInfoUserEntity extends BaseManagedEntity {
	private static final long serialVersionUID = 7505663854928410474L;
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
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date validBegin;
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date validEnd;
	private String photoPath;// 用户头像照片路径
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")  
	private Date birthday;
	//前端使用
	private Integer index;
	
	
	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

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
	
	@Override
	public String toString() {
		return "BaseInfoUserEntity [userId=" + userId + ", userName=" + userName + ", passWord=" + passWord + ", nickName=" + nickName + ", nickNameQp=" + nickNameQp + ", sex=" + sex + ", email=" + email + ", phoneNo=" + phoneNo + ", address=" + address + ", postcode=" + postcode + ", remark=" + remark + ", validBegin=" + validBegin + ", validEnd=" + validEnd + ", photoPath=" + photoPath + ", birthday=" + birthday + "]";
	}

}
