package com.flchy.blog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
//@ComponentScan(basePackages = {"com.flchy.blog.base"})
@EnableScheduling
@EnableEurekaClient
public class Application {
	public static void main(String[] args) {
		SpringApplication springApplication = new SpringApplication(Application.class);
		// springApplication.addListeners(new ApplicationStartupListener());
		springApplication.run(args);
		// org.springframework.boot.autoconfigure
	}

//	@Bean
//	public ServletRegistrationBean jersetServlet() {
//		ServletRegistrationBean registration = new ServletRegistrationBean(new ServletContainer(), "/*");
//		// our rest resources will be available in the path /jersey/*
//		registration.addInitParameter(ServletProperties.JAXRS_APPLICATION_CLASS, JerseyConfig.class.getName());
//		return registration;
//	}

}
