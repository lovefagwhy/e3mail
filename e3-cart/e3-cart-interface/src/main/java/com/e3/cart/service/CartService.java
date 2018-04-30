package com.e3.cart.service;

import java.util.List;

import com.e3.shop.common.E3mallResult;
import com.e3.shop.pojo.TbItem;

public interface CartService {

	E3mallResult addCart(Long id, Long itemId, Integer num);

	E3mallResult mergeCart(Long id, List<TbItem> list);

	List<TbItem> getCartList(Long id);

}
