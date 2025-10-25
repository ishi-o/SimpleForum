package com.dlut.simpleforum.common.aspect;

import java.util.List;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.dlut.simpleforum.common.annotation.RequireRole;
import com.dlut.simpleforum.common.enums.UserRole;
import com.dlut.simpleforum.common.session.SessionManager;

/**
 * @author Ishi_O
 * @since
 */
@Aspect
@Component
public class RoleCheckAspect {
	@Around("@annotation(requireRole)")
	public Object checkRole(ProceedingJoinPoint pjp, RequireRole requireRole) throws Throwable {
		List<UserRole> include = List.of(requireRole.include()), exclude = List.of(requireRole.exclude());
		UserRole currRole = SessionManager.getSessionUser().getUserRole();
		if (!include.contains(currRole) || exclude.contains(currRole)) {
			throw new IllegalArgumentException("");
		}
		return pjp.proceed();
	}
}
