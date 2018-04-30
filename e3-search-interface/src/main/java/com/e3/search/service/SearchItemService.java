package com.e3.search.service;

import com.e3.shop.common.E3mallResult;
import com.e3.shop.pojo.SearchResult;

public interface SearchItemService {

	E3mallResult dataImport();
	
	public SearchResult queryItemIndex(String qName, Integer page,Integer rows);

}
