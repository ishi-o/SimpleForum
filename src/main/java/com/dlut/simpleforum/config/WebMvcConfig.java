package com.dlut.simpleforum.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.dlut.simpleforum.interceptor.AuthInterceptor;
import com.dlut.simpleforum.interceptor.PermissionInterceptor;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
	private final AuthInterceptor authInterceptor;
	private final PermissionInterceptor permissionInteceptor;

	public WebMvcConfig(AuthInterceptor authInterceptor, PermissionInterceptor permissionInterceptor) {
		this.authInterceptor = authInterceptor;
		this.permissionInteceptor = permissionInterceptor;
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
		registry.addInterceptor(permissionInteceptor)
				.addPathPatterns("/**")
				.excludePathPatterns(
						"/auth/**",
						"/public/**",
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
