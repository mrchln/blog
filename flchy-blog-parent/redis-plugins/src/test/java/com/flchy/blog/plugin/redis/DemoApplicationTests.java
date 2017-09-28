package com.flchy.blog.plugin.redis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.flchy.blog.plugin.redis.RedisBusines;
import com.flchy.blog.plugin.redis.util.MapUtil;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {
	@Autowired
	ApplicationContext context;

	@Test
	public void contextLoads() throws Exception {
		List<abc> lista = new ArrayList<>();
		abc a = new abc();
		a.setA(1);
		a.setB("ssssssssssssss");
		lista.add(a);
		lista.add(a);
		RedisBusines redisBusiness = (RedisBusines) context.getBean("redisBusines");
		redisBusiness.set("sss1", JSON.toJSON(lista).toString());
		String str = redisBusiness.get("sss1");
		List<abc> l = JSONArray.parseArray(str, abc.class);
		for (abc abc : l) {
			System.out.println(abc.toString());
		}
		System.out.println(str);
	}
	
	@Test
	public void contextLoadsx() throws Exception {
		abc a = new abc();
		a.setA(1);
		a.setB("ssssssssssssss");
		RedisBusines redisBusiness = (RedisBusines) context.getBean("redisBusines");
		redisBusiness.set("sss2", JSON.toJSONString(a).toString());
		String str = redisBusiness.get("sss2");
		abc l = JSON.parseObject(str, abc.class);
			System.out.println(l.toString());
	}


}
