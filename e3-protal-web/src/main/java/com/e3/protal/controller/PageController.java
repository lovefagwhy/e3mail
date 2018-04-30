package com.e3.protal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.e3.content.service.ContentService;
import com.e3.shop.common.ADItem;
import com.e3.shop.common.JsonUtils;
@Controller
public class PageController {
	@Autowired
	private ContentService contentService;
	@Value("${CATEGORY_ID}")
	private Long CATEGORY_ID;
	@Value("${WIDTH}")
	private Integer WIDTH;
	@Value("${WIDTHB}")
	private Integer WIDTHB;
	@Value("${HEIGHT}")
	private Integer HEIGHT;
	@Value("${HEIGHTB}")
	private Integer HEIGHTB;
	
	@RequestMapping({"/index"})
	public String getADList(Model model){
		List<ADItem> list = contentService.getADList(CATEGORY_ID);
		for (ADItem adItem : list) {
			adItem.setHeight(HEIGHT);
			adItem.setHeightB(HEIGHTB);
			adItem.setWidth(WIDTH);
			adItem.setWidthB(WIDTHB);
		}
		String json = JsonUtils.objectToJson(list);
		model.addAttribute("ad1", json);
		return "index";
	}
}
