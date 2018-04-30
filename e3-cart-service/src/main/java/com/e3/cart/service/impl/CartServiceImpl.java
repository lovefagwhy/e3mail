package com.e3.cart.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.e3.cart.service.CartService;
import com.e3.cart.service.JedisService;
import com.e3.shop.common.E3mallResult;
import com.e3.shop.common.JsonUtils;
import com.e3.shop.mapper.TbItemMapper;
import com.e3.shop.pojo.TbItem;
@Service
public class CartServiceImpl implements CartService{
	@Value("${REDIS_CART_KEY}")
	private String REDIS_CART_KEY;
	@Value("${SORT_CART_KEY}")
	private String SORT_CART_KEY;
	@Autowired
	private JedisService jedisService;
	@Autowired
	private TbItemMapper tbItemMapper;
	@Override
	public E3mallResult addCart(Long id, Long itemId, Integer num) {
		String json = jedisService.hget(REDIS_CART_KEY+":"+id, itemId+"");
		if(StringUtils.isNotBlank(json)){
			TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
			tbItem.setNum(tbItem.getNum()+num);
			return addSortCart(id, itemId, tbItem);
		}
		TbItem tbItem = tbItemMapper.selectByPrimaryKey(itemId);
		tbItem.setNum(num);
		return addSortCart(id, itemId, tbItem);
	}
	public E3mallResult addSortCart(Long id, Long itemId, TbItem tbItem) {
		jedisService.hset(REDIS_CART_KEY+":"+id, itemId+"", JsonUtils.objectToJson(tbItem));
		jedisService.zadd(SORT_CART_KEY+":"+id, System.currentTimeMillis(), itemId+"");
		return E3mallResult.ok();
	}
	@Override
	public E3mallResult mergeCart(Long id, List<TbItem> list) {
		for (TbItem tbItem : list) {
			addCart(id, tbItem.getId(), tbItem.getNum());
		}
		return E3mallResult.ok();
	}
	@Override
	public List<TbItem> getCartList(Long id) {
		Set<String> set = jedisService.zrevrange(SORT_CART_KEY+":"+id, 0, -1);
		List<TbItem> list = new ArrayList<TbItem>();
		for (String itemId : set) {
			String json = jedisService.hget(REDIS_CART_KEY, itemId);
			TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
			list.add(tbItem);
		}
		return list;
	}

}
