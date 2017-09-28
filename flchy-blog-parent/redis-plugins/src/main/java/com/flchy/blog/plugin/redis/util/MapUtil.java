package com.flchy.blog.plugin.redis.util;

import java.util.HashMap;
import java.util.Map;

public class MapUtil {
	private Map<String, Object> collect = new HashMap<>();

	public MapUtil() {
	}

	public MapUtil(String key, Object value) {
		collect.put(key, value);
	}

	public MapUtil set(String key, Object value) {
		collect.put(key, value);
		return this;
	}

	public Map<String, Object> get() {
		return collect;
	}
}
