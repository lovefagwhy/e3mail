package com.e3.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.e3.shop.pojo.TbItem;
import com.e3.shop.pojo.TbItemDesc;
import com.e3.shop.service.ItemService;

@Controller
public class ItemDetailController {
	@Autowired
	private ItemService itemService;
	@RequestMapping("/item/{itemId}")
	public String itemDetail(Model model , @PathVariable Long itemId){
		TbItem tbItem = itemService.findItemByID(itemId);
		TbItemDesc tbItemDesc = itemService.findItemDescByID(itemId);
		model.addAttribute("item", tbItem);
		model.addAttribute("itemDesc", tbItemDesc);
		return "item";
	}
}
