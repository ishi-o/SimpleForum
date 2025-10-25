package com.dlut.simpleforum.common.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.dlut.simpleforum.common.enums.ABACPermission;
import com.dlut.simpleforum.common.enums.ABACPermission.ResourceType;
import com.dlut.simpleforum.common.interceptor.AbacPermissionChecker;
import com.dlut.simpleforum.common.interceptor.BoardAbacPermissionChecker;
import com.dlut.simpleforum.common.interceptor.PostAbacPermissionChecker;

/**
 * @author Ishi_O
 * @since
 */
@Configuration
public class PermissionCheckerConfig {
	@Bean
	public Map<ResourceType, AbacPermissionChecker> permissionCheckers(
			BoardAbacPermissionChecker boardAbacPermissionChecker,
			PostAbacPermissionChecker postAbacPermissionChecker) {
		Map<ResourceType, AbacPermissionChecker> ret = new HashMap<>();
		ret.put(ResourceType.BOARD, boardAbacPermissionChecker);
		ret.put(ResourceType.POST, postAbacPermissionChecker);
		return ret;
	}

	@Bean
	public Map<ABACPermission, AbacPermissionChecker> permissionCheckers(
			Map<ResourceType, AbacPermissionChecker> checkers) {
		Map<ABACPermission, AbacPermissionChecker> ret = new HashMap<>();
		for (ABACPermission abacPermission : ABACPermission.values()) {
			ret.put(abacPermission, checkers.get(abacPermission.getResourceType()));
		}
		return ret;
	}
}
