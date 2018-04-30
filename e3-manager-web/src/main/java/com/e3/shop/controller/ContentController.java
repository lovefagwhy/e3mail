package com.e3.shop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.e3.content.service.ContentService;
import com.e3.shop.common.DataGridResult;
import com.e3.shop.common.E3mallResult;
import com.e3.shop.pojo.TbContent;
@Controller
public class ContentController {
	@Autowired
	private ContentService contentService;
	@RequestMapping("/content/query/list")
	@ResponseBody
	public DataGridResult getContentList(Long categoryId,Integer page,Integer rows){
		DataGridResult result = contentService.getContentList(categoryId,page,rows);
		return result;
	}
	@RequestMapping("/content/save")
	@ResponseBody
	public E3mallResult saveContent(TbContent tbContent){
		E3mallResult result = contentService.saveContent(tbContent);
		return result;
	}
}
