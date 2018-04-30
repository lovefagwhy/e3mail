package com.e3.search.mq;

import java.io.IOException;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import com.e3.search.mapper.SearchItemMapper;
import com.e3.shop.pojo.SearchItem;

public class ItemAddListener implements MessageListener {
	@Autowired
	private SearchItemMapper searchItemMapper;
	@Autowired
	private SolrServer solrServer;

	@Override
	public void onMessage(Message message) {
		// 获取消息内容
		TextMessage tm = (TextMessage) message;
		Long itemId = null;
		try {
			itemId = Long.parseLong(tm.getText());
			//防止数据库事务未提交,查询索引库没有查询数据
			Thread.sleep(3000);
			SearchItem searchItem = searchItemMapper.findIndexByID(itemId);
			// 创建一个文档对象
			// 创建文档对象，封装查询数据库数据集
			SolrInputDocument doc = new SolrInputDocument();
			// 封装Id
			doc.addField("id", searchItem.getId());
			// 封装商品标题
			doc.addField("item_title", searchItem.getTitle());
			// 封装商品买点
			doc.addField("item_sell_point", searchItem.getSell_point());
			// 封装商品价格
			doc.addField("item_price", searchItem.getPrice());
			// 封装商品图片地址
			doc.addField("item_image", searchItem.getImage());
			// 商品类别
			doc.addField("item_category_name", searchItem.getCategory_name());
			// 商品描述
			doc.addField("item_desc", searchItem.getItem_desc());
			try {
				solrServer.add(doc);
				solrServer.commit();
			} catch (SolrServerException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
