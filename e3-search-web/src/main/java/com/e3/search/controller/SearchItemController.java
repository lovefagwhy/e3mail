package com.e3.search.controller;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.e3.search.service.SearchItemService;
import com.e3.shop.pojo.SearchResult;

@Controller
public class SearchItemController {
	@Autowired
	private SearchItemService searchItemService;
	@RequestMapping("/search")
	public String queryIntemIndex(@RequestParam(value="q") String qName,
			@RequestParam(defaultValue="1") Integer page,
			@RequestParam(defaultValue="60")Integer rows,
			Model model){
		try {
			qName = new String(qName.getBytes("iso_8859-1"),"utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		SearchResult searchResult = searchItemService.queryItemIndex(qName, page, rows);
		
		model.addAttribute("query",qName);
		//回显总页数
		model.addAttribute("totalPages", searchResult.getPageCount());
		//回显商品列表
		model.addAttribute("itemList", searchResult.getList());
		//回显当前页
		model.addAttribute("page",searchResult.getCurPage());
		return "search";
	}
}
