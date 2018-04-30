package com.e3.shop.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.activemq.command.ActiveMQTopic;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.e3.shop.common.DataGridResult;
import com.e3.shop.common.E3mallResult;
import com.e3.shop.common.EasyUITreeNode;
import com.e3.shop.common.IDUtils;
import com.e3.shop.common.JsonUtils;
import com.e3.shop.mapper.TbItemCatMapper;
import com.e3.shop.mapper.TbItemDescMapper;
import com.e3.shop.mapper.TbItemMapper;
import com.e3.shop.pojo.TbItem;
import com.e3.shop.pojo.TbItemCat;
import com.e3.shop.pojo.TbItemCatExample;
import com.e3.shop.pojo.TbItemCatExample.Criteria;
import com.e3.shop.pojo.TbItemDesc;
import com.e3.shop.pojo.TbItemExample;
import com.e3.shop.service.ItemService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import redis.clients.jedis.JedisCluster;

@Service
public class ItemServiceImpl implements ItemService {
	@Value("${ITEM_DETAIL_CACHE}")
	private String ITEM_DETAIL_CACHE;
	@Value("${ITEM_TIME}")
	private int ITEM_TIME;
	@Autowired
	private JedisCluster JedisCluster;
	@Autowired
	private ActiveMQTopic activeMQTopic;
	@Autowired
	private JmsTemplate JmsTemplate;
	@Autowired
	private TbItemMapper itemMapper;
	@Autowired
	private TbItemCatMapper tbItemCatMapper;
	@Autowired
	private TbItemDescMapper tbItemDescMapper;

	public DataGridResult getList(int page, int rows) {
		// 设置分页 使用PageHelper
		PageHelper.startPage(page, rows);
		List<TbItem> list = itemMapper.selectByExample(new TbItemExample());
		PageInfo<TbItem> pageInfo = new PageInfo<>(list);
		DataGridResult result = new DataGridResult();
		result.setTotal(pageInfo.getTotal());
		result.setRows(pageInfo.getList());
		return result;
	}

	public List<EasyUITreeNode> getCatList(long parentId) {
		TbItemCatExample tbItemCatExample = new TbItemCatExample();
		Criteria criteria = tbItemCatExample.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		List<TbItemCat> list = tbItemCatMapper.selectByExample(tbItemCatExample);
		List<EasyUITreeNode> results = new ArrayList<>();
		for (TbItemCat tbItemCat : list) {
			EasyUITreeNode easyUITreeNode = new EasyUITreeNode();
			easyUITreeNode.setId(tbItemCat.getId());
			easyUITreeNode.setText(tbItemCat.getName());
			easyUITreeNode.setState(tbItemCat.getIsParent() ? "closed" : "open");
			results.add(easyUITreeNode);
		}
		return results;
	}

	@Override
	public E3mallResult saveItem(TbItem item, TbItemDesc desc) {
		final long itemId = IDUtils.genItemId();
		item.setId(itemId);
		item.setStatus((byte) 1);
		Date date = new Date();
		item.setCreated(date);
		item.setUpdated(date);
		itemMapper.insert(item);
		desc.setCreated(date);
		desc.setItemId(itemId);
		tbItemDescMapper.insert(desc);
		JmsTemplate.send(activeMQTopic, new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				return session.createTextMessage(itemId + "");
			}
		});
		return E3mallResult.ok();
	}

	@Override
	public TbItem findItemByID(Long itemId) {
		try {
			String json = JedisCluster.get(ITEM_DETAIL_CACHE + itemId + ":tbItem");
			if (StringUtils.isNotBlank(json)) {
				TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
				return tbItem;
			}
			TbItem tbItem = itemMapper.selectByPrimaryKey(itemId);
			JedisCluster.set(ITEM_DETAIL_CACHE + itemId + ":tbItem", JsonUtils.objectToJson(tbItem));
			JedisCluster.expire(ITEM_DETAIL_CACHE + itemId + ":tbItem", ITEM_TIME);
			return tbItem;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public TbItemDesc findItemDescByID(Long itemId) {
		try {
			String json = JedisCluster.get(ITEM_DETAIL_CACHE + itemId + ":tbItemDesc");
			if (StringUtils.isNotBlank(json)) {
				TbItemDesc tbItemDesc = JsonUtils.jsonToPojo(json, TbItemDesc.class);
				return tbItemDesc;
			}
			TbItemDesc tbItemDesc = tbItemDescMapper.selectByPrimaryKey(itemId);
			JedisCluster.set(ITEM_DETAIL_CACHE + itemId + ":tbItemDesc", JsonUtils.objectToJson(tbItemDesc));
			JedisCluster.expire(ITEM_DETAIL_CACHE + itemId + ":tbItemDesc", ITEM_TIME);
			return tbItemDesc;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
