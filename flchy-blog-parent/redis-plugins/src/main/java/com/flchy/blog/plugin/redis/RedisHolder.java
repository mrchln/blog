package com.flchy.blog.plugin.redis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Properties;

import com.flchy.blog.plugin.redis.util.HttpManager;
import com.flchy.blog.plugin.redis.util.StringUtil;

import redis.clients.jedis.BinaryJedisCluster;

public class RedisHolder {
	public static final String NAME = "/redis.properties";

	public static Properties getProperties() {
		Properties props = new Properties();
		try {
			InputStream ips = RedisHolder.class.getResourceAsStream(NAME);
			BufferedReader ipss = new BufferedReader(new InputStreamReader(ips));
			props.load(ipss);
			return props;
		} catch (IOException e) {
			System.out.println("读properties文件出错:" + e.getMessage());
			return null;
		}
	}
	public static BinaryJedisCluster getJedisCluster(){
		return getJedisCluster(null,null);
	}

	public static BinaryJedisCluster getJedisCluster(String redisCluster, String requirePass) {
		Properties props = getProperties();
		if (StringUtil.isNullOrEmpty(redisCluster)) {
			redisCluster = props.getProperty("plugin.redis.address");
		}
		if (StringUtil.isNullOrEmpty(requirePass)) {
			requirePass = props.getProperty("plugin.redis.password");
		}
		Map<String, Object> loginRedisMap = HttpManager.getInstance().login(redisCluster, requirePass);

		if (null == loginRedisMap || loginRedisMap.isEmpty()) {
			return null;
		}
		return (BinaryJedisCluster) loginRedisMap.get("jedisCluster");
	}
	
	
}
