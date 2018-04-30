package com.e3.shop.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.e3.content.service.ContentCategoryService;
import com.e3.shop.common.E3mallResult;
import com.e3.shop.common.EasyUITreeNode;

@Controller
public class ContentCategoryController {
	@Autowired
	private ContentCategoryService contentCategoryService;
	@RequestMapping("/content/category/list")
	@ResponseBody
	public List<EasyUITreeNode> getContentCategory(@RequestParam(value="id",defaultValue="0") Long parentId){
		List<EasyUITreeNode> list = contentCategoryService.getContentCategoryByParentId(parentId);
		return list;
	}
	@RequestMapping("/content/category/create")
	@ResponseBody
	public E3mallResult createContentCategory(@RequestParam(value="parentId",defaultValue="0") Long parentId,String name){
		E3mallResult result = contentCategoryService.createContentCategoryByParentId(parentId,name);
		return result;
	}
	
}
