package com.flchy.blog.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@Service
public class ArticleService {
	@Autowired
	RestTemplate restTemplate;

	@HystrixCommand(fallbackMethod = "hiError")
	public Object selectArticlePage(HttpServletRequest request) {
		return restTemplate.postForEntity("http://SERVICE-BLOG/page", request, null);
	}
	
	public String hiError(HttpServletRequest request) {
        return "hi,"+"错误了"+",sorry,error!";
    }

}
