package com.flchy.blog.base.response;

import java.io.Serializable;

import com.baomidou.mybatisplus.plugins.Page;

public class PageHelperResult  extends VisitsListResult  implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 8715690182822472139L;

	/* 总数 */
    private long total;

    /* 每页显示条数，默认 10 */
    private int size;

    /* 总页数 */
    private int pages;

    /* 当前页 */
    private int current;
    
    
    public PageHelperResult(Page<?> page){
    	this.total=page.getTotal();
    	this.size=page.getSize();
    	this.pages=page.getPages();
    	this.current=page.getCurrent();
    	super.setResult(page.getRecords());
    }

	public PageHelperResult(com.github.pagehelper.Page<?> pageInfo) {
		super();
		super.setResult(pageInfo.getResult());
	  	this.total=pageInfo.getTotal();
    	this.size=pageInfo.getPageSize();
    	this.pages= pageInfo.getPages();
    	this.current=pageInfo.getPageNum();
	}
	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getPages() {
		return pages;
	}

	public void setPages(int pages) {
		this.pages = pages;
	}

	public int getCurrent() {
		return current;
	}

	public void setCurrent(int current) {
		this.current = current;
	}

    

}
