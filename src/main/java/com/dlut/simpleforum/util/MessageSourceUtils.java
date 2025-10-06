package com.dlut.simpleforum.util;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
public class MessageSourceUtils {
	private static MessageSource MESSAGE_SOURCE;

	private MessageSourceUtils(MessageSource messageSource) {
		MESSAGE_SOURCE = messageSource;
	}

	public static String getMessage(String code, Object[] args) {
		return MESSAGE_SOURCE.getMessage(code, args, LocaleContextHolder.getLocale());
	}

	public static String getMessage(String code, Object[] args, Locale locale) {
		return MESSAGE_SOURCE.getMessage(code, args, locale);
	}
}
