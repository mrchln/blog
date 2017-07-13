package com.flchy.blog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
@SpringBootTest
public class Application {
	
	public static void main(String[] args) {
		SpringApplication springApplication = new SpringApplication(Application.class); 
//		springApplication.addListeners(new ApplicationStartupListener());
		springApplication.run(args);
		
		
//		org.springframework.boot.autoconfigure
	}
	

}
