package com.e3.search.mapper;

import java.util.List;

import com.e3.shop.pojo.SearchItem;

public interface SearchItemMapper {
	public List<SearchItem> dataImport();

	public SearchItem findIndexByID(Long itemId);
}
