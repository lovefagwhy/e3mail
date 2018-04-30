package com.e3.user.service;

import com.e3.shop.common.E3mallResult;
import com.e3.shop.pojo.TbUser;

public interface UserService {
	//检查用户名. 邮箱 手机等是否已存在
	public E3mallResult dataCheck(String param,Integer type);
	//注册
	public E3mallResult register(TbUser tbUser);
	//登录
	public E3mallResult login(String username,String password);
	//根据token查询用户
	public E3mallResult userCheck(String token);
}
