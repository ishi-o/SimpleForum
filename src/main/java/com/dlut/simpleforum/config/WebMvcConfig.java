package com.dlut.simpleforum.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.dlut.simpleforum.interceptor.AuthenticationInterceptor;
import com.dlut.simpleforum.interceptor.GuestGetInterceptor;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
	private final AuthenticationInterceptor authInterceptor;
	private final GuestGetInterceptor guestGetInterceptor;

	public WebMvcConfig(AuthenticationInterceptor authInterceptor, GuestGetInterceptor guestGetInterceptor) {
		this.authInterceptor = authInterceptor;
		this.guestGetInterceptor = guestGetInterceptor;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(authInterceptor)
				.addPathPatterns("/**")
				.excludePathPatterns(
						"/auth/register",
						"/auth/login",
						"/auth/logout",
						"/auth/guest",
						"/error")
				.order(1);
		registry.addInterceptor(guestGetInterceptor)
				.addPathPatterns("/**")
				.excludePathPatterns(
						"/auth/login",
						"/auth/register",
						"/auth/guest",
						"/error")
				.order(2);
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
				.allowedOriginPatterns("http://localhost:*")
				.allowedMethods("*")
				.allowedHeaders("*")
				.allowCredentials(true)
				.maxAge(3600);
	}
}
