package com.dlut.simpleforum.interceptor;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;

import com.dlut.simpleforum.entity.User.UserRole;
import com.dlut.simpleforum.util.PermissionUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * @author Ishi_O
 * @since
 */
@Component
public class GuestGetInterceptor implements HandlerInterceptor {
	private static final List<String> excludeGetPaths = List.of(
			"/boards",
			"/boards/*",
			"/boards/*/posts");
	private static final AntPathMatcher pathMatcher = new AntPathMatcher();

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String requestPath = request.getServletPath();
		System.out.println(requestPath);
		if (!("GET".equals(request.getMethod())
				&& excludeGetPaths
						.stream()
						.anyMatch(path -> pathMatcher.match(path, requestPath)))) {
			HttpSession session = request.getSession(false);
			PermissionUtils.isRoleThenThrow(UserRole.GUEST, session.getAttribute("userRole"));
		}
		return true;
	}
}
