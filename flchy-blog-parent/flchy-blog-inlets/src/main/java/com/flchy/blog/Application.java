package com.flchy.blog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@SpringBootApplication
public class Application {
	
	public static void main(String[] args) {
		SpringApplication springApplication = new SpringApplication(Application.class); 
//		springApplication.addListeners(new ApplicationStartupListener());
		springApplication.run(args);
		
		
//		org.springframework.boot.autoconfigure
	}
	

}
