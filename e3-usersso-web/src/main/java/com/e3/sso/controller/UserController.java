package com.e3.sso.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.e3.shop.common.CookieUtils;
import com.e3.shop.common.E3mallResult;
import com.e3.shop.pojo.TbUser;
import com.e3.user.service.UserService;

@Controller
public class UserController {
	@Autowired
	private UserService userSerivce;
	@Value("${TOKEN_KEY}")
	private String TOKEN_KEY;
	@Value("${TOKEN_KEY_TIME}")
	private int TOKEN_KEY_TIME;
	@RequestMapping("/user/check/{param}/{type}")
	@ResponseBody
	public E3mallResult dataCheck(@PathVariable String param,@PathVariable Integer type){
		return userSerivce.dataCheck(param, type);
	} 
	@RequestMapping("/user/register")
	@ResponseBody
	public E3mallResult register(TbUser tbUser){
		return userSerivce.register(tbUser);
	}
	@RequestMapping("/user/login")
	@ResponseBody
	public E3mallResult login(HttpServletRequest request,HttpServletResponse response,String username,String password){
		E3mallResult login = userSerivce.login(username,password);
		if(login.getStatus()==200){
			CookieUtils.setCookie(request, response, TOKEN_KEY, login.getData().toString(),TOKEN_KEY_TIME,true);
		}
		return login;
	}
	@RequestMapping("/user/check/{token}")
	@ResponseBody
	public Object userCheck(@PathVariable String token,String callback){
		E3mallResult e3mallResult = userSerivce.userCheck(token);
		if(StringUtils.isBlank(callback)){
			return e3mallResult;
		}
		//否则是一个跨域请求
		//返回json格式就是必须是callback(json) callback(userCheck)
		MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(e3mallResult);
		mappingJacksonValue.setJsonpFunction(callback);
		return mappingJacksonValue;
	}
}
