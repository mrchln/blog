package com.flchy.blog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
/**
 * 服务注册中心
 * @author 24845
 */
@SpringBootApplication
@EnableEurekaServer
public class FlchyBlogEurekaServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(FlchyBlogEurekaServerApplication.class, args);
	}
}
