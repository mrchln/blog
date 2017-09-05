package com.tools.plugin.kafka.util;

import java.util.Map;

public class MapUtil {
	
	public static boolean isNullOrEmpty(Map<String, Object> objMap) {
		boolean flag = false;
		if (null == objMap || objMap.isEmpty()) {
			flag = true;
		}
		return flag;
	}
}
