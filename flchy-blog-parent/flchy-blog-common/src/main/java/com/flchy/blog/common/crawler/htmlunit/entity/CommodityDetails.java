package com.flchy.blog.common.crawler.htmlunit.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

public class CommodityDetails implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8923098220875025993L;
	/**
	 * 标题
	 */
	private String title;
	/**
	 * 原价
	 */
	private Double originalPrice;
	/**
	 * 淘宝价格
	 */
	private Double price;
	/**
	 * 内容
	 */
	private String content;

	/**
	 * 图片
	 */
	private Set<String> image;
	/**
	 * 分类
	 */
	private List<Types> classify;
	/**
	 * 库存
	 */
	private Integer stock;
	/**
	 * 用于尺码等 尺码|35()|37()|38()|39()|40()||颜色|米白色()|棕色()|黑色()
	 */
	private String attr_val;
	/**
	 * 用户加价 库存等
	 * 尺码:35|颜色:米白色|0|500||尺码:35|颜色:棕色|0|500||尺码:35|颜色:黑色|0|500||尺码:37|颜色:米白色|0|500||尺码:37|颜色:棕色|0|500||尺码:37|颜色:黑色|0|500||尺码:38|颜色:米白色|0|500||尺码:38|颜色:棕色|0|500||尺码:38|颜色:黑色|0|500||尺码:39|颜色:米白色|0|500||尺码:39|颜色:棕色|0|500||尺码:39|颜色:黑色|0|500||尺码:40|颜色:米白色|0|500||尺码:40|颜色:棕色|0|500||尺码:40|颜色:黑色|0|500
	 */
	private String attr_store;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Double getOriginalPrice() {
		return originalPrice;
	}

	public void setOriginalPrice(Double originalPrice) {
		this.originalPrice = originalPrice;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Set<String> getImage() {
		return image;
	}

	public void setImage(Set<String> image) {
		this.image = image;
	}

	public List<Types> getClassify() {
		return classify;
	}

	public void setClassify(List<Types> classify) {
		this.classify = classify;
	}

	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}

	public String getAttr_val() {
		return attr_val;
	}

	public void setAttr_val(String attr_val) {
		this.attr_val = attr_val;
	}

	public String getAttr_store() {
		return attr_store;
	}

	public void setAttr_store(String attr_store) {
		this.attr_store = attr_store;
	}
}
