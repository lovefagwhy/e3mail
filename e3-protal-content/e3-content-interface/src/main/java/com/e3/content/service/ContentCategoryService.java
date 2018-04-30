package com.e3.content.service;

import java.util.List;

import com.e3.shop.common.E3mallResult;
import com.e3.shop.common.EasyUITreeNode;

public interface ContentCategoryService {
	public List<EasyUITreeNode> getContentCategoryByParentId(Long parentId);

	public E3mallResult createContentCategoryByParentId(Long parentId, String name);

	
}
