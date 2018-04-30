package com.e3.shop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class PageController {
	@RequestMapping("/")
	public String toIndex(){
		return "index";
	}
	@RequestMapping("/{page}")
	public String toItem(@PathVariable String page){
		return page;
	}

}
