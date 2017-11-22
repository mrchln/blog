package com.flchy.blog.pojo;

import java.io.Serializable;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;

/**
 * <p>
 * 配置信息表
 * </p>
 *
 * @author nieqs
 * @since 2017-11-22
 */
public class Config extends Model<Config> {

    private static final long serialVersionUID = 1L;

    /**
     * 配置信息ID
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 配置信息KEY
     */
	private String key;
    /**
     * 配置名称
     */
	@TableField("config_name")
	private String configName;
	private String config;
    /**
     * 说明
     */
	private String explain;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getConfigName() {
		return configName;
	}

	public void setConfigName(String configName) {
		this.configName = configName;
	}

	public String getConfig() {
		return config;
	}

	public void setConfig(String config) {
		this.config = config;
	}

	public String getExplain() {
		return explain;
	}

	public void setExplain(String explain) {
		this.explain = explain;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
