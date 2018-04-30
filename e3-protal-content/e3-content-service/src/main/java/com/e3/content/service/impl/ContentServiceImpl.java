package com.e3.content.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.e3.content.service.ContentService;
import com.e3.content.service.JedisService;
import com.e3.shop.common.ADItem;
import com.e3.shop.common.DataGridResult;
import com.e3.shop.common.E3mallResult;
import com.e3.shop.common.EasyUITreeNode;
import com.e3.shop.common.JsonUtils;
import com.e3.shop.mapper.TbContentMapper;
import com.e3.shop.pojo.TbContent;
import com.e3.shop.pojo.TbContentCategory;
import com.e3.shop.pojo.TbContentExample;
import com.e3.shop.pojo.TbContentExample.Criteria;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
@Service
public class ContentServiceImpl implements ContentService {
	@Value("${INDEX_CACHE}")
	private String INDEX_CACHE;
	@Autowired
	private JedisService jedisService;
	@Autowired
	private TbContentMapper tbContentMapper;
	@Override
	public DataGridResult getContentList(Long categoryId, Integer page, Integer rows) {
		TbContentExample tbContentExample = new TbContentExample();
		Criteria criteria = tbContentExample.createCriteria();
		criteria.andCategoryIdEqualTo(categoryId);
		PageHelper.startPage(page, rows);
		//如果需要查询大字段值.就是用 selectByExampleWithBLOBs 方法
//		List<TbContent> list = tbContentMapper.selectByExampleWithBLOBs(tbContentExample);
		List<TbContent> list = tbContentMapper.selectByExample(tbContentExample);
		PageInfo<TbContent> pageInfo = new PageInfo<TbContent>(list);
		DataGridResult dataGridResult = new DataGridResult();
		dataGridResult.setRows(list);
		dataGridResult.setTotal(pageInfo.getTotal());
		return dataGridResult;
	}
	@Override
	public E3mallResult saveContent(TbContent tbContent) {
		//添加广告位的时候,把原有的缓存去掉.实现了缓存同步
		jedisService.hdel(INDEX_CACHE, tbContent.getCategoryId()+"");
		Date date =  new Date();
		tbContent.setCreated(date);
		tbContent.setUpdated(date);
		tbContentMapper.insert(tbContent);
		return E3mallResult.ok();
	}
	@Override
	public List<ADItem> getADList(Long categoryId) {
		try {
			String json = jedisService.hget(INDEX_CACHE, categoryId+"");
			if(StringUtils.isNotBlank(json)){
				List<ADItem> jsonToList = JsonUtils.jsonToList(json, ADItem.class);
				return jsonToList;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<ADItem> list  = new ArrayList<>();
		TbContentExample tbContentExample = new TbContentExample();
		Criteria criteria = tbContentExample.createCriteria();
		criteria.andCategoryIdEqualTo(categoryId);
		List<TbContent> listContent = tbContentMapper.selectByExampleWithBLOBs(tbContentExample);
		for (TbContent tbContent : listContent) {
			ADItem adItem = new ADItem();
			adItem.setAlt(tbContent.getTitle());
			adItem.setHref(tbContent.getUrl());
			adItem.setSrc(tbContent.getPic());
			adItem.setSrcB(tbContent.getPic2());
			list.add(adItem);
		}
		String objectToJson = JsonUtils.objectToJson(list);
		jedisService.hset(INDEX_CACHE, categoryId+"", objectToJson);
		return list;
	}

}
