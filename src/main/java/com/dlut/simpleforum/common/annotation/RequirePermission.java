package com.dlut.simpleforum.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.dlut.simpleforum.common.enums.ABACPermission;
import com.dlut.simpleforum.common.enums.RBACPermission;

/**
 * @author Ishi_O
 * @since
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequirePermission {
	RBACPermission[] permissions() default {};

	RequireAbacPermission[] attributes() default {};

	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface RequireAbacPermission {
		ABACPermission value();

		String resourceId();
	}
}
