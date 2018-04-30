package com.e3.user.service.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.e3.shop.common.E3mallResult;
import com.e3.shop.common.JsonUtils;
import com.e3.shop.mapper.TbUserMapper;
import com.e3.shop.pojo.TbUser;
import com.e3.shop.pojo.TbUserExample;
import com.e3.shop.pojo.TbUserExample.Criteria;
import com.e3.user.service.JedisService;
import com.e3.user.service.UserService;

import redis.clients.jedis.JedisCluster;
@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private TbUserMapper tbUserMapper;
	@Autowired
	private JedisService jedisService;
	@Value("${SESSION_KEY}")
	private String SESSION_KEY;
	@Value("${SESSION_EXPIRE}")
	private int SESSION_EXPIRE;
	@Override
	public E3mallResult dataCheck(String param, Integer type) {
		TbUserExample tbUserExample = new TbUserExample();
		Criteria criteria = tbUserExample.createCriteria();
		if(type==1){
			//param 是用户名
			criteria.andUsernameEqualTo(param);
		}else if(type ==2){
			//param是手机号
			criteria.andPhoneEqualTo(param);
		}else if(type == 3){
			//param是邮箱号
			criteria.andEmailEqualTo(param);
		}
		List<TbUser> list = tbUserMapper.selectByExample(tbUserExample);
		if(list==null || list.isEmpty()){
			return E3mallResult.ok(true);
		}
		return E3mallResult.ok(false);
	}
	@Override
	public E3mallResult register(TbUser tbUser) {
		try {
			Date date = new Date();
			tbUser.setCreated(date);
			tbUser.setUpdated(date);
			String password = tbUser.getPassword();
			password = DigestUtils.md5DigestAsHex(password.getBytes());
			tbUser.setPassword(password);
			tbUserMapper.insert(tbUser);
		} catch (Exception e) {
			e.printStackTrace();
			return E3mallResult.build(400, "注册失败,请稍后重试", null);
		}
		return E3mallResult.ok();
	}
	@Override
	public E3mallResult login(String username, String password) {
		TbUserExample tbUserExample = new TbUserExample();
		Criteria criteria = tbUserExample.createCriteria();
		criteria.andUsernameEqualTo(username);
		List<TbUser> list = tbUserMapper.selectByExample(tbUserExample);
		if(list==null || list.isEmpty()){
			return E3mallResult.build(400, "用户名不存在", null);
		}
		TbUser tbUser = list.get(0);
		String md5 = DigestUtils.md5DigestAsHex(password.getBytes());
		if(!md5.equals(tbUser.getPassword())){
			return E3mallResult.build(400, "用户名或密码错误", null);
		}
		//登录成功
		String token = UUID.randomUUID().toString();
		tbUser.setPassword(null);
		jedisService.set(SESSION_KEY+":"+token, JsonUtils.objectToJson(tbUser));
		jedisService.expire(SESSION_KEY+":"+token, SESSION_EXPIRE);
		return E3mallResult.ok(token);
	}
	@Override
	public E3mallResult userCheck(String token) {
		String userInfo = jedisService.get(SESSION_KEY+":"+token);
		if(StringUtils.isNotBlank(userInfo)){
			TbUser tbUser = JsonUtils.jsonToPojo(userInfo, TbUser.class);
			jedisService.expire(SESSION_KEY+":"+token, SESSION_EXPIRE);
			return E3mallResult.ok(tbUser);
		}
		return E3mallResult.build(201, "登录已过期,请重新登录");
	}
	
}
