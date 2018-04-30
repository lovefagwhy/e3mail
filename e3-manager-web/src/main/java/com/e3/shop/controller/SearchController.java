package com.e3.shop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.e3.search.service.SearchItemService;
import com.e3.shop.common.E3mallResult;

@Controller
public class SearchController {
	@Autowired
	private SearchItemService searchItemService;
	@RequestMapping("item/dataImport")
	@ResponseBody
	public E3mallResult dataImport(){
		E3mallResult result = searchItemService.dataImport();
		return result;
	}
}
