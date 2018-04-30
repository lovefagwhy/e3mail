package com.e3.content.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.e3.content.service.ContentCategoryService;
import com.e3.shop.common.E3mallResult;
import com.e3.shop.common.EasyUITreeNode;
import com.e3.shop.mapper.TbContentCategoryMapper;
import com.e3.shop.pojo.TbContentCategory;
import com.e3.shop.pojo.TbContentCategoryExample;
import com.e3.shop.pojo.TbContentCategoryExample.Criteria;
import com.github.pagehelper.PageHelper;
@Service
public class ContentCategoryServiceImpl  implements ContentCategoryService{
	@Autowired
	private TbContentCategoryMapper tbContentCategoryMapper;
	
	public List<EasyUITreeNode> getContentCategoryByParentId(Long parentId){
		List<EasyUITreeNode> result = new ArrayList<>();
		TbContentCategoryExample tbContentCategoryExample = new TbContentCategoryExample();
		Criteria criteria = tbContentCategoryExample.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		List<TbContentCategory> list = tbContentCategoryMapper.selectByExample(tbContentCategoryExample);
		for (TbContentCategory tb : list) {
			EasyUITreeNode easyUITreeNode = new EasyUITreeNode();
			easyUITreeNode.setId(tb.getId());
			easyUITreeNode.setState(tb.getIsParent()?"closed":"open");
			easyUITreeNode.setText(tb.getName());
			result.add(easyUITreeNode);
		}
		return result;
	}

	@Override
	public E3mallResult createContentCategoryByParentId(Long parentId, String name) {
		TbContentCategory tbContentCategory = new TbContentCategory();
		Date date = new Date();
		tbContentCategory.setCreated(date);
		tbContentCategory.setIsParent(false);
		tbContentCategory.setName(name);
		tbContentCategory.setStatus(1);
		tbContentCategory.setParentId(parentId);
		tbContentCategory.setSortOrder(1);
		tbContentCategory.setUpdated(date);
		tbContentCategoryMapper.insert(tbContentCategory);
		//判断是否是父节点.不是的话,更改
		TbContentCategory contentCategory = tbContentCategoryMapper.selectByPrimaryKey(parentId);
		if(!contentCategory.getIsParent()){
			contentCategory.setIsParent(true);
			tbContentCategoryMapper.updateByPrimaryKey(contentCategory);
		}
		return E3mallResult.ok(tbContentCategory);
	}

}
