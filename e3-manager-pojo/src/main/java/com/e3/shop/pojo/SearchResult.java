package com.e3.shop.pojo;

import java.io.Serializable;
import java.util.List;

public class SearchResult implements Serializable {
	private Long recordCount;
	private	List<SearchItem> list;
	private Integer pageCount;
	private Integer curPage;
	public Long getRecordCount() {
		return recordCount;
	}
	public void setRecordCount(Long recordCount) {
		this.recordCount = recordCount;
	}
	public List<SearchItem> getList() {
		return list;
	}
	public void setList(List<SearchItem> list) {
		this.list = list;
	}
	public Integer getPageCount() {
		return pageCount;
	}
	public void setPageCount(Integer pageCount) {
		this.pageCount = pageCount;
	}
	public Integer getCurPage() {
		return curPage;
	}
	public void setCurPage(Integer curPage) {
		this.curPage = curPage;
	}
	
}
