package com.flchy.blog.pojo;

import com.baomidou.mybatisplus.enums.FieldStrategy;
import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import java.io.Serializable;

/**
 * <p>
 * 评论表
 * </p>
 *
 * @author nieqs
 * @since 2017-11-02
 */
public class Comment extends Model<Comment> {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 上级ID
     */
	private Integer upperId;
    /**
     * 文章ID
     */
	private Integer articleId;
    /**
     * 昵称
     */
	private String nickname;
    /**
     * 邮件
     */
	private String mail;
    /**
     * 网址
     */
	private String url;
    /**
     * 评论
     */
	private String comment;
    /**
     * 1：正常，-1：删除,2,待审核
     */
	private Integer status;
	  /**
     * 创建时间
     */
	@TableField("create_time")
	private Date createTime;
    /**
     * 发送给
     */
	private Integer send;
    /**
     * 是否接受通知
     */
	private boolean notice;
	
	
	@TableField(exist = false)
	private List<Comment> list;
	


	public Integer getSend() {
		return send;
	}

	public void setSend(Integer send) {
		this.send = send;
	}

	public boolean getNotice() {
		return notice;
	}

	public void setNotice(boolean notice) {
		this.notice = notice;
	}

	public List<Comment> getList() {
		return list;
	}

	public void setList(List<Comment> list) {
		this.list = list;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUpperId() {
		return upperId;
	}

	public void setUpperId(Integer upperId) {
		this.upperId = upperId;
	}

	public Integer getArticleId() {
		return articleId;
	}

	public void setArticleId(Integer articleId) {
		this.articleId = articleId;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
