package com.e3.shop.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.e3.shop.common.DataGridResult;
import com.e3.shop.common.E3mallResult;
import com.e3.shop.common.EasyUITreeNode;
import com.e3.shop.pojo.TbItem;
import com.e3.shop.pojo.TbItemDesc;
import com.e3.shop.service.ItemService;

@Controller
@RequestMapping("/item")
public class ItemController {
	@Autowired
	private ItemService itemService;
	
	@RequestMapping("/list")
	@ResponseBody
	public DataGridResult getList( int page, int rows){
		DataGridResult result = itemService.getList(page,rows);
		return result;
	}
	@RequestMapping("/cat/list")
	@ResponseBody
	public List<EasyUITreeNode> getTreeList(@RequestParam(value="id",defaultValue="0") long parentId){
		List<EasyUITreeNode> list = itemService.getCatList(parentId);
		return list;
	}
	@RequestMapping("/save")
	@ResponseBody
	public E3mallResult saveItem(TbItem item, TbItemDesc desc){
		E3mallResult result  = itemService.saveItem(item,desc);
		return result;
	}
}
