package com.flchy.blog.plugin.redis.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class RedisUtil {
	public static String convertObject(String obj) {
		return null;
	}
	public static void main(String[] args) {
		String str="{2w34234=sdfdf, 234=ssssssssssssssssssssss}";
	JSONObject json=JSONObject.parseObject(str);
	JSONArray arr=JSONArray.parseArray(str);
	}
}
