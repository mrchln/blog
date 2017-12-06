package com.flchy.blog.pojo;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author nieqs
 * @since 2017-12-03
 */
public class Images extends Model<Images> {

    private static final long serialVersionUID = 1L;

	private Integer id;
    /**
     * 图片地址
     */
	@TableField("image_path")
	private String imagePath;
    /**
     * 图片类型
     */
	@TableField("is_album")
	private Boolean isAlbum;
    /**
     * 图片名称
     */
	private String name;
    /**
     * 备注
     */
	private String remark;
    /**
     * 组名称
     */
	@TableField("group_name")
	private String groupName;
    /**
     * 远程文件名称
     */
	@TableField("remote_file_name")
	private String remoteFileName;
    /**
     * 存放类型 如：fastDfs 
     */
	private String type;
    /**
     * 1：正常，-1：删除,2,草稿
     */
	private Integer status;
    /**
     * 创建用户
     */
	@TableField("create_user")
	private String createUser;
    /**
     * 创建时间
     */
	@TableField("create_time")
	private Date createTime;
    /**
     * 修改用户
     */
	@TableField("update_user")
	private String updateUser;
    /**
     * 修改时间
     */
	@TableField("update_time")
	private Date updateTime;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public Boolean getIsAlbum() {
		return isAlbum;
	}

	public void setIsAlbum(Boolean isAlbum) {
		this.isAlbum = isAlbum;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getRemoteFileName() {
		return remoteFileName;
	}

	public void setRemoteFileName(String remoteFileName) {
		this.remoteFileName = remoteFileName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
