package com.dlut.simpleforum.interceptor;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.dlut.simpleforum.dto.response.ApiResponse;
import com.dlut.simpleforum.dto.response.ErrorResponse;
import com.dlut.simpleforum.util.MessageSourceUtils;
import com.dlut.simpleforum.util.ObjectMapperUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class AuthInterceptor implements HandlerInterceptor {

	public AuthInterceptor() {
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		HttpSession session = request.getSession(false);
		if (session == null) {
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			response.getWriter()
					.write(ObjectMapperUtils.writeValueAsString(
							ApiResponse.failure(
									ErrorResponse
											.builder()
											.message(MessageSourceUtils.getMessage("error.unauthorized", null,
													request.getLocale()))
											.build())));
			return false;
		}
		return true;
	}
}
