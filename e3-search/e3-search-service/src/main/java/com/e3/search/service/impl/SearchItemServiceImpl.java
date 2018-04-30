package com.e3.search.service.impl;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.e3.search.dao.SearchItemDao;
import com.e3.search.mapper.SearchItemMapper;
import com.e3.search.service.SearchItemService;
import com.e3.shop.common.E3mallResult;
import com.e3.shop.pojo.SearchItem;
import com.e3.shop.pojo.SearchResult;
@Service
public class SearchItemServiceImpl  implements SearchItemService{
	@Autowired
	private SearchItemDao searchItemDao;
	@Autowired
	private SearchItemMapper searchItemMapper;
	@Autowired
	private SolrServer httpSolrServer;
	@Override
	public E3mallResult dataImport() {
		List<SearchItem> list = searchItemMapper.dataImport();
		if(list!=null && list.size()>0){
			for (SearchItem searchItem : list) {
				SolrInputDocument doc = new SolrInputDocument();
				doc.addField("id", searchItem.getId());
				doc.addField("item_title", searchItem.getTitle());
				doc.addField("item_price", searchItem.getPrice());
				doc.addField("item_image", searchItem.getImage());
				doc.addField("item_category_name", searchItem.getCategory_name());
				doc.addField("item_desc", searchItem.getItem_desc());
				try {
					httpSolrServer.add(doc);
					httpSolrServer.commit();
				} catch (SolrServerException | IOException e) {
					e.printStackTrace();
				}
			}
			return E3mallResult.ok();
		}
		return null;
	}
	@Override
	public SearchResult queryItemIndex(String qName, Integer page, Integer rows) {
		SolrQuery solrQuery = new SolrQuery();
		if(StringUtils.isNotBlank(qName)){
			solrQuery.setQuery(qName);
		}else{
			solrQuery.setQuery("*:*");
		}
		solrQuery.set("df","item_keywords");
		int start = (page-1)*rows;
		solrQuery.setStart(start);
		solrQuery.setRows(rows);
		solrQuery.setHighlight(true);
		solrQuery.addHighlightField("item_title");
		solrQuery.setHighlightSimplePre("<font class=\"skcolor_ljg\">");
		solrQuery.setHighlightSimplePost("</font>");
		SearchResult searchResult = searchItemDao.queryItemIndex(solrQuery);
		Long recordCount = searchResult.getRecordCount();
		int pages = (int)(recordCount/rows);
		if(recordCount%rows!=0){
			pages++;
		}
		searchResult.setPageCount(pages);
		searchResult.setCurPage(page);
		return searchResult;
	}
	
}
