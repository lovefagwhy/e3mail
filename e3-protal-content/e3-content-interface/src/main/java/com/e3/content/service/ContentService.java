package com.e3.content.service;

import java.util.List;

import com.e3.shop.common.ADItem;
import com.e3.shop.common.DataGridResult;
import com.e3.shop.common.E3mallResult;
import com.e3.shop.pojo.TbContent;

public interface ContentService {
	public DataGridResult getContentList(Long categoryId, Integer page, Integer rows);

	public E3mallResult saveContent(TbContent tbContent);

	public List<ADItem> getADList(Long categoryId);
	
	
}
