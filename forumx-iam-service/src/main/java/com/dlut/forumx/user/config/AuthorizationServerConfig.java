package com.dlut.forumx.user.config;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Arrays;
import java.util.UUID;

import org.apache.hc.core5.http.HttpHeaders;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.dlut.forumx.user.security.converter.CodeRequestAuthenticationConverter;
import com.dlut.forumx.user.security.provider.CodeRequestAuthenticationProvider;
import com.dlut.forumx.user.security.provider.EmailAuthenticationProvider;
import com.dlut.forumx.user.security.provider.PasswordAuthenticationProvider;
import com.dlut.forumx.user.security.provider.SmsAuthenticationProvider;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class AuthorizationServerConfig {

	private final CodeRequestAuthenticationProvider codeRequestAuthenticationProvider;
	private final PasswordAuthenticationProvider passwordAuthenticationProvider;
	private final SmsAuthenticationProvider smsAuthenticationProvider;
	private final EmailAuthenticationProvider emailAuthenticationProvider;

	@Bean
	@Order(1)
	public SecurityFilterChain oauth2FilterChain(HttpSecurity http, CorsConfigurationSource corsConfigurationSource)
			throws Exception {
		OAuth2AuthorizationServerConfigurer authorizationServerConfigurer = OAuth2AuthorizationServerConfigurer
				.authorizationServer();
		http
				.formLogin(form -> form.disable())
				.httpBasic(basic -> basic.disable())
				.csrf(csrf -> csrf.disable())
				.headers(headers -> headers
						.frameOptions(frame -> frame.disable())
						.contentTypeOptions(cto -> {
						})
						.xssProtection(xss -> xss.disable()))
				.sessionManagement(session -> session
						.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
						.sessionFixation()
						.migrateSession())
				.cors(cors -> cors.configurationSource(corsConfigurationSource))
				.securityMatcher(authorizationServerConfigurer.getEndpointsMatcher())
				.with(authorizationServerConfigurer,
						(cfg) -> {
							cfg.authorizationEndpoint(authEndpoint -> {
								authEndpoint
										.authorizationRequestConverter(new CodeRequestAuthenticationConverter())
										.authenticationProvider(codeRequestAuthenticationProvider)
										.authenticationProvider(passwordAuthenticationProvider)
										.authenticationProvider(smsAuthenticationProvider)
										.authenticationProvider(emailAuthenticationProvider);
							});
							cfg.oidc(Customizer.withDefaults());
						})
				.authorizeHttpRequests((authorize) -> authorize.anyRequest().authenticated());

		return http.build();
	}

	@Bean
	@Order(2)
	public SecurityFilterChain defaultFilterChain(HttpSecurity http, CorsConfigurationSource corsConfigurationSource)
			throws Exception {
		http
				.oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
				.formLogin(form -> form.disable())
				.httpBasic(basic -> basic.disable())
				.csrf(csrf -> csrf.disable())
				.headers(headers -> headers
						.frameOptions(frame -> frame.disable())
						.contentTypeOptions(cto -> {
						})
						.xssProtection(xss -> xss.disable()))
				.cors(cors -> cors.configurationSource(corsConfigurationSource))
				.sessionManagement(session -> session
						.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/auth/**").permitAll()
						.requestMatchers("/error").permitAll()
						.requestMatchers("/actuator/health", "/actuator/info").permitAll()
						.anyRequest()
						.authenticated());
		return http.build();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowedOrigins(Arrays.asList(
				"http://localhost:8848",
				"http://api-gateway:8848"));
		config.setAllowedMethods(Arrays.asList(
				"GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
		config.setAllowedHeaders(Arrays.asList(
				HttpHeaders.AUTHORIZATION,
				HttpHeaders.CONTENT_TYPE,
				HttpHeaders.ACCEPT,
				"Origin",
				"X-Requested-With",
				"X-CSRF-Token"));
		config.setExposedHeaders(Arrays.asList(HttpHeaders.AUTHORIZATION, "X-Total-Count"));
		config.setAllowCredentials(true);
		config.setMaxAge(3600L);
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		return source;
	}

	@Bean
	public RegisteredClientRepository registeredClientRepository() {
		RegisteredClient client = RegisteredClient.withId(UUID.randomUUID().toString())
				.clientId("forumx-client")
				.clientSecret("{noop}secret")
				.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
				.authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
				.scope(OidcScopes.OPENID)
				.scope(OidcScopes.PROFILE)
				.clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
				.build();
		return new InMemoryRegisteredClientRepository(client);
	}

	@Bean
	public JWKSource<SecurityContext> jwkSource() {
		KeyPair keyPair = generateRsaKey();
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		RSAKey rsaKey = new RSAKey.Builder(publicKey)
				.privateKey(privateKey)
				.keyID(UUID.randomUUID().toString())
				.build();
		JWKSet jwkSet = new JWKSet(rsaKey);
		return new ImmutableJWKSet<>(jwkSet);
	}

	private static KeyPair generateRsaKey() {
		KeyPair keyPair;
		try {
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
			keyPairGenerator.initialize(2048);
			keyPair = keyPairGenerator.generateKeyPair();
		} catch (Exception ex) {
			throw new IllegalStateException(ex);
		}
		return keyPair;
	}

	@Bean
	public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
		return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
	}

	@Bean
	public AuthorizationServerSettings authorizationServerSettings() {
		return AuthorizationServerSettings.builder().build();
	}
}
