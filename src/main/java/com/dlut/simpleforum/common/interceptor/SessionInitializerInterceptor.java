package com.dlut.simpleforum.common.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.dlut.simpleforum.common.session.SessionUser;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * @author Ishi_O
 * @since
 */
@Component
public class SessionInitializerInterceptor implements HandlerInterceptor {
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		HttpSession session = request.getSession();
		if (session.getAttribute("sessionUser") == null) {
			session.setAttribute("sessionUser", SessionUser.createDefaultSessionUser());
		}
		return true;
	}
}
