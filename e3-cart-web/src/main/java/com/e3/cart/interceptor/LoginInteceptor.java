package com.e3.cart.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.e3.shop.common.CookieUtils;
import com.e3.shop.common.E3mallResult;
import com.e3.shop.pojo.TbUser;
import com.e3.user.service.UserService;

public class LoginInteceptor implements HandlerInterceptor{
	@Autowired
	private UserService userService;
	@Value("${TOKEN_KEY}")
	private String TOKEN_KEY;
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String token = CookieUtils.getCookieValue(request, TOKEN_KEY);
		if(StringUtils.isNotBlank(token)){
			E3mallResult e3mallResult = userService.userCheck(token);
			if(e3mallResult.getStatus()==200){
				TbUser tbUser = (TbUser) e3mallResult.getData();
				request.setAttribute("user", tbUser);
			}
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

}
