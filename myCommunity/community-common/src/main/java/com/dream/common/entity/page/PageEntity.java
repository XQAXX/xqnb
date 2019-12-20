package com.dream.common.entity.page;
import cn.hutool.json.JSONUtil;

import java.io.Serializable;
import java.util.List;

/**
 * 分页实体类
 * @author 小乔
 */
public class PageEntity implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private int currentPage=1;//当前页
	private int pageSize=10;//页面显示记录数
	private int totalResultSize;//总记录数
	private int totalPageSize;//总页数
	private boolean first = false;//是否是首页
	private boolean last = false;//是否是尾页
	private boolean automaticCount = true;//是否采用自动分页
	private List datas;//用于翻页传值

	public int getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getTotalResultSize() {
		return totalResultSize;
	}
	public void setTotalResultSize(int totalResultSize) {
		this.totalResultSize = totalResultSize;
	}
	public int getTotalPageSize() {
		return totalPageSize;
	}
	public void setTotalPageSize(int totalPageSize) {
		this.totalPageSize = totalPageSize;
	}
	public boolean isFirst() {
		 return getCurrentPage() <= 1;
	}
	public void setFirst(boolean first) {
		this.first = first;
	}
	public boolean isLast() {
		 return getCurrentPage() >= getTotalPageSize();
	}
	public void setLast(boolean last) {
		this.last = last;
	}
	public boolean isAutomaticCount() {return automaticCount;}
	public void setAutomaticCount(boolean automaticCount) {this.automaticCount = automaticCount;}
	public void setDatas(List datas) {
		this.datas = datas;
	}
	public String getDatas() {
		return JSONUtil.toJsonStr(datas);
	}
}
