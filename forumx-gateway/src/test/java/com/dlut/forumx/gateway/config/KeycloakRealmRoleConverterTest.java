package com.dlut.forumx.gateway.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

class KeycloakRealmRoleConverterTest {

	private final KeycloakRealmRoleConverter converter = new KeycloakRealmRoleConverter();

	@Test
	void shouldReturnRolesWhenRealmAccessExists() {
		Jwt jwt = mock(Jwt.class);

		Map<String, Object> realmAccess = new HashMap<>();
		realmAccess.put("roles", List.of("admin", "user"));

		when(jwt.getClaimAsMap("realm_access")).thenReturn(realmAccess);

		Collection<GrantedAuthority> authorities = converter.convert(jwt);

		assertThat(authorities)
				.hasSize(2)
				.extracting(GrantedAuthority::getAuthority)
				.containsExactlyInAnyOrder("ROLE_ADMIN", "ROLE_USER");
	}

	@Test
	void shouldReturnEmptyWhenRealmAccessIsNull() {
		Jwt jwt = mock(Jwt.class);
		when(jwt.getClaimAsMap("realm_access")).thenReturn(null);
		Collection<GrantedAuthority> authorities = converter.convert(jwt);
		assertThat(authorities).isEmpty();
	}

	@Test
	void shouldReturnEmptyWhenRolesIsMissing() {
		Jwt jwt = mock(Jwt.class);
		Map<String, Object> realmAccess = new HashMap<>();
		when(jwt.getClaimAsMap("realm_access")).thenReturn(realmAccess);
		Collection<GrantedAuthority> authorities = converter.convert(jwt);
		assertThat(authorities).isEmpty();
	}

	@Test
	void shouldReturnEmptyWhenRolesIsEmpty() {
		Jwt jwt = mock(Jwt.class);
		Map<String, Object> realmAccess = new HashMap<>();
		realmAccess.put("roles", List.of());
		when(jwt.getClaimAsMap("realm_access")).thenReturn(realmAccess);
		Collection<GrantedAuthority> authorities = converter.convert(jwt);
		assertThat(authorities).isEmpty();
	}
}
