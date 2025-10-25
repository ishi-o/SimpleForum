package com.dlut.simpleforum.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.dlut.simpleforum.common.enums.UserRole;

/**
 * @author Ishi_O
 * @since
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequireRole {
	UserRole[] include() default {};

	UserRole[] exclude() default {};
}
