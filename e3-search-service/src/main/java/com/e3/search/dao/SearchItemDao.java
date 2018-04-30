package com.e3.search.dao;

import org.apache.solr.client.solrj.SolrQuery;

import com.e3.shop.pojo.SearchResult;

public interface SearchItemDao {
	SearchResult queryItemIndex(SolrQuery solrQuery);
}
