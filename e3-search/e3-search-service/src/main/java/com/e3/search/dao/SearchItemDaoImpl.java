package com.e3.search.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.e3.shop.pojo.SearchItem;
import com.e3.shop.pojo.SearchResult;
@Repository
public class SearchItemDaoImpl implements SearchItemDao{
	@Autowired
	private SolrServer solrServer;

	@Override
	public SearchResult queryItemIndex(SolrQuery solrQuery) {
		//封裝搜索結果集
		SearchResult result = new SearchResult();
		//商品集
		List<SearchItem> list = new ArrayList<>();
		try {
			QueryResponse response = solrServer.query(solrQuery);
			SolrDocumentList results = response.getResults();
			long count = results.getNumFound();
			//查询总条数
			result.setRecordCount(count);
			Map<String, Map<String, List<String>>> map = response.getHighlighting();
			for(SolrDocument solrDocument : results){
				SearchItem searchItem = new SearchItem();
				String id = (String) solrDocument.get("id");
				searchItem.setId(Long.parseLong(id));
				String title = (String) solrDocument.get("item_title");
				//高亮查询
				Map<String, List<String>> maps = map.get(id);
				List<String> listHight = maps.get("item_title");
				if(listHight!=null && listHight.size()>0){
					title = listHight.get(0);
				}
				searchItem.setTitle(title);
				String sell_point = (String) solrDocument.get("item_sell_point");
				searchItem.setSell_point(sell_point);
				Long price = (Long) solrDocument.get("item_price");
				searchItem.setPrice(price);
				String image = (String) solrDocument.get("item_image");
				searchItem.setImage(image);
				String category_name = (String) solrDocument.get("item_category_name");
				searchItem.setCategory_name(category_name);
				String desc = (String) solrDocument.get("item_desc");
				searchItem.setItem_desc(desc);
				list.add(searchItem);
			}
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		result.setList(list);
		return result;
	}

}
