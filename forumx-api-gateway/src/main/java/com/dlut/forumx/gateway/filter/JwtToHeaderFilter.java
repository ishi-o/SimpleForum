package com.dlut.forumx.gateway.filter;

import java.util.List;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.GatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
public class JwtToHeaderFilter implements GatewayFilterFactory<JwtToHeaderFilter.Config> {
	@Data
	public static class Config {
		private String userIdClaim = "sub";
		private String usernameClaim = "preferred_username";
		private String rolesClaim = "realm_access.roles";
	}

	@Override
	public GatewayFilter apply(Config config) {
		return (exchange, chain) -> {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

			if (authentication != null && authentication.getPrincipal() instanceof Jwt) {
				Jwt jwt = (Jwt) authentication.getPrincipal();

				String userId = jwt.getClaim(config.getUserIdClaim());
				String username = jwt.getClaim(config.getUsernameClaim());
				List<String> roles = jwt.getClaim(config.getRolesClaim());

				ServerHttpRequest request = exchange.getRequest().mutate()
						.header("X-User-Id", userId)
						.header("X-Username", username)
						.header("X-User-Roles", String.join(",", roles))
						.header("X-Auth-Time", jwt.getClaim("auth_time"))
						.build();

				return chain.filter(exchange.mutate().request(request).build());
			}

			return chain.filter(exchange);
		};
	}

}
