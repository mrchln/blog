package com.flchy.blog;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.message.GZipEncoder;
import org.glassfish.jersey.message.filtering.EntityFilteringFeature;
import org.glassfish.jersey.moxy.json.MoxyJsonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.web.filter.RequestContextFilter;

import com.fasterxml.jackson.databind.ser.FilterProvider;

import org.glassfish.jersey.server.filter.EncodingFilter;

public class JerseyConfig extends ResourceConfig {
	public JerseyConfig() {

		// 配置restful package.
		packages("com.flchy.blog");
		register(RequestContextFilter.class);
//		register(JacksonFeature.class);
		register(MultiPartFeature.class);
		register(FilterProvider.class);
		// 注册 JSON 转换器
		
		register(MoxyJsonFeature.class);
//		register(JacksonJsonProvider.class);
//		EncodingFilter.enableFor(this, GZipEncoder.class);	
	}
}
