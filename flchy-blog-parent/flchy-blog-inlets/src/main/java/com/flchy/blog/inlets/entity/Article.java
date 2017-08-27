package com.flchy.blog.inlets.entity;

import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import java.io.Serializable;

/**
 * <p>
 * 文章表
 * </p>
 *
 * @author nieqs
 * @since 2017-08-08
 */
@XmlRootElement
public class Article extends Model<Article> {

    private static final long serialVersionUID = 1L;

    /**
     * 文章ID
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 文章标题
     */
	private String title;
    /**
     * 文章内容
     */
	private String content;
    /**
     * 简介
     */
	private String synopsis;
    /**
     * 图片
     */
	private String image;
    /**
     * 文章分类ID
     */
	@TableField("type_id")
	private Integer typeId;
    /**
     * 查看人数
     */
	private Integer see;
    /**
     * 1：正常，-1：删除2,草稿
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
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
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
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
	@TableField("update_time")
	private Date updateTime;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSynopsis() {
		return synopsis;
	}

	public void setSynopsis(String synopsis) {
		this.synopsis = synopsis;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Integer getTypeId() {
		return typeId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}

	public Integer getSee() {
		return see;
	}

	public void setSee(Integer see) {
		this.see = see;
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
