package com.flchy.blog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.flchy.blog.privilege.extend.listener.ApplicationStartupListener;


@SpringBootApplication
//@ComponentScan(basePackages = {"com.flchy.blog.privilege"})
@EnableScheduling
//@EnableEurekaClient
public class Application {
	public static void main(String[] args) {
		SpringApplication springApplication = new SpringApplication(Application.class);
		 springApplication.addListeners(new ApplicationStartupListener());
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

	
	/**
	 * 访问请求权限过滤
	 * @return
	 */
/*	@Bean
	public FilterRegistrationBean filterRegistrationBean() {
		FilterRegistrationBean registrationBean = new FilterRegistrationBean();
		PermissionsAuthFilter httpBasicFilter = new PermissionsAuthFilter();
		registrationBean.setFilter(httpBasicFilter);
		List<String> urlPatterns = new ArrayList<String>();
		urlPatterns.add("/*");
		registrationBean.setUrlPatterns(urlPatterns);
		return registrationBean;
	}*/
}
