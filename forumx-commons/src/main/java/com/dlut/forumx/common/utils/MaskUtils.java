package com.dlut.forumx.common.utils;

public class MaskUtils {

	public enum MaskType {
		PHONENUMBER,
		EMAIL,
	}

	public static String maskTarget(String target, MaskType type) {
		return switch (type) {
			case MaskType.PHONENUMBER ->
				maskPhonenumber(target);
			case MaskType.EMAIL ->
				maskEmail(target);
		};
	}

	public static String maskPhonenumber(String target) {
		return target.substring(0, 3) + "****" + target.substring(7);
	}

	public static String maskEmail(String target) {
		int atIndex = target.indexOf("@");
		if (atIndex <= 3) {
			return "***@" + target.substring(atIndex + 1);
		}
		return target.substring(0, 3) + "***@" + target.substring(atIndex + 1);
	}
}
