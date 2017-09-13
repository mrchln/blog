package com.flchy.blog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class FlchyBlogConfigClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(FlchyBlogConfigClientApplication.class, args);
	}
}
