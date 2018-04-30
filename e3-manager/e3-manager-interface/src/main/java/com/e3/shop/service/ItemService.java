package com.e3.shop.service;


import java.util.List;

import com.e3.shop.common.DataGridResult;
import com.e3.shop.common.E3mallResult;
import com.e3.shop.common.EasyUITreeNode;
import com.e3.shop.pojo.TbItem;
import com.e3.shop.pojo.TbItemDesc;

public interface ItemService {
	
	public TbItem findItemByID(Long itemId);
	
	public TbItemDesc findItemDescByID(Long itemId);
	
	DataGridResult getList(int page, int rows);

	public List<EasyUITreeNode> getCatList(long parentId);

	E3mallResult saveItem(TbItem item, TbItemDesc desc);
}
