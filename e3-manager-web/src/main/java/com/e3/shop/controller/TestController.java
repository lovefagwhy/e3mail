package com.e3.shop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TestController {
	private int i=0;
	@RequestMapping("/num")
	public String pring(){
		System.out.println("----------------------"+ i++ +"----------");
		return "index";
	}
}
