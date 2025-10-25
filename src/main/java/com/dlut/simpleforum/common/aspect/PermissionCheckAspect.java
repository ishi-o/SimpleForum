package com.dlut.simpleforum.common.aspect;

import java.util.List;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.dlut.simpleforum.common.annotation.RequirePermission;
import com.dlut.simpleforum.common.annotation.RequirePermission.RequireAbacPermission;
import com.dlut.simpleforum.common.enums.ABACPermission;
import com.dlut.simpleforum.common.enums.RBACPermission;
import com.dlut.simpleforum.common.enums.UserRole;
import com.dlut.simpleforum.common.interceptor.AbacPermissionChecker;
import com.dlut.simpleforum.common.session.SessionManager;
import com.dlut.simpleforum.common.session.SessionUser;
import com.dlut.simpleforum.utils.MessageSourceUtils;

import lombok.AllArgsConstructor;

/**
 * @author Ishi_O
 * @since
 */
@AllArgsConstructor
@Aspect
@Component
public class PermissionCheckAspect {

	private final Map<ABACPermission, AbacPermissionChecker> abacCheckers;

	@Around("@annotation(requirePermission)")
	public Object checkPermission(ProceedingJoinPoint pjp, RequirePermission requirePermission) throws Throwable {
		SessionUser sessionUser = SessionManager.getSessionUser();
		UserRole userRole = sessionUser.getUserRole();
		List<RBACPermission> permissions = userRole.getPermissions();
		if (!permissions.containsAll(List.of(requirePermission.permissions()))) {
			throw new IllegalArgumentException(MessageSourceUtils.getMessage("error.no-permission", null));
		}
		for (RequireAbacPermission abacPermission : requirePermission.attributes()) {
			abacCheckers.get(abacPermission.value()).checkPermission(Long.parseLong(abacPermission.resourceId()));
		}
		return pjp.proceed();
	}
}
