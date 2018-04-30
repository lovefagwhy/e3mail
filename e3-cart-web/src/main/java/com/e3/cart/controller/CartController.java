package com.e3.cart.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.e3.cart.service.CartService;
import com.e3.shop.common.CookieUtils;
import com.e3.shop.common.E3mallResult;
import com.e3.shop.common.JsonUtils;
import com.e3.shop.pojo.TbItem;
import com.e3.shop.pojo.TbItemCat;
import com.e3.shop.pojo.TbUser;
import com.e3.shop.service.ItemService;

@Controller
public class CartController {
	@Autowired
	private ItemService itemService;
	@Value("${CART_KEY}")
	private String CART_KEY;
	@Value("${CART_KEY_TIME}")
	private int CART_KEY_TIME;
	@Autowired
	private CartService cartService;
	@RequestMapping("/cart/add/{itemId}")
	public String addCart(@PathVariable Long itemId,
			@RequestParam(defaultValue="1") Integer num,
			HttpServletRequest request,HttpServletResponse response){
		Object object = request.getAttribute("user");
		if(object != null){
			//添加到redis 购物车
			TbUser user = (TbUser)object;
			cartService.addCart(user.getId(),itemId,num);
			return "cartSuccess";
		}
		//添加到cookie购物车
		List<TbItem> list  = getCookieList(request);
		boolean falg  = false;
		for (TbItem tbItem : list) {
			if(tbItem.getId() == itemId.longValue()){
				tbItem.setNum(tbItem.getNum()+num);
				falg =  true;
				break;
			}
		}
		if(!falg){
			TbItem tbItem = itemService.findItemByID(itemId);
			tbItem.setNum(num);
			list.add(tbItem);
		}
		String json = JsonUtils.objectToJson(list);
		CookieUtils.setCookie(request, response, CART_KEY, json,CART_KEY_TIME, true);
		return "cartSuccess";
	}
	private List<TbItem> getCookieList(HttpServletRequest request) {
		String json = CookieUtils.getCookieValue(request, CART_KEY, true);
		if(StringUtils.isNotBlank(json)){
			return JsonUtils.jsonToList(json, TbItem.class);
		}
		return new ArrayList<>();
	}
	@RequestMapping("/cart/cart")
	public String findAllItem(HttpServletRequest request,HttpServletResponse response){
		List<TbItem> list = getCookieList(request);
		TbUser user = (TbUser) request.getAttribute("user");
		if(user != null){
			if(!list.isEmpty()){
				E3mallResult result = cartService.mergeCart(user.getId(),list);
				CookieUtils.setCookie(request, response, CART_KEY,"", 0, true);
			}
			list = cartService.getCartList(user.getId());
		}
		request.setAttribute("cartList", list);
		return "cart";
	}
}
