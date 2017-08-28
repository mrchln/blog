package com.flchy.blog.inlets.entity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 错误日志表
 * </p>
 *
 * @author nieqs
 * @since 2017-08-28
 */
@TableName("error_log")
public class ErrorLog extends Model<ErrorLog> {

    private static final long serialVersionUID = 1L;

    /**
     * 错误ID
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 错误类
     */
	private String classs;
    /**
     * 错误原因
     */
	private String cause;
    /**
     * 错误详情
     */
	private String message;
	
	/**
	 * 创建时间
	 */
	@TableField("create_time")
	private Date createTime;


	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getClasss() {
		return classs;
	}

	public void setClasss(String classs) {
		this.classs = classs;
	}

	public String getCause() {
		return cause;
	}

	public void setCause(String cause) {
		this.cause = cause;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
