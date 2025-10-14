package com.dlut.simpleforum.interceptor;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.dlut.simpleforum.dto.response.ApiResponse;
import com.dlut.simpleforum.dto.response.ErrorResponse;
import com.dlut.simpleforum.entity.User.UserRole;
import com.dlut.simpleforum.util.MessageSourceUtils;
import com.dlut.simpleforum.util.ObjectMapperUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * @author Ishi_O
 * @since
 */
@Component
public class AuthenticationInterceptor implements HandlerInterceptor {
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("userId") == null
				|| (UserRole.GUEST.equals(session.getAttribute("userRole"))
						&& "/auth/me".equals(request.getServletPath()))) {
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			response.getWriter()
					.write(ObjectMapperUtils.writeValueAsString(
							ApiResponse.failure(
									ErrorResponse
											.builder()
											.message(MessageSourceUtils.getMessage("http.401", null))
											.build())));
			return false;
		}
		return true;
	}
}
