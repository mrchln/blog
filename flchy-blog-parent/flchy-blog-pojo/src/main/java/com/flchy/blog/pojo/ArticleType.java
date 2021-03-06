package com.flchy.blog.pojo;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 文章类型
 * </p>
 *
 * @author nieqs
 * @since 2017-08-08
 */
@TableName("article_type")
public class ArticleType extends Model<ArticleType> {

    private static final long serialVersionUID = 1L;

    /**
     * 文章类型ID
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 文章类型名称
     */
	private String name;
	
	private Integer type;
	private String url;
	private Integer order;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}
	
	
	

}
