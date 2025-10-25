package com.dlut.simpleforum.common.interceptor;

import com.dlut.simpleforum.common.enums.ABACPermission;

/**
 * @author Ishi_O
 * @since
 */
public interface AbacPermissionChecker {
	boolean supports(ABACPermission abacPermission);

	void checkPermission(Long resourceId);
}
