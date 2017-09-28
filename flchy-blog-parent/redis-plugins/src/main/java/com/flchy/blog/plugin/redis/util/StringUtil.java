package com.flchy.blog.plugin.redis.util;

import java.util.List;
import java.util.Map;

public class StringUtil {

	public static boolean isNullOrEmpty(String str) {
		boolean flag = false;
		if (null == str || "".equals(str.trim()) || "n/a".equals(str.trim().toLowerCase()) || "null".equals(str.trim().toLowerCase()) || "undefined".equals(str.trim().toLowerCase())) {
			flag = true;
		}
		return flag;
	}

	public static boolean isNullOrEmpty(Map str) {
		boolean flag = false;
		if (null == str || str.isEmpty()) {
			flag = true;
		}
		return flag;
	}

	public static boolean isNullOrEmpty(Object str) {
		return isNullOrEmpty(String.valueOf(str));
	}

	public static boolean isNullOrEmpty(String[] strs) {
		boolean flag = false;
		if (null != strs && strs.length > 0) {
			for (String str : strs) {
				if (null == str || "".equals(str.trim()) || "n/a".equals(str.trim().toLowerCase()) || "null".equals(str.trim().toLowerCase()) || "undefined".equals(str.trim().toLowerCase())) {
					flag = true;
					break;
				}
			}
		}
		return flag;
	}

	public static boolean isNullOrEmpty(List<String> strs) {
		boolean flag = false;
		if (null != strs && strs.size() > 0) {
			for (String str : strs) {
				if (null == str || "".equals(str.trim()) || "n/a".equals(str.trim().toLowerCase()) || "null".equals(str.trim().toLowerCase()) || "undefined".equals(str.trim().toLowerCase())) {
					flag = true;
				}
			}
		}
		return flag;
	}
}
